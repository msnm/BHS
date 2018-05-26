package be.kdg.bhs.organizer.api;

import be.kdg.bhs.organizer.dto.RouteDTO;
import be.kdg.bhs.organizer.exceptions.ConveyorServiceException;
import be.kdg.bhs.organizer.model.Suitcase;

/**
 * @author Michael
 * @project BHS
 * A class implementing this interface provides RouteInformation, based on {@link Suitcase} object.
 */
public interface ConveyorService {

    /**
     *
     * @param startConveyor the startingpoint of a route
     * @param boardingConveyor the endpoint of the route
     * @return
     * @throws ConveyorServiceException
     */
    RouteDTO routeInformation(Integer startConveyor, Integer boardingConveyor) throws ConveyorServiceException;
}
