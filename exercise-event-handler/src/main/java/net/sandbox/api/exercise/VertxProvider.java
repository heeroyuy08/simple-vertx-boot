package net.sandbox.api.exercise;

import com.hazelcast.core.HazelcastInstance;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by eagleeyeyuy on 26/4/2017.
 */
@Component
public class VertxProvider {

    private static final Logger LOG = LoggerFactory.getLogger(VertxProvider.class);

    @Inject
    private HazelcastInstance hz;

    private Vertx vertx;

    @PostConstruct
    void init() throws ExecutionException, InterruptedException {
        VertxOptions options = new VertxOptions()
                .setClusterManager(new HazelcastClusterManager(hz));
        CompletableFuture<Vertx> f = new CompletableFuture<>();
        Vertx.clusteredVertx(options, ar -> f.complete(ar.result()));
        vertx = f.get();
    }

    /**
     * Exposes the clustered Vert.x instance.
     * We must disable destroy method inference, otherwise Spring will call the {@link Vertx#close()} automatically.
     */
    @Bean(destroyMethod = "")
    Vertx vertx() {
        return vertx;
    }

    @PreDestroy
    void close() throws ExecutionException, InterruptedException {
        vertx.close(ar -> new CompletableFuture<>().complete(null));
    }
}
