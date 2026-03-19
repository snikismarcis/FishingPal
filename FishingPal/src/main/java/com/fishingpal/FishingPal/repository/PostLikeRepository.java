package com.fishingpal.FishingPal.repository;

import com.fishingpal.FishingPal.entity.LogPost;
import com.fishingpal.FishingPal.entity.PostLike;
import com.fishingpal.FishingPal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLike.PostLikeId> {
    boolean existsByUserAndPost(User user, LogPost post);
    void deleteByUserAndPost(User user, LogPost post);
    long countByPost(LogPost post);
}
