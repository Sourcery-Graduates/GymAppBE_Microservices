package com.sourcery.gymapp.backend.userProfile.audit;

import com.sourcery.gymapp.backend.utils.CreateUUID;
import java.util.UUID;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<UUID> {

    @Override
    public Optional<UUID> getCurrentAuditor() {
        return Optional.of(CreateUUID.generateUUID());
    }

}
