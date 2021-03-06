package be.kdg.bhs.organizer.services;

import be.kdg.bhs.organizer.api.FlightService;
import be.kdg.bhs.organizer.exceptions.FlightServiceException;
import be.kdg.se3.proxy.FlightServiceProxy;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Michael
 * @project BHS
 *
 * Implementation of the FlightService interface. This service returns a boardingConveyorId for a given FlightId.
 */
public class FlightServiceImpl implements FlightService {
    private static Logger logger = LoggerFactory.getLogger(FlightService.class);
    FlightServiceProxy flightServiceProxy;

    public FlightServiceImpl() {
        this.flightServiceProxy = new FlightServiceProxy();
    }

    @Override
    public Integer flightInFormation(Integer flightNumber) throws FlightServiceException {
        logger.debug("Entered: flightservice({})",flightNumber);

        JSONObject payLoad = null;
        String url = "www.services4se3.com/flightservice/";
        Integer boardingConveyorId = null;
        try {
            logger.info("Entering flightInFormation({})", flightNumber);
            payLoad = new JSONObject(flightServiceProxy.get(url.concat(flightNumber.toString())));

            //{"flightID":1234567,"boardingConveyorID":74}

            boardingConveyorId = payLoad.getInt("boardingConveyorID");

        } catch (Exception e) {
            throw new FlightServiceException(e.getMessage(),flightNumber);
        }
        logger.info("End flightservice({}) with boardingConveyor {}",flightNumber,boardingConveyorId);
        return boardingConveyorId;
    }
}
