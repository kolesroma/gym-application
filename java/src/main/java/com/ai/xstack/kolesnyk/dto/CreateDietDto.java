package com.ai.xstack.kolesnyk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDietDto {

    private int numberOfMeals;

    private int kcal;

    private double proteinsCoefficient;

    private double fatsCoefficient;

    private double carbohydratesCoefficient;

}
