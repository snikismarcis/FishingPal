package com.fishingpal.FishingPal.api;

import com.fishingpal.FishingPal.api.dto.ConditionsResponseDto;
import com.fishingpal.FishingPal.service.ConditionsService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conditions")
public class ConditionsController {

    private final ConditionsService conditionsService;

    public ConditionsController(ConditionsService conditionsService) {
        this.conditionsService = conditionsService;
    }

    @GetMapping
    public ConditionsResponseDto getConditions(
            @RequestParam(defaultValue = "56.9") double lat,
            @RequestParam(defaultValue = "24.1") double lon) {
        return conditionsService.getCurrentConditions(lat, lon);
    }
}
