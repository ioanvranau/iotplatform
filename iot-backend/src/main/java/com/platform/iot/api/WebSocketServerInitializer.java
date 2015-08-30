package com.platform.iot.api;

import com.platform.iot.api.codec.json.JsonMessageDecoder;
import com.platform.iot.api.codec.json.JsonMessageEncoder;
import com.platform.iot.api.message.MessageHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Websocket server initializer.
 * Establishes the processing pipeline chain
 */
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("codec", new HttpRequestDecoder());
//        pipeline.addLast("codec-http", new HttpServerCodec());
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("websocket-handler", new WebSocketServerHandler());
        pipeline.addLast("message-codec", new JsonMessageDecoder());
        pipeline.addLast("message-encoder", new JsonMessageEncoder());
        pipeline.addLast("bl-handler", new MessageHandler());
    }
}
