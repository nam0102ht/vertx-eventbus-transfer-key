package com.ntnn.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import lombok.extern.log4j.Log4j2;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Log4j2
public class AliceVerticle extends AbstractVerticle {
    private final static String ALICE_KEY = "12345678";

    @Override
    public void start() throws Exception {
        final String randomKey = "alicePassword";
        EventBus eventBus = vertx.eventBus();
        vertx.setPeriodic(5000, v -> {
            log.info("Alice Key: "+randomKey);
            eventBus.publish("sentTrent", encrypt(randomKey, ALICE_KEY));
        });
        eventBus.consumer("sentAlice", message -> {
            log.info(message.body().toString());
            JsonObject jo = new JsonObject(message.body().toString());
            if(randomKey.equals(jo.getString("key"))) {
                log.info("Log in success");
            }
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
    public String encrypt(String plainText, String KEY) {
        String encrypted = "";
        SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes(), "DES");
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] byteEncrypted = cipher.doFinal(plainText.getBytes());
            encrypted =  Base64.getEncoder().encodeToString(byteEncrypted);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException |
                IllegalBlockSizeException | InvalidKeyException  e) {
            log.error(e.getMessage());
        }
        return encrypted;
    }
}