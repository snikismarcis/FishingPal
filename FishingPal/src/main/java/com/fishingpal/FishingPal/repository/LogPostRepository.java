package com.fishingpal.FishingPal.repository;

import com.fishingpal.FishingPal.entity.LogPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LogPostRepository extends JpaRepository<LogPost, UUID> {
    List<LogPost> findAllByOrderByCreatedAtDesc();
}
