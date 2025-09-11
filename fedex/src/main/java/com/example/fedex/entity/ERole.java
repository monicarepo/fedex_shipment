package com.example.fedex.entity;

public enum ERole {
    ROLE_USER,
    ROLE_ADMIN;

    public static ERole fromString(String role) {
        try {
            return ERole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + role);
        }
    }

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}