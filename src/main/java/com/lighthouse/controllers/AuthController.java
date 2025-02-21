package com.lighthouse.controllers;

import com.lighthouse.DTOs.UserDTO;
import com.lighthouse.models.Role;
import com.lighthouse.models.User;
import com.lighthouse.repositories.RoleRepository;
import com.lighthouse.repositories.UserRepository;
import com.lighthouse.security.Utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthController(
        AuthenticationManager authenticationManager,
        JwtUtil jwtUtil,
        UserDetailsService userDetailsService,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        RoleRepository roleRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(userDetails.getUsername());

        return Map.of("token", token);
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signupUser(@RequestBody Map<String, Object> requestBody) {
        Map<String, Object> response = new HashMap<>();

        // Extract user details from request body
        String email = (String) requestBody.get("email");
        String firstName = (String) requestBody.get("firstName");
        String lastName = (String) requestBody.get("lastName");
        String password = (String) requestBody.get("password");
        List<String> roleIds = (List<String>) requestBody.get("roles"); // UUIDs of roles

        // Check if email is already registered
        if (userRepository.findByEmail(email).isPresent()) {
            response.put("message", "Email already exists, try signing in!");
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Fetch role entities based on UUIDs
        Set<Role> roles = roleIds.stream()
                .map(UUID::fromString) // Convert string UUIDs to UUID type
                .map(roleId -> roleRepository.findById(roleId).orElseThrow(
                        () -> new RuntimeException("Role not found: " + roleId)))
                .collect(Collectors.toSet());

        // Create a new user
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email.toLowerCase());
        newUser.setPassword(passwordEncoder.encode(password)); // Encode password
        newUser.setRoles(roles); // Assign fetched roles

        // Save user
        User savedUser = userRepository.save(newUser);

        // Generate JWT token
        String token = jwtUtil.generateToken(savedUser.getEmail());

        // Convert user to DTO
        UserDTO userDTO = new UserDTO(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getRoles(),
                savedUser.getCreatedBy(),
                savedUser.getCreatedOn(),
                savedUser.getLastUpdatedBy(),
                savedUser.getLastUpdatedOn()
        );

        // Build success response
        response.put("message", "User registered successfully!");
        response.put("success", true);
        response.put("userInfo", userDTO);
        response.put("token", token);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> Logout(@RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        if (token == null || !token.startsWith("Bearer ")) {
            response.put("success", false);
            response.put("message", "Invalid token or missing Authorization header");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        // Extract actual token by removing "Bearer " prefix
        String jwtToken = token.substring(7);
        String userEmail;
        try {
            userEmail = jwtUtil.extractUsername(jwtToken);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Invalid token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        // Find user by email
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            response.put("success", false);
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.put("success",true);
        response.put("message","Logout Successful!");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }








}
