package be.kdg.bhs.organizer.services;

import be.kdg.bhs.organizer.api.MessageFormatterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author Michael
 * @project BHS
 * Implements the {@link MessageFormatterService} and makes use of JAXB to marshal unmarshal objects which are
 * JAXB annotated.
 */
public class JAXBFormatterServiceImpl implements MessageFormatterService {

    Logger logger = LoggerFactory.getLogger(JAXBFormatterServiceImpl.class);

    @Override
    public <T> String marshalMessage(T aMessageDTO) {
        logger.info("Entering marshalMessage({})", aMessageDTO.toString());
        StringWriter result = new StringWriter();

        try {
            JAXBContext context = JAXBContext.newInstance(aMessageDTO.getClass());
            Marshaller marshaller = context.createMarshaller();

            marshaller.marshal(aMessageDTO,result);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("End marshalMessage({}) with result {}", aMessageDTO.toString(),result.toString());
        return result.toString();
    }

    /**
     * Generic unMarshaller to unmarshal a String to a given Class.
     * @param message String
     * @param aClazz An object of the class that needs to be unmarshalled to
     * @param <T>
     * @return
     */

    @Override
    public <T> T unmarshalMessage(String message, Class<T> aClazz) {

        //TODO Move this method to a UTIL class because this can be used by different classes in the far far future.
        //TODO Wright custom ExceptionHandler;
        T object = null;

        try {

            JAXBContext context = JAXBContext.newInstance(aClazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            StringReader reader = new StringReader(message);

            object = aClazz.cast(unmarshaller.unmarshal(reader));

        } catch (JAXBException e) {
            e.printStackTrace();

        }
        return object;
    }

}
