package com.example.diplomayinjava.controller;

import com.example.diplomayinjava.dto.AuthResponseDto;
import com.example.diplomayinjava.dto.LoginUserDto;
import com.example.diplomayinjava.dto.RegisterUserDto;
import com.example.diplomayinjava.entity.AppUser;
import com.example.diplomayinjava.repository.AppUserRepository;
import com.example.diplomayinjava.security.auth.CurrentUser;
import com.example.diplomayinjava.security.auth.service.AuthenticationService;
import com.example.diplomayinjava.security.auth.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final AppUserRepository appUserRepository;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, AppUserRepository appUserRepository) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.appUserRepository = appUserRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterUserDto dto) {
        try {
            authenticationService.signup(dto);
            return ResponseEntity.ok("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<AppUser> edit(@RequestBody RegisterUserDto registerUserDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
        if (!currentUser.getUsername().equals(registerUserDto.getEmail())) {
            throw new IllegalArgumentException();
        }
        Optional<AppUser> byEmail = appUserRepository.findByEmail(registerUserDto.getEmail());
        AppUser appUser = byEmail.get();
        appUser.setFirstname(registerUserDto.getFirstname());
        appUser.setLastname(registerUserDto.getLastname());
        appUser.setPhone(registerUserDto.getPhone());
        appUser.setProfilePicture(registerUserDto.getProfilePicture());
        AppUser registeredUser = appUserRepository.save(appUser);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> authenticate(@RequestBody LoginUserDto loginUserDto) {
        AppUser authenticate = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticate);
        CurrentUser currentUser = CurrentUser.builder()
                        .id(authenticate.getId())
                        .lastname(authenticate.getLastname())
                        .email(authenticate.getEmail())
                        .role(authenticate.getRole().name())
                        .phone(authenticate.getPhone())
                        .profilePicture(authenticate.getProfilePicture())
                        .build();
        currentUser.setToken(jwtToken);
        AuthResponseDto authResponseDto = AuthResponseDto.builder()
                .id(currentUser.getId())
                .lastname(currentUser.getLastname())
                .email(currentUser.getEmail())
                .role(currentUser.getRole())
                .phone(currentUser.getPhone())
                .profilePicture(currentUser.getProfilePicture())
                .token(currentUser.getToken())
                .build();
        return ResponseEntity.ok(authResponseDto);
    }
}
