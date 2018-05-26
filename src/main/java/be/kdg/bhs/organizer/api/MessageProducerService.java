package be.kdg.bhs.organizer.api;

public interface MessageProducerService {

    <T> void publishMessage(T genericMessage, MessageFormatterService messageFormatterService);
}
