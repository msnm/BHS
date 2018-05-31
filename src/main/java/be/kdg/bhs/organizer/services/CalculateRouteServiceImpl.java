package be.kdg.bhs.organizer.services;

import be.kdg.bhs.organizer.api.CalculateRouteService;
import be.kdg.bhs.organizer.model.Route;
import be.kdg.bhs.organizer.model.Routes;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements the {@link CalculateRouteService} and calculates the most optimal route. In order to do this this
 * class has a ConcurrentHashMap which stores the routeDensity.
 * @author Michael
 * @project BHS
 */
public class CalculateRouteServiceImpl implements CalculateRouteService {
    Logger logger = LoggerFactory.getLogger(CalculateRouteService.class);

    ConcurrentHashMap<Integer, Integer> conveyorTraffic;

    @Override
    public void setConveyorTraffic(ConcurrentHashMap<Integer, Integer> conveyorTraffic) {
        this.conveyorTraffic = conveyorTraffic;
    }

    public ConcurrentHashMap<Integer, Integer> getConveyorTraffic() {
        return conveyorTraffic;
    }

    @Override
    public Integer nextConveyorInRoute(Routes routes, Integer currentConveyor) {
        logger.debug("Entered nextConveyorInRoute({},{})",routes.toString(), currentConveyor);

        //1. Calculate the most optimal route
        Route mostOptimalRoute = shortestRoute(routes, currentConveyor);

        //2. Retrieve the next conveyor
        Integer nextConveyor = mostOptimalRoute.getRoute().get(mostOptimalRoute.getRoute().indexOf(currentConveyor)+1);

        //3. Update the conveyorTraffic.
        //3.a
        //When the currentConveyor does not exist in the hashmap it is not added to the hashmap,
        // because the suitcase will shortly disappear from that conveyor
        // If the conveyor does exist in the hashmap the count is set minus 1
        if (conveyorTraffic.containsKey(currentConveyor)) {
            conveyorTraffic.put(currentConveyor,conveyorTraffic.get(currentConveyor)-1);
        }
        //3.b adding the new nextConveyor to the hashmap, or augmenting its count if it already exists.
        if (conveyorTraffic.containsKey(currentConveyor)) {
            conveyorTraffic.put(currentConveyor,conveyorTraffic.get(currentConveyor)+1);
        }
        else {
            conveyorTraffic.put(currentConveyor,1);
        }
        logger.debug("End nextConveyorInRoute({},{}) with the optimalroute {} and nextconveyor {}",routes.toString(), currentConveyor,mostOptimalRoute.toString(),nextConveyor);

        return nextConveyor;

    }

    private Route shortestRoute(Routes routes, Integer currentConveyor) {


        //2. The route density is the sum of the suitcases on each conveyor.
        Map<Route, Integer> routeDensity = new HashMap<>();
        Route mostOptimalRoute = null;
        for (Route route : routes.getRouteList()) {
            int density = 0;
            for (int conveyor : route.getRoute()) {
                if (conveyorTraffic.containsKey(conveyorTraffic)) {
                    density+=conveyorTraffic.get(conveyor);
                }
            }
            routeDensity.put(route,density);
        }
        //3. The shortest route is de route with the lowest routeDensity
        Integer min = routeDensity.values().stream().min(Integer::compareTo).get();
        List<Route> minRoutes = new ArrayList<>();
        for(Route route : routeDensity.keySet()) {
            if (routeDensity.get(route).equals(min)) {
                minRoutes.add(route);
            }
        }

        //3.b When there are more routes where the value equals min we take the route with the least conveyors.
        // If there are two or more routes with the same amount of conveyors we take the first one, because we are indifferent.
        if (minRoutes.size()>1) {
            min = minRoutes.get(0).getRoute().size();
            Route minRoute= minRoutes.get(0);
            for (int i =1; i< minRoutes.size();i++) {
                Integer countConveyors = new Integer(minRoutes.get(i).getRoute().size());
                if (countConveyors.compareTo(min)<0) {
                    minRoute = minRoutes.get(i);
                    min = countConveyors;
                }
            }
            mostOptimalRoute = minRoute;
        }
        else {
            mostOptimalRoute = minRoutes.get(0);
        }
        return mostOptimalRoute;
    }


}
