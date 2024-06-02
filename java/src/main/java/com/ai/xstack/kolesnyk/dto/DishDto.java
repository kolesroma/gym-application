package com.ai.xstack.kolesnyk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishDto {

    private int massInGrams;

    private String desc;

    private double proteins;

    private double fats;

    private double carbohydrates;

    public double getCalories() {
        return proteins * 4d + fats * 9d + carbohydrates * 4d;
    }

}
