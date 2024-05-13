package com.epam.ai.xstack.kolesnyk.service;

import com.epam.ai.xstack.kolesnyk.dto.CreateDietDto;
import com.epam.ai.xstack.kolesnyk.dto.MealDto;

import java.util.List;

public interface DietService {

    List<MealDto> generateDayRation(CreateDietDto createDietDto);

}
