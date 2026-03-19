package com.fishingpal.FishingPal.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/species")
public class SpeciesController {

    @GetMapping
    public List<String> getSpecies() {
        return List.of("perch", "pike", "bream");
    }
}
