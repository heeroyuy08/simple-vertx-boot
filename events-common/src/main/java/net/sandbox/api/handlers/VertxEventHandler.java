package net.sandbox.api.handlers;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/**
 * Created by eagleeyeyuy on 2/5/2017.
 */
public interface VertxEventHandler {

    final Logger log = LoggerFactory.getLogger(VertxEventHandler.class);

    /**
     * Executes function in vertx worker thread pool. This prevents blocking of vertx event loop due to
     * long running thread-blocking message handling, i.e. DPS web service call, JDBC calls, etc.
     *
     * @param vertx
     * @param busAddress
     * @param consumer   event handling function
     * @param type       the event type
     */
    default <T> void consume(Vertx vertx, String busAddress, Consumer<T> consumer, Class<T> type) {
        MessageConsumer<String> c = vertx.eventBus().consumer(busAddress);
        c.completionHandler(res -> {
            log.info("listening to address '{}'->{}", busAddress, res.succeeded());
        });

        c.handler(msg -> {
            log.info(Thread.currentThread().getName());
            log.info("seen request->{}", msg.body().toString());
            vertx.executeBlocking(future -> {
                try {
                    log.info("inside blocking " + Thread.currentThread().getName());
                    T event = new JsonObject(msg.body().toString()).mapTo(type);
                    consumer.accept(event);
                    msg.reply(JsonObject.mapFrom(event).toString());
                    future.complete();
                } catch (Exception ex) {
                    future.fail(ex);
                }
            }, ar -> {
                if (ar.failed()) {
                    ar.cause().printStackTrace();
                }
            });
        });
    }

    /**
     * Executes message handling function inside the Vertx event loop. Please make sure that
     * the handler will execute very fast otherwise it might block the vertx event loop.
     *
     * @param vertx
     * @param busAddress
     * @param consumer
     */
    default <T> void consumeBlocking(Vertx vertx, String busAddress, Consumer<T> consumer, Class<T> type) {
        MessageConsumer<String> c = vertx.eventBus().consumer(busAddress);
        c.completionHandler(res -> {
            log.info("listening to address '{}'->{}", busAddress, res.succeeded());
        });

        c.handler(msg -> {
            log.info("nonblocking " + Thread.currentThread().getName());
            T event = new JsonObject(msg.body().toString()).mapTo(type);
            consumer.accept(event);
            msg.reply(JsonObject.mapFrom(event).toString());
        });
    }

}
