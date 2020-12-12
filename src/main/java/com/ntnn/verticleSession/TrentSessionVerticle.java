package com.ntnn.verticleSession;

import com.ntnn.utils.GenKey;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class TrentSessionVerticle extends AbstractVerticle {
    private final String ALICE_KEY = "12345678";
    private final String BOB_KEY = "87654321";
    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();
        eventBus.consumer("trentSession", message -> {
            JsonObject jo = new JsonObject();
            String key = System.currentTimeMillis()+"Key";
            log.info("KEY: "+key);
            jo.put("alice", GenKey.getInstance().encrypt(key, ALICE_KEY));
            jo.put("bob", GenKey.getInstance().encrypt(key, BOB_KEY));
            eventBus.publish("trentSendAlice", jo);
        });
    }
}