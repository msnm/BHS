package be.kdg.bhs.organizer.model;

import java.sql.Timestamp;

/**
 * @author Michael
 * @project BHS
 *
 * Business entity representing a Suitcase
 */
public class Suitcase {

    private final Integer id;
    private final Integer flightNumber;
    private Integer conveyorId;
    private Timestamp date;

    public Suitcase(Integer id, Integer flightNumber, Integer conveyorId, Timestamp date) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.conveyorId = conveyorId;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public Integer getFlightNumber() {
        return flightNumber;
    }

    public Integer getConveyorId() {
        return conveyorId;
    }

    public void setConveyorId(Integer conveyorId) {
        this.conveyorId = conveyorId;
    }


    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
