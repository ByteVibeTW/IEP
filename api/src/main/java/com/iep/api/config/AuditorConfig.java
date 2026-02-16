package com.iep.api.config;

import com.iep.api.util.JwtUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorConfig implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        return JwtUtils.getCurrentUserId();
    }
}
