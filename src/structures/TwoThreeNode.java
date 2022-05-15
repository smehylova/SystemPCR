package structures;

import java.util.ArrayList;
import java.util.List;

public class TwoThreeNode<T extends Comparable<T>> {
    private TwoThreeNode<T> leftSon;
    private  TwoThreeNode<T> rightSon;
    private  TwoThreeNode<T> middleSon;
    private  TwoThreeNode<T> pomSon;
    private TwoThreeNode<T> father;
    private List<T> keys;
    private int lengthKeys;

    public TwoThreeNode() {
        this.lengthKeys = 0;
        this.keys = new ArrayList<T>(3);
    }

    public void addKey(T key) {
        if (this.lengthKeys == 0) {
            this.keys.add(0, key);
        }
        for (int i = 0; i < this.lengthKeys; i++) {
            if (this.keys.get(i).compareTo(key) > 0) {
                this.keys.add(i, key);
                i = this.lengthKeys;
            } else if (i == this.lengthKeys - 1) {
                this.keys.add(i + 1, key);
            }
        }
        this.lengthKeys++;
    }

    public T deleteKey(T key) {
        for (int i = 0; i < lengthKeys; i++) {
            if (this.keys.get(i).compareTo(key) == 0) {
                lengthKeys--;
                return this.keys.remove(i);
            }
        }
        return null;
    }

    public TwoThreeNode<T> getLeftSon() {
        return leftSon;
    }

    public void setLeftSon(TwoThreeNode<T> leftSon) {
        this.leftSon = leftSon;
    }

    public TwoThreeNode<T> getRightSon() {
        return rightSon;
    }

    public void setRightSon(TwoThreeNode<T> rightSon) {
        this.rightSon = rightSon;
    }

    public TwoThreeNode<T> getMiddleSon() {
        return middleSon;
    }

    public void setMiddleSon(TwoThreeNode<T> middleSon) {
        this.middleSon = middleSon;
    }

    public TwoThreeNode<T> getPomSon() {
        return pomSon;
    }

    public void setPomSon(TwoThreeNode<T> pomSon) {
        this.pomSon = pomSon;
    }

    public TwoThreeNode<T> getFather() {
        return father;
    }

    public void setFather(TwoThreeNode<T> father) {
        this.father = father;
    }

    public List<T> getKeys() {
        return keys;
    }

    public int getLengthKeys() {
        return lengthKeys;
    }
}
