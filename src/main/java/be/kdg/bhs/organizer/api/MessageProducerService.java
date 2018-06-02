package be.kdg.bhs.organizer.api;

/**
 * @author Michael
 * @project BHS
 *
 * Interface implemented by a class that can publish a message to queue. For example RabbitMQ
 *
 */
public interface MessageProducerService {

    /**
     * Marshals a class to a string so it can be published
     * @param genericMessage a DTO object containing the message
     * @param messageFormatterService a Marshaller service to convert the class to string or jsons or any other format
     * @param <T> The type of the genericMessage can by any class, as long as the messageFormatterService can marshall it.
     */
    <T> void publishMessage(T genericMessage, MessageFormatterService messageFormatterService);
}
