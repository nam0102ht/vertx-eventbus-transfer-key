package com.ntnn.verticleChallenge;

import com.ntnn.utils.GenKey;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Log4j2
public class AliceChallenge extends AbstractVerticle {
    private final static String ALICE_KEY = "12345678";
    @Override
    public void start() throws Exception {
        EventBus event = vertx.eventBus();
        vertx.setPeriodic(1000, v -> {
            event.publish("aliceAuthentication", "");
        });
        event.consumer("trentSentAlice", handler -> {
            JsonObject jo = new JsonObject(handler.body().toString());
            JsonObject alice = jo.getJsonObject("alice");
            JsonObject bob = jo.getJsonObject("bob");
            bob.put("nonce", System.currentTimeMillis());
            String nonce = jo.getString("nonce");
            if(!covertDate(Long.parseLong(nonce))) {
                log.info("Challenge not accept");
                return;
            }
            if(alice.getString("username").equals("Bob")) {
                String plainText = GenKey.getInstance().decrypt(alice.getString("password").getBytes(), ALICE_KEY);
                event.publish("aliceSentBob", bob.toString());
            }
        });
        event.consumer("bobToAlice", handler -> {
            JsonObject obj = new JsonObject(handler.body().toString());
            String key = obj.getString("key");
            if(!covertDate(Long.parseLong(obj.getString("nonce")))) {
                log.info("Challenge not accept");
                return;
            }
            event.publish("challengeNonce", new JsonObject()
                    .put("key", key)
                    .put("message", "Hello Bob, I'm Alice"));
        });
    }
    public boolean covertDate(long time) {
        try {
            Date date = new Date(time);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            Calendar nowCal = Calendar.getInstance();
            if (nowCal.get(Calendar.DATE) == cal.get(Calendar.DATE) &&
            nowCal.get(Calendar.MONTH) == cal.get(Calendar.MONTH) &&
            nowCal.get(Calendar.YEAR) == cal.get(Calendar.YEAR)) {
                return true;
            }
        } catch(Exception ex) {
            log.info(ex.getMessage());
        }
        return false;
    }
}
