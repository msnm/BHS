package be.kdg.bhs.organizer.dto;

import be.kdg.bhs.organizer.model.StatusMessage;

/**
 * Converts Entity Objects to Data Transfer Objects
 * @author Michael
 * @project BHS
 */
public class EOtoDTO {

    public static RouteMessageDTO RouteToRouteMessageDTO(Integer nextConveyor, Integer suitcaseId) {
        return  new RouteMessageDTO(suitcaseId, nextConveyor);
    }

    public static StatusMessageDTO StatusMessageToStatusMessageDTO(StatusMessage statusMessage) {
        return  new StatusMessageDTO(statusMessage.getStatus(),statusMessage.getSuitcaseId(),statusMessage.getConveyorId());
    }
}
