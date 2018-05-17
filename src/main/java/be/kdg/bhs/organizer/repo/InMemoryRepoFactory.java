package be.kdg.bhs.organizer.repo;

/**
 * @author Michael
 * @project BHS
 * An interface used to define which CRUD operations an InmemoryRepoImp needs to implement
 */
public interface InMemoryRepoFactory<T> {

    void createInMemoryRepoMap(T... object);

    T getInMemoryCache(String key);

    void addInMemoryCache(T object);

    void removeInMemoryCache(String key);
}
