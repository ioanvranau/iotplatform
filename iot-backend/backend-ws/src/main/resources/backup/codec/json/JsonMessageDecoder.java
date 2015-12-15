package backup.codec.json;

import backup.codec.MessageFactory;
import backup.codec.MessageFactoryBuilder;
import backup.message.Message;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Message decode(String message) throws Exception {
        return new Message();
    }
}
