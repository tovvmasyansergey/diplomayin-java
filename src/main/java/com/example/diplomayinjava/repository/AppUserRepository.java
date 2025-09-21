package com.example.diplomayinjava.repository;

import com.example.diplomayinjava.entity.AppUser;
import com.example.diplomayinjava.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);

    AppUser findByRole(Role role);
}


