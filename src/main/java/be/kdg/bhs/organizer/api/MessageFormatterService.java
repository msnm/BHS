package be.kdg.bhs.organizer.api;

import be.kdg.bhs.organizer.dto.MessageDTO;

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
    MessageDTO formatMessage(String message);
}
