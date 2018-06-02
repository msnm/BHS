package be.kdg.bhs.organizer;

import be.kdg.bhs.organizer.api.MessageFormatterService;
import be.kdg.bhs.organizer.api.MessageProducerService;
import be.kdg.bhs.organizer.dto.SensorMessageDTO;
import be.kdg.bhs.organizer.dto.SuitcaseMessageDTO;
import be.kdg.bhs.organizer.jms.RabbitMQProducer;
import be.kdg.bhs.organizer.services.JAXBFormatterServiceImpl;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.concurrent.TimeoutException;

/**
 * @author Michael
 * @project BHS
 * This is a Mock implementation of the SimulatorService, putting SuitcaseMessages and SensorMessages on the queues.
 */
public class SimulatorMockService {


    public void start() {
        SuitcaseMessageDTO suitcase1 = new SuitcaseMessageDTO(1, 1111111, 11, new Timestamp(System.currentTimeMillis()));
        SensorMessageDTO sensor21 = new SensorMessageDTO(1, 21, new Timestamp(System.currentTimeMillis()));
        SensorMessageDTO sensor41 = new SensorMessageDTO(1, 41, new Timestamp(System.currentTimeMillis()));
        SuitcaseMessageDTO suitcase2 = new SuitcaseMessageDTO(2, 1111112, 12, new Timestamp(System.currentTimeMillis()));
        SensorMessageDTO sensor22 = new SensorMessageDTO(2, 22, new Timestamp(System.currentTimeMillis()));
        SensorMessageDTO sensor42 = new SensorMessageDTO(2, 42, new Timestamp(System.currentTimeMillis()));
        SuitcaseMessageDTO suitcaseLost = new SuitcaseMessageDTO(3, 1111113, 11, new Timestamp(System.currentTimeMillis()));
        SuitcaseMessageDTO suitcaseUndeliverable = new SuitcaseMessageDTO(4, 1111112, 11, new Timestamp(System.currentTimeMillis()));
        SensorMessageDTO sensor44Undeliverable = new SensorMessageDTO(4, 44, new Timestamp(System.currentTimeMillis()));
        consumeAMessage(routeMessageQueue);
        consumeAMessage(statusMessageQueue);

        try {

            sendSuitcaseMessage(suitcase1);
            sendSuitcaseMessage(suitcase2);
            sendSuitcaseMessage(suitcaseLost);
            sendSuitcaseMessage(suitcaseUndeliverable);
            Thread.sleep(500);
            sendSensorMessage(sensor21);
            sendSensorMessage(sensor22);
            sendSensorMessage(sensor44Undeliverable);
            Thread.sleep(500);
            sendSensorMessage(sensor41);
            sendSensorMessage(sensor42);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * URL to cloudAMQP messageBroker
     */
    final String connection = "amqp://idaoxzsx:coOo7dyLae6xsQV1xyALY0XZRwq5738S@eagle.rmq.cloudamqp.com/idaoxzsx";

    /**
     * Name of different queues
     */
    final String suitCaseQueue = "suitcaseQueue";
    final String routeMessageQueue = "routeMessageQueue";
    final String statusMessageQueue = "statusMessageQueue";
    final String sensorMessageQueue = "sensorMessageQueue";

    MessageProducerService suitcaseProducer = new RabbitMQProducer(suitCaseQueue, connection);
    MessageProducerService sensorProducer = new RabbitMQProducer(sensorMessageQueue, connection);
    MessageFormatterService jaxbFormatterService = new JAXBFormatterServiceImpl();

    public void sendSuitcaseMessage(SuitcaseMessageDTO suitcaseMessageDTO) {
        this.suitcaseProducer.publishMessage(suitcaseMessageDTO, jaxbFormatterService);
    }

    public void sendSensorMessage(SensorMessageDTO sensorMessageDTO) {

        this.sensorProducer.publishMessage(sensorMessageDTO, jaxbFormatterService);
    }


    public void consumeAMessage(String queue) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri(this.connection);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(queue, false, false, false, null);

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String result = new String(body, "UTF-8");
                    System.out.printf("Consumed from %s: %n%s%n", queue, result);
                }


            };
            channel.basicConsume(queue, true, consumer);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

}
