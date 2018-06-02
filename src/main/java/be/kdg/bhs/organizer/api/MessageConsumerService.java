package be.kdg.bhs.organizer.api;


/**
 * @author Michael
 * @project BHS
 *
 * An async service that can be used to receive messages from a communication interface
 */
public interface MessageConsumerService {

    /**
     * Setup communication with the service (new connection,...)
     * @param messageConsumerListener callback interface for receiving messages from the queue
     */
    void initialize(MessageConsumerListener messageConsumerListener, MessageFormatterService messageFormatter, Class aClass);


    /**
     * End communication with the messagebroker.
     */
    void shutdown();
}
