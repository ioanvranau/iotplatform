package com.platform.iot.api.web;

import com.platform.iot.api.Config;
import org.jboss.resteasy.plugins.server.sun.http.HttpContextBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by magdalena.gherasim on 6/23/2014.
 */
public class HttpServer {

    public static void start() {
        com.sun.net.httpserver.HttpServer httpServer = null;
        try {
            httpServer = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(Config.INSTANCE.getWebAPIPort()), 10);
        } catch (IOException e) {
            return;
        }
        HttpContextBuilder contextBuilder = new HttpContextBuilder();
        contextBuilder.getDeployment().getActualResourceClasses().add(HttpServerHandler.class);
        com.sun.net.httpserver.HttpContext context = contextBuilder.bind(httpServer);
        context.getAttributes().put("some.config.info", "42");
        httpServer.start();

//        contextBuilder.cleanup();
//        httpServer.stop(0);
    }
}
