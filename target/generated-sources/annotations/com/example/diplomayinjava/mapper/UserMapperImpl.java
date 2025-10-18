package com.example.diplomayinjava.mapper;

import com.example.diplomayinjava.dto.AuthResponseDto;
import com.example.diplomayinjava.entity.AppUser;
import com.example.diplomayinjava.security.auth.CurrentUser;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-12T15:28:46+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 24.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public CurrentUser appUserToCurrentUser(AppUser appUser) {
        if ( appUser == null ) {
            return null;
        }

        CurrentUser.CurrentUserBuilder currentUser = CurrentUser.builder();

        currentUser.role( roleToString( appUser.getRole() ) );
        currentUser.id( appUser.getId() );
        currentUser.firstname( appUser.getFirstname() );
        currentUser.lastname( appUser.getLastname() );
        currentUser.email( appUser.getEmail() );
        currentUser.phone( appUser.getPhone() );
        currentUser.profilePicture( appUser.getProfilePicture() );

        return currentUser.build();
    }

    @Override
    public AuthResponseDto currentUserToAuthResponseDto(CurrentUser currentUser) {
        if ( currentUser == null ) {
            return null;
        }

        AuthResponseDto.AuthResponseDtoBuilder authResponseDto = AuthResponseDto.builder();

        authResponseDto.id( currentUser.getId() );
        authResponseDto.firstname( currentUser.getFirstname() );
        authResponseDto.lastname( currentUser.getLastname() );
        authResponseDto.email( currentUser.getEmail() );
        authResponseDto.role( currentUser.getRole() );
        authResponseDto.token( currentUser.getToken() );
        authResponseDto.phone( currentUser.getPhone() );
        authResponseDto.profilePicture( currentUser.getProfilePicture() );

        return authResponseDto.build();
    }

    @Override
    public AuthResponseDto appUserToAuthResponseDto(AppUser appUser) {
        if ( appUser == null ) {
            return null;
        }

        AuthResponseDto.AuthResponseDtoBuilder authResponseDto = AuthResponseDto.builder();

        authResponseDto.role( roleToString( appUser.getRole() ) );
        authResponseDto.id( appUser.getId() );
        authResponseDto.firstname( appUser.getFirstname() );
        authResponseDto.lastname( appUser.getLastname() );
        authResponseDto.email( appUser.getEmail() );
        authResponseDto.phone( appUser.getPhone() );
        authResponseDto.profilePicture( appUser.getProfilePicture() );

        return authResponseDto.build();
    }
}
