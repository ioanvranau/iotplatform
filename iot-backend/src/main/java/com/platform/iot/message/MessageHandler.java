package com.platform.iot.message;

import com.platform.iot.bussiness.MemoryStorage;
import com.platform.iot.exception.ApplicationException;
import com.platform.iot.message.server.ErrorMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MessageHandler extends ChannelInboundMessageHandlerAdapter<Message> {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);


    public void messageReceived(ChannelHandlerContext ctx, Message message) throws Exception {
        logger.info(String.format("handle business logic for message %s", message.toString() + " channel id: " + ctx.channel().id() ));

        try {
            MessageForwarder.forward(ctx.channel(), message);
        } catch (ApplicationException e) {
            ctx.channel().write(new ErrorMessage(e));
        } catch (Exception e) {
            ApplicationException technicalAppException = ApplicationException.newTechnicalProblem(e);
            logger.error(technicalAppException.toStringForLogger(), technicalAppException);
            ctx.channel().write(new ErrorMessage(e));
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        MemoryStorage.INSTANCE.getNotLoggedUsers().add(ctx.channel());
        super.channelActive(ctx);
    }
}