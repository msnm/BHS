package be.kdg.bhs.organizer.dto;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;

/**
 * Represents a Suitcase Message that is read from a queue. It is JAXB annotated, to be marshalled or unmarshalled.
 * @author Michael
 * @project BHS
 */


@XmlRootElement(name = "suitcasemessage")
public class SuitcaseMessageDTO {
    private Integer suitcaseId;
    private Integer flightNumber;
    private Integer conveyorId;


    private Timestamp date;

    public SuitcaseMessageDTO(Integer suitcaseId, Integer flightNumber, Integer conveyorId, Timestamp date) {
        this.suitcaseId = suitcaseId;
        this.flightNumber = flightNumber;
        this.conveyorId = conveyorId;
        this.date = date;
    }

    public SuitcaseMessageDTO() {
    }

    public void setSuitcaseId(Integer suitcaseId) {
        this.suitcaseId = suitcaseId;
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

    public Integer getSuitcaseId() {
        return suitcaseId;
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
        return String.format(" Id %d%n Flightnumber %d%n ConveyorId %d%n Date %s%n",
                this.suitcaseId, this.flightNumber, this.conveyorId, this.date);
    }
}
