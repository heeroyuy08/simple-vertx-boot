package net.sandbox.api.events.resource;

import io.vertx.core.Vertx;
import net.sandbox.api.domain.OptionEvent;
import net.sandbox.api.events.OptionExerciseEvent;
import net.sandbox.api.events.VertxEventSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by eagleeyeyuy on 2/5/2017.
 */
@RestController
public class OptionExerciseResource implements VertxEventSource {

    private Logger log = LoggerFactory.getLogger(getClass());

    private Vertx vertx;

    public OptionExerciseResource(Vertx vertx) {
        this.vertx = checkNotNull(vertx, "vertx must not be null");
    }

    @PostMapping("/exercise")
    public ResponseEntity execute(OptionExerciseEvent optionExerciseEvent) throws ExecutionException, InterruptedException {
        log.info("{}, {}", vertx, this);

        CompletableFuture<?> future = sendEvent(vertx, OptionEvent.EXERCISE_EVENT_ADDRESS, optionExerciseEvent);

        if (future.isCompletedExceptionally()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(future.get());
        }
        return ResponseEntity.ok(future.get());
    }
}
