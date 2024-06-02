package com.ai.xstack.kolesnyk.service;

import com.ai.xstack.kolesnyk.dto.CreateDietDto;
import com.ai.xstack.kolesnyk.dto.MealDto;

import java.util.List;

public interface DietService {

    List<MealDto> generateDayRation(CreateDietDto createDietDto);

}
