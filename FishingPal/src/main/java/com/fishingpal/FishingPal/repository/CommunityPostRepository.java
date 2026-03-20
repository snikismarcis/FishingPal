package com.fishingpal.FishingPal.repository;

import com.fishingpal.FishingPal.entity.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, UUID> {
    List<CommunityPost> findAllByOrderByCreatedAtDesc();
}
