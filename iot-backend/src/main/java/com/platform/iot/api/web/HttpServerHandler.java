package com.platform.iot.api.web;

import com.platform.iot.api.TopicDistributionApplication;
import com.platform.iot.api.balancing.ClientVersion;
import com.platform.iot.api.balancing.LoadBalancer;
import com.platform.iot.api.balancing.ProducerServer;
import com.platform.iot.api.balancing.Server;
import com.platform.iot.api.bussiness.MemoryStorage;
import com.platform.iot.api.bussiness.model.Producer;
import com.platform.iot.api.producer.ProducerHandler;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * Load balancer public API
 * Assigns a server for a client requesting a connection to game
 */
@Path("/load-balancer")
public class HttpServerHandler {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

    /**
     * Depending on the provided version sent by client as query parameter a server is assigned to client
     *
     * @param clientVersion
     * @return the assigned server in format <code>server_addres:server_port</code> or empty string if no server available
     */
    @GET
    @Path("/server")
    @Produces("text/plain")
    public String getServer(@QueryParam("clientVersion") String clientVersion) {
        LoadBalancer loadBalancer = TopicDistributionApplication.context.getBean(LoadBalancer.class);
        Server server = loadBalancer.getBestServer(ClientVersion.fromString(clientVersion));
        if (server != null) {
            return "{\"hostname\":\"" + server.getAddress() + "\", \"port\":" + String.valueOf(server.getPort()) + "}";
        } else {
            return "No best server found";
        }
    }

    @GET
    @Path("/connect-producer")
    @Produces("text/plain")
    public String getTopicPublishingQueue(@QueryParam("producerName") String producerName) {
        LoadBalancer loadBalancer = TopicDistributionApplication.context.getBean(LoadBalancer.class);
        Producer producer = ProducerHandler.registerProducer(producerName);
        logger.info("producers size " + MemoryStorage.INSTANCE.getProducers().size());
        logger.info("Producer name : " +  producerName + ", producerId : " + producer.getId());
        if (producer != null) {
            ProducerServer producerServer = loadBalancer.getTopicQueue();
            if (producerServer != null) {
                return "{\"brokerUrl\":\"" + producerServer.getBrokerURL() + "\", \"queueName\":\"" + String.valueOf(producerServer.getQueueName()) + "\", \"producerId\":" + producer.getId()+"}";
            } else {
                return "No available topic queue found";
            }
        } else {
            return "Unsuccessful producer registration!";
        }
    }

}