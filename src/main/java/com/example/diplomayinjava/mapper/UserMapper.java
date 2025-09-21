package com.example.diplomayinjava.mapper;

import com.example.diplomayinjava.dto.AuthResponseDto;
import com.example.diplomayinjava.entity.AppUser;
import com.example.diplomayinjava.security.auth.CurrentUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role", qualifiedByName = "roleToString")
    @Mapping(target = "token", ignore = true)
    CurrentUser appUserToCurrentUser(AppUser appUser);

    AuthResponseDto currentUserToAuthResponseDto(CurrentUser currentUser);

    @Mapping(target = "role", source = "role", qualifiedByName = "roleToString")
    @Mapping(target = "token", ignore = true)
    AuthResponseDto appUserToAuthResponseDto(AppUser appUser);

    @Named("roleToString")
    default String roleToString(com.example.diplomayinjava.entity.Role role) {
        return role != null ? role.name() : null;
    }
}
