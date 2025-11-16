package com.example.diplomayinjava.service;

import com.example.diplomayinjava.model.ArchitecturalCalculation;
import com.example.diplomayinjava.model.Material;
import org.springframework.stereotype.Service;

@Service
public interface ArchitecturalCalculationService {

    /**
     * Расчет параболической арки
     */
    public ArchitecturalCalculation calculateParabola(double span, double height, double thickness, Material material);

    /**
     * Расчет эллиптического купола
     */
    public ArchitecturalCalculation calculateEllipse(double a, double b, double thickness, Material material);

    /**
     * Расчет гиперболической башни
     */
    public ArchitecturalCalculation calculateHyperbola(double a, double b, double thickness, Material material);
}

