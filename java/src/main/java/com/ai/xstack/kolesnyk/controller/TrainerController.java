package com.ai.xstack.kolesnyk.controller;

import com.ai.xstack.kolesnyk.dto.TrainerDto;
import com.ai.xstack.kolesnyk.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trainers")
public class TrainerController {
    private final TrainerService trainerService;

    @GetMapping("/free")
    public List<TrainerDto> getAllFreeTrainees(Principal principal) {
        return trainerService.getAllFreeTrainers(principal.getName());
    }
}
