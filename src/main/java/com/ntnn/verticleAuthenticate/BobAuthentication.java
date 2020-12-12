package com.ntnn.verticleAuthenticate;

import com.ntnn.utils.GenKey;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class BobAuthentication extends AbstractVerticle {
    private final static String BOB_KEY = "87654321";
    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();
        eventBus.consumer("aliceSentBob", handler -> {
            JsonObject obj = new JsonObject(handler.body().toString());;
            if(obj.getString("username").equals("Alice")) {
                eventBus.publish("bobToAlice", new JsonObject()
                        .put("key", GenKey.getInstance().decrypt(obj.getString("password").getBytes(), BOB_KEY))
                        .put("message", "Hello Alice, I'm Bob")
                        .toString());
            }
        });
    }
}
