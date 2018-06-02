package be.kdg.bhs.organizer.model;

/**
 * @author Michael
 * @project BHS
 * Entity class represeting a StatusMessage that is send to the Simulator Application.
 */
public class StatusMessage {

    private Status status;
    private Integer suitcaseId;
    private Integer conveyorId;

    public StatusMessage(Status status, Integer suitcaseId, Integer conveyorId) {
        this.status = status;
        this.suitcaseId = suitcaseId;
        this.conveyorId = conveyorId;
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

    @Override
    public String toString() {
        return String.format("Status: %s  Suitcase: %d Conveyor: %d",status.toString(),suitcaseId,conveyorId);
    }
}
