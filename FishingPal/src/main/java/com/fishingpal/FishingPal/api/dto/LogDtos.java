package com.fishingpal.FishingPal.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public class LogDtos {

    public record LogPostRequest(
            @NotBlank @Size(max = 500) String content
    ) {}

    public record LogPostResponse(
            UUID id,
            String content,
            String username,
            Instant createdAt,
            long likeCount,
            boolean likedByMe
    ) {}
}
