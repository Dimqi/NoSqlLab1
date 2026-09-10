package com.example.nosqllab1.favorites;

import com.example.nosqllab1.models.User;
import com.example.nosqllab1.products.ProductResponse;
import com.example.nosqllab1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/favorite")
@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getFavorites() {
        Long userId = getUserId();
        return ResponseEntity.ok(favoriteService.getUsersFavorites(userId));
    }

    @PostMapping("/{productId}")
    public ResponseEntity<Void> addFavorite(@PathVariable Long productId) {
        Long userId = getUserId();
        favoriteService.addFavorite(userId, productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long productId) {
        Long userId = getUserId();
        favoriteService.deleteFavorite(userId, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private Long getUserId() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findAll().stream()
                .filter(u -> u.getName().equalsIgnoreCase(currentUsername))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No user in context"));

        return Long.parseLong(user.getId());
    }
}