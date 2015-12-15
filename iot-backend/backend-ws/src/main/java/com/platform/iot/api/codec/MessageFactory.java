package com.platform.iot.api.codec;

import com.platform.iot.api.message.Message;
import com.platform.iot.api.message.server.ServerMessage;

/**
 * Created by Magda Gherasim
 */

public interface MessageFactory {

    public Message decode(String input) throws Exception;

    public String encode(ServerMessage messageServer);

}
