package com.fishingpal.FishingPal.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "post_like")
@IdClass(PostLike.PostLikeId.class)
public class PostLike {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private LogPost post;

    private Instant createdAt = Instant.now();

    protected PostLike() {}

    public PostLike(User user, LogPost post) {
        this.user = user;
        this.post = post;
    }

    public User getUser() { return user; }
    public LogPost getPost() { return post; }
    public Instant getCreatedAt() { return createdAt; }

    public static class PostLikeId implements Serializable {
        private UUID user;
        private UUID post;

        public PostLikeId() {}

        public PostLikeId(UUID user, UUID post) {
            this.user = user;
            this.post = post;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PostLikeId that)) return false;
            return Objects.equals(user, that.user) && Objects.equals(post, that.post);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user, post);
        }
    }
}
