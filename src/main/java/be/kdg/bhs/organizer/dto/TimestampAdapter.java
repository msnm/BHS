package be.kdg.bhs.organizer.dto;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Michael
 * @project BHS
 */
public class TimestampAdapter  extends XmlAdapter<Date,Timestamp> {

    @Override
    public Timestamp unmarshal(Date v) throws Exception {
        return new Timestamp(v.getTime());
    }

    @Override
    public Date marshal(Timestamp v) throws Exception {
        return new Date(v.getTime());
    }
}
