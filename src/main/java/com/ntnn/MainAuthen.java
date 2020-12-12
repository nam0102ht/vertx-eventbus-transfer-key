package com.ntnn;

import com.ntnn.verticleAuthenticate.AliceAuthentication;
import com.ntnn.verticleAuthenticate.BobAuthentication;
import com.ntnn.verticleAuthenticate.TrentAuthentication;
import com.ntnn.verticleSession.TrentSessionVerticle;
import io.vertx.core.Vertx;

public class MainAuthen {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new AliceAuthentication());
        vertx.deployVerticle(new BobAuthentication());
        vertx.deployVerticle(new TrentAuthentication());
    }
}
