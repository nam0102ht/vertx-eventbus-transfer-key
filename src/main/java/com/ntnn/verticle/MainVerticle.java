package com.ntnn.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
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
public class MainVerticle extends AbstractVerticle {
    private final static String ALICE_KEY = "12345678";
    private final static String BOB_KEY = "87654321";

    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();
        eventBus.consumer("sentTrent", message -> {
            String mess = decrypt(message.body().toString().getBytes(), ALICE_KEY);
            eventBus.publish("sentBob", encrypt(mess, BOB_KEY));
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
    public String encrypt(String plainText, String KEY) {
        Cipher cipher = null;
        String encrypted = "";
        SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes(), "DES");
        try {
            cipher = Cipher.getInstance("DES/ECB/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] byteEncrypted = cipher.doFinal(plainText.getBytes());
            encrypted =  Base64.getEncoder().encodeToString(byteEncrypted);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException |
                IllegalBlockSizeException | InvalidKeyException  e) {
            log.error(e.getMessage());
        }
        return encrypted;
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
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException  e) {
            log.error(e.getMessage());
        }
        return decrypted;
    }
}
