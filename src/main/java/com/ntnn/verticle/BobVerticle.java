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
public class BobVerticle extends AbstractVerticle {
    private final static String BOB_KEY = "87654321";

    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();
        eventBus.consumer("sentBob", message -> {
            String mess = decrypt(message.body().toString().getBytes(), BOB_KEY);
            eventBus.publish("sentAlice", new JsonObject()
                    .put("message", "Hello Alice, I'm Bob, your key is K={"+mess+"}")
                    .put("key", mess));
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public String decrypt(byte[] encrypted, String KEY) {
        Cipher cipher = null;
        String decrypted = "";
        SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes(), "DES");
        try {
            cipher = Cipher.getInstance("DES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] byteDecrypted64 = Base64.getDecoder().decode(encrypted);
            byte[] byteDecrypted = cipher.doFinal(byteDecrypted64);
            decrypted = new String(byteDecrypted);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            log.error(e.getMessage());
        }
        return decrypted;
    }
}