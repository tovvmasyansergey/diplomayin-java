package com.example.diplomayinjava.security.auth.service;

import com.example.diplomayinjava.dto.LoginUserDto;
import com.example.diplomayinjava.dto.RegisterUserDto;
import com.example.diplomayinjava.entity.AppUser;
import com.example.diplomayinjava.security.auth.CurrentUser;

public interface AuthenticationService {
    void signup(RegisterUserDto registerUserDto);

    AppUser authenticate(LoginUserDto input);
    
    AppUser editUser(RegisterUserDto registerUserDto, CurrentUser currentUser);
}