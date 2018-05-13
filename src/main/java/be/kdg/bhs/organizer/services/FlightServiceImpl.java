package be.kdg.bhs.organizer.services;

import be.kdg.bhs.organizer.api.FlightService;
import be.kdg.se3.proxy.FlightServiceProxy;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.HttpResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael
 * @project BHS
 *
 * CImplementation of the FlightService interface. This service returns a boardingConveyorId for a given FlightId.
 */
public class FlightServiceImpl implements FlightService {
    private static Logger logger = LoggerFactory.getLogger(FlightService.class);
    FlightServiceProxy flightServiceProxy;

    public FlightServiceImpl(FlightServiceProxy flightServiceProxy) {
        this.flightServiceProxy = flightServiceProxy;
    }

    @Override
    public Integer flightInFormation(Integer flightNumber) {
        //Todo exception mapper schrijven, want flightService proxy gooit verschillende exceptions
        JSONObject payLoad = null;
        String url = "www.services4se3.com/flightservice/";
        Integer boardingConveyorId = null;
        try {
            logger.info("Entering flightInFormation({})", flightNumber);
            payLoad = new JSONObject(flightServiceProxy.get(url.concat(flightNumber.toString())));

            if(payLoad == null) {
                //TODO exception
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //{"flightID":1234567,"boardingConveyorID":74}

        boardingConveyorId = payLoad.getInt("boardingConveyorID");

        return boardingConveyorId;
    }
}
