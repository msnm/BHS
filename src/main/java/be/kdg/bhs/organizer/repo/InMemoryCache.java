package be.kdg.bhs.organizer.repo;


import be.kdg.bhs.organizer.api.InMemoryBehaviourService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Michael
 * @project BHS
 * This generic Class is a custom implementation for an InMemoryCache. It caches objects which are {@link CacheObject} or
 * extending it. The behaviour of the InMemoryCache is determined with the injection of the {@link InMemoryBehaviourService}. This makes it possible
 * to use this specific class and let the definition of what needs to be done with expired cacheobjects differ from usecase to usecase.
 */
public class InMemoryCache<K, V extends CacheObject> {
    Logger logger = LoggerFactory.getLogger(InMemoryCache.class);
    private ConcurrentHashMap<K, V> cacheMap;
    private InMemoryBehaviourService inMemoryBehaviourService;
    private boolean activateCache = true;

    /**
     * Constructor to instantiate the InMemoryCache.
     * @param expireTime time in milliseconds setting the cachetime (=expirationtime) for each Cacheobject
     * @param intervalToCheck time in milliseconds when the secondary thread needs to execute the {@link InMemoryBehaviourService} clearExpiredObjects
     * @param inMemoryBehaviourService defines what needs to be done with expiredobjects.
     */
    public InMemoryCache(long expireTime, long intervalToCheck, InMemoryBehaviourService inMemoryBehaviourService) {
        //Todo Exception maken, die getrowd wordt als expireTime en intervalToCheck waarden niet logisch zijn ingevuld!
        this.start(expireTime, intervalToCheck);
        this.inMemoryBehaviourService = inMemoryBehaviourService;
    }


    public synchronized ConcurrentHashMap<K, V> getCacheMap() {
        if (cacheMap==null) {
            this.cacheMap = new ConcurrentHashMap<>();
        }
        return this.cacheMap;
    }

    public void setCacheMap(ConcurrentHashMap<K, V> cacheMap) {
        this.cacheMap = cacheMap;
    }

    public boolean containsCacheObject(K key) {
        if (this.cacheMap == null) {
            this.cacheMap = new ConcurrentHashMap<>();
        }
        return this.cacheMap.containsKey(key);
    }

    public void putCacheObject(K key, V value) {
        if (this.cacheMap == null) {
            this.cacheMap = new ConcurrentHashMap<>();
        }
        this.cacheMap.put(key, value);
    }

    public V getCacheObject(K key) {
        if (this.cacheMap == null) {
            this.cacheMap = new ConcurrentHashMap<>();
        }
        return this.cacheMap.get(key);
    }

    public void removeCacheObject(K key) {
        if (this.cacheMap == null) {
            this.cacheMap = new ConcurrentHashMap<>();
        }
        this.cacheMap.remove(key);
    }


    private void start(long expireTime, long intervalToCheck) {

        Thread thread = new Thread(() -> {
            while (activateCache) {
                try {
                    Thread.sleep(intervalToCheck);
                    inMemoryBehaviourService.clearExpiredObjects(cacheMap,expireTime,intervalToCheck);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //This is a Deamon thread, because it is a non-blocking thread. This means if the JVM stops, this thread
        //will not prevent it. A deamon thread is like a background service.
        thread.setDaemon(true);
        thread.start();

    }

    public boolean isActivateCache() {
        return activateCache;
    }

    public void setActivateCache(boolean activateCache) {
        this.activateCache = activateCache;
    }
}
