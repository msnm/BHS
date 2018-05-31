package be.kdg.bhs.organizer.utils;

/**
 * @author Michael
 * @project BHS
 * Every entity/model Class that wants an inMemoryCacheMap needs to make use of this generic CacheObject class
 * in order to be able to use {@link InMemoryCache}.
 */
public class CacheObject<V> {
    private V cacheObject;
    private long timeEnteredCache = System.currentTimeMillis();

    public CacheObject(V cacheObject) {
        this.cacheObject = cacheObject;
    }

    public V getCacheObject() {
        if (cacheObject==null) {
            return null;
        }
        return cacheObject;
    }

    public void setCacheObject(V cacheObject) {
        this.cacheObject = cacheObject;
    }

    public long getTimeEnteredCache() {
        return timeEnteredCache;
    }

}
