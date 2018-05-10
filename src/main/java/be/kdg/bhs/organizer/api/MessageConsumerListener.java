package be.kdg.bhs.organizer.api;

import be.kdg.bhs.organizer.dto.MessageDTO;

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
    void onReceive(MessageDTO messageDTO);

    void onError();
}
