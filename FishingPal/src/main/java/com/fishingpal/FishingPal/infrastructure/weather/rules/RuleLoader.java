package com.fishingpal.FishingPal.infrastructure.weather.rules;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class RuleLoader {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode getRule(String metric) {
        try {

            String path = "rules/" + metric + ".json";

            InputStream inputStream =
                    new ClassPathResource(path).getInputStream();

            return objectMapper.readTree(inputStream);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load rule: " + metric, e);
        }
    }
}