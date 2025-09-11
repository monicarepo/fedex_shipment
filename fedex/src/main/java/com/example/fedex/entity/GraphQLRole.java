package com.example.fedex.entity;

public enum GraphQLRole {
    USER,
    ADMIN;

    public ERole toERole() {
        return ERole.valueOf(this.name());
    }

    public static GraphQLRole fromERole(ERole eRole) {
        return GraphQLRole.valueOf(eRole.name());
    }
}
