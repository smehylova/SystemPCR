package structures;

public class TwoThreeDataNode<T extends Comparable<T>> {
    private T key;
    private TwoThreeNode<T> node;

    public TwoThreeDataNode(T key_, TwoThreeNode<T> node_) {
        this.key = key_;
        this.node = node_;
    }

    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }

    public TwoThreeNode<T> getNode() {
        return node;
    }

    public void setNode(TwoThreeNode<T> node) {
        this.node = node;
    }
}
