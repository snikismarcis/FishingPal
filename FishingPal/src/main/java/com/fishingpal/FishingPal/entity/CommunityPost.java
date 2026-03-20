package com.fishingpal.FishingPal.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "community_post")
public class CommunityPost {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String imageFilename;

    @Column(length = 300)
    private String caption;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected CommunityPost() {}

    public CommunityPost(User user, String imageFilename, String caption) {
        this.user = user;
        this.imageFilename = imageFilename;
        this.caption = caption;
    }

    public UUID getId() { return id; }
    public User getUser() { return user; }
    public String getImageFilename() { return imageFilename; }
    public String getCaption() { return caption; }
    public Instant getCreatedAt() { return createdAt; }
}
