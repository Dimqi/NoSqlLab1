package com.example.nosqllab1.products;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private ArrayList<ProductResponse> products = new ArrayList<>(List.of(
            new ProductResponse(1L, "пиво", "светлое нефильтрованное", new BigDecimal("120.99")),
            new ProductResponse(2L, "пицца", "4 сыра 25см", new BigDecimal("550.99"))
    )); //вместо этого потом бд будет

    public List<ProductResponse> getProducts() {
        return products;
        // тут получение из бд вместо фиксированного листа
    }

    public ProductResponse getProductById(Long id) {
        ProductResponse productResponse = products.stream()
                .filter(product -> product.id().equals(id))
                .findFirst()
                .orElse(null);
        if (productResponse == null) {
            throw new ProductNotFoundException(String.format("Product with id %s not found", id));
        }
        return productResponse;
        //тут поиск в бд вместо листа
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
