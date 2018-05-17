package be.kdg.bhs.organizer.repo;

import be.kdg.bhs.organizer.utils.CacheObject;
import be.kdg.bhs.organizer.utils.InMemoryCache;

import java.util.HashMap;
import java.util.List;

/**
 * @author Michael
 * @project BHS
 * Class implements InMemoryRepoFactory methods and makes use of the self created {@link be.kdg.bhs.organizer.utils.InMemoryCache}
 */
public class InMemoryRepoFactoryImpl implements  InMemoryRepoFactory<InMemoryCache> {

    private HashMap<String,InMemoryCache> repo;

    @Override
    public InMemoryCache getInMemoryCache(String key) {
        if(repo==null) {
            repo = new HashMap<>();
        }
        return repo.get(key);
    }

    @Override
    public void addInMemoryCache(InMemoryCache object) {
        if(repo==null) {
            repo = new HashMap<>();
        }
        repo.put(object.getClass().getSimpleName(),object);
    }

    @Override
    public void removeInMemoryCache(String key) {
        if(repo==null) {
            repo = new HashMap<>();
        }
        repo.remove(key);
    }

    @Override
    public void createInMemoryRepoMap(InMemoryCache... object) {
        this.repo = new HashMap<>();
        for (InMemoryCache o : object) {
            repo.put(o.getClass().getSimpleName(), o);
        }
    }

}
