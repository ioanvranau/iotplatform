package com.platform.iot.test;

import com.platform.iot.api.TopicDistributionApplication;
import com.platform.iot.api.bussiness.model.User;
import com.platform.iot.api.message.Message;
import com.platform.iot.api.message.server.MigrationMessage;
import com.platform.iot.api.message.server.ServerMessage;
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

public class JavaTestClientHandler extends ChannelInboundMessageHandlerAdapter<Object> {

    private Logger logger = LoggerFactory.getLogger(JavaTestClientHandler.class);

    private final WebSocketClientHandshaker handshaker;
    private ChannelPromise handshakeFuture;
    public String migrationToken;
    public String migrationServerAddress;
    public int migrationServerPort;

    public JavaTestClientHandler(WebSocketClientHandshaker handshaker) {
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

    @Override
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
        logger.info("Received " + json);

        JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(json);
        String type;
        type = jsonObject.getString(Message.FIELD_TYPE);

        switch (type) {
            case ServerMessage.MESSAGE.REGISTER:
                channel.write(print("{type:'login', username:'" + jsonObject.getString("username") + "', password:'" + jsonObject.getString("password") + "'}"));
                break;
            case ServerMessage.MESSAGE.TOKEN:
                token = jsonObject.getString(User.FIELD_TOKEN);
//                userProfile(channel);
//                updateUserProfile(channel);
//                subscriebeToTopics(channel);
//                getTopicList(channel);
//                startListentingToChanges(channel);
                break;
            case ServerMessage.MESSAGE.MIGRATION:
                migration(jsonObject);
                channel.close();
                break;
            case ServerMessage.MESSAGE.UPDATE_USER_PROFILE:
                disableAccount(channel);
                break;
            }
    }

    private void migration(JSONObject jsonObject) {
        migrationToken = jsonObject.getString(MigrationMessage.FIELD_TOKEN);
        migrationServerAddress = jsonObject.getString(MigrationMessage.FIELD_SERVER_ADDRESS);
        migrationServerPort = jsonObject.getInt(MigrationMessage.FIELD_SERVER_PORT);
    }

    private void startListentingToChanges(Channel channel) {
        channel.write(print("{type:'startListening', token:'" + token + "'}"));
    }

    private void userProfile(Channel channel) {
        channel.write(print("{type:'userProfile', token:'" + token + "'}"));
    }


    private void getTopicList(Channel channel) {
        channel.write(print("{type:'topics', token:'" + token + "', producerId:'" + TopicDistributionApplication.DEFAULT_PRODUCER_ID + "'}"));
    }

    private void subscriebeToTopics(Channel channel) {
        channel.write(print("{type:'topicSubscribe', token:'" + token + "', topicCode:'TLV'}"));
        channel.write(print("{type:'topicSubscribe', token:'" + token + "', topicCode:'SNP'}"));
        channel.write(print("{type:'topicSubscribe', token:'" + token + "', topicCode:'FP'}"));
        delay();
    }

    private void updateUserProfile(Channel channel) {
        channel.write(print("{type:'updateUserProfile', token:'" + token + "',name:'Magda Gherasim', country:'Romania', skills:'Java, Pearl', email:'magdalena.gherasim@yhaoo.com', password:'pass'}"));
    }

    private void emailNotificationMessage(Channel channel) {
        channel.write(print("{type:'emailNotification', token:'" + token + "',notificationType:'SECURITY_EMAILS', email:''magdalena.gherasim@yahoo.com'}"));
    }

    private void disableAccount(Channel channel) {
        channel.write(print("{type:'disableAccount', token:'" + token + "',weeksNumber:1}"));
    }

    private void delay() {
        try {
            Thread.sleep(3000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private TextWebSocketFrame print(String message) {
//        logger.info(message);
        System.out.println("WebSocket Client sent    : " + message);
        return new TextWebSocketFrame(message);
    }
}