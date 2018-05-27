package be.kdg.bhs.organizer.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Michael
 * @project BHS
 */
@XmlRootElement(name = "sensorMessage")
public class SensorMessageDTO {

    private Integer bagageID;
    private Integer conveyorID;

    public SensorMessageDTO() {
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
