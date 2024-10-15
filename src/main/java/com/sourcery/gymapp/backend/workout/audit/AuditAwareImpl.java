package com.sourcery.gymapp.backend.workout.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<UUID> {

    @Override
    public Optional<UUID> getCurrentAuditor() {
        return Optional.of(UUID.fromString("4012527c-334e-4605-aa8e-1fef26ea37a5"));
    }
}
