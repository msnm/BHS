import be.kdg.bhs.organizer.api.*;

import be.kdg.bhs.organizer.jms.ConsumerLogic;
import be.kdg.bhs.organizer.jms.RabbitMQProducer;
import be.kdg.bhs.organizer.services.*;

import java.sql.Timestamp;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author Michael
 * @project BHS
 */
public class IntegrationTest {
    private RoutingService routingService;
    private MockConsumer suitCaseConsumer;
    private MockConsumer sensorConsumer;
    @Before
    public void setUp() {

        /**
         * URL to cloudAMQP messageBroker
         */
        final String connection = "amqp://idaoxzsx:coOo7dyLae6xsQV1xyALY0XZRwq5738S@eagle.rmq.cloudamqp.com/idaoxzsx";

        /**
         * Name of different queues
         */
        final String routeMessageQueue="routeMessageQueue";
        final String statusMessageQueue="statusMessageQueue";

        /**
         * MessageFormatService used to transform the incoming message protocol to a DTO object
         * Is in this specific case JAXB to transform XML. Could be Jackson to transform JSON messages in the future.
         */
        MessageFormatterService formatterService = new JAXBFormatterServiceImpl();

        /**
         * MessageConsumerService which initilializes a messagebroker implementation to listen to a given queue on a given broker.
         * Here we haven choosen RabbitMQ via cloudAMQP. Could be ActiveMQ in the future.
         */
        suitCaseConsumer = new MockConsumer(50,0);
        sensorConsumer = new MockConsumer(50,100);
        MessageConsumerService consumerServiceForSuitcases = suitCaseConsumer;
        MessageConsumerService consumerServiceForSensorMessages = sensorConsumer;
        List<MessageConsumerService> messageConsumerServiceList = new ArrayList<>();
        messageConsumerServiceList.add(consumerServiceForSuitcases);
        messageConsumerServiceList.add(consumerServiceForSensorMessages);

        /**
         * MessageProducerService which initilializes a messagebroker implementation to publish to a given queue on given broker.
         * Here we haven choosen RabbitMQ via cloudAMQP. Could be ActiveMQ in the future.
         */
        MessageProducerService producerServiceForRoutingMessages = new RabbitMQProducer(routeMessageQueue, connection);
        MessageProducerService producerServiceForStatusMessages = new RabbitMQProducer(statusMessageQueue,connection);
        List<MessageProducerService> messageProducerList = new ArrayList<>();
        messageProducerList.add(producerServiceForRoutingMessages);
        messageProducerList.add(producerServiceForStatusMessages);

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
        routingService = new RoutingService(messageConsumerServiceList,messageProducerList,formatterService,
                flightService,conveyorService,calculateRouteService,60000,200);


    }
    @Test
    public void integrationTestWithMockedConsumers() {
        List<String> suitcaseMessages = new ArrayList<>();
        List<String> sensorMessages = new ArrayList<>();

        suitcaseMessages.add(String.format("" +
                "<suitcasemessage>" +
                "<suitcaseId>1</suitcaseId>" +
                "<flightNumber>1111111</flightNumber>\n" +
                "<conveyorId>11</conveyorId><date>%s</date>\n" +
                "</suitcasemessage>",new Timestamp(System.currentTimeMillis())));
        suitcaseMessages.add(String.format("" +
                "<suitcasemessage>" +
                "<suitcaseId>2</suitcaseId>" +
                "<flightNumber>1111112</flightNumber>\n" +
                "<conveyorId>11</conveyorId><date>%s</date>\n" +
                "</suitcasemessage>",new Timestamp(System.currentTimeMillis())));
        suitcaseMessages.add(String.format("" +
                "<suitcasemessage>" +
                "<suitcaseId>3</suitcaseId>" +
                "<flightNumber>1111113</flightNumber>\n" +
                "<conveyorId>10</conveyorId><date>%s</date>\n" +
                "</suitcasemessage>",new Timestamp(System.currentTimeMillis())));
        suitCaseConsumer.setSuitcasemessages(suitcaseMessages);
        sensorConsumer.setSuitcasemessages(new ArrayList<>());
        routingService.start();

        while(true) {

        }
    }
}
