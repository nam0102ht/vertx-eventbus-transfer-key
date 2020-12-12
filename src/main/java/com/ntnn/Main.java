package com.ntnn;

import com.ntnn.verticle.AliceVerticle;
import com.ntnn.verticle.BobVerticle;
import com.ntnn.verticle.MainVerticle;
import io.vertx.core.Vertx;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Main {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle());
        vertx.deployVerticle(new BobVerticle());
        vertx.deployVerticle(new AliceVerticle());
        log.info("Start trent");
    }
}
