package be.kdg.bhs.organizer.dto;

import be.kdg.bhs.organizer.model.Route;
import be.kdg.bhs.organizer.model.Routes;
import be.kdg.bhs.organizer.model.Suitcase;
import java.util.List;


import java.util.ArrayList;

/**
 * @author Michael
 * @project BHS
 */
public class DTOtoEO {

    public static Suitcase suitCaseDTOtoEO(SuitcaseMessageDTO messageDTO) {
        return new Suitcase(messageDTO.getSuitcaseId(),messageDTO.getFlightNumber(),messageDTO.getConveyorId(),messageDTO.getDate());
    }

    public static Routes routesDTOtoEO(RouteDTO routeDTO) {
        List<Route> routeList= new  ArrayList<>();
        routeDTO.getRoutes().forEach(v -> routeList.add(new Route(v)));
        return new Routes(routeList,routeDTO.getStartConvor(),routeDTO.getEndConveyor());
    }

}
