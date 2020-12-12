package com.ntnn.verticleChallenge;

import com.ntnn.utils.GenKey;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

public class TrentChallenge extends AbstractVerticle {
    private final static String ALICE_KEY = "12345678";
    private final static String BOB_KEY = "87654321";
    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();
        eventBus.consumer("aliceAuthentication", handler -> {
            JsonObject alice = new JsonObject();
            String plainText = "password";
            String keyAlice = GenKey.getInstance().encrypt(plainText, ALICE_KEY);
            String keyBob = GenKey.getInstance().encrypt(plainText, BOB_KEY);
            alice.put("username", "Bob");
            alice.put("password", keyAlice);
            JsonObject bob = new JsonObject();
            bob.put("username", "Alice");
            bob.put("password", keyBob);
            JsonObject trent = new JsonObject();
            trent.put("alice", alice);
            trent.put("bob", bob);
            trent.put("nonce", System.currentTimeMillis()+"");
            eventBus.publish("trentSentAlice", trent.toString());
        });
    }
}
