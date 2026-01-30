package com.fishingpal.FishingPal.api;

import com.fishingpal.FishingPal.api.dto.ConditionsResponseDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fishingpal.FishingPal.service.ConditionsService;
import org.springframework.web.bind.annotation.RequestMapping;
@RestController
@RequestMapping("/conditions")
    public class ConditionsController {

    private final ConditionsService conditionsService;

    public ConditionsController(ConditionsService conditionsService) {
        this.conditionsService = conditionsService;
    }

    @GetMapping
    public ConditionsResponseDto getConditions() {
        return conditionsService.getCurrentConditions();
    }
}