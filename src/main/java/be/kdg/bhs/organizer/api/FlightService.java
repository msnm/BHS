package be.kdg.bhs.organizer.api;

import be.kdg.bhs.organizer.exceptions.FlightServiceException;

/**
 * @author Michael
 * @project BHS
 * A class implementing this interface provides flightInformation, based on String input.
 */
public interface FlightService {

    /**
     *
     * @param flightNumber is needed to retrieve the boardingconveyor.
     * @return the boardingconveyor (=gate)
     * @throws FlightServiceException
     */
    Integer flightInFormation(Integer flightNumber) throws FlightServiceException;

}
