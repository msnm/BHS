package be.kdg.bhs.organizer.api;

import be.kdg.bhs.organizer.dto.RouteDTO;
import be.kdg.bhs.organizer.model.Suitcase;

/**
 * @author Michael
 * @project BHS
 * A class implementing this interface provides RouteInformation, based on {@link Suitcase} object.
 */
public interface ConveyorService {

    RouteDTO routeInformation(Suitcase suitcase);
}
