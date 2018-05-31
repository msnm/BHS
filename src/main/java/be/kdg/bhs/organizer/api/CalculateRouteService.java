package be.kdg.bhs.organizer.api;

import be.kdg.bhs.organizer.model.Route;
import be.kdg.bhs.organizer.model.Routes;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Michael
 * @project BHS
 * Interface that provides method to calculate the optimal route
 */
public interface CalculateRouteService {

    /**
     * Calculates the optimal route, based on conveyordensity and return the next conveyor.
     * @param routes are the routes provided by the {@link ConveyorService}
     * @param currentConveyor is the currentconveyor where the suitcase resides.
     * @return the next conveyor for the suitcase.
     */
    Integer nextConveyorInRoute(Routes routes, Integer currentConveyor);

    /**
     * A central concurrentHashMap needs to be set where the conveyorTraffic is stored.
     * @param conveyorTraffic
     */
    void setConveyorTraffic(ConcurrentHashMap<Integer, Integer> conveyorTraffic);
}