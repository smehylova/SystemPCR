package Objects;

import structures.TwoThreeTree;

public class District implements Comparable<District> {
    private int id;
    private TwoThreeTree<Objects.PCR> PCR;
    private TwoThreeTree<Objects.PCR_notes> PCR_notes;

    public int getId() {
        return id;
    }

    public TwoThreeTree<Objects.PCR> getPCR() {
        return PCR;
    }

    public District(int id_) {
        this.id = id_;
        this.PCR = new TwoThreeTree<>();
        this.PCR_notes = new TwoThreeTree<>();
    }

    public TwoThreeTree<Objects.PCR_notes> getPCR_notes() {
        return PCR_notes;
    }

    public void setPCR_notes(TwoThreeTree<Objects.PCR_notes> PCR_notes) {
        this.PCR_notes = PCR_notes;
    }

    @Override
    public int compareTo(District o) {
        return Integer.compare(this.id, o.getId());
    }
}
