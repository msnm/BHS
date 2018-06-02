package be.kdg.bhs.organizer.api;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Michael
 * @project BHS
 *  This interface specifies commands for an inMemoryCache implementation. It defines the behaviour of an InMemoryCache.
 */
public interface InMemoryBehaviourService {

    /**
     * An InMemoryCache performs an action after a certain time on the expired objects. This action named
     * clearExpiredObjects can differ from situation to situation.
     * @param cacheMap A concurrentHashmap which stores the objects
     * @param expireTime time when an object expires
     * @param intervalToCheck time the thread needs to execute the clearExpiredObjects method
     * @param <K>   generic key
     * @param <V>   generic value
     */
    <K,V> void clearExpiredObjects(ConcurrentHashMap<K, V> cacheMap, long expireTime, long intervalToCheck);
}
