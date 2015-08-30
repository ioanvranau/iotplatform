package com.platform.iot.producer;

import com.platform.iot.api.producer.Event;
import com.platform.iot.api.producer.EventDispatcher;
import com.platform.iot.producer.jms.EventSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


@Component
public class EventLogger {

    private final static Logger logger = LoggerFactory.getLogger(EventLogger.class);

    @Autowired
    EventSender eventSender;

    @Autowired
    EventDispatcher eventDispatcher;


    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> eventDispatcherHandle;

    public void startEventDispatcherScheduler() {
        eventDispatcherHandle = scheduler
                .scheduleAtFixedRate(eventDispatcher, 2, 2, TimeUnit.MILLISECONDS);
    }

    public void sendToQueue(Event event) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("event", event.getJsonObject().toString());
        eventSender.convertAndSendMessage(map);
    }
}
