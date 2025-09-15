package com.example.diplomayinjava.config;

import com.example.diplomayinjava.entity.AppUser;
import com.example.diplomayinjava.entity.Role;
import com.example.diplomayinjava.repository.AppUserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CreateAdmin {
    private final PasswordEncoder passwordEncoder;
    private final AppUserRepository appUserRepository;

//    @PostConstruct
//    public void initialize() {
//        AppUser user = new AppUser();
//       user.setUsername("admin");
//        user.setEmail("admin@example.com");
//       user.setPassword(passwordEncoder.encode("admin"));
//       user.setRole(Role.ADMIN);
//       appUserRepository.save(user);
//    }

}

