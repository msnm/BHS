package be.kdg.bhs.organizer.services;

import be.kdg.bhs.organizer.api.MessageFormatterService;
import be.kdg.bhs.organizer.dto.MessageDTO;
import be.kdg.bhs.organizer.dto.SuitcaseMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

/**
 * @author Michael
 * @project BHS
 */
public class JAXBFormatterServiceImpl implements MessageFormatterService {

    Logger logger = LoggerFactory.getLogger(JAXBFormatterServiceImpl.class);

    @Override
    public MessageDTO formatMessage(String message)  {
        logger.info("Entering formatMessage({})",message);
        SuitcaseMessageDTO messageDTO = new SuitcaseMessageDTO();

        try {
            messageDTO = unmarshalObject(message,messageDTO);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return messageDTO;
    }

    /**
     * Generic unMarshaller to unmarshal a String to a given Class.
     * @param message String
     * @param object An object of the class that needs to be unmarshalled to
     * @param <T>
     * @return
     */

    private <T> T unmarshalObject(String message, T object) throws Exception {

        //TODO Move this method to a UTIL class because this can be used by different classes in the far far future.
        //TODO Wright custom ExceptionHandler;

        try {
            JAXBContext context = JAXBContext.newInstance(object.getClass());
            Unmarshaller unmarshaller = context.createUnmarshaller();

            StringReader reader = new StringReader(message);

            object = (T) unmarshaller.unmarshal(reader);

            return object;
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

    }
}
