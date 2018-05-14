package be.kdg.bhs.organizer.jms;

import be.kdg.bhs.organizer.api.MessageConsumerListener;
import be.kdg.bhs.organizer.api.MessageConsumerService;
import be.kdg.bhs.organizer.api.MessageFormatterService;
import be.kdg.bhs.organizer.dto.SuitcaseMessageDTO;
import com.rabbitmq.client.*;
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
 *
 * Consumes messages from RabbitMQConsumer and calls a MessageConsumerListener
 */
public class RabbitMQConsumer implements MessageConsumerService {
    private final String queue;
    private final String connectionLink;


    private Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);

    public RabbitMQConsumer(String queue, String connectionLink) {
        this.queue = queue;
        this.connectionLink = connectionLink;
    }

    @Override
    public void initialize(MessageConsumerListener messageConsumerListener, MessageFormatterService formatter) {
        ConnectionFactory factory = new ConnectionFactory();

        try {
            factory.setUri(this.connectionLink);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(queue, false, false, false, null);

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException{
                    logger.info("Received message from RabbitMQConsumer queue " + queue);
                    String message = new String(body, "UTF-8");
                    logger.debug("Message content: "+message);

                    if (messageConsumerListener!=null) {
                        // TODO: Message needs to be formatted
                        // TODO: Write an exception when message formatting fails and implement onError.
                        messageConsumerListener.onReceive((SuitcaseMessageDTO) formatter.formatMessage(message));
                    }
                }

            };

            channel.basicConsume(queue,true,consumer);

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


    @Override
    public void shutdown() {

    }
}
