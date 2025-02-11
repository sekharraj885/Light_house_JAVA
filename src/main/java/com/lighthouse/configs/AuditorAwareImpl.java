package com.lighthouse.configs;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // Fetch the current user (e.g., from Spring Security)
        return Optional.of("systemUser"); // Replace with actual user fetching logic
    }
}

