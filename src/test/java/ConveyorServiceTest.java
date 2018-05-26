import be.kdg.bhs.organizer.api.ConveyorService;
import be.kdg.bhs.organizer.dto.RouteDTO;
import be.kdg.bhs.organizer.exceptions.ConveyorServiceException;
import be.kdg.bhs.organizer.services.ConveyorServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael
 * @project BHS
 */
public class ConveyorServiceTest {

    ConveyorService conveyorService;

    @Before
    public void setUp() {
        this.conveyorService = new ConveyorServiceImpl(2000,100);
    }

    @Test
    public void conveyorTest() throws ConveyorServiceException {
        RouteDTO route1 = conveyorService.routeInformation(11, 21);
        System.out.println(route1.toString());
        Assert.assertTrue(route1!=null);
    }

    @Test(expected = ConveyorServiceException.class)
    public void conveyorExceptionTest() throws ConveyorServiceException {
        RouteDTO route1 = conveyorService.routeInformation(111, 21);
        System.out.println(route1.toString());
    }

    @Test
    public void conveyorCacheTest() throws ConveyorServiceException, InterruptedException {
        RouteDTO route1 = conveyorService.routeInformation(1, 22);
        Thread.sleep(500);
        RouteDTO route2 = conveyorService.routeInformation(1, 22);

        System.out.println(route1.toString());
        System.out.println(route1.toString());
        Assert.assertEquals(route1,route2);
        Thread.sleep(3000);
        RouteDTO route3 = conveyorService.routeInformation(1, 22);
        Assert.assertNotEquals(route1,route3);
        Assert.assertNotEquals(route2, route3);
    }
}
