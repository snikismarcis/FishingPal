package com.fishingpal.FishingPal.api;

import com.fishingpal.FishingPal.api.dto.LogDtos.*;
import com.fishingpal.FishingPal.entity.LogPost;
import com.fishingpal.FishingPal.entity.PostLike;
import com.fishingpal.FishingPal.entity.User;
import com.fishingpal.FishingPal.repository.LogPostRepository;
import com.fishingpal.FishingPal.repository.PostLikeRepository;
import com.fishingpal.FishingPal.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogPostRepository logPostRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;

    public LogController(LogPostRepository logPostRepository,
                         PostLikeRepository postLikeRepository,
                         UserRepository userRepository) {
        this.logPostRepository = logPostRepository;
        this.postLikeRepository = postLikeRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<LogPostResponse> getAllPosts(Authentication authentication) {
        String currentUsername = authentication != null && authentication.isAuthenticated()
                ? authentication.getName() : null;
        User currentUser = currentUsername != null
                ? userRepository.findByUsername(currentUsername).orElse(null) : null;

        return logPostRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(post -> toResponse(post, currentUser))
                .toList();
    }

    @PostMapping
    public ResponseEntity<LogPostResponse> createPost(
            @Valid @RequestBody LogPostRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        var post = logPostRepository.save(new LogPost(user, req.content()));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(post, user));
    }

    @PostMapping("/{id}/like")
    @Transactional
    public LogPostResponse toggleLike(@PathVariable UUID id,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        LogPost post = logPostRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (postLikeRepository.existsByUserAndPost(user, post)) {
            postLikeRepository.deleteByUserAndPost(user, post);
        } else {
            postLikeRepository.save(new PostLike(user, post));
        }
        return toResponse(post, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        LogPost post = logPostRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!post.getUser().getUsername().equals(userDetails.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your post");
        }
        logPostRepository.delete(post);
        return ResponseEntity.noContent().build();
    }

    private LogPostResponse toResponse(LogPost post, User currentUser) {
        long likeCount = postLikeRepository.countByPost(post);
        boolean likedByMe = currentUser != null && postLikeRepository.existsByUserAndPost(currentUser, post);
        return new LogPostResponse(
                post.getId(),
                post.getContent(),
                post.getUser().getUsername(),
                post.getCreatedAt(),
                likeCount,
                likedByMe
        );
    }
}
