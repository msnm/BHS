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
 * Produces messages to a CLOUDAMQP queue
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
        logger.debug("Entered: publishMessage()");
        ConnectionFactory factory = new ConnectionFactory();
        try {
            factory.setUri(connectionLink);

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            boolean durable = false;    //durable - RabbitMQ will never lose the queue if a crash occurs
            boolean exclusive = false;  //exclusive - if queue only will be used by one connection
            boolean autoDelete = false; //autodelete - queue is deleted when last consumer unsubscribes

            channel.queueDeclare(queue, durable, exclusive, autoDelete, null);
            String message= messageFormatterService.marshalMessage(genericMessage);
            channel.basicPublish("", queue, null, message.getBytes());

            channel.close();
            connection.close();
            logger.debug("End: publishMessage() with published message ", message);

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
}
