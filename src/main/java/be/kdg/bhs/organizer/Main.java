package be.kdg.bhs.organizer;

import be.kdg.bhs.organizer.api.MessageConsumerListener;
import be.kdg.bhs.organizer.api.MessageConsumerService;
import be.kdg.bhs.organizer.api.MessageFormatterService;
import be.kdg.bhs.organizer.jms.RabbitMQConsumer;
import be.kdg.bhs.organizer.services.JAXBFormatterServiceImpl;
import be.kdg.bhs.organizer.services.RoutingServiceImpl;

public class Main {
    public static void main(String[] args) {

        final String connection = "amqp://pdxgjznm:mA6rcIpvVaZNPGuY9OB5xFJAK_1cu7S6@hound.rmq.cloudamqp.com/pdxgjznm";
        final String queue = "TEST";
        MessageFormatterService formatterService = new JAXBFormatterServiceImpl();
        MessageConsumerService consumerService = new RabbitMQConsumer(queue, connection);
        RoutingServiceImpl routingService = new RoutingServiceImpl(consumerService,formatterService);
        routingService.start();

    }
}
