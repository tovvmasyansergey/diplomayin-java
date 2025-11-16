package com.example.diplomayinjava.service;

import com.example.diplomayinjava.entity.AppUser;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public AppUser findById(Long id);
}
