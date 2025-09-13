package com.example.fedex.service;

import com.example.fedex.dto.UserResponse;
import com.example.fedex.entity.GraphQLRole;
import com.example.fedex.entity.Role;
import com.example.fedex.entity.User;
import com.example.fedex.repository.RoleRepository;
import com.example.fedex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

    @Cacheable(value = "userResponses", key = "'response:' + #id", unless = "#result == null")
    public Optional<UserResponse> getUserById(Long id) {
        return userRepository.findById(id).map( user -> {
                user.getRoles().size();
                return new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getGraphQLRoles()
                );
            }
        );
    }

    @Cacheable(value = "user", key = "'entity:' + #id", unless = "#result == null")
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Cacheable(value = "allUsers", key = "'list'", unless = "#result == null or #result.isEmpty()")
    public List<UserResponse> getAllUsers() {
       return userRepository.findAll().stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getGraphQLRoles()
                ))
                .collect(Collectors.toList());
    }

    @Caching(evict = {
        @CacheEvict(value = "userResponses", key = "'response:' + #id", beforeInvocation = true),
        @CacheEvict(value = "user", key = "'entity:' + #id", beforeInvocation = true),
        @CacheEvict(value = "allUsers", key = "'list'", beforeInvocation = true)
    }, put = {
        @CachePut(value = "userResponses", key = "'response:' + #id"),
        @CachePut(value = "user", key = "'entity:' + #id")
    }
    )
    @Transactional
    public UserResponse updateUserRole(Long id, List<GraphQLRole> roles) {
        User user = findUserById(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Set<Role> newRoles = new HashSet<>();
        roles.forEach(graphQLRole -> {
            Role role = roleRepository.findByName(graphQLRole.toERole())
                    .orElseGet(() -> {
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

    @Cacheable(value = "userExists", key = "'username:' + #username")
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Cacheable(value = "userExists", key = "'email:' + #email")
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Cacheable(value = "userExists", key = "'id:' + #id")
    public Boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Caching(evict = {
        @CacheEvict(value = "userResponses", key = "'response:' + #id", beforeInvocation = true),
        @CacheEvict(value = "user", key = "'entity:' + #id", beforeInvocation = true),
        @CacheEvict(value = "allUsers", key = "'list'", beforeInvocation = true),
        @CacheEvict(value = "userExists", key = "'id:' + #id", beforeInvocation = true)
    })
    @Transactional
    public void deleteById(Long id) {
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(userDetail -> {
            evictExistenceCaches(userDetail.getUsername(), userDetail.getEmail());
        });
        userRepository.deleteById(id);
    }

    @Caching(
        evict = {
            @CacheEvict(value = "userResponses", key = "'response:' + #user.id", beforeInvocation = true),
            @CacheEvict(value = "user", key = "'entity:' + #user.id", beforeInvocation = true),
            @CacheEvict(value = "allUsers", key = "'list'", beforeInvocation = true)
        },
        put = {
            @CachePut(value = "userExists", key = "'username:' + #user.username"),
            @CachePut(value = "userExists", key = "'email:' + #user.email"),
            @CachePut(value = "userExists", key = "'id:' + #user.id")
        }
    )
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Caching(evict = {
        @CacheEvict(value = "userExists", key = "'username:' + #username", beforeInvocation = true),
        @CacheEvict(value = "userExists", key = "'email:' + #email", beforeInvocation = true)
    })
    public void evictExistenceCaches(String username, String email) {
        System.out.println("evictExistenceCaches");
    }
}
