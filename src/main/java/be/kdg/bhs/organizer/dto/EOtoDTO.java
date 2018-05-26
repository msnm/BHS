package be.kdg.bhs.organizer.dto;

import be.kdg.bhs.organizer.model.Route;
import be.kdg.bhs.organizer.model.Suitcase;

/**
 * @author Michael
 * @project BHS
 */
public class EOtoDTO {

    public static RouteMessageDTO RouteToRouteMessageDTO(Route route, Suitcase suitcase) {
        //TODO logic of getting the next conveyor should not be happing in EOtoDTO class. Need to improve this.
        return  new RouteMessageDTO(suitcase.getId(),route.getRoute().get(route.getRoute().indexOf(suitcase.getConveyorId())+1));
    }
}
