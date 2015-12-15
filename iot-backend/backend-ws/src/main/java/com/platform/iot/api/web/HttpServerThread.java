package com.platform.iot.api.web;

/**
 * Created by magdalena.gherasim on 6/23/2014.
 */
public class HttpServerThread extends Thread {

    public HttpServerThread() {
        setName("Http Server");
    }

    @Override
    public void run() {
        HttpServer.start();
    }

}
