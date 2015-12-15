package com.platform.iot.producer;

/**
 * Created by magdalena.gherasim on 9/3/2014.
 */

import com.platform.iot.api.TopicDistributionApplication;
import com.platform.iot.api.bussiness.model.Topic;
import com.platform.iot.api.message.server.PriceMessage;
import com.platform.iot.api.producer.Event;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: magdalena.gherasim
 */

public class TopicPublishingTimer {
    private static final Logger logger = LoggerFactory.getLogger(TopicPublisher.class);
    private static EventLogger eventLogger = TopicDistributionApplication.context.getBean(EventLogger.class);

    private Timer timer;
    private int numberOfSentVariations;
    private Topic topic;
    private double variance;
    private long producerId;

    public TopicPublishingTimer(Topic topic, long producerId) {
        logger.info("Topic publishing simulator STARTED ...");
        this.numberOfSentVariations = 6000; // cat timp sa dureze - 100 de minute
        timer = new Timer();  //At this line a new Thread will be created

        timer.scheduleAtFixedRate(new VariationTask(),
                0, //initial delay
                800); // la cat timp sa se trimita
                    // 800 - la 0.8 secunde
                    // 100 - la 0.1 secunde
                    // 10 - la 0.01 secunde
                    // 1 - la o milisecunda
        this.variance = Math.random();
        this.topic = topic;
        this.producerId = producerId;
    }


    class VariationTask extends TimerTask {
        public void run() {
            //INTRA AICI LA FIECARE SECUNDA ( timpul total/numberOfSentVariations)
            try {
                if (numberOfSentVariations-- > 0) {
//                    long time = System.currentTimeMillis();
//                    if (time - scheduledExecutionTime() > 0) {
//                        return;
//                    }
//                    System.out.println(numberOfSentVariations + " : " + DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date()));
//                    for (int i = 0; i <1; i++) {
                        variance = Math.random();
                        PriceMessage priceMessage = new PriceMessage();
                        priceMessage.setCode(topic.getCode());
                        priceMessage.setName(topic.getName());
                        double newPrice = topic.getPrice() * (variance + 1);
                        priceMessage.setChanged(newPrice - topic.getPrice());
                        priceMessage.setPrice(newPrice);
                        logger.info("variation " + numberOfSentVariations + " topic "+ topic.getCode() + " " + newPrice + "");
                        priceMessage.setProducerId(producerId);
//                    priceMessage.setPrice(1001);

                        JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(priceMessage.toJSON());
                        eventLogger.sendToQueue(new Event(jsonObject));
//                    }
                } else {
                    logger.info("Topic publishing simulator ENDED");
                    timer.cancel();
                    //Stop the process by calling System.exit
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    public static void main(String[] args) {
//        new TopicPublishingTimer(null,1l);
//    }
}
