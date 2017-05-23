package net.sandbox.api.events;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Created by eagleeyeyuy on 2/5/2017.
 */
public interface VertxEventSource {

    Logger log = LoggerFactory.getLogger(VertxEventSource.class);

    default <T> CompletableFuture<?> sendEvent(Vertx vertx, String busAddress, T event) {
        CompletableFuture<Object> f = new CompletableFuture<>();

        vertx.eventBus().send(busAddress,
                JsonObject.mapFrom(event).toString(),
                res -> {
                    if (res.succeeded()) {
                        log.info("sent {}", event);
                        Optional.of(res.result()).ifPresent(
                                result -> log.info("send() success. RESULT:{}", result.body()));
                        f.complete(res.result().body());
                    } else {
                        if (res.cause().getMessage().contains("No handlers for address")) {
                            log.error("event handler is DOWN!, please check with IT support", res.cause());
                        } else {
                            log.error("send() failed. ERROR:{}", res.cause().getMessage(), res.cause());
                        }
                        f.completeExceptionally(res.cause());
                    }
                });

        return f;
    }

}
