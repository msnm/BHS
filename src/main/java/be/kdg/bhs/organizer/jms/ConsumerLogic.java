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

    public ConsumerLogic() {
    }

    public void consumeMessage(MessageConsumerListener messageConsumerListener, MessageFormatterService formatter, String message, Class aClass) {
        logger.debug("Entered: consumeMessage() ");
        try {
            if (messageConsumerListener != null) {
                if (aClass.equals((new SuitcaseMessageDTO()).getClass())) {
                    messageConsumerListener.onReceiveSuitcaseMessage((SuitcaseMessageDTO) formatter.unmarshalMessage(message, aClass));

                } else if (aClass.equals((new SensorMessageDTO()).getClass())) {
                    messageConsumerListener.onReceiveSensorMessage((SensorMessageDTO) formatter.unmarshalMessage(message, aClass));

                }
            }
        } catch (Exception e) {
            logger.error("consumeMessage failed for message {} of class {} with exceptionmessage", message, aClass.getName(),e.getMessage());
        }
        logger.debug("End: consumeMessage() ");
    }
}
