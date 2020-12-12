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
public class BobChallenge extends AbstractVerticle {
    private final static String BOB_KEY = "87654321";
    @Override
    public void start() throws Exception {
        List<String> list = new ArrayList<>();
        EventBus eventBus = vertx.eventBus();
        eventBus.consumer("aliceSentBob", handler -> {
            JsonObject obj = new JsonObject(handler.body().toString());;
            if(obj.getString("username").equals("Alice")) {
                if(!covertDate(obj.getLong("nonce"))) {
                    log.info("Wrong noce");
                    return;
                }
                list.add(GenKey.getInstance().decrypt(obj.getString("password").getBytes(), BOB_KEY));
                eventBus.publish("bobToAlice", new JsonObject()
                        .put("key", GenKey.getInstance().decrypt(obj.getString("password").getBytes(), BOB_KEY))
                        .put("message", "Hello Alice, I'm Bob")
                        .put("nonce", System.currentTimeMillis())
                        .toString());
            }
        });
        eventBus.consumer("challengeNonce", hanlder -> {
            JsonObject obj = new JsonObject(hanlder.body().toString());
            if(list.get(0).equals(obj.getString("key"))) {
                log.info(obj.toString());
                list.remove(0);
            }
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
