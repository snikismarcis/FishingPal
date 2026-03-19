package com.fishingpal.FishingPal.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "log_post")
public class LogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected LogPost() {}

    public LogPost(User user, String content) {
        this.user = user;
        this.content = content;
    }

    public UUID getId() { return id; }
    public User getUser() { return user; }
    public String getContent() { return content; }
    public Instant getCreatedAt() { return createdAt; }
}
