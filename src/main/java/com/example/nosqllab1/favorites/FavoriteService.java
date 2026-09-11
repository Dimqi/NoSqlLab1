package com.example.nosqllab1.favorites;

import com.example.nosqllab1.models.Product;
import com.example.nosqllab1.products.ProductResponse;
import com.example.nosqllab1.repository.FavoriteRepository;
import com.example.nosqllab1.repository.ProductRepository;
import com.example.nosqllab1.operations.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;
    private final OperationService operationService;

    public List<ProductResponse> getUsersFavorites(Long userId) {
        Set<Long> favoriteIds = favoriteRepository.findProductIdsByUserId(userId);

        if (favoriteIds.isEmpty()) {
            return Collections.emptyList();
        }

        return favoriteIds.stream()
                .map(productId -> productRepository.findById(String.valueOf(productId)).orElse(null))
                .filter(Objects::nonNull)
                .map(ProductResponse::fromEntity)
                .toList();
    }

    public void addFavorite(Long userId, Long productId) {
        Product product = productRepository.findById(String.valueOf(productId))
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        operationService.logOperation(userId, String.format("User (id=%s) added product (id=%s) to favs",
                userId,
                productId));
        favoriteRepository.addProductId(userId, Long.valueOf(product.getId()));
    }

    public void deleteFavorite(Long userId, Long productId) {
        operationService.logOperation(userId, String.format("User (id=%s) deleted product (id=%s) from favs",
                userId,
                productId));
        favoriteRepository.removeProductId(userId, productId);
    }
}