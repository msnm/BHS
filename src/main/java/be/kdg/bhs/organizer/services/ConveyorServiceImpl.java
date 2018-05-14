package be.kdg.bhs.organizer.services;

import be.kdg.bhs.organizer.api.ConveyorService;
import be.kdg.bhs.organizer.dto.RouteDTO;
import be.kdg.bhs.organizer.model.Suitcase;
import be.kdg.se3.proxy.ConveyorServiceProxy;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Michael
 * @project BHS
 */
public class ConveyorServiceImpl implements ConveyorService {
    private ConveyorServiceProxy conveyorServiceProxy;

    public ConveyorServiceImpl(ConveyorServiceProxy conveyorServiceProxy) {
        this.conveyorServiceProxy = conveyorServiceProxy;
    }

    @Override
    public RouteDTO routeInformation(Suitcase suitcase) {
        String url = "www.services4se3.com/conveyorservice/route/";
        JSONObject payload = null;
        RouteDTO routesDTO = new RouteDTO();
        if(conveyorServiceProxy!=null) {
            try {
                payload = new JSONObject(conveyorServiceProxy.get(url.concat(suitcase.getConveyorId().toString())
                        .concat("-").concat(suitcase.getBoardingConveyorId().toString())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //{"routes":[{"route":"55-61-60-70-67"},{"route":"55-66-72-50-67"},{"route":"55-94-67-62-67"}]}
        JSONArray routesJsonArray = payload.getJSONArray("routes");
        List<List<Integer>> routes = new ArrayList<>();

        for(int i = 0; i < routesJsonArray.length(); i++) {
            JSONObject object = routesJsonArray.getJSONObject(i);
            String[] route = object.getString("route").split("-");
            List<Integer> aRoute = new ArrayList<>();
            for(int j = 0; j < route.length ; j++) {
                aRoute.add(Integer.parseInt(route[j]));
            }
            routes.add(aRoute);
        }
        routesDTO.setRoutes(routes);

        return routesDTO;
    }
}
