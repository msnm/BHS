package be.kdg.bhs.organizer.api;

import be.kdg.bhs.organizer.dto.SensorMessageDTO;
import be.kdg.bhs.organizer.dto.SuitcaseMessageDTO;
import be.kdg.bhs.organizer.model.StatusMessage;

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
     * Called when a new SuitcaseMessage is available.
     * @param messageDTO the message converted from the wire format to a DTO.
     */
    void onReceiveSuitcaseMessage(SuitcaseMessageDTO messageDTO);

    /**
     * Called when a new SensorMessage is available.
     * @param messageDTO
     */
    void onReceiveSensorMessage(SensorMessageDTO messageDTO);

    /**
     * Called when a suitcase is arrived, or lost, or undeliverable to send a statusMessage
     * @param statusMessage
     */
    void sendStatusMessage(StatusMessage statusMessage);
}
