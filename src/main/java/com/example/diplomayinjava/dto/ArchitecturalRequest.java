package com.example.diplomayinjava.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArchitecturalRequest {
    private String type; // "parabola", "ellipse", "hyperbola"
    
    // Параметры для параболы
    private Double span;
    private Double height;
    
    // Параметры для эллипса и гиперболы
    private Double a;
    private Double b;
    
    // Общие параметры
    private Double thickness;
    
    // Материал
    private String materialName;
    private Double density;
    private Double costPerUnit;
    private Double strength;
    private Double elasticity;
}

