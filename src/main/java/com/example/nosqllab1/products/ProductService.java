package com.example.nosqllab1.products;

import com.example.nosqllab1.models.Product;
import com.example.nosqllab1.repository.ProductRepository;
import com.example.nosqllab1.riakservices.RiakCounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final RiakCounterService riakCounterService;

    public List<ProductResponse> getProducts() {
        List<Product> products = productRepository.findAll();

        List<ProductResponse> productsResponse = products.stream()
                .map(product -> ProductResponse.fromEntity(product))
                .toList();


        return productsResponse;
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return ProductResponse.fromEntity(product);
    }


    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        long id = incrementProductCounter();

        product.setId(String.valueOf(id));
        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());

        productRepository.save(product);

        return ProductResponse.fromEntity(product);
    }


    public void deleteProductById(Long id) {
        productRepository.delete(String.valueOf(id));

    }

    private long incrementProductCounter(){
        try {
            return riakCounterService.generateNextId(Product.class);
        }catch(ExecutionException | InterruptedException e){
            throw new RuntimeException("error increment counter for products");
        }
    }
}
