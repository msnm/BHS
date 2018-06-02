package be.kdg.bhs.organizer.api;

/**
 * @author Michael
 * @project BHS
 *
 * Marshalling and unmarshalling classes to any representation format.
 */
public interface MessageFormatterService {

    /**
     * Converts a message from a string based wire format to a class
     * @param message a string that needs to be unmarshalled
     * @param aClass where the message needs to be unmarshalled to.
     * @return the aClass. Is the class representation of the message
     */
    <T> T unmarshalMessage(String message, Class<T> aClass);

    /**
     * Converts a class to a string
     * @param aMessageDTO a class that can be marshalled to a string the representation
     * @return the string representation of the aMessageDTO.
     */
    <T> String marshalMessage(T aMessageDTO);
}
