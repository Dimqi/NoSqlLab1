package com.example.nosqllab1.repository;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.core.query.indexes.StringBinIndex;
import com.example.nosqllab1.models.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends RiakRepository<User> {

    public UserRepository(RiakClient client) {
        super(client, "users", User.class);
    }

    @Override
    protected String extractId(User entity) {
        return String.valueOf(entity.getId());
    }


}