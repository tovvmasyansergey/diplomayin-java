package com.example.diplomayinjava.service.impl;

import com.example.diplomayinjava.entity.AppUser;
import com.example.diplomayinjava.repository.AppUserRepository;
import com.example.diplomayinjava.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final AppUserRepository appUserRepository;
    
    public AppUser findById(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
}
