package com.ecobank.soole.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

// Implement this interface to provide the current auditor i.e. the current user
@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    // Get details of user currently logged in trying to perform certain actions
    @Override
    public Optional<String> getCurrentAuditor() {
        // return Optional.ofNullable("1");
       return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}