package be.kdg.bhs.organizer.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Michael
 * @project BHS
 */

@XmlRootElement(name="routeMessage")
public class RouteMessageDTO {
    private Integer bagageID;
    private Integer conveyorID;

    public RouteMessageDTO() {
    }

    public RouteMessageDTO(Integer bagageID, Integer conveyourID) {
        this.bagageID = bagageID;
        this.conveyorID = conveyourID;
    }

    public Integer getBagageID() {
        return bagageID;
    }

    public void setBagageID(Integer bagageID) {
        this.bagageID = bagageID;
    }

    public Integer getConveyorID() {
        return conveyorID;
    }

    public void setConveyorID(Integer conveyorID) {
        this.conveyorID = conveyorID;
    }
}
