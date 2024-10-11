package com.sourcery.gymapp.backend.utils;

import java.util.UUID;

public class CreateUUID {

    public static UUID generateUUID(String uuid) {
        return UUID.fromString(uuid);
    }
    public static UUID generateUUID() {
        return UUID.randomUUID();
    }
}
