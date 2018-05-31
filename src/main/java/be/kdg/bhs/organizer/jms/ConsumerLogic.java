package be.kdg.bhs.organizer.jms;

import be.kdg.bhs.organizer.api.MessageConsumerListener;
import be.kdg.bhs.organizer.api.MessageFormatterService;
import be.kdg.bhs.organizer.dto.SensorMessageDTO;
import be.kdg.bhs.organizer.dto.SuitcaseMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Michael
 * @project BHS
 * The logic of the messageConsumer is extracted outside the RabbitMQConsumer for testing purposes. This makes it possible
 * to do integration tests without needing RabbitMQ, but reusing the same logic.
 */
public class ConsumerLogic {
    private Logger logger = LoggerFactory.getLogger(ConsumerLogic.class);
    private MessageConsumerListener messageConsumerListener;

    public ConsumerLogic() {
    }

    public void consumeMessage(MessageConsumerListener messageConsumerListener, MessageFormatterService formatter, String message, Class aClass) {
        logger.debug("Message content: "+message);

        if (messageConsumerListener!=null) {
            // TODO: Message needs to be formatted
            // TODO: Write an exception when message formatting fails and implement onError.

            if (aClass.equals((new SuitcaseMessageDTO()).getClass())) {
                messageConsumerListener.onReceiveSuitcase((SuitcaseMessageDTO) formatter.unmarshalMessage(message,aClass));

            }
            else if (aClass.equals((new SensorMessageDTO()).getClass())) {
                messageConsumerListener.onReceiveSensorMessage((SensorMessageDTO) formatter.unmarshalMessage(message,aClass));

            }
            else {
                //TODO throwing an exception
            }
        }
    }
}
