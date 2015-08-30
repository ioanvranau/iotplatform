package com.platform.iot.test;

import com.platform.iot.api.Config;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.Random;

/* * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
//The MIT License
//
//Copyright (c) 2009 Carl Bystršm
//
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to deal
//in the Software without restriction, including without limitation the rights
//to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in
//all copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//THE SOFTWARE.

public class JavaTestClient {

    private static String version = "1.0.0";
    public String migrationToken;
    public String migrationServerAddress;
    public int migrationServerPort;

    public JavaTestClient() {
    }

    public void run(NioEventLoopGroup group) throws Exception {

    }

    public void run(URI uri) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            String protocol = uri.getScheme();
            if (!"ws".equals(protocol)) {
                throw new IllegalArgumentException("Unsupported protocol: " + protocol);
            }

            HttpHeaders customHeaders = new DefaultHttpHeaders();
            customHeaders.add("MyHeader", "MyValue");

            // Connect with V13 (RFC 6455 aka HyBi-17). You can change it to V08 or V00.
            // If you change it to V00, ping is not supported and remember to change
            // HttpResponseDecoder to WebSocketHttpResponseDecoder in the pipeline.
            final JavaTestClientHandler handler =
                    new JavaTestClientHandler(
                            WebSocketClientHandshakerFactory.newHandshaker(
                                    uri, WebSocketVersion.V13, null, false, customHeaders)
                    );

            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("http-codec", new HttpClientCodec());
                            pipeline.addLast("aggregator", new HttpObjectAggregator(8192));
                            pipeline.addLast("ws-handler", handler);
                        }
                    });

            System.out.println("WebSocket Client connecting");
            Channel ch = b.connect(uri.getHost(), uri.getPort()).sync().channel();
            handler.handshakeFuture().sync();
            if (handler.migrationToken != null) {
                ch.write(new TextWebSocketFrame("{type:'migration', username:'magda" + new Random().nextInt() + "', password:'pass', email:'magda@utcn.com', token:'" + migrationToken + "'}"));
            } else {
                ch.write(new TextWebSocketFrame("{type:'register', username:'magda" + new Random().nextInt() +"', password:'pass', email:'magda@utcn.com'}"));
            }

            ChannelFuture closeFuture = ch.closeFuture();
            closeFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    System.out.println("channel closed");
                    if(handler.migrationToken != null){
                        migrationToken = handler.migrationToken;
                        migrationServerAddress = handler.migrationServerAddress;
                        migrationServerPort = handler.migrationServerPort;
                    }
                }
            });
            closeFuture.sync();
        } finally {
            group.shutdownGracefully();
        }

        if (migrationToken != null) {
            System.out.println("Creating new connection ...");
            run(new URI("ws://" + migrationServerAddress + ":" + migrationServerPort + "/prices"));
        }
    }


    public static void main(String[] args) throws Exception {
        String server = findServer();
//        String server = "{hostname='xena-PC', port=8087, minClientVersionSupported=1.0.0}";
//        String server = "puck460.startdedicated.com:8081";

        if (server.isEmpty()) {
            System.out.println("No available server was found!");
            return;
        }
        JSONObject serverJson = (JSONObject) JSONSerializer.toJSON(server);
        URI uri;
        if (args.length > 0) {
            uri = new URI(args[0]);
        } else {
            uri = new URI("ws://" + serverJson.getString("hostname") + ":" + serverJson.getInt("port") + "/prices");
        }
        new JavaTestClient().run(uri);
    }


    public static String findServer() throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();

//        httpclient.getCredentialsProvider().setCredentials(
//                new AuthScope("localhost", 443),
//                new UsernamePasswordCredentials("username", "password"));

        HttpGet httpget = new HttpGet("http://localhost:" + Config.INSTANCE.getWebAPIPort() + "/load-balancer/server?clientVersion=" + version);
//        HttpGet httpget = new HttpGet("http://85.25.246.85:9081/load-balancer/server?clientVersion=" + version);
//        HttpGet httpget = new HttpGet("http://94.242.230.5:9081/load-balancer/server?clientVersion=" + version);

        System.out.println("executing request" + httpget.getRequestLine());
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();

        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        if (entity != null) {
            System.out.println("Response content length: " + entity.getContentLength());
        }
        String entityContents = EntityUtils.toString(entity);
        System.out.println(entityContents);
        if (entity != null) {
            entity.consumeContent();
        }

        // When HttpClient instance is no longer needed,
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        httpclient.getConnectionManager().shutdown();

        return entityContents;
    }

}
