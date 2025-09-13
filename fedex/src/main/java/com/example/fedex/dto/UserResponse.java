package com.example.fedex.dto;

import com.example.fedex.entity.GraphQLRole;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse implements Serializable {
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