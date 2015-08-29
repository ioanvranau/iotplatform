package com.platform.iot.codec;


import com.platform.iot.message.Message;
import com.platform.iot.message.server.ServerMessage;

/**
 * Created by Magda Gherasim
 */

public interface MessageFactory {

    public Message decode(String input) throws Exception;

    public String encode(ServerMessage messageServer);

}
