package net.sandbox.api.exercise;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.inject.Inject;

@SpringBootApplication
public class Application {

    private static Logger log = LoggerFactory.getLogger(Application.class);

    @Inject
    private Vertx vertx;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener
    public void onAppReady(ApplicationReadyEvent e) {
        log.info("vertx is up.... {}", vertx);
    }

}
