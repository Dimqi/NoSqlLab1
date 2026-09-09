package com.example.nosqllab1.favorites;

import com.example.nosqllab1.products.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/favorite")
@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

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

    private Long getUserId() { //тут надо получать ид пользователя из security context но пока так
        return 1L;
    }
}
