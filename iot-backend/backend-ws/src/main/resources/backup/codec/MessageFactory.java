package backup.codec;


import backup.message.Message;
import backup.message.server.ServerMessage;

/**
 * Created by Magda Gherasim
 */

public interface MessageFactory {

    public Message decode(String input) throws Exception;

    public String encode(ServerMessage messageServer);

}
