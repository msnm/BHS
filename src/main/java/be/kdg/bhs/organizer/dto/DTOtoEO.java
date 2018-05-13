package be.kdg.bhs.organizer.dto;

import be.kdg.bhs.organizer.model.Suitcase;

/**
 * @author Michael
 * @project BHS
 */
public class DTOtoEO {

    public static Suitcase suitCaseDTOtoEO(SuitcaseMessageDTO messageDTO) {
        return new Suitcase(messageDTO.getId(),messageDTO.getFlightNumber(),messageDTO.getConveyorId(),messageDTO.getDate());
    }

}
