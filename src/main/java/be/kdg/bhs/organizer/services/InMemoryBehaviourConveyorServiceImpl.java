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
 */
public class InMemoryBehaviourConveyorServiceImpl implements InMemoryBehaviourService {
    Logger logger = LoggerFactory.getLogger(InMemoryBehaviourConveyorServiceImpl.class);
    @Override
    public <K, V> void clearExpiredObjects(Map<K, V> cacheMap, long expireTime, long intervalToCheck) {
        //As long as the map is not empty we need to check the expirationtime of the inMemoryCache objects
        if (cacheMap != null) {

            Iterator iterator = cacheMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                CacheObject<V> aCacheObject = (CacheObject<V>) entry.getValue();

                //An object is expired when the delta between current time and timeEnteredCache > expireTime
                long timeInterval = System.currentTimeMillis() - aCacheObject.getTimeEnteredCache();
                if (timeInterval > expireTime) {
                    logger.info("Removed item with ID "+entry.getKey().toString()+ " on " +new Date(aCacheObject.getTimeEnteredCache())+ " after timeInterval: " +timeInterval +"ms");
                    iterator.remove();
                }
            }
        }
    }
}
