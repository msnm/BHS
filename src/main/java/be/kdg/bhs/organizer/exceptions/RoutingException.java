package be.kdg.bhs.organizer.exceptions;

/**
 * Any Exception thrown in {@link be.kdg.bhs.organizer.services.RoutingService} is wrapped in this exception.
 * @author Michael
 * @project BHS
 */
public class RoutingException extends  Exception {

    public RoutingException() {
    }

    public RoutingException(String message) {
        super(message);
    }
}
