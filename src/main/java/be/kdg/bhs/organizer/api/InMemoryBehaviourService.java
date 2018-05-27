package be.kdg.bhs.organizer.api;

import java.util.Map;

/**
 * @author Michael
 * @project BHS
 *
 */
public interface InMemoryBehaviourService {

    <K,V> void clearExpiredObjects(Map<K, V> cacheMap,long expireTime, long intervalToCheck);
}
