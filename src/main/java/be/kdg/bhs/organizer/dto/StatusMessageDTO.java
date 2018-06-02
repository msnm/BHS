package be.kdg.bhs.organizer.dto;

import be.kdg.bhs.organizer.model.Status;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a Status Message that is put on a queue. It is JAXB annotated, to be marshalled or unmarshalled.
 * @author Michael
 * @project BHS
 */
@XmlRootElement(name="statusMessage")
public class StatusMessageDTO {

    private Status status;
    private Integer suitcaseId;
    private Integer conveyorId;

    public StatusMessageDTO(Status status, Integer suitcaseId, Integer conveyorId) {
        this.status = status;
        this.suitcaseId = suitcaseId;
        this.conveyorId = conveyorId;
    }

    public StatusMessageDTO() {
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
