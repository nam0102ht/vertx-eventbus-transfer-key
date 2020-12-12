package com.ntnn.verticleAuthenticate;

import com.ntnn.utils.GenKey;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class AliceAuthentication extends AbstractVerticle {
    private final static String ALICE_KEY = "12345678";
    @Override
    public void start() throws Exception {
        List<String> list = new ArrayList<>();
        EventBus event = vertx.eventBus();
        vertx.setPeriodic(1000, v -> {
            event.publish("aliceAuthentication", "");
        });
        event.consumer("trentSentAlice", handler -> {
            JsonObject jo = new JsonObject(handler.body().toString());
            JsonObject alice = jo.getJsonObject("alice");
            JsonObject bob = jo.getJsonObject("bob");
            if(alice.getString("username").equals("Bob")) {
                String plainText = GenKey.getInstance().decrypt(alice.getString("password").getBytes(), ALICE_KEY);
                list.add(plainText);
                event.publish("aliceSentBob", bob.toString());
            }
        });
        event.consumer("bobToAlice", handler -> {
            JsonObject obj = new JsonObject(handler.body().toString());
            String key = obj.getString("key");
            if(list.get(0).equals(key)) {
                log.info("Message: " + obj.getString("message"));
                list.remove(0);
            }
        });
    }
}
