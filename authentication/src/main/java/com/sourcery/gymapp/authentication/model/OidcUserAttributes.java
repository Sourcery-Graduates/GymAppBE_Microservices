package com.sourcery.gymapp.authentication.model;

public record OidcUserAttributes(String email, String name, String provider, String providerId) {}
