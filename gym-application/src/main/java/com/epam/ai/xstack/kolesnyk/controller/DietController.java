package com.epam.ai.xstack.kolesnyk.controller;

import com.epam.ai.xstack.kolesnyk.dto.CreateDietDto;
import com.epam.ai.xstack.kolesnyk.dto.MealDto;
import com.epam.ai.xstack.kolesnyk.service.DietService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diet")
public class DietController {

    private final DietService dietService;

    @PostMapping
    private List<MealDto> generateDayRation(@RequestBody CreateDietDto createDietDto) {
        return dietService.generateDayRation(createDietDto);
    }

}
