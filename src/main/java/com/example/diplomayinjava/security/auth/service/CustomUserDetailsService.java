package com.example.diplomayinjava.security.auth.service;

import com.example.diplomayinjava.entity.AppUser;
import com.example.diplomayinjava.repository.AppUserRepository;
import com.example.diplomayinjava.security.auth.CurrentUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService  implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public CustomUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        AppUser appUser = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return CurrentUser.builder()
                .id(appUser.getId())
                .email(appUser.getEmail())
                .firstname(appUser.getFirstname())
                .lastname(appUser.getLastname())
                .role(appUser.getRole().name())
                .build();
    }
}
