package be.kdg.bhs.organizer.services;

import be.kdg.bhs.organizer.api.InMemoryBehaviourService;
import be.kdg.bhs.organizer.utils.CacheObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Michael
 * @project BHS
 * Implemention of {@link InMemoryBehaviourService} for the {@link be.kdg.bhs.organizer.utils.InMemoryCache} in the {@link RoutingService}.
 * After a certain time without interaction a suitcase is lost and putted on the lostqueue.
 */
public class InMemoryBehaviourRouteServiceImpl implements InMemoryBehaviourService{
    Logger logger = LoggerFactory.getLogger(InMemoryBehaviourRouteServiceImpl.class);
    @Override
    public <K, V> void clearExpiredObjects(Map<K, V> cacheMap, long expireTime, long intervalToCheck) {
        //As long as the map is not empty we need to check the expirationtime of the inMemoryCache objects
        if (cacheMap != null) {

            Iterator iterator = cacheMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                CacheObject<V> aCacheObject = (CacheObject<V>) entry.getValue();

                //A suitcase is lost when the delta between current time and timeEnteredCache > expireTime
                long timeInterval = System.currentTimeMillis() - aCacheObject.getTimeEnteredCache();
                if (timeInterval > expireTime) {
                    logger.info("Removed suitcase with ID because it is lost"+entry.getKey()+ " on " + new Date(aCacheObject.getTimeEnteredCache())+ " after timeInterval: " +timeInterval +"ms");
                    //Todo implementatie die ervoor zorgt dat bericht lost wordt!
                    iterator.remove();
                }
            }
        }
    }
}
