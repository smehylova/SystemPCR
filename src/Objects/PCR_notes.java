package Objects;

public class PCR_notes implements Comparable<PCR_notes>  {
    public String notes;
    public PCR PCR;

    public PCR_notes(String notes_) {
        this.notes = notes_;
    }

    public PCR_notes(String notes_, PCR pcr_) {
        this.notes = notes_;
        this.PCR = pcr_;
    }

    @Override
    public int compareTo(PCR_notes o) {
        if (this.notes.compareTo(o.getNotes()) == 0) {
            return this.PCR.getId().compareTo(o.getPCR().getId());
        }
        return this.notes.compareTo(o.getNotes());
    }

    public String getNotes() {
        return notes;
    }

    public PCR getPCR() {
        return PCR;
    }
}
