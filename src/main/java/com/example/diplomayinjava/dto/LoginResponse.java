package com.example.diplomayinjava.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse {
    private String token;
    public String getToken() {
        return token;
    }
}
