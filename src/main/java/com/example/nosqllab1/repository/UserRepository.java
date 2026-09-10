package com.example.nosqllab1.repository;

import com.basho.riak.client.api.RiakClient;
import com.example.nosqllab1.models.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends RiakRepository<User>{

    public UserRepository(RiakClient client, String bucketName, Class<User> entityClass) {
        super(client, bucketName, entityClass);
    }

    @Override
    protected String extractId(User entity) {
        return entity.getId();
    }

}
