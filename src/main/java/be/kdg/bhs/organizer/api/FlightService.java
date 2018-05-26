package be.kdg.bhs.organizer.api;

import be.kdg.bhs.organizer.exceptions.FlightServiceException;

import java.util.Map;

/**
 * @author Michael
 * @project BHS
 * A class implementing this interface provides flightInformation, based on String input.
 */
public interface FlightService {

    /**
     *
     * @param flightNumber
     * @return
     * @throws FlightServiceException
     */
    Integer flightInFormation(Integer flightNumber) throws FlightServiceException;

}
