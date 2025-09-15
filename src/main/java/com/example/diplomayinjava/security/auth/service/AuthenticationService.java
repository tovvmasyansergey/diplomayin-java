package com.example.diplomayinjava.security.auth.service;

import com.example.diplomayinjava.dto.LoginUserDto;
import com.example.diplomayinjava.dto.RegisterUserDto;
import com.example.diplomayinjava.entity.AppUser;
import com.example.diplomayinjava.repository.AppUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AppUserRepository appUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            AppUserRepository appUserRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser signup(RegisterUserDto input) {
        AppUser user = new AppUser();
        user.setLastname(input.getLastname());
        user.setPhone(input.getPhone());
        user.setProfilePicture(input.getProfilePicture());
        user.setFirstname(input.getFirstname());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        return appUserRepository.save(user);
    }

    public AppUser authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return appUserRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}