package Objects;

public class PCR_id implements Comparable<PCR_id> {
    public String id;
    public PCR PCR;

    public PCR_id(String id_) {
        this.id = id_;
    }

    public PCR_id(String id_, PCR pcr_) {
        this.id = id_;
        this.PCR = pcr_;
    }

    @Override
    public int compareTo(PCR_id o) {
        return this.id.compareTo(o.getId());
    }

    public String getId() {
        return id;
    }

    public PCR getPCR() {
        return PCR;
    }
}
