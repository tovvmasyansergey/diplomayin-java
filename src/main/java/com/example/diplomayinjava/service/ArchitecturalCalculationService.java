package com.example.diplomayinjava.service;

import com.example.diplomayinjava.model.ArchitecturalCalculation;
import com.example.diplomayinjava.model.Material;
import org.springframework.stereotype.Service;

@Service
public interface ArchitecturalCalculationService {

    /**
     * Расчет параболической арки
     */
    ArchitecturalCalculation calculateParabola(double span, double height, double thickness, Material material);

    /**
     * Расчет эллиптического купола
     */
    ArchitecturalCalculation calculateEllipse(double a, double b, double thickness, Material material);

    /**
     * Расчет гиперболической башни
     */
    ArchitecturalCalculation calculateHyperbola(double a, double b, double thickness, Material material);
}

