package com.example.diplomayinjava.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Material {
    private String name;
    private Double density; // кг/м³
    private Double costPerUnit; // руб/м³
    private String unit;
    private Double strength; // МПа
    private Double elasticity; // ГПа
    private Double thermalConductivity; // Вт/(м·К)
    private String category;
    private String description;
}

