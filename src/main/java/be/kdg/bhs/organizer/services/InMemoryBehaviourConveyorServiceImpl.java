package be.kdg.bhs.organizer.services;

import be.kdg.bhs.organizer.api.InMemoryBehaviourService;
import be.kdg.bhs.organizer.dto.RouteDTO;
import be.kdg.bhs.organizer.repo.CacheObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Michael
 * @project BHS
 * Implemention of {@link InMemoryBehaviourService} for the {@link be.kdg.bhs.organizer.repo.InMemoryCache} in the {@link ConveyorServiceImpl}.
 * After a certain time without interaction a route is not anymore cached.
 */
public class InMemoryBehaviourConveyorServiceImpl implements InMemoryBehaviourService {
    private Logger logger = LoggerFactory.getLogger(InMemoryBehaviourConveyorServiceImpl.class);
    @Override
    public <K, V> void clearExpiredObjects(ConcurrentHashMap<K, V> cacheMap, long expireTime, long intervalToCheck) {
        //As long as the map is not empty we need to check the expirationtime of the inMemoryCache objects
        if (cacheMap != null) {

            Iterator iterator = cacheMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                CacheObject<V> aCacheObject = (CacheObject<V>) entry.getValue();
                RouteDTO routeDTO = (RouteDTO) aCacheObject.getCacheObject();

                //An object is expired when the delta between current time and timeEnteredCache > expireTime
                long timeInterval = System.currentTimeMillis() - aCacheObject.getTimeEnteredCache();
                if (timeInterval > expireTime) {
                    logger.info("Removed Route {} from cacheOfRoutes on {} after timeinterval {} ms",routeDTO.toString(),new Date(aCacheObject.getTimeEnteredCache()), timeInterval);
                    iterator.remove();
                }
            }
        }
    }
}
