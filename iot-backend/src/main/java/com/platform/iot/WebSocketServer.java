package com.platform.iot;

import com.platform.iot.WebSocketServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The websocket server class
 */
public class WebSocketServer {

    Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    private final int port;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private boolean running;

    public WebSocketServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        bossGroup = new NioEventLoopGroup(); // parents (acceptors)
        workerGroup = new NioEventLoopGroup(1000); // children (clients)
        try {
            // These EventLoopGroup's are used to handle all the events and IO for SocketChannel and Channel's.
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
//            bootstrap.option(ChannelOption.IP_MULTICAST_ADDR, InetAddress.getByName("localhost"));
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WebSocketServerInitializer());

            ChannelFuture syncFuture = bootstrap.bind(port).sync();
            syncFuture.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        running = true;
                    }
                }
            });
            Channel channel = syncFuture.channel();
            logger.info("Web socket server started at port " + port + '.');
            logger.info("Open your browser and navigate to http://localhost:" + port + '/');

            ChannelFuture closeFuture = channel.closeFuture();
            ChannelFuture done = closeFuture.sync();

            done.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    running = false;
                }
            });
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void shutdown() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public boolean isRunning() {
        return running;
    }
}
