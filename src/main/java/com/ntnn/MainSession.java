package com.ntnn;

import com.ntnn.verticle.AliceVerticle;
import com.ntnn.verticleSession.AliceSessionVerticle;
import com.ntnn.verticleSession.BobSessionVerticle;
import com.ntnn.verticleSession.TrentSessionVerticle;
import io.vertx.core.Vertx;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MainSession {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new BobSessionVerticle());
        vertx.deployVerticle(new TrentSessionVerticle());
        vertx.deployVerticle(new AliceSessionVerticle());
        log.info("Start Alice");
    }
}