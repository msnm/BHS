package be.kdg.bhs.organizer.exceptions;

/**
 * @author Michael
 * @project BHS
 * Whenever the external implementation of the FlightService instance throws a message it is wrapped in this FlighServiceException
 */
public class FlightServiceException extends Exception {
    private Integer flightNumber;

    public FlightServiceException(String message) {
        super(message);
    }

    public FlightServiceException(String message,Integer flightNumber) {
        super(message);
        this.flightNumber=flightNumber;
    }

    public Integer getFlightNumber() {
        return flightNumber;
    }

}
