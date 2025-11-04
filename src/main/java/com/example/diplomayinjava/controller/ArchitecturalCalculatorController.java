package com.example.diplomayinjava.controller;

import com.example.diplomayinjava.dto.ArchitecturalRequest;
import com.example.diplomayinjava.model.ArchitecturalCalculation;
import com.example.diplomayinjava.model.Material;
import com.example.diplomayinjava.service.ArchitecturalCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/architectural")
@CrossOrigin(origins = "*")
public class ArchitecturalCalculatorController {

    @Autowired
    private ArchitecturalCalculationService calculationService;

    /**
     * Расчет архитектурной конструкции
     */
    @PostMapping("/calculate")
    public ResponseEntity<ArchitecturalCalculation> calculate(@RequestBody ArchitecturalRequest request) {
        try {
            // Создание объекта материала из запроса
            Material material = new Material();
            material.setName(request.getMaterialName());
            material.setDensity(request.getDensity());
            material.setCostPerUnit(request.getCostPerUnit());
            material.setStrength(request.getStrength());
            material.setElasticity(request.getElasticity());
            material.setUnit("м³");

            ArchitecturalCalculation result;

            switch (request.getType()) {
                case "parabola":
                    result = calculationService.calculateParabola(
                            request.getSpan(),
                            request.getHeight(),
                            request.getThickness(),
                            material
                    );
                    break;
                case "ellipse":
                    result = calculationService.calculateEllipse(
                            request.getA(),
                            request.getB(),
                            request.getThickness(),
                            material
                    );
                    break;
                case "hyperbola":
                    result = calculationService.calculateHyperbola(
                            request.getA(),
                            request.getB(),
                            request.getThickness(),
                            material
                    );
                    break;
                default:
                    return ResponseEntity.badRequest().build();
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Получение списка материалов
     */
    @GetMapping("/materials")
    public ResponseEntity<Material[]> getMaterials() {
        Material[] materials = {
                new Material("Бетон", 2400.0, 5000.0, "м³", 25.0, 30.0, 1.7, "structural", "Основной строительный материал"),
                new Material("Сталь", 7850.0, 80000.0, "м³", 400.0, 200.0, 50.0, "structural", "Высокопрочный материал"),
                new Material("Алюминий", 2700.0, 120000.0, "м³", 200.0, 70.0, 205.0, "structural", "Легкий металл"),
                new Material("Стекло", 2500.0, 15000.0, "м³", 50.0, 70.0, 1.0, "decorative", "Прозрачный материал"),
                new Material("Дерево", 600.0, 15000.0, "м³", 40.0, 12.0, 0.15, "structural", "Экологичный материал"),
                new Material("Кирпич", 1800.0, 8000.0, "м³", 15.0, 10.0, 0.8, "structural", "Традиционный материал")
        };

        return ResponseEntity.ok(materials);
    }
}

