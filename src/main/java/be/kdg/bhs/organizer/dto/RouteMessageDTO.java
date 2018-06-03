package be.kdg.bhs.organizer.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a Routing Message that is put on a queue. It is JAXB annotated, to be marshalled or unmarshalled.
 * @author Michael
 * @project BHS
 */

@XmlRootElement(name="routeMessage")
public class RouteMessageDTO {
    private Integer suitcaseId;
    private Integer conveyorId;

    public RouteMessageDTO() {
    }

    public RouteMessageDTO(Integer bagageID, Integer conveyourID) {
        this.suitcaseId = bagageID;
        this.conveyorId = conveyourID;
    }

    public Integer getSuitcaseId() {
        return suitcaseId;
    }

    public void setSuitcaseId(Integer suitcaseId) {
        this.suitcaseId = suitcaseId;
    }

    public Integer getConveyorId() {
        return conveyorId;
    }

    public void setConveyorId(Integer conveyorId) {
        this.conveyorId = conveyorId;
    }
}
