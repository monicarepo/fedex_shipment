package com.example.fedex.entity;

public enum GraphQLRole {
    USER,
    ADMIN;

    public ERole toERole() {
        // Convert USER -> ROLE_USER, ADMIN -> ROLE_ADMIN
        return ERole.valueOf("ROLE_" + this.name());
    }

    public static GraphQLRole fromERole(ERole eRole) {
        // Convert ROLE_USER -> USER, ROLE_ADMIN -> ADMIN
        String roleName = eRole.name().replace("ROLE_", "");
        return GraphQLRole.valueOf(roleName);
    }
}
