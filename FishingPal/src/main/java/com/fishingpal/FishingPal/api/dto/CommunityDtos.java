package com.fishingpal.FishingPal.api.dto;

import java.time.Instant;
import java.util.UUID;

public class CommunityDtos {

    public record CommunityPostResponse(
            UUID id,
            String imageUrl,
            String caption,
            String username,
            Instant createdAt
    ) {}
}
