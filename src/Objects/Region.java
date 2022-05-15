package Objects;
import structures.TwoThreeTree;

public class Region implements Comparable<Region> {
    private int id;
    private TwoThreeTree<PCR> PCR;

    public int getId() {
        return id;
    }

    public TwoThreeTree<Objects.PCR> getPCR() {
        return PCR;
    }

    public Region(int id_) {
        this.id = id_;
        this.PCR = new TwoThreeTree<PCR>();
    }

    @Override
    public int compareTo(Region o) {
        return Integer.compare(this.id, o.getId());
    }
}
