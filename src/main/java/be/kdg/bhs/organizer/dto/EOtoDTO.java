package be.kdg.bhs.organizer.dto;

import be.kdg.bhs.organizer.model.Route;
import be.kdg.bhs.organizer.model.Suitcase;

/**
 * @author Michael
 * @project BHS
 */
public class EOtoDTO {

    public static RouteMessageDTO RouteToRouteMessageDTO(Integer nextConveyor, Integer suitcaseId) {
        //TODO logic of getting the next conveyor should not be happing in EOtoDTO class. Need to improve this.
        return  new RouteMessageDTO(suitcaseId, nextConveyor);
    }
}
