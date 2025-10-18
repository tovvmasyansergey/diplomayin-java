package com.example.diplomayinjava.controller;

import com.example.diplomayinjava.dto.AuthResponseDto;
import com.example.diplomayinjava.dto.LoginUserDto;
import com.example.diplomayinjava.dto.RegisterUserDto;
import com.example.diplomayinjava.dto.UserDto;
import com.example.diplomayinjava.entity.AppUser;
import com.example.diplomayinjava.mapper.UserMapper;
import com.example.diplomayinjava.security.auth.CurrentUser;
import com.example.diplomayinjava.security.auth.service.AuthenticationService;
import com.example.diplomayinjava.security.auth.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;


@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;
    
    private static final String UPLOAD_DIR = "uploads";

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, UserMapper userMapper) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userMapper = userMapper;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> register(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("firstname") String firstname,
            @RequestParam("lastname") String lastname,
            @RequestParam("phone") String phone,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture) {
        try {
            RegisterUserDto dto = new RegisterUserDto();
            dto.setEmail(email);
            dto.setPassword(password);
            dto.setFirstname(firstname);
            dto.setLastname(lastname);
            dto.setPhone(phone);
            dto.setLocation(location);
            
            // Если загружено фото, сохраняем его
            if (profilePicture != null && !profilePicture.isEmpty()) {
                String fileUrl = saveProfilePicture(profilePicture);
                dto.setProfilePicture(fileUrl);
            }
            
            authenticationService.signup(dto);
            return ResponseEntity.ok("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error saving profile picture: " + e.getMessage());
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<String> edit(@RequestBody @Valid RegisterUserDto registerUserDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
            AppUser updatedUser = authenticationService.editUser(registerUserDto, currentUser);
            return ResponseEntity.ok("User updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating user");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> authenticate(@RequestBody LoginUserDto loginUserDto) {
        AppUser authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        AuthResponseDto authResponseDto = userMapper.appUserToAuthResponseDto(authenticatedUser);
        authResponseDto.setToken(jwtToken);
        
        return ResponseEntity.ok(authResponseDto);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<AppUser>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<AppUser> users = authenticationService.getAllUsers(pageable);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/edit/{userId}")
    public ResponseEntity<String> editUser(
            @PathVariable Long userId,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "firstname", required = false) String firstname,
            @RequestParam(value = "lastname", required = false) String lastname,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture) {
        try {
            RegisterUserDto dto = new RegisterUserDto();
            dto.setEmail(email);
            dto.setFirstname(firstname);
            dto.setLastname(lastname);
            dto.setPhone(phone);
            dto.setLocation(location);
            if (profilePicture != null && !profilePicture.isEmpty()) {
                String fileUrl = saveProfilePicture(profilePicture);
                dto.setProfilePicture(fileUrl);
            }
            
            authenticationService.editUserById(userId, dto);
            return ResponseEntity.ok("User updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error saving profile picture: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            authenticationService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error deleting user: " + e.getMessage());
        }
    }

    private String saveProfilePicture(MultipartFile file) throws IOException {
        // Проверяем тип файла
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null && originalFilename.contains(".") 
            ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
            : ".jpg";
        String filename = UUID.randomUUID().toString() + fileExtension;
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/" + filename;
    }

}
