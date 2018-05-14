package be.kdg.bhs.organizer;

import be.kdg.bhs.organizer.api.*;
import be.kdg.bhs.organizer.jms.RabbitMQConsumer;
import be.kdg.bhs.organizer.jms.RabbitMQProducer;
import be.kdg.bhs.organizer.services.*;
import be.kdg.se3.proxy.ConveyorServiceProxy;
import be.kdg.se3.proxy.FlightServiceProxy;

/**
 * Main class to parameterize and start the BHS Organizer.
 */
public class Main {
    public static void main(String[] args) {

        /**
         * URL to cloudAMQP messageBroker
         */
        final String connection = "amqp://idaoxzsx:coOo7dyLae6xsQV1xyALY0XZRwq5738S@eagle.rmq.cloudamqp.com/idaoxzsx";

        /**
         * Name of incomingQueue with suitcaseDTO messages
         */
        final String queue = "suitcaseQueue";

        /**
         * MessageFormatService used to transform the incoming message protocol to a DTO object
         * Is in this specific case JAXB to transform XML. Could be Jackson to transform JSON messages in the future.
         */
        MessageFormatterService formatterService = new JAXBFormatterServiceImpl();

        /**
         * MessageConsumerService which initilializes a messabroker implementation to listen to a given queue on given broker.
         * Here we haven choosen RabbitMQ via cloudAMQP. Could be ActiveMQ in the future.
         */
        MessageConsumerService consumerService = new RabbitMQConsumer(queue, connection);

        /**
         * The flightService asks the flightGate (boardingConveyorID).
         * FlightService has a wrapper FlightServiceImpl which inject FlightServiceProxy. A new FlightServiceImpl based on a restService can be used.
         */
        FlightService flightService = new FlightServiceImpl(new FlightServiceProxy());

        /**
         * The conveyorService retrieves possible routes between two conveyorIDs.
         * ConveyorService has wrapper ConveyorServiceImpl which inject ConveyourServiceProxy. A new ConveyorServiceImpl based on a restService can be used.
        */
        ConveyorService conveyorService = new ConveyorServiceImpl(new ConveyorServiceProxy());

        /**
         * CalculateRouteService calculates the optimal route for the suitcases to the boardingConveyor
         */
        CalculateRouteService calculateRouteService = new CalculateRouteImpl();

        /**
         * RoutingService is like a controller and fungates as a callback when a message is read from a queue.
         */
        RoutingService routingService = new RoutingService(consumerService,new RabbitMQProducer(),formatterService,flightService,conveyorService,calculateRouteService);
        routingService.start();

    }
}
