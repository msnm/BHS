package be.kdg.bhs.organizer.dto;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;

/**
 * @author Michael
 * @project BHS
 */
@XmlRootElement(name = "sensormessage")
public class SensorMessageDTO {

    private Integer suitcaseId;
    private Integer conveyorId;

    private Timestamp date;

    public SensorMessageDTO() {
    }

    public Integer getBagageID() {
        return suitcaseId;
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
    @XmlJavaTypeAdapter(TimestampAdapter.class)
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
