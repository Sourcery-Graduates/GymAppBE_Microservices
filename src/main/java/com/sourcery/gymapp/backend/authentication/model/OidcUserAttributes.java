package com.sourcery.gymapp.backend.authentication.model;

public record OidcUserAttributes(String email, String name, String provider, String providerId) {}
