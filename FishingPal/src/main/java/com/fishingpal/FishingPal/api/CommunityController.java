package com.fishingpal.FishingPal.api;

import com.fishingpal.FishingPal.api.dto.CommunityDtos.CommunityPostResponse;
import com.fishingpal.FishingPal.entity.CommunityPost;
import com.fishingpal.FishingPal.entity.User;
import com.fishingpal.FishingPal.repository.CommunityPostRepository;
import com.fishingpal.FishingPal.repository.UserRepository;
import com.fishingpal.FishingPal.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/community")
public class CommunityController {

    private final CommunityPostRepository communityPostRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public CommunityController(CommunityPostRepository communityPostRepository,
                               UserRepository userRepository,
                               FileStorageService fileStorageService) {
        this.communityPostRepository = communityPostRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public List<CommunityPostResponse> getAll() {
        return communityPostRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping
    public ResponseEntity<CommunityPostResponse> create(
            @RequestParam("image") MultipartFile image,
            @RequestParam(value = "caption", required = false, defaultValue = "") String caption,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (image.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image is required");
        }

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        try {
            String filename = fileStorageService.store(image);
            var post = communityPostRepository.save(new CommunityPost(user, filename, caption));
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(post));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store image");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        CommunityPost post = communityPostRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!post.getUser().getUsername().equals(userDetails.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your post");
        }
        try {
            fileStorageService.delete(post.getImageFilename());
        } catch (IOException ignored) {}
        communityPostRepository.delete(post);
        return ResponseEntity.noContent().build();
    }

    private CommunityPostResponse toResponse(CommunityPost post) {
        return new CommunityPostResponse(
                post.getId(),
                "/uploads/community/" + post.getImageFilename(),
                post.getCaption(),
                post.getUser().getUsername(),
                post.getCreatedAt()
        );
    }
}
