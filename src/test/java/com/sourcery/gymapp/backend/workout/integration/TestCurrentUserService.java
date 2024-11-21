package com.sourcery.gymapp.backend.workout.integration;

import com.sourcery.gymapp.backend.globalconfig.CurrentUserService;
import java.util.UUID;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class TestCurrentUserService extends CurrentUserService {

    @Override
    public UUID getCurrentUserId() {
        return UUID.fromString("00000000-0000-0000-0000-000000000001");
    }
}
