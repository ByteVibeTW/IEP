package com.iep.api.config;

import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
public class OpenAPIOperationIdConfig {

    private static String toPascalCase(String value) {
        if (value == null) {
            return "";
        }
        String v = value.trim();
        if (v.isEmpty()) {
            return "";
        }

        // If already looks like camelCase/PascalCase, just capitalize first char.
        if (v.matches("[A-Za-z][A-Za-z0-9]*")) {
            return Character.toUpperCase(v.charAt(0)) + v.substring(1);
        }

        // Otherwise, split on non-alphanumeric and build PascalCase.
        String[] parts = v.split("[^A-Za-z0-9]+");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (p == null || p.isEmpty()) {
                continue;
            }
            sb.append(Character.toUpperCase(p.charAt(0)));
            if (p.length() > 1) {
                sb.append(p.substring(1));
            }
        }
        return sb.toString();
    }

    /**
     * Adds controller method name into OpenAPI so frontend can reference it.
     * - operationId: {ControllerSimpleName}_{methodName} (only when not already set)
     * - extension: x-function-name = {methodName}
     */
    @Bean
    public OperationCustomizer operationIdCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            if (operation == null || handlerMethod == null) {
                return operation;
            }

            String methodName = handlerMethod.getMethod().getName();
            String controllerName = handlerMethod.getBeanType().getSimpleName();

            Map<String, Object> extensions = operation.getExtensions();
            if (extensions == null) {
                extensions = new HashMap<>();
            } else if (!(extensions instanceof HashMap)) {
                extensions = new HashMap<>(extensions);
            }
            extensions.put("x-function-name", methodName);
            operation.setExtensions(extensions);

            if (operation.getOperationId() == null || operation.getOperationId().isBlank()) {
                operation.setOperationId(controllerName + "_" + methodName);
            }

            String operationId = operation.getOperationId();
            if (operationId != null && !operationId.isBlank()) {
                String hookName = "use" + toPascalCase(operationId);

                String existingDescription = Objects.toString(operation.getDescription(), "");
                String trimmed = existingDescription.stripLeading();

                // Avoid adding the prefix multiple times.
                if (!trimmed.startsWith("method:")) {
                    String prefix = "method: " + operationId + "\n\n" +
                            "Tanstack Query: " + hookName + "\n\n";
                    operation.setDescription(prefix + existingDescription);
                }

                Map<String, Object> ext = operation.getExtensions();
                if (ext == null) {
                    ext = new HashMap<>();
                } else if (!(ext instanceof HashMap)) {
                    ext = new HashMap<>(ext);
                }
                ext.put("x-orval-hook", hookName);
                operation.setExtensions(ext);
            }

            return operation;
        };
    }
}