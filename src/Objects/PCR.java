package Objects;
import java.util.Date;

public class PCR implements Comparable<PCR> {
    private Date dateTime;
    private String id;
    private Person person;
    private Workplace workplace;
    private District district;
    private Region region;
    private boolean result;
    private String notes;

    public PCR(Date date_) {
        this.dateTime = date_;
        this.id = "0";
    }

    public PCR(Date dateTime_, String id_, Person person_, Workplace workplace_, District district_,Region region_,boolean result_, String notes_) {
        this.dateTime = dateTime_;
        this.id = id_;
        this.person = person_;
        this.workplace = workplace_;
        this.district = district_;
        this.region = region_;
        this.result = result_;
        this.notes = notes_;
    }

    @Override
    public int compareTo(PCR o) {
        if (this.dateTime.compareTo(o.getDateTime()) != 0) {
            return this.dateTime.compareTo(o.getDateTime());
        } else {
            return this.id.compareTo(o.getId());
        }
    }

    public Date getDateTime() {
        return dateTime;
    }

    public String getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public Workplace getWorkplace() {
        return workplace;
    }

    public District getDistrict() {
        return district;
    }

    public Region getRegion() {
        return region;
    }

    public boolean getResult() {
        return result;
    }

    public String getNotes() {
        return notes;
    }
}
