package Objects;

import structures.TwoThreeTree;

public class Workplace implements Comparable<Workplace> {
    private int id;
    private TwoThreeTree<PCR> PCR;

    public Workplace(int id_) {
        this.id = id_;
        this.PCR = new TwoThreeTree<>();
    }

    public TwoThreeTree<PCR> getPCR() {
        return PCR;
    }

    public int getId() {
        return id;
    }

    @Override
    public int compareTo(Workplace o) {
        return Integer.compare(this.id, o.getId());
    }
}
