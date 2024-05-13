package com.epam.ai.xstack.kolesnyk.service.impl;

import com.epam.ai.xstack.kolesnyk.dto.CreateDietDto;
import com.epam.ai.xstack.kolesnyk.dto.DishDto;
import com.epam.ai.xstack.kolesnyk.dto.MealDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class DietServiceImplTest {

    @InjectMocks
    private DietServiceImpl dietService;

    @Test
    void generateDayRation() {
        CreateDietDto createDietDto = new CreateDietDto(5, 2500, .4d, .3d, .3d);

        List<MealDto> meals = dietService.generateDayRation(createDietDto);

        meals.forEach(System.out::println);
    }

}