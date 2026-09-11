package com.example.nosqllab1.configuration;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.core.RiakCluster;
import com.basho.riak.client.core.RiakNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.UnknownHostException;

@Configuration
public class RiakConfig {

    @Value("${riak.host:127.0.0.1}")
    private String host;

    @Value("${riak.port:8087}")
    private int port;

    @Bean
    public RiakClient riakClient() throws UnknownHostException {
        RiakNode node = new RiakNode.Builder()
                .withRemoteAddress(host)
                .withRemotePort(port)
                .build();
        RiakCluster cluster = new RiakCluster.Builder(node).build();
        cluster.start();
        return new RiakClient(cluster);
    }

}