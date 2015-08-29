package com.platform.iot;

/**
 * Created by vranau on 8/29/2015.
 */

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import io.netty.channel.EventLoopGroup;

import java.net.InetAddress;

public class WebSocketServer {
//    Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    private final int port;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private boolean running;
    public static final int DEFAULT_PORT = 8086;

    public WebSocketServer(int port) {
        this.port = port;
    }

    public WebSocketServer() {
        this.port = 8086;
    }

    public void run() throws Exception {
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup(1000);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Integer.valueOf(10000));
            bootstrap.option(ChannelOption.IP_MULTICAST_ADDR, InetAddress.getByName("localhost"));
            ((ServerBootstrap)bootstrap.group(this.bossGroup, this.workerGroup).channel(NioServerSocketChannel.class)).childHandler(new WebSocketServerInitializer());
            ChannelFuture syncFuture = bootstrap.bind(this.port).sync();
            syncFuture.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        WebSocketServer.this.running = true;
                    }

                }
            });
            Channel channel = syncFuture.channel();
            System.out.println("Web socket server started at port " + this.port + '.');
            System.out.println("Open your browser and navigate to http://localhost:" + this.port + '/');
            ChannelFuture closeFuture = channel.closeFuture();
            ChannelFuture done = closeFuture.sync();
            done.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    WebSocketServer.this.running = false;
                }
            });
        } finally {
            this.bossGroup.shutdownGracefully();
            this.workerGroup.shutdownGracefully();
        }

    }

    public void shutdown() {
        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();
    }

    public boolean isRunning() {
        return this.running;
    }

    public static void main(String[] args) {
        try {
            new WebSocketServer().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
