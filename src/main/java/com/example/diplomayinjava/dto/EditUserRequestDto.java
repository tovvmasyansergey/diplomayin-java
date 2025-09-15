package com.example.diplomayinjava.dto;


import lombok.Data;

@Data
public class EditUserRequestDto {
    private String firstname;
    private String email;
    private String lastname;
    private String phone;
    private String profilePicture;

}
