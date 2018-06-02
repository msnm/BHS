package be.kdg.bhs.organizer.exceptions;

/**
 * Whenever the external conveyorservice application throws a message it is wrapped in this ConveyorServiceException
 * @author Michael
 * @project BHS
 */
public class ConveyorServiceException extends RoutingException {

    public ConveyorServiceException(String message) {
        super(message);
    }
}
