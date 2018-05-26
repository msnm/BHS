package be.kdg.bhs.organizer.api;

import be.kdg.bhs.organizer.dto.MessageDTO;
import be.kdg.bhs.organizer.dto.SensorMessageDTO;
import be.kdg.bhs.organizer.dto.SuitcaseMessageDTO;

/**
 * @author Michael
 * @project BHS
 *
 * Is a callback interface, which is used by the RabbitMQConsumer when a new message is consumed (new event).
 * An appropriate notifier class implements this interface.
 */
public interface MessageConsumerListener
{
    /**
     * Called when a mew message is available.
     * @param messageDTO the message converted from the wire format to a DTO.
     */
    void onReceiveSuitcase(SuitcaseMessageDTO messageDTO);

    void onReceiveSensorMessage(SensorMessageDTO messageDTO);

    void onError();
}
