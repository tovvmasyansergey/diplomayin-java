package com.example.diplomayinjava.service;

import com.example.diplomayinjava.model.ArchitecturalCalculation;
import com.example.diplomayinjava.model.Material;
import com.example.diplomayinjava.dto.ArchitecturalRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArchitecturalCalculationService {

    /**
     * Расчет параболической арки
     */
    public ArchitecturalCalculation calculateParabola(double span, double height, double thickness, Material material) {
        double a = 4 * height / (span * span);
        String equation = String.format("y = %.4fx²", a);

        // Расчет площади параболы
        double area = (2 * span * height) / 3;

        // Расчет длины дуги
        double arcLength = calculateParabolaArcLength(span, height);

        // Расчет объема
        double volume = area * thickness;

        // Расчет материала
        double weight = volume * material.getDensity();
        double totalCost = volume * material.getCostPerUnit();

        // Структурный анализ
        double maxStress = (weight * 9.81) / (volume * 1000); // МПа
        double safetyFactor = material.getStrength() / maxStress;
        double deflection = (weight * 9.81 * Math.pow(span, 3)) / 
                           (48 * material.getElasticity() * 1000 * volume);
        double bucklingLoad = (Math.PI * Math.PI * material.getElasticity() * 1000 * volume) / 
                              Math.pow(height, 2);
        double naturalFrequency = Math.sqrt(material.getElasticity() * 1000 * volume / weight) / (2 * Math.PI);

        // Генерация точек для графика
        List<double[]> graphData = generateParabolaPoints(span, height);

        return ArchitecturalCalculation.builder()
                .type("parabola")
                .equation(equation)
                .parameters(java.util.Map.of("a", a, "b", a, "p", span / 2))
                .area(area)
                .arcLength(arcLength)
                .volume(volume)
                .weight(weight)
                .totalCost(totalCost)
                .maxStress(maxStress)
                .safetyFactor(safetyFactor)
                .deflection(deflection)
                .bucklingLoad(bucklingLoad)
                .naturalFrequency(naturalFrequency)
                .graphData(graphData)
                .build();
    }

    /**
     * Расчет эллиптического купола
     */
    public ArchitecturalCalculation calculateEllipse(double a, double b, double thickness, Material material) {
        String equation = String.format("x²/%.2f² + y²/%.2f² = 1", a, b);

        // Расчет площади эллипса
        double area = Math.PI * a * b;

        // Расчет длины дуги (формула Рамануджана)
        double arcLength = calculateEllipseArcLength(a, b);

        // Расчет объема
        double volume = area * thickness;

        // Расчет материала
        double weight = volume * material.getDensity();
        double totalCost = volume * material.getCostPerUnit();

        // Генерация точек для графика
        List<double[]> graphData = generateEllipsePoints(a, b);

        return ArchitecturalCalculation.builder()
                .type("ellipse")
                .equation(equation)
                .parameters(java.util.Map.of("a", a, "b", b))
                .area(area)
                .arcLength(arcLength)
                .volume(volume)
                .weight(weight)
                .totalCost(totalCost)
                .graphData(graphData)
                .build();
    }

    /**
     * Расчет гиперболической башни
     */
    public ArchitecturalCalculation calculateHyperbola(double a, double b, double thickness, Material material) {
        String equation = String.format("x²/%.2f² - y²/%.2f² = 1", a, b);

        // Упрощенный расчет площади
        double area = Math.PI * a * b * 0.5;

        // Упрощенный расчет длины дуги
        double arcLength = Math.PI * (a + b) * 0.5;

        // Расчет объема
        double volume = area * thickness;

        // Расчет материала
        double weight = volume * material.getDensity();
        double totalCost = volume * material.getCostPerUnit();

        return ArchitecturalCalculation.builder()
                .type("hyperbola")
                .equation(equation)
                .parameters(java.util.Map.of("a", a, "b", b))
                .area(area)
                .arcLength(arcLength)
                .volume(volume)
                .weight(weight)
                .totalCost(totalCost)
                .build();
    }

    /**
     * Расчет длины дуги параболы
     */
    private double calculateParabolaArcLength(double span, double height) {
        double a = 4 * height / (span * span);
        double s = span / 2;
        return s * Math.sqrt(1 + 4 * a * a * s * s) +
               (1 / (2 * a)) * Math.log(2 * a * s + Math.sqrt(1 + 4 * a * a * s * s));
    }

    /**
     * Расчет длины дуги эллипса (формула Рамануджана)
     */
    private double calculateEllipseArcLength(double a, double b) {
        double h = Math.pow((a - b) / (a + b), 2);
        return Math.PI * (a + b) * (1 + (3 * h) / (10 + Math.sqrt(4 - 3 * h)));
    }

    /**
     * Генерация точек для графика параболы
     */
    private List<double[]> generateParabolaPoints(double span, double height) {
        List<double[]> points = new ArrayList<>();
        double a = 4 * height / (span * span);
        double step = span / 100;

        for (double x = -span/2; x <= span/2; x += step) {
            double y = a * x * x;
            points.add(new double[]{x, y});
        }

        return points;
    }

    /**
     * Генерация точек для графика эллипса
     */
    private List<double[]> generateEllipsePoints(double a, double b) {
        List<double[]> points = new ArrayList<>();
        double step = (2 * Math.PI) / 100;

        for (double t = 0; t <= 2 * Math.PI; t += step) {
            double x = a * Math.cos(t);
            double y = b * Math.sin(t);
            points.add(new double[]{x, y});
        }

        return points;
    }
}

