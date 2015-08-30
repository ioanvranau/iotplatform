package com.platform.iot.api.codec.json;

import com.platform.iot.api.codec.MessageFactory;
import com.platform.iot.api.codec.MessageFactoryBuilder;
import com.platform.iot.api.exception.ApplicationException;
import com.platform.iot.api.exception.ExceptionType;
import com.platform.iot.api.message.Message;
import com.platform.iot.api.message.server.ErrorMessage;
import io.netty.buffer.MessageBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decodes incoming json text messages to Java model
 */
public class JsonMessageDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {

    private static final Logger logger = LoggerFactory.getLogger(JsonMessageDecoder.class.getName());

    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg,
                          MessageBuf<Object> out) throws Exception {
        try {
            MessageFactory modelFactory = MessageFactoryBuilder.INSTANCE.createFactory();
            Message message = modelFactory.decode(msg.text());

            out.add(message);
        } catch (ApplicationException e) {
            ctx.channel().write(new ErrorMessage(new ApplicationException(ExceptionType.TECHNICAL_PROBLEM, e)));
        }
    }

    protected Message decode(String message) throws ApplicationException {
        return new Message();
    }
}
