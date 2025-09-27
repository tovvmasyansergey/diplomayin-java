package com.example.diplomayinjava.security.auth.service;

import com.example.diplomayinjava.dto.LoginUserDto;
import com.example.diplomayinjava.dto.RegisterUserDto;
import com.example.diplomayinjava.entity.AppUser;
import com.example.diplomayinjava.security.auth.CurrentUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthenticationService {
    void signup(RegisterUserDto registerUserDto);

    AppUser authenticate(LoginUserDto input);
    
    AppUser editUser(RegisterUserDto registerUserDto, CurrentUser currentUser);
    
    List<AppUser> getAllUsers();
    
    Page<AppUser> getAllUsers(Pageable pageable);
    
    AppUser editUserById(Long userId, RegisterUserDto registerUserDto);
    
    void deleteUser(Long userId);
}