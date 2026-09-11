package com.example.nosqllab1.repository;

import com.basho.riak.client.api.RiakClient;
import com.example.nosqllab1.models.Product;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository extends RiakRepository<Product>{

    public ProductRepository(RiakClient client) {
        super(client, "products", Product.class);
    }

    @Override
    protected String extractId(Product entity) {
        return entity.getId();
    }


}
