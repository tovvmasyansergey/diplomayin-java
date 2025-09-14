package com.example.diplomayinjava.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    
    USER("USER", "Обычный пользователь"),
    ADMIN("ADMIN", "Администратор");
    
    private final String name;
    private final String description;
    
    /**
     * Получить роль по имени
     */
    public static Role fromName(String name) {
        for (Role role : Role.values()) {
            if (role.name.equals(name)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + name);
    }
    
    /**
     * Проверить, является ли роль административной
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }
    
    /**
     * Получить роль с префиксом ROLE_ для Spring Security
     */
    public String getAuthority() {
        return "ROLE_" + this.name;
    }
}


