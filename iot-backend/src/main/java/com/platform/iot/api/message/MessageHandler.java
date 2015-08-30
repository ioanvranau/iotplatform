package com.platform.iot.api.message;

import com.platform.iot.api.bussiness.MemoryStorage;
import com.platform.iot.api.exception.ApplicationException;
import com.platform.iot.api.exception.ExceptionType;
import com.platform.iot.api.message.server.ErrorMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MessageHandler extends ChannelInboundMessageHandlerAdapter<Message> {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);


    @Override
    public void messageReceived(ChannelHandlerContext ctx, Message message) throws Exception {
        logger.info(String.format("received %s", message.toString() + " channel id: " + ctx.channel().id() ));

        try {
//            logger.info("received " + message.toString());
            MessageForwarder.forward(ctx.channel(), message);
        } catch (Exception e) {
            ApplicationException exception = new ApplicationException(ExceptionType.TECHNICAL_PROBLEM, e);
            logger.error(exception.toString());
            ctx.channel().write(new ErrorMessage(exception));
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        MemoryStorage.INSTANCE.getNotLoggedUsers().add(ctx.channel());
        super.channelActive(ctx);
    }
}