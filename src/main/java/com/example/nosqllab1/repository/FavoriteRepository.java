package com.example.nosqllab1.repository;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.commands.datatypes.FetchSet;
import com.basho.riak.client.api.commands.datatypes.SetUpdate;
import com.basho.riak.client.api.commands.datatypes.UpdateSet;
import com.basho.riak.client.core.query.Location;
import com.basho.riak.client.core.query.Namespace;
import com.basho.riak.client.core.query.crdt.types.RiakSet;
import com.basho.riak.client.core.util.BinaryValue;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Repository
public class FavoriteRepository {

    private final RiakClient client;
    private final Namespace namespace;

    public FavoriteRepository(RiakClient client) {
        this.client = client;
        this.namespace = new Namespace("sets", "favorites");
    }

    public Set<Long> findProductIdsByUserId(Long userId) {
        try {
            Location location = new Location(namespace, String.valueOf(userId));
            FetchSet fetch = new FetchSet.Builder(location).build();
            FetchSet.Response response = client.execute(fetch);

            RiakSet riakSet = response.getDatatype();
            if (riakSet == null) {
                return Collections.emptySet();
            }

            return riakSet.view().stream()
                    .map(BinaryValue::toString)
                    .map(Long::valueOf)
                    .collect(Collectors.toSet());

        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Ошибка чтения CRDT Set из Riak", e);
        }
    }

    public void addProductId(Long userId, Long productId) {
        Location location = new Location(namespace, String.valueOf(userId));
        SetUpdate setUpdate = new SetUpdate().add(BinaryValue.create(String.valueOf(productId)));
        UpdateSet update = new UpdateSet.Builder(location, setUpdate).build();

        executeUpdate(update);
    }

    public void removeProductId(Long userId, Long productId) {
        Location location = new Location(namespace, String.valueOf(userId));
        SetUpdate setUpdate = new SetUpdate().remove(BinaryValue.create(String.valueOf(productId)));
        UpdateSet update = new UpdateSet.Builder(location, setUpdate).build();

        executeUpdate(update);
    }

    private void executeUpdate(UpdateSet updateCommand) {
        try {
            client.execute(updateCommand);
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Ошибка записи CRDT Set в Riak", e);
        }
    }
}