package com.example.diplomayinjava.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArchitecturalCalculation {
    private String type;
    private String equation;
    private Map<String, Double> parameters;
    private Double area;
    private Double arcLength;
    private Double volume;
    private Double weight;
    private Double totalCost;
    
    // Структурный анализ
    private Double maxStress;
    private Double safetyFactor;
    private Double deflection;
    private Double bucklingLoad;
    private Double naturalFrequency;
    
    // Данные для графика (массив [x, y])
    private List<double[]> graphData;
}

