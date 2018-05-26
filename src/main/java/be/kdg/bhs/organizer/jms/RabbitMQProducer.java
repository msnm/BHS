package be.kdg.bhs.organizer.jms;

import be.kdg.bhs.organizer.api.MessageFormatterService;
import be.kdg.bhs.organizer.api.MessageProducerService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

/**
 * @author Michael
 * @project BHS
 */
public class RabbitMQProducer implements MessageProducerService {
    private final String queue;
    private final String connectionLink;


    private Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);

    public RabbitMQProducer(String queue, String connectionLink) {
        this.queue = queue;
        this.connectionLink = connectionLink;
    }


    @Override
    public <T> void publishMessage(T genericMessage, MessageFormatterService messageFormatterService) {
        ConnectionFactory factory = new ConnectionFactory();
        try {
            factory.setUri(connectionLink);

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            boolean durable = false;    //durable - RabbitMQ will never lose the queue if a crash occurs
            boolean exclusive = false;  //exclusive - if queue only will be used by one connection
            boolean autoDelete = false; //autodelete - queue is deleted when last consumer unsubscribes

            channel.queueDeclare(queue, durable, exclusive, autoDelete, null);

            channel.basicPublish("", queue, null, messageFormatterService.marshalMessage(genericMessage).getBytes());

            channel.close();
            connection.close();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*

     String uri = "amqp://pdxgjznm:mA6rcIpvVaZNPGuY9OB5xFJAK_1cu7S6@hound.rmq.cloudamqp.com/pdxgjznm";
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(uri); // indien je met een lokaal geinstalleerde RabbitMQ server werkt doe je hier setHost

        //Recommended settings
        factory.setRequestedHeartbeat(30);
        factory.setConnectionTimeout(30000);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String queue = "SOCIAL MEDIA V1";     //queue name
        boolean durable = false;    //durable - RabbitMQ will never lose the queue if a crash occurs
        boolean exclusive = false;  //exclusive - if queue only will be used by one connection
        boolean autoDelete = false; //autodelete - queue is deleted when last consumer unsubscribes

        channel.queueDeclare(queue, durable, exclusive, autoDelete, null);

        Logger logger = Logger.getLogger(Sender.class);
        logger.info("Using uri '" + uri + "'");
        logger.info("Using queue '" + queue + "'");

        pollConsole(channel, queue, logger); // endless loop unit exit typed (!)

        channel.close();
        connection.close();

    }


    private static void pollConsole(Channel channel, String queue, Logger logger) throws IOException {
        logger.info("Listening for sentences");
        Scanner scanner = new Scanner(System.in);
        String message = scanner.nextLine();
        while (!message.equals("exit")) {
            channel.basicPublish("", queue, null, message.getBytes());
            logger.info("Sent '" + message + "'");
            message = scanner.nextLine();
        }
    }
     */
}
