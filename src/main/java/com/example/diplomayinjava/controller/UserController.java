package com.example.diplomayinjava.controller;

import com.example.diplomayinjava.dto.UserDto;
import com.example.diplomayinjava.entity.AppUser;
import com.example.diplomayinjava.security.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final AuthenticationService authenticationService;

    @GetMapping("/users/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        try {
            log.info("🔍 Getting all users...");
            List<AppUser> users = authenticationService.getAllUsers();
            log.info("👥 Found {} users", users.size());
            
            List<UserDto> userDtos = users.stream()
                    .map(user -> {
                        log.info("👤 Processing user: {}", user.getEmail());
                        return UserDto.builder()
                                .id(user.getId())
                                .firstName(user.getFirstname())
                                .lastName(user.getLastname())
                                .email(user.getEmail())
                                .phone(user.getPhone())
                                .profilePicture(user.getProfilePicture())
                                .role(user.getRole() != null ? user.getRole().toString() : null)
                                .build();
                    })
                    .collect(Collectors.toList());
            
            log.info("✅ Returning {} users", userDtos.size());
            return ResponseEntity.ok(userDtos);
        } catch (Exception e) {
            log.error("❌ Error getting users: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
