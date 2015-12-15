package com.platform.iot.api.producer.jms;

import com.platform.iot.api.producer.Event;
import com.platform.iot.api.producer.EventContainer;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("eventReceiver")
public class EventReceiver {

    private final static Logger logger = LoggerFactory.getLogger(EventReceiver.class);

    @Autowired
    private EventContainer eventContainer;

    public void receiveMessage(Map<String, Object> data) {
        try {
            String eventString = (String) data.get("event");
            JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(eventString);
            eventContainer.addEvent(new Event(jsonObject));
//            PriceMessage priceMessage = new PriceMessage();
//            priceMessage.setCode(jsonObject.getString("code"));
//            priceMessage.setPrice(jsonObject.getDouble("price"));
//            priceMessage.setChanged(jsonObject.getDouble("changed"));
//            ServerMessage message = (ServerMessage) priceMessage;
//            MessageDispatcher.sendMessageToLoggedUsers(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

