package com.example.fedex.entity;

public enum ERole {
    USER,
    ADMIN;

    public static ERole fromString(String role) {
        try {
            String normalizedRole = role.startsWith("ROLE_") ? role.substring(5) : role;
            return ERole.valueOf(normalizedRole.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + role);
        }
    }

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}