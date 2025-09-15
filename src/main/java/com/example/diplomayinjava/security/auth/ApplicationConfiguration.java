package com.example.diplomayinjava.security.auth;

import com.example.diplomayinjava.entity.AppUser;
import com.example.diplomayinjava.repository.AppUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApplicationConfiguration {
    private final AppUserRepository appUserRepository;

    public ApplicationConfiguration(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Bean
    UserDetailsService userDetailsService() {
        return email -> {
            AppUser user = appUserRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return CurrentUser.builder()
                    .id(user.getId())
                    .lastname(user.getLastname())
                    .email(user.getEmail())
                    .role(user.getRole().name())
                    .phone(user.getPhone())
                    .password(user.getPassword())
                    .token(null)
                    .profilePicture(user.getProfilePicture())
                    .build();
        };
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}