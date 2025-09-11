package com.example.fedex.dto;

import com.example.fedex.entity.GraphQLRole;
import lombok.Data;

import java.util.Set;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Set<GraphQLRole> roles;

    public UserResponse(Long id, String username, String email, Set<GraphQLRole> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}