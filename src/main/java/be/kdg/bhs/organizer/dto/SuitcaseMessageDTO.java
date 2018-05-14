package be.kdg.bhs.organizer.dto;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * @author Michael
 * @project BHS
 */


@XmlRootElement(name = "suitcase")
public class SuitcaseMessageDTO implements MessageDTO {
    private Integer id;
    private Integer flightNumber;
    private Integer conveyorId;


    private Timestamp date;

    public SuitcaseMessageDTO() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFlightNumber(Integer flightNumber) {
        this.flightNumber = flightNumber;
    }

    public void setConveyorId(Integer conveyorId) {
        this.conveyorId = conveyorId;
    }

    public void setDate(Timestamp date) {
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

    @XmlJavaTypeAdapter(TimestampAdapter.class)
    public Timestamp getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("Id %d%n Flightnumber %d%n ConveyorId %d%n Date %s%n",
                this.id, this.flightNumber, this.conveyorId, this.date);
    }
}
