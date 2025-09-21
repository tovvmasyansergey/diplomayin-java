package com.example.diplomayinjava.security.auth.service.impl;

import com.example.diplomayinjava.dto.LoginUserDto;
import com.example.diplomayinjava.dto.RegisterUserDto;
import com.example.diplomayinjava.entity.AppUser;
import com.example.diplomayinjava.repository.AppUserRepository;
import com.example.diplomayinjava.security.auth.CurrentUser;
import com.example.diplomayinjava.security.auth.service.AuthenticationService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AppUserRepository appUserRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void signup(RegisterUserDto registerUserDto) {
        if (appUserRepository.findByEmail(registerUserDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }
        AppUser user = new AppUser();
        user.setFirstname(registerUserDto.getFirstname());
        user.setLastname(registerUserDto.getLastname());
        user.setEmail(registerUserDto.getEmail());
        user.setPhone(registerUserDto.getPhone());
        user.setProfilePicture(registerUserDto.getProfilePicture());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        appUserRepository.save(user);
    }

    @Override
    public AppUser authenticate(LoginUserDto input) {
        AppUser user = appUserRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!passwordEncoder.matches(input.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return user;
    }

    @Override
    public AppUser editUser(RegisterUserDto registerUserDto, CurrentUser currentUser) {
        if (!currentUser.getUsername().equals(registerUserDto.getEmail())) {
            throw new IllegalArgumentException("You can only edit your own profile");
        }
        AppUser appUser = appUserRepository.findByEmail(registerUserDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        appUser.setFirstname(registerUserDto.getFirstname());
        appUser.setLastname(registerUserDto.getLastname());
        appUser.setPhone(registerUserDto.getPhone());
        appUser.setProfilePicture(registerUserDto.getProfilePicture());
        return appUserRepository.save(appUser);
    }
}
