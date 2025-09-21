package com.example.diplomayinjava.security.auth.service;

import com.example.diplomayinjava.dto.LoginUserDto;
import com.example.diplomayinjava.dto.RegisterUserDto;
import com.example.diplomayinjava.entity.AppUser;

public interface AuthenticationService {
    void signup(RegisterUserDto registerUserDto);

    AppUser authenticate(LoginUserDto input);
}