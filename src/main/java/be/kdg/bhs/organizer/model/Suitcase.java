package be.kdg.bhs.organizer.model;

import java.sql.Timestamp;

/**
 * @author Michael
 * @project BHS
 *
 * Business entity representing a Suitcase.
 */
public class Suitcase {

    private final Integer suitCaseId;
    private final Integer flightNumber;
    private Integer conveyorId;
    private Integer boardingConveyorId;
    private Timestamp date;

    public Suitcase(Integer id, Integer flightNumber, Integer conveyorId, Timestamp date) {
        this.suitCaseId = id;
        this.flightNumber = flightNumber;
        this.conveyorId = conveyorId;
        this.date = date;
    }

    public Integer getSuitCaseId() {
        return suitCaseId;
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

    public Integer getBoardingConveyorId() {
        return boardingConveyorId;
    }

    public void setBoardingConveyorId(Integer boardingConveyorId) {
        this.boardingConveyorId = boardingConveyorId;
    }
}
