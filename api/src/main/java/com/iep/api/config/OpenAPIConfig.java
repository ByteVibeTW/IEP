package com.iep.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI (Swagger) 配置
 * 配置 API 文檔和 JWT 認證
 */
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("IEP API")
                        .version("1.0.0")
                        .description("""
                                IEP 系統 API 文檔
                                
                                ## 認證方式
                                1. 使用 `/api/v1/auth/login` 登入獲取 JWT token
                                2. 點擊右上角的 'Authorize' 按鈕
                                3. 在彈出框中輸入: `Bearer {your_token}`
                                4. 點擊 'Authorize' 完成認證""")
                )
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("輸入 JWT token (不需要加 'Bearer ' 前綴，系統會自動添加)")));
    }
}