package com.ntnn.verticleSession;

import com.ntnn.utils.GenKey;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AliceSessionVerticle extends AbstractVerticle {
    private final String ALICE_KEY = "12345678";
    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();
        vertx.setPeriodic(3000, v -> {eventBus.publish("trentSession", "");});
        eventBus.consumer("trentSendAlice", message -> {
            JsonObject jo = new JsonObject(message.body().toString());
            String alice = jo.getString("alice");
            String deAlice = GenKey.getInstance().decrypt(alice.getBytes(), ALICE_KEY);
            String bob = jo.getString("bob");
            eventBus.publish("sendBob", bob);
            eventBus.consumer("sendAlice", message1 -> {
                if(deAlice.equals(message1.body())) {
                    log.info("Login Success");
                }
            });
        });
    }
}
