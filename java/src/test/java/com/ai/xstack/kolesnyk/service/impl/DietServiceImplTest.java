package com.ai.xstack.kolesnyk.service.impl;

import com.ai.xstack.kolesnyk.dto.CreateDietDto;
import com.ai.xstack.kolesnyk.dto.MealDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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