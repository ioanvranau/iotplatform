package com.platform.iot.producer.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import java.util.Map;

@Component("eventSender")
public class EventSender {

    @Autowired
    @Qualifier("jmsTemplate")
    private JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("asyncEventQueue")
    private Queue queue;

    public void convertAndSendMessage(Map<String, Object> notification) {
        jmsTemplate.convertAndSend(queue, notification);
    }

}
