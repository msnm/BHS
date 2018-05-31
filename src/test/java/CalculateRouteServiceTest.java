import be.kdg.bhs.organizer.api.CalculateRouteService;
import be.kdg.bhs.organizer.model.Route;
import be.kdg.bhs.organizer.model.Routes;
import be.kdg.bhs.organizer.services.CalculateRouteServiceImpl;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Michael
 * @project BHS
 */
public class CalculateRouteServiceTest {

    private CalculateRouteService calculateRouteService;

    @Before
    public void setUp() {
        this.calculateRouteService = new CalculateRouteServiceImpl();
        this.calculateRouteService.setConveyorTraffic(new ConcurrentHashMap<>());
    }

    @Test
    public void testRouteCalculation() {
        List<Integer> conveyorsRoute1 = new ArrayList<>();
        conveyorsRoute1.add(1);
        conveyorsRoute1.add(2);
        Route route1 = new Route(conveyorsRoute1);

        List<Integer> conveyorsRoute2 = new ArrayList<>();
        conveyorsRoute2.add(1);
        conveyorsRoute2.add(3);
        Route route2 = new Route(conveyorsRoute2);

        //We populate the concurrenthashMap
        ConcurrentHashMap<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put(1,10);
        concurrentHashMap.put(2,20);
        concurrentHashMap.put(3,30);
        List<Route> routeList = new ArrayList<>();
        routeList.add(route1);
        routeList.add(route2);
        Routes routes = new Routes(routeList);

        this.calculateRouteService.setConveyorTraffic(concurrentHashMap);
        Integer nextConveyor = this.calculateRouteService.nextConveyorInRoute(routes,1);

        Assert.assertEquals(new Integer(2), nextConveyor);

    }

    @Test
    public void testRouteCalculationNoTraffic() {
        // When the traffic density is the same it should take the shortestRoute.
        List<Integer> conveyorsRoute1 = new ArrayList<>();
        conveyorsRoute1.add(1);
        conveyorsRoute1.add(2);
        conveyorsRoute1.add(3);
        Route route1 = new Route(conveyorsRoute1);

        List<Integer> conveyorsRoute2 = new ArrayList<>();
        conveyorsRoute2.add(1);
        conveyorsRoute2.add(3);
        Route route2 = new Route(conveyorsRoute2);


        List<Route> routeList = new ArrayList<>();
        routeList.add(route1);
        routeList.add(route2);
        Routes routes = new Routes(routeList);

        Integer nextConveyor = this.calculateRouteService.nextConveyorInRoute(routes,1);

        Assert.assertEquals(new Integer(3), nextConveyor);

    }
}
