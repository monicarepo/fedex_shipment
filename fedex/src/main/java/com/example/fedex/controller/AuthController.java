package com.example.fedex.controller;

import com.example.fedex.dto.UserResponse;
import com.example.fedex.entity.*;
import com.example.fedex.repository.RoleRepository;
import com.example.fedex.security.jwt.JwtUtils;
import com.example.fedex.service.UserDetailsImpl;
import com.example.fedex.service.UserDetailsServiceImpl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailsServiceImpl  userDetailsService;

    @QueryMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public UserResponse currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserResponse user = userDetailsService.getUserById(userDetails.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        return user;
    }

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> users() {
        return userDetailsService.getAllUsers();
    }

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<GraphQLRole> roles() {
        return List.of(GraphQLRole.values());
    }

    @MutationMapping
    public AuthPayload signIn(@Argument SignInInput input) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        UserResponse user = userDetailsService.getUserById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new AuthPayload(jwt, user);
    }

    @MutationMapping
    public SignUpResponse signUp(@Argument SignUpInput input) {
        try {
            if (userDetailsService.existsByEmail(input.getEmail())) {
                return new SignUpResponse("Email '" + input.getEmail() + "' is already registered. Please use a different email address.");
            }

            if (userDetailsService.existsByUsername(input.getUsername())) {
                return new SignUpResponse("Username '" + input.getUsername() + "' is already taken. Please choose a different username.");
            }

            User user = new User(input.getUsername(), input.getEmail(), encoder.encode(input.getPassword()));

            Set<GraphQLRole> graphQLRoles = input.getRoles();
            Set<Role> roles = new HashSet<>();

            if (graphQLRoles == null || graphQLRoles.isEmpty()) {
                Role userRole = roleRepository.findByName(ERole.USER)
                        .orElseGet(() -> {
                            // Create the role if it doesn't exist
                            Role newRole = new Role(ERole.USER);
                            return roleRepository.save(newRole);
                        });
                roles.add(userRole);
            } else {
                graphQLRoles.forEach(graphQLRole -> {
                    Role role = roleRepository.findByName(graphQLRole.toERole())
                            .orElseGet(() -> {
                                // Create the role if it doesn't exist
                                Role newRole = new Role(graphQLRole.toERole());
                                return roleRepository.save(newRole);
                            });
                    roles.add(role);
                });
            }

            user.setRoles(roles);
            userDetailsService.save(user);

            return new SignUpResponse("User registered successfully!", true);
        } catch (RuntimeException exception) {
            return new SignUpResponse("An unexpected error occurred: " + exception.getMessage());
        }
    }

@MutationMapping
@PreAuthorize("hasRole('ADMIN')")
@Transactional
public UserResponse updateUserRoles(@Argument Long id, @Argument List<GraphQLRole> roles) {
       return userDetailsService.updateUserRole(id, roles);
}

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Boolean deleteUser(@Argument Long id) {
        if (userDetailsService.existsById(id)) {
            userDetailsService.deleteById(id);
            return true;
        }
        return false;
    }

    @Setter
    @Getter
    public static class SignInInput {
        private String username;
        private String password;

    }

    @Setter
    @Getter
    public static class SignUpInput {
        private String username;
        private String email;
        private String password;
        private Set<GraphQLRole> roles;

        public SignUpInput(String username, String email, String password, Set<GraphQLRole> roles) {
            this.username = username;
            this.email = email;
            this.password = password;
            this.roles = roles;
        }
    }

    @Getter
    public static class AuthPayload {
        private String token;
        private UserResponse user;

        public AuthPayload(String token, UserResponse user) {
            this.token = token;
            this.user = user;
        }

    }
}