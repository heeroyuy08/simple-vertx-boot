package net.sandbox.api.exercise;

import io.vertx.core.Vertx;
import net.sandbox.api.domain.OptionEvent;
import net.sandbox.api.events.OptionExerciseEvent;
import net.sandbox.api.handlers.VertxEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Created by eagleeyeyuy on 26/4/2017.
 */
@Component
public class OptionExerciseEventHandler implements VertxEventHandler {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    private Vertx vertx;

    @PostConstruct
    public void init() throws Exception {
        consume(vertx, OptionEvent.EXERCISE_EVENT_ADDRESS, this::handle, OptionExerciseEvent.class);
    }

    public void handle(OptionExerciseEvent optionExerciseEvent) {
        log.info(optionExerciseEvent.toString());
    }

}
