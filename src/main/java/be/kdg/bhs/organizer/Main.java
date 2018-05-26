package be.kdg.bhs.organizer;

import be.kdg.bhs.organizer.api.*;
import be.kdg.bhs.organizer.jms.RabbitMQConsumer;
import be.kdg.bhs.organizer.jms.RabbitMQProducer;
import be.kdg.bhs.organizer.services.*;
import java.util.List;

import java.util.ArrayList;


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
         * Name of different queues
         */
        final String suitCaseQueue = "suitcaseQueue";
        final String routeMessageQueue="routeMessageQueue";
        final String sensorMessageQueue="sensorMessageQueue";

        /**
         * MessageFormatService used to transform the incoming message protocol to a DTO object
         * Is in this specific case JAXB to transform XML. Could be Jackson to transform JSON messages in the future.
         */
        MessageFormatterService formatterService = new JAXBFormatterServiceImpl();

        /**
         * MessageConsumerService which initilializes a messagebroker implementation to listen to a given queue on given broker.
         * Here we haven choosen RabbitMQ via cloudAMQP. Could be ActiveMQ in the future.
         */
        MessageConsumerService consumerServiceForSuitcases = new RabbitMQConsumer(suitCaseQueue, connection);
        MessageConsumerService consumerServiceForSensorMessages = new RabbitMQConsumer(suitCaseQueue, connection);
        List<MessageConsumerService> messageConsumerServiceList = new ArrayList<>();
        messageConsumerServiceList.add(consumerServiceForSuitcases);
        messageConsumerServiceList.add(consumerServiceForSensorMessages);

        /**
         * MessageProducerService which initilializes a messagebroker implementation to publish to a given queue on given broker.
         * Here we haven choosen RabbitMQ via cloudAMQP. Could be ActiveMQ in the future.
         */
        MessageProducerService producerService = new RabbitMQProducer(routeMessageQueue, connection);

        /**
         * The flightService asks the flightGate (boardingConveyorID).
         * FlightService has a wrapper FlightServiceImpl which inject FlightServiceProxy. A new FlightServiceImpl based on a restService can be used.
         */
        FlightService flightService = new FlightServiceImpl();

        /**
         * The conveyorService retrieves possible routes between two conveyorIDs.
         * ConveyorService has wrapper ConveyorServiceImpl which inject ConveyourServiceProxy. A new ConveyorServiceImpl based on a restService can be used.
        */
        ConveyorService conveyorService = new ConveyorServiceImpl(50000,200);

        /**
         * CalculateRouteService calculates the optimal route for the suitcases to the boardingConveyor
         */
        CalculateRouteService calculateRouteService = new CalculateRouteServiceImpl();

        /**
         * RoutingService is like a controller and fungates as a callback when a message is read from a queue.
         */
        RoutingService routingService = new RoutingService(messageConsumerServiceList,producerService,formatterService,flightService,conveyorService,calculateRouteService);
        routingService.start();

    }
}
