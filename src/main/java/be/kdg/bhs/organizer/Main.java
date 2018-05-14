package be.kdg.bhs.organizer;

import be.kdg.bhs.organizer.api.*;
import be.kdg.bhs.organizer.jms.RabbitMQConsumer;
import be.kdg.bhs.organizer.services.ConveyorServiceImpl;
import be.kdg.bhs.organizer.services.FlightServiceImpl;
import be.kdg.bhs.organizer.services.JAXBFormatterServiceImpl;
import be.kdg.bhs.organizer.services.RoutingServiceImpl;
import be.kdg.se3.proxy.ConveyorServiceProxy;
import be.kdg.se3.proxy.FlightServiceProxy;

public class Main {
    public static void main(String[] args) {

        final String connection = "amqp://idaoxzsx:coOo7dyLae6xsQV1xyALY0XZRwq5738S@eagle.rmq.cloudamqp.com/idaoxzsx";
        final String queue = "suitcaseQueue";
        MessageFormatterService formatterService = new JAXBFormatterServiceImpl();
        MessageConsumerService consumerService = new RabbitMQConsumer(queue, connection);
        FlightService flightService = new FlightServiceImpl(new FlightServiceProxy());
        ConveyorService conveyorService = new ConveyorServiceImpl(new ConveyorServiceProxy());
        RoutingServiceImpl routingService = new RoutingServiceImpl(consumerService,formatterService,flightService,conveyorService);
        routingService.start();

    }
}
