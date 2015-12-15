package com.platform.iot.api.producer;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class EventContainer {

    BlockingQueue<Event> events = new LinkedBlockingQueue<Event>();

    AtomicInteger countOnPeriod = new AtomicInteger(0);
    AtomicInteger insertsPerPeriod = new AtomicInteger(0);
    long timestamp = System.currentTimeMillis();

    public void addEvent(Event event) {
        events.add(event);
        calculateMediumInsertsInPeriod(1);
    }

    public void addEvents(Collection<Event> events) {
        this.events.addAll(events);
        calculateMediumInsertsInPeriod(events.size());
    }

    private void calculateMediumInsertsInPeriod(int numberOfInserts) {
        synchronized (this) {
            long newTimestamp = System.currentTimeMillis();
            if (timestamp + 5000 <= newTimestamp) {
                //calculate medium
                insertsPerPeriod.set((countOnPeriod.get() + insertsPerPeriod.get()) / 2);
                countOnPeriod.set(0);
                timestamp = newTimestamp;
                return;
            }
        }
        countOnPeriod.addAndGet(numberOfInserts);
    }

    public Collection<Event> getEvents() {
        Event event;
        List<Event> preparedEvents = new ArrayList<Event>();
        do {
            event = events.poll();
            if (event == null) {
                break;
            }
            preparedEvents.add(event);
        } while (preparedEvents.size() < insertsPerPeriod.get());

        return preparedEvents;
    }
}
