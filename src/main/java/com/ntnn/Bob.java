package com.ntnn;

import com.ntnn.verticle.AliceVerticle;
import com.ntnn.verticle.BobVerticle;
import io.vertx.core.Vertx;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Bob {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new BobVerticle());
        log.info("Start Bob");
    }
}
