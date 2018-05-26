package be.kdg.bhs.organizer.services;

import be.kdg.bhs.organizer.api.CalculateRouteService;
import be.kdg.bhs.organizer.model.Route;
import be.kdg.bhs.organizer.model.Routes;

/**
 * @author Michael
 * @project BHS
 */
public class CalculateRouteServiceImpl implements CalculateRouteService {

    @Override
    public Route shortestRoute(Routes route) {
        return route.getRouteList().get(0);
    }

    @Override
    public void nextConveyourInRoute() {

    }
}
