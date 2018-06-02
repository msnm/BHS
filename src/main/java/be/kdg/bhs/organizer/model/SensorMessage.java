package be.kdg.bhs.organizer.model;

import java.sql.Timestamp;

/**
 * @author Michael
 * @project BHS
 * Entity Class representing a SensorMessage to correspond with the Simulator program.
 */
public class SensorMessage {
    private final Integer Id;
    private Integer conveyorId;
    private final Timestamp now;

    public SensorMessage(Integer id, Integer conveyorId, Timestamp now) {
        Id = id;
        this.conveyorId = conveyorId;
        this.now = now;
    }

    public Integer getId() {
        return Id;
    }

    public Integer getConveyorId() {
        return conveyorId;
    }

    public void setConveyorId(Integer conveyorId) {
        this.conveyorId = conveyorId;
    }

    public Timestamp getNow() {
        return now;
    }
}
