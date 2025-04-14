package com.sourcery.gymapp.workout.util;

import com.sourcery.gymapp.workout.exception.UserNotAuthorizedException;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public final class AuthorizationUtil {

    public static void checkIsUserAuthorized(UUID currentUserId, UUID expectedUserId) {

        if (!expectedUserId.equals(currentUserId)) {
            throw new UserNotAuthorizedException();
        }
    }

}
