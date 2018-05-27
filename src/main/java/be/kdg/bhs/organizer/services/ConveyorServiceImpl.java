package be.kdg.bhs.organizer.services;

import be.kdg.bhs.organizer.api.ConveyorService;
import be.kdg.bhs.organizer.dto.RouteDTO;
import be.kdg.bhs.organizer.exceptions.ConveyorServiceException;
import be.kdg.bhs.organizer.utils.CacheObject;
import be.kdg.bhs.organizer.utils.InMemoryCache;
import be.kdg.se3.proxy.ConveyorServiceProxy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael
 * @project BHS
 */
public class ConveyorServiceImpl implements ConveyorService {
    private ConveyorServiceProxy conveyorServiceProxy;
    private InMemoryCache<String,CacheObject<RouteDTO>> cacheOfRoutes;
    private Logger logger = LoggerFactory.getLogger(ConveyorServiceImpl.class);

    public ConveyorServiceImpl(long expiretimeOfCache, long intervalToCheckCache) {
        this.conveyorServiceProxy = new ConveyorServiceProxy();
        this.cacheOfRoutes = new InMemoryCache<>(expiretimeOfCache,intervalToCheckCache,new InMemoryBehaviourConveyorServiceImpl());

    }

    @Override
    public RouteDTO routeInformation(Integer startConveyor, Integer boardingConveyor) throws ConveyorServiceException {
        logger.info("Entered routeInformation({},{})",startConveyor,boardingConveyor);
        RouteDTO routesDTO = checkIfRouteInfoIsCached(startConveyor,boardingConveyor);
        if (routesDTO == null) {
            routesDTO = askConveyorService(startConveyor,boardingConveyor);
            cacheOfRoutes.putCacheObject(makeKey(startConveyor, boardingConveyor),new CacheObject<>(routesDTO));
            System.out.println("Added item with ID "+makeKey(startConveyor,boardingConveyor));
        }

        logger.info("End routeInformation({},{}) returned routes {}",startConveyor,boardingConveyor,routesDTO.toString());
        return routesDTO;
    }

    private RouteDTO askConveyorService(Integer startConveyor, Integer boardingConveyor) throws ConveyorServiceException {
        RouteDTO routesDTO = new RouteDTO();

        if(conveyorServiceProxy!=null) {
            try {
                String url = "www.services4se3.com/conveyorservice/route/";
                JSONObject payload = new JSONObject(conveyorServiceProxy.get(url.concat(startConveyor.toString())
                        .concat("-").concat(boardingConveyor.toString())));

                //{"routes":[{"route":"55-61-60-70-67"},{"route":"55-66-72-50-67"},{"route":"55-94-67-62-67"}]}
                JSONArray routesJsonArray = payload.getJSONArray("routes");
                List<List<Integer>> routes = new ArrayList<>();

                for(int i = 0; i < routesJsonArray.length(); i++) {
                    JSONObject object = routesJsonArray.getJSONObject(i);
                    String[] route = object.getString("route").split("-");
                    List<Integer> aRoute = new ArrayList<>();
                    for (String aRoute1 : route) {
                        aRoute.add(Integer.parseInt(aRoute1));
                    }
                    routes.add(aRoute);
                }
                routesDTO.setRoutes(routes);

            } catch (Exception e) {
                throw new ConveyorServiceException(e.getMessage());
            }
        }
        return routesDTO;
    }

    private RouteDTO checkIfRouteInfoIsCached(Integer conveyorId, Integer boardingConveyorId) {
        CacheObject<RouteDTO> routeDTOCache = cacheOfRoutes.getCacheObject(makeKey(conveyorId,boardingConveyorId));
        if ( routeDTOCache !=null) {
            return routeDTOCache.getCacheObject();
        }
       return null;
    }

    private String makeKey(Integer startConveyor, Integer boardingConveyor) {
        return startConveyor.toString().concat("-").concat(boardingConveyor.toString());
    }

}
