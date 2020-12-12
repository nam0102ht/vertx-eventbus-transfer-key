package com.ntnn;

import com.ntnn.verticleChallenge.AliceChallenge;
import com.ntnn.verticleChallenge.BobChallenge;
import com.ntnn.verticleChallenge.TrentChallenge;
import io.vertx.core.Vertx;

public class MainChallenge {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new AliceChallenge());
        vertx.deployVerticle(new TrentChallenge());
        vertx.deployVerticle(new BobChallenge());
    }
}
