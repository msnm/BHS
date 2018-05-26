package be.kdg.bhs.organizer.api;

import be.kdg.bhs.organizer.model.Route;
import be.kdg.bhs.organizer.model.Routes;

/**
 * @author Michael
 * @project BHS
 * Interface that provides method to calculate the optimal route
 */
public interface CalculateRouteService {


    Route shortestRoute(Routes route);

    void nextConveyourInRoute();
}
