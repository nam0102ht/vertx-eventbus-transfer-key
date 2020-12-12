package com.ntnn.verticleSession;

import com.ntnn.utils.GenKey;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class BobSessionVerticle extends AbstractVerticle {
    private final String BOB_KEY = "87654321";
    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();
        eventBus.consumer("sendBob", message -> {
            String bob = GenKey.getInstance().decrypt(message.body().toString().getBytes(), BOB_KEY);
            eventBus.publish("sendAlice", bob);
        });
    }
}