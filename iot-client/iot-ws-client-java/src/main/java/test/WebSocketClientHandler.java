package test;

import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Copyright 2012 The Netty Project
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

public class WebSocketClientHandler extends ChannelInboundMessageHandlerAdapter<Object> {

    private Logger logger = LoggerFactory.getLogger(WebSocketClientHandler.class);

    private final WebSocketClientHandshaker handshaker;
    private ChannelPromise handshakeFuture;

    public WebSocketClientHandler(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        handshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("WebSocket Client disconnected!");
    }

    public void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        if (!handshaker.isHandshakeComplete()) {
            handshaker.finishHandshake(ch, (FullHttpResponse) msg);
            System.out.println("WebSocket Client connected!");
            handshakeFuture.setSuccess();
            return;
        }

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new Exception("Unexpected FullHttpResponse (getStatus=" + response.getStatus() + ", content="
                    + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            System.out.println("WebSocket Client received : " + textFrame.text());
            System.out.println();
            handleText(ctx.channel(), textFrame.text());
        } else if (frame instanceof PongWebSocketFrame) {
            System.out.println("WebSocket Client received pong");
        } else if (frame instanceof CloseWebSocketFrame) {
            System.out.println("WebSocket Client received closing");
            ch.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();

        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }

        ctx.close();
    }

    private String token;


    private void handleText(Channel channel, String json) throws Exception {
//        logger.info("Received " + json);

        JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(json);
        String type;
        type = jsonObject.getString("type");

        if (type.equals("register")) {
            channel.write(print("{type:'login', username:'" + jsonObject.getString("username") + "', password:'" + jsonObject.getString("password") + "'}"));

        } else if (type.equals("token")) {
            token = jsonObject.getString("token");
            updateUserProfile(channel, "magdaNewName", "magdaNewPass", "magdaNewEmail@gmail.com", "Germany");
//            subscriebeToTopics(channel);
//            getTopicList(channel);
//            startListentingToChanges(channel);
        }
        else if (type.equals("updateUserProfile")) {
            getAllUsers(channel);
        }
    }

    private void getTopicList(Channel channel) {
        channel.write(print("{type:'topics', token:'" + token + "'}"));
    }

    private void subscriebeToTopics(Channel channel) {
        channel.write(print("{type:'topicSubscribe', token:'" + token + "', topicCode:'Topic1'}"));
        channel.write(print("{type:'topicSubscribe', token:'" + token + "', topicCode:'Topic2'}"));
        channel.write(print("{type:'topicSubscribe', token:'" + token + "', topicCode:'Topic3'}"));
    }

    private void startListentingToChanges(Channel channel) {
        channel.write(print("{type:'startListening', token:'" + token + "'}"));
    }

    private void updateUserProfile(Channel channel, String name, String password, String email, String country) {
        channel.write(print("{type:'updateUserProfile', token:'" + token + "', name:'" + name + "', password:'" + password + "', email:'" + email + "', country:'" + country + "'}"));
    }

    private void getAllUsers(Channel channel) {
        channel.write(print("{type:'allUsers', token:'" + token + "'}"));
    }


    private TextWebSocketFrame print(String message) {
//        logger.info(message);
        System.out.println("WebSocket Client sent    : " + message);
        return new TextWebSocketFrame(message);
    }
}