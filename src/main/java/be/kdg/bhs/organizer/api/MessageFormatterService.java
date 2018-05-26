package be.kdg.bhs.organizer.api;

import be.kdg.bhs.organizer.dto.MessageDTO;
import be.kdg.bhs.organizer.dto.SuitcaseMessageDTO;

/**
 * @author Michael
 * @project BHS
 *
 * Is a callback interface, which is used to format a string.
 */
public interface MessageFormatterService {

    /**
     * Converts a message from a string based wire format to a class implementing {@link MessageDTO}
     * @param message
     * @return
     */
    <T> T unmarshalMessage(String message, Class<T> aClass);

    <T> String marshalMessage(T aMessageDTO);
}
