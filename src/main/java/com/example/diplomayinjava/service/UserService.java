package com.example.diplomayinjava.service;

import com.example.diplomayinjava.entity.AppUser;
import com.example.diplomayinjava.entity.Role;
import com.example.diplomayinjava.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final AppUserRepository appUserRepository;
    
    public AppUser findById(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    
    public AppUser findByEmail(String email) {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
    
    public AppUser findAdminUser() {
        return appUserRepository.findByRole(Role.ADMIN)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Admin user not found"));
    }
    
    public List<AppUser> findAllUsers() {
        return appUserRepository.findAll();
    }
    
    public List<AppUser> findUsersByRole(Role role) {
        return appUserRepository.findByRole(role);
    }
}
