package com.example.nosqllab1.repository;

import com.basho.riak.client.api.RiakClient;
import com.example.nosqllab1.models.OperationLog;
import org.springframework.stereotype.Repository;

@Repository
public class OperationRepository extends RiakRepository<OperationLog> {
    public OperationRepository(RiakClient client) {
        super(client, "operation_log", OperationLog.class);
    }

    @Override
    protected String extractId(OperationLog entity) {
        return entity.id();
    }
}
