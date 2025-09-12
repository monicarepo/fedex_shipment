package com.example.fedex.controller;

import com.example.fedex.dto.UserResponse;
import com.example.fedex.entity.*;
import com.example.fedex.repository.RoleRepository;
import com.example.fedex.repository.UserRepository;
import com.example.fedex.security.jwt.JwtUtils;
import com.example.fedex.service.UserDetailsImpl;
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
import java.util.stream.Collectors;

@Controller
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @QueryMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public UserResponse currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getGraphQLRoles()
        );
    }

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> users() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getGraphQLRoles()
                ))
                .collect(Collectors.toList());
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

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getGraphQLRoles()
        );

        return new AuthPayload(jwt, userResponse);
    }

    @MutationMapping
    public SignUpResponse signUp(@Argument SignUpInput input) {
        try {
            if (userRepository.existsByEmail(input.getEmail())) {
                return new SignUpResponse("Email '" + input.getEmail() + "' is already registered. Please use a different email address.");
            }

            if (userRepository.existsByUsername(input.getUsername())) {
//                throw new RuntimeException("Error: Username is already taken!");
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
            userRepository.save(user);

            return new SignUpResponse("User registered successfully!", true);
        } catch (RuntimeException exception) {
            return new SignUpResponse("An unexpected error occurred: " + exception.getMessage());
        }
    }

@MutationMapping
@PreAuthorize("hasRole('ADMIN')")
@Transactional
public UserResponse updateUserRoles(@Argument Long id, @Argument List<GraphQLRole> roles) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

    Set<Role> newRoles = new HashSet<>();
    roles.forEach(graphQLRole -> {
        Role role = roleRepository.findByName(graphQLRole.toERole())
                .orElseGet(() -> {
                    // Create the role if it doesn't exist
                    Role newRole = new Role(graphQLRole.toERole());
                    return roleRepository.save(newRole);
                });
        newRoles.add(role);
    });

    user.setRoles(newRoles);
    User savedUser = userRepository.save(user);

    return new UserResponse(
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getEmail(),
            savedUser.getGraphQLRoles()
    );
}

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Boolean deleteUser(@Argument Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public static class SignInInput {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class SignUpInput {
        private String username;
        private String email;
        private String password;
        private Set<GraphQLRole> roles;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public Set<GraphQLRole> getRoles() { return roles; }
        public void setRoles(Set<GraphQLRole> roles) { this.roles = roles; }
    }

    public static class AuthPayload {
        private String token;
        private UserResponse user;

        public AuthPayload(String token, UserResponse user) {
            this.token = token;
            this.user = user;
        }

        public String getToken() { return token; }
        public UserResponse getUser() { return user; }
    }
}