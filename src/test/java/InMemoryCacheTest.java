import be.kdg.bhs.organizer.model.Suitcase;
import be.kdg.bhs.organizer.utils.CacheObject;
import be.kdg.bhs.organizer.utils.InMemoryCache;
import org.junit.Before;
import org.junit.Test;
import sun.jvm.hotspot.utilities.Assert;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Michael
 * @project BHS
 * TestClass that tests the behaviour of {@linkInMemoryCache}.
 */
public class InMemoryCacheTest {
    List<Suitcase> suitcaseList;
    InMemoryCache<Integer,CacheObject<Suitcase>> inMemoryCache;

    @Before
    public void setUp() {
        this.suitcaseList = new ArrayList<>();
        this.suitcaseList.add(new Suitcase(1,123,1,new Timestamp(System.currentTimeMillis())));
        this.suitcaseList.add(new Suitcase(2,123,1,new Timestamp(System.currentTimeMillis())));
        this.suitcaseList.add(new Suitcase(3,123,1,new Timestamp(System.currentTimeMillis())));
    }

    @Test
    public void expirationTester() {
        inMemoryCache = new InMemoryCache<>(4000,1000);

        //1 second between each object
        for(Suitcase suitcase : suitcaseList) {
            inMemoryCache.putCacheObject(suitcase.getId(),new CacheObject<>(suitcase));
            System.out.println("Suitcase id: "+suitcase.getId()+" Entered on: "
                    + new Date(inMemoryCache.getCacheObject(suitcase.getId()).getTimeEnteredCache()));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //The three objects should be still in the list!
        System.out.println(inMemoryCache.getCacheMap().toString());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        org.junit.Assert.assertTrue(inMemoryCache.getCacheMap().containsKey(1)==false);
        org.junit.Assert.assertTrue(inMemoryCache.getCacheMap().size()<3);
        System.out.println(inMemoryCache.getCacheMap().toString());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(inMemoryCache.getCacheMap().toString());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(inMemoryCache.getCacheMap().toString());
        inMemoryCache.putCacheObject(suitcaseList.get(0).getId(),new CacheObject<>(suitcaseList.get(0)));
        System.out.println("Suitcase id: "+suitcaseList.get(0).getId()+" Entered on: "
                + new Date(inMemoryCache.getCacheObject(suitcaseList.get(0).getId()).getTimeEnteredCache()));
        System.out.println(inMemoryCache.getCacheMap().toString());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(inMemoryCache.getCacheMap().toString());
    }

}
