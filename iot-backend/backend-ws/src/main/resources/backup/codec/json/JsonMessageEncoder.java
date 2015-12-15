package backup.codec.json;

import backup.codec.MessageFactory;
import backup.codec.MessageFactoryBuilder;
import backup.message.server.ServerMessage;
import io.netty.buffer.MessageBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * Encodes messages sent to client from Java object to json text
 */
public class JsonMessageEncoder extends MessageToMessageEncoder<ServerMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ServerMessage serverMessage,
                          MessageBuf<Object> out) throws Exception {
        MessageFactory modelFactory = MessageFactoryBuilder.INSTANCE.createFactory();
        String output = modelFactory.encode(serverMessage);

        ctx.channel().write(new TextWebSocketFrame(output));
    }

}