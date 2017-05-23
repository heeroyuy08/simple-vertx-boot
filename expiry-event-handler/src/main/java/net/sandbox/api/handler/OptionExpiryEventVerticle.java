package net.sandbox.api.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import net.sandbox.api.domain.OptionEvent;
import net.sandbox.api.events.OptionExpiryEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by eagleeyeyuy on 26/4/2017.
 */
@Component
public class OptionExpiryEventVerticle {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    private Vertx vertx;

    @PostConstruct
    public void init() throws Exception {
        MessageConsumer<String> consumer = vertx.eventBus().consumer(OptionEvent.EXPIRE_EVENT_ADDRESS);
        consumer.completionHandler(res -> {
            log.info("registration({}) -> {}", res.succeeded(), res.result());
        });

        consumer.handler(msg -> {
            try {
                OptionExpiryEvent event = new ObjectMapper().readValue(msg.body(), OptionExpiryEvent.class);
                log.info(event.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            log.info(msg.body());
            msg.reply("success");
        });
    }
}
