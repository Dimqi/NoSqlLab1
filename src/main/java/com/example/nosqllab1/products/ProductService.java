package com.example.nosqllab1.products;

import com.example.nosqllab1.models.Product;
import com.example.nosqllab1.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getProducts() {
        // to do
        return null
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return ProductResponse.fromEntity(product);
    }

    public ProductResponse createProduct(ProductRequest productRequest) {
        ProductResponse productResponse = new ProductResponse(products.getLast().id() + 1,
                productRequest.name(),
                productRequest.description(),
                productRequest.price());
        products.add(productResponse);
        return productResponse;
        //тут сохранение в бд
    }

    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        boolean found = products.removeIf(product -> product.id().equals(id));
        if (found) {
            ProductResponse productResponse = new ProductResponse(id,
                    productRequest.name(),
                    productRequest.description(),
                    productRequest.price());
            products.add(productResponse);
            return productResponse;
        }
        throw new ProductNotFoundException(String.format("Product with id %s not found", id));
        //тут обновление в бд
    }

    public void deleteProductById(Long id) {
        boolean found = products.removeIf(product -> product.id().equals(id));
        if (!found) {
            throw new ProductNotFoundException(String.format("Product with id %s not found", id));
        }
        //тут удаление из бд
    }
}
