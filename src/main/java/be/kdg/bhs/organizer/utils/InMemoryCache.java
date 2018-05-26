package be.kdg.bhs.organizer.utils;


import java.util.*;

/**
 * @author Michael
 * @project BHS
 * This Class is a custom implementation for an InMemoryCache. When this class is instantiated, it will check every
 * x seconds if the objects in het cacheMap are expired. Objects V will be removed after expiration.
 * A future improvement would be to implement the Collection interface and create an implementation of its methods.
 */
public class InMemoryCache<K, V extends CacheObject> {
    private Map<K, V> cacheMap;

    public InMemoryCache(long expireTime, long intervalToCheck) {
        //Todo Exception maken, die getrowd wordt als expireTime en intervalToCheck waarden niet logisch zijn ingevuld!
        this.start(expireTime, intervalToCheck);
    }


    public synchronized Map<K, V> getCacheMap() {
        if (cacheMap==null) {
            this.cacheMap = new HashMap<>();
        }
        return this.cacheMap;
    }

    public void setCacheMap(Map<K, V> cacheMap) {
        this.cacheMap = cacheMap;
    }

    public void putCacheObject(K key, V value) {
        if (this.cacheMap == null) {
            this.cacheMap = new HashMap<>();
        }
        this.cacheMap.put(key, value);
    }

    public V getCacheObject(K key) {
        if (this.cacheMap == null) {
            this.cacheMap = new HashMap<>();
        }
        return this.cacheMap.get(key);
    }


    private void start(long expireTime, long intervalToCheck) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(intervalToCheck);
                        clearExpiredObjects(expireTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //This is a Deamon thread, because it is a non-blocking thread. This means if the JVM stops, this thread
        //will not prevent it. A deamon thread is like a background service.
        thread.setDaemon(true);
        thread.start();

    }

    private void clearExpiredObjects(long expireTime) {
        //As long as the map is not empty we need to check the expirationtime of the inMemoryCache objects
        if (this.cacheMap != null) {

            Iterator iterator = cacheMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                CacheObject<V> aCacheObject = (CacheObject<V>) entry.getValue();

                //An object is expired when the delta between current time and timeEnteredCache > expireTime
                long timeInterval = System.currentTimeMillis() - aCacheObject.getTimeEnteredCache();
                if (timeInterval > expireTime) {
                    System.out.println("Removed item with ID "+entry.getKey().toString()+ " on "
                            +new Date(aCacheObject.getTimeEnteredCache())+ " after timeInterval: " +timeInterval +"ms");
                    iterator.remove();
                }
            }
        }
    }
}
