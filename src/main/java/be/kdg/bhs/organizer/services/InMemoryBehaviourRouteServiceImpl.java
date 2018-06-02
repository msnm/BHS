package be.kdg.bhs.organizer.services;

import be.kdg.bhs.organizer.api.InMemoryBehaviourService;
import be.kdg.bhs.organizer.api.MessageConsumerListener;
import be.kdg.bhs.organizer.model.Status;
import be.kdg.bhs.organizer.model.StatusMessage;
import be.kdg.bhs.organizer.model.Suitcase;
import be.kdg.bhs.organizer.repo.CacheObject;
import be.kdg.bhs.organizer.repo.InMemoryCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Michael
 * @project BHS
 * Implemention of {@link InMemoryBehaviourService} for the {@link InMemoryCache} in the {@link RoutingService}.
 * After a certain time without interaction a suitcase is lost and putted on the lostqueue.
 */
public class InMemoryBehaviourRouteServiceImpl implements InMemoryBehaviourService{
    private Logger logger = LoggerFactory.getLogger(InMemoryBehaviourRouteServiceImpl.class);

    MessageConsumerListener messageConsumerListener;

    public InMemoryBehaviourRouteServiceImpl(MessageConsumerListener messageConsumerListener) {
        this.messageConsumerListener = messageConsumerListener;
    }

    @Override
    public <K, V> void clearExpiredObjects(ConcurrentHashMap<K, V> cacheMap, long expireTime, long intervalToCheck) {
        //As long as the map is not empty we need to check the expirationtime of the inMemoryCache objects
        if (cacheMap != null) {

            Iterator iterator = cacheMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                CacheObject<V> aCacheObject = (CacheObject<V>) entry.getValue();

                //We can cast this because this is a specific implementation, so we know the type.
                Suitcase suitcase = (Suitcase) aCacheObject.getCacheObject();

                //A suitcase is lost when the delta between current time and timeEnteredCache > expireTime
                long timeInterval = System.currentTimeMillis() - aCacheObject.getTimeEnteredCache();
                if (timeInterval > expireTime) {
                    logger.info("InMemoryBehaviourRouteService removed suitcase with ID {} because it is lost {} on {} after time interval {} ms",suitcase.getSuitCaseId(),new Date(aCacheObject.getTimeEnteredCache()),timeInterval);
                    messageConsumerListener.sendStatusMessage(new StatusMessage(Status.LOST,suitcase.getSuitCaseId(),suitcase.getConveyorId()));
                    iterator.remove();

                }
            }
        }
    }
}
