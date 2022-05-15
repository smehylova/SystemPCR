package structures;
import java.util.ArrayList;

public class TwoThreeTree<T extends Comparable<T>> {
    private TwoThreeNode<T> root;
    private int count;

    public TwoThreeTree() {
        this.count = 0;
    }

    public void setRoot(TwoThreeNode<T> root) {
        this.root = root;
        if (this.root == null) {
            this.count = 0;
        }
    }

    public TwoThreeNode<T> getRoot() { return root; }

    //vrati pocet prvkov v strome
    public int getCount() {
        return count;
    }

    //vrati hodnotu boolean, ci je node listom
    public boolean isLeaf(TwoThreeNode<T> leaf) { return leaf.getRightSon() == null && leaf.getLeftSon() == null; }

    //vrati hodnotu boolean, ci je node korenom stromu
    public boolean isRoot(TwoThreeNode<T> root) { return root.getFather() == null; }

    //zistuje ktory syn je aktualnz node svojho otca (lavy-1, stredny-0, pravy--1)
    public int getSonPosition(TwoThreeNode<T> actualNode) {
        if (!isRoot(actualNode)) {
            if (actualNode.getFather().getLeftSon() == actualNode) {
                return 1;//lavy syn
            } else if (actualNode.getFather().getMiddleSon() == actualNode) {
                return  0;//stredny syn
            } else if (actualNode.getFather().getRightSon() == actualNode) {
                return -1;//pravy syn
            }
        }
        return -2;
    }

    //vytvorené podľa prednášky a vlastných poznámok
    public void add(T key) {
        //kontrola, ci strom uz dany prvok neuchovava
        if (this.find(key) != null) {
            System.out.println("Strom uz dany prvok obsahuje");
            return;
        }
        count++;

        //najdenie listu stromu, do ktoreho ideme vkladat novy kluc
        TwoThreeNode<T> leaf = this.findLeaf(key);

        //ak je strom prazdny, prida prvok a koncime
        if (leaf == null) {
            this.root = new TwoThreeNode<T>();
            this.root.addKey(key);
            return;
        }

        //pridanie prvku
        leaf.addKey(key);

        while (true) {
            //zistime, ktory syn je u svojho otca
            int son = this.getSonPosition(leaf);

            //ak ma po pridani node 2 kluce tak koncime
            if (leaf.getLengthKeys() == 2) {
                return;
            }

            T middleK = leaf.getKeys().get(1);
            T maxK = leaf.getKeys().get(2);
            leaf.deleteKey(middleK);
            leaf.deleteKey(maxK);

            TwoThreeNode<T> maxL = new TwoThreeNode<T>();
            maxL.addKey(maxK);

            if (isLeaf(leaf)) {
                if (isRoot(leaf)) {
                    // NEMA SYNOV A NEMA OTCA
                    TwoThreeNode<T> newRoot = new TwoThreeNode<>();
                    newRoot.addKey(middleK);
                    this.root = newRoot;

                    this.root.setLeftSon(leaf);
                    this.root.setRightSon(maxL);
                    this.root.getLeftSon().setFather(this.root);
                    this.root.getRightSon().setFather(this.root);

                    return;
                } else {
                    // NEMA SYNOV A MA OTCA
                    leaf.getFather().addKey(middleK);

                    if (leaf.getFather().getLengthKeys() == 2) {//otec je 3-vrchol
                        if (son == 1) {
                            leaf.getFather().setMiddleSon(maxL);
                        } else {
                            leaf.getFather().setMiddleSon(leaf);
                            leaf.getFather().setRightSon(maxL);
                        }
                        leaf.getFather().getLeftSon().setFather(leaf.getFather());
                        leaf.getFather().getRightSon().setFather(leaf.getFather());
                        leaf.getFather().getMiddleSon().setFather(leaf.getFather());

                        return;
                    } else {//otec je 2-vrchol
                        leaf.getFather().setPomSon(maxL);
                        leaf.getFather().getPomSon().setFather(leaf.getFather());

                        leaf = leaf.getFather();
                    }
                }
            } else {
                if (isRoot(leaf)) {
                    // MA SYNOV A NEMA OTCA
                    TwoThreeNode<T> newRoot = new TwoThreeNode<>();
                    newRoot.addKey(middleK);
                    this.root = newRoot;

                    TwoThreeNode<T> pom = leaf.getPomSon();
                    if (pom.getKeys().get(0).compareTo(maxL.getKeys().get(0)) > 0) {
                        maxL.setRightSon(pom);
                        maxL.setLeftSon(leaf.getRightSon());
                        leaf.setRightSon(leaf.getMiddleSon());
                    } else {
                        maxL.setRightSon(leaf.getRightSon());
                        if (pom.getKeys().get(0).compareTo(leaf.getMiddleSon().getKeys().get(0)) > 0) {
                            maxL.setLeftSon(pom);
                            leaf.setRightSon(leaf.getMiddleSon());
                        } else {
                            maxL.setLeftSon(leaf.getMiddleSon());
                            if (pom.getKeys().get(0).compareTo(leaf.getKeys().get(0)) > 0) {
                                leaf.setRightSon(pom);
                            } else {
                                leaf.setRightSon(leaf.getLeftSon());
                                leaf.setLeftSon(pom);
                            }
                        }
                    }
                    leaf.setMiddleSon(null);
                    leaf.setPomSon(null);
                    this.root.setLeftSon(leaf);
                    this.root.setRightSon(maxL);
                    this.root.getLeftSon().setFather(this.root);
                    this.root.getRightSon().setFather(this.root);

                    this.root.getLeftSon().getLeftSon().setFather(this.root.getLeftSon());
                    this.root.getLeftSon().getRightSon().setFather(this.root.getLeftSon());
                    this.root.getRightSon().getLeftSon().setFather(this.root.getRightSon());
                    this.root.getRightSon().getRightSon().setFather(this.root.getRightSon());

                    return;
                } else {
                    // MA SYNOV A MA OTCA
                    leaf.getFather().addKey(middleK);

                    if (leaf.getFather().getLengthKeys() == 2) {//otec je 3-vrchol
                        TwoThreeNode<T> pom = leaf.getPomSon();
                        leaf.setPomSon(null);
                        if (pom.getKeys().get(0).compareTo(maxL.getKeys().get(0)) > 0) {
                            maxL.setRightSon(pom);
                            maxL.setLeftSon(leaf.getRightSon());
                            leaf.setRightSon(leaf.getMiddleSon());
                        } else {
                            maxL.setRightSon(leaf.getRightSon());
                            if (pom.getKeys().get(0).compareTo(leaf.getMiddleSon().getKeys().get(0)) > 0) {
                                maxL.setLeftSon(pom);
                                leaf.setRightSon(leaf.getMiddleSon());
                            } else {
                                maxL.setLeftSon(leaf.getMiddleSon());
                                if (pom.getKeys().get(0).compareTo(leaf.getKeys().get(0)) > 0) {
                                    leaf.setRightSon(pom);
                                } else {
                                    leaf.setRightSon(leaf.getLeftSon());
                                    leaf.setLeftSon(pom);
                                }
                            }
                        }
                        leaf.setMiddleSon(null);

                        if (son == 1) {
                            leaf.getFather().setMiddleSon(maxL);
                        } else {
                            leaf.getFather().setMiddleSon(leaf);
                            leaf.getFather().setRightSon(maxL);
                        }

                        leaf.getFather().getLeftSon().setFather(leaf.getFather());
                        leaf.getFather().getRightSon().setFather(leaf.getFather());
                        leaf.getFather().getMiddleSon().setFather(leaf.getFather());

                        leaf.getFather().getLeftSon().getLeftSon().setFather(leaf.getFather().getLeftSon());
                        leaf.getFather().getLeftSon().getRightSon().setFather(leaf.getFather().getLeftSon());
                        leaf.getFather().getRightSon().getLeftSon().setFather(leaf.getFather().getRightSon());
                        leaf.getFather().getRightSon().getRightSon().setFather(leaf.getFather().getRightSon());
                        leaf.getFather().getMiddleSon().getLeftSon().setFather(leaf.getFather().getMiddleSon());
                        leaf.getFather().getMiddleSon().getRightSon().setFather(leaf.getFather().getMiddleSon());

                        return;
                    } else {//otec je 2-vrchol
                        TwoThreeNode<T> pom = leaf.getPomSon();
                        leaf.setPomSon(null);
                        if (pom.getKeys().get(0).compareTo(maxL.getKeys().get(0)) > 0) {
                            maxL.setRightSon(pom);
                            maxL.setLeftSon(leaf.getRightSon());
                            leaf.setRightSon(leaf.getMiddleSon());
                        } else {
                            maxL.setRightSon(leaf.getRightSon());
                            if (pom.getKeys().get(0).compareTo(leaf.getMiddleSon().getKeys().get(0)) > 0) {
                                maxL.setLeftSon(pom);
                                leaf.setRightSon(leaf.getMiddleSon());
                            } else {
                                maxL.setLeftSon(leaf.getMiddleSon());
                                if (pom.getKeys().get(0).compareTo(leaf.getKeys().get(0)) > 0) {
                                    leaf.setRightSon(pom);
                                } else {
                                    leaf.setRightSon(leaf.getLeftSon());
                                    leaf.setLeftSon(pom);
                                }
                            }
                        }
                        leaf.setMiddleSon(null);

                        if (son == 1) {
                            leaf.getFather().setLeftSon(leaf);
                        } else if (son == 0) {
                            leaf.getFather().setMiddleSon(leaf);
                        } else {
                            leaf.getFather().setRightSon(leaf);
                        }
                        leaf.getFather().setPomSon(maxL);
                    }

                    leaf.getFather().getLeftSon().setFather(leaf.getFather());
                    leaf.getFather().getRightSon().setFather(leaf.getFather());
                    leaf.getFather().getPomSon().setFather(leaf.getFather());

                    leaf.getFather().getLeftSon().getLeftSon().setFather(leaf.getFather().getLeftSon());
                    leaf.getFather().getLeftSon().getRightSon().setFather(leaf.getFather().getLeftSon());
                    leaf.getFather().getRightSon().getLeftSon().setFather(leaf.getFather().getRightSon());
                    leaf.getFather().getRightSon().getRightSon().setFather(leaf.getFather().getRightSon());
                    leaf.getFather().getMiddleSon().getLeftSon().setFather(leaf.getFather().getMiddleSon());
                    leaf.getFather().getMiddleSon().getRightSon().setFather(leaf.getFather().getMiddleSon());
                    leaf.getFather().getPomSon().getLeftSon().setFather(leaf.getFather().getPomSon());
                    leaf.getFather().getPomSon().getRightSon().setFather(leaf.getFather().getPomSon());

                    leaf = leaf.getFather();
                }
            }
        }
    }

    //vytvorené podľa prednášky a vlastných poznámok
    public boolean delete(T key) {
        //najdeme node y ktoreho mazeme
        TwoThreeNode<T> actualNode = this.findNode(key);

        if (actualNode == null) {
            System.out.println("prvok s klucom " + key + " sa v strome nenachadzal");
            return false;
        }
        this.count--;

        //ak node nie je list tak vymazany prvok najdeme v inorder liste a presuvame sa na dany list
        if (!isLeaf(actualNode)) {
            TwoThreeNode<T> pomNode = findInOrder(actualNode, key);
            actualNode.deleteKey(key);
            actualNode.addKey(pomNode.getKeys().get(0));
            pomNode.deleteKey(pomNode.getKeys().get(0));
            actualNode = pomNode;
        } else {
            actualNode.deleteKey(key);
        }

        //zaciname od listu
        while (true) {
            if (actualNode.getLengthKeys() == 1) {//koncime ak ma node 1 kluc
                return true;
            }
            //sme v koreni stromu a prehadzujeme referenciu korena stromu na novy node
            if (isRoot(actualNode)) {
                if (actualNode.getLeftSon() != null) {
                    actualNode.getLeftSon().setFather(null);
                    this.root = actualNode.getLeftSon();
                } else if (actualNode.getRightSon() != null) {
                    actualNode.getRightSon().setFather(null);
                    this.root = actualNode.getRightSon();
                } else if (actualNode.getMiddleSon() != null) {
                    actualNode.getMiddleSon().setFather(null);
                    this.root = actualNode.getMiddleSon();
                } else {
                    this.root = null;
                }
                return true;
            }
            int son = this.getSonPosition(actualNode);

            if (actualNode.getFather().getLengthKeys() == 2) {//otec je 3-vrchol
                if (son == 1) {//lavy syn
                    if (actualNode.getFather().getMiddleSon().getLengthKeys() == 2) {
                        actualNode.addKey(actualNode.getFather().getKeys().get(0));
                        actualNode.getFather().deleteKey(actualNode.getFather().getKeys().get(0));
                        actualNode.getFather().addKey(actualNode.getFather().getMiddleSon().getKeys().get(0));
                        actualNode.getFather().getMiddleSon().deleteKey(actualNode.getFather().getMiddleSon().getKeys().get(0));

                        if (!isLeaf(actualNode)) {
                            if (actualNode.getRightSon() != null) {
                                actualNode.setLeftSon(actualNode.getRightSon());
                            }
                            actualNode.setRightSon(actualNode.getFather().getMiddleSon().getLeftSon());
                            actualNode.getFather().getMiddleSon().setLeftSon(actualNode.getFather().getMiddleSon().getMiddleSon());
                            actualNode.getFather().getMiddleSon().setMiddleSon(null);

                            actualNode.getRightSon().setFather(actualNode);
                        }
                    } else {
                        actualNode.addKey(actualNode.getFather().getKeys().get(0));
                        actualNode.getFather().deleteKey(actualNode.getFather().getKeys().get(0));
                        actualNode.addKey(actualNode.getFather().getMiddleSon().getKeys().get(0));
                        actualNode.getFather().getMiddleSon().deleteKey(actualNode.getFather().getMiddleSon().getKeys().get(0));

                        if (!isLeaf(actualNode)) {
                            if (actualNode.getRightSon() != null) {
                                actualNode.setLeftSon(actualNode.getRightSon());
                            }
                            actualNode.setMiddleSon(actualNode.getFather().getMiddleSon().getLeftSon());
                            actualNode.setRightSon(actualNode.getFather().getMiddleSon().getRightSon());

                            actualNode.getMiddleSon().setFather(actualNode);
                            actualNode.getRightSon().setFather(actualNode);
                        }
                        actualNode.getFather().setMiddleSon(null);
                    }
                } else if (son == 0) {//stredny syn
                    if (actualNode.getFather().getLeftSon().getLengthKeys() == 1) {
                        actualNode.getFather().getLeftSon().addKey(actualNode.getFather().getKeys().get(0));
                        actualNode.getFather().deleteKey(actualNode.getFather().getKeys().get(0));

                        if (!isLeaf(actualNode)) {
                            actualNode.getFather().getLeftSon().setMiddleSon(actualNode.getFather().getLeftSon().getRightSon());
                            if (actualNode.getLeftSon() != null) {
                                actualNode.getFather().getLeftSon().setRightSon(actualNode.getLeftSon());
                            } else {
                                actualNode.getFather().getLeftSon().setRightSon(actualNode.getRightSon());
                            }
                            actualNode.getFather().getLeftSon().getRightSon().setFather(actualNode.getFather().getLeftSon());
                        }
                        actualNode = actualNode.getFather();
                        actualNode.setMiddleSon(null);
                    } else if (actualNode.getFather().getRightSon().getLengthKeys() == 1) {
                        actualNode.getFather().getRightSon().addKey(actualNode.getFather().getKeys().get(1));
                        actualNode.getFather().deleteKey(actualNode.getFather().getKeys().get(1));

                        if (!isLeaf(actualNode)) {
                            actualNode.getFather().getRightSon().setMiddleSon(actualNode.getFather().getRightSon().getLeftSon());
                            if (actualNode.getLeftSon() != null) {
                                actualNode.getFather().getRightSon().setLeftSon(actualNode.getLeftSon());
                            } else {
                                actualNode.getFather().getRightSon().setLeftSon(actualNode.getRightSon());
                            }
                            actualNode.getFather().getRightSon().getLeftSon().setFather(actualNode.getFather().getRightSon());
                        }
                        actualNode = actualNode.getFather();
                        actualNode.setMiddleSon(null);
                    } else {
                        actualNode.addKey(actualNode.getFather().getKeys().get(1));
                        actualNode.getFather().deleteKey(actualNode.getFather().getKeys().get(1));
                        actualNode.getFather().addKey(actualNode.getFather().getRightSon().getKeys().get(0));
                        actualNode.getFather().getRightSon().deleteKey(actualNode.getFather().getRightSon().getKeys().get(0));

                        if (!isLeaf(actualNode)) {
                            if (actualNode.getRightSon() != null) {
                                actualNode.setLeftSon(actualNode.getRightSon());
                            }
                            actualNode.setRightSon(actualNode.getFather().getRightSon().getLeftSon());
                            actualNode.getFather().getRightSon().setLeftSon(actualNode.getFather().getRightSon().getMiddleSon());
                            actualNode.getFather().getRightSon().setMiddleSon(null);
                            actualNode.getRightSon().setFather(actualNode);
                        }
                    }
                } else if (son == -1) {//pravy syn
                    if (actualNode.getFather().getMiddleSon().getLengthKeys() == 2) {
                        actualNode.addKey(actualNode.getFather().getKeys().get(1));
                        actualNode.getFather().deleteKey(actualNode.getFather().getKeys().get(1));
                        actualNode.getFather().addKey(actualNode.getFather().getMiddleSon().getKeys().get(1));
                        actualNode.getFather().getMiddleSon().deleteKey(actualNode.getFather().getMiddleSon().getKeys().get(1));

                        if (!isLeaf(actualNode)) {
                            if (actualNode.getLeftSon() != null) {
                                actualNode.setRightSon(actualNode.getLeftSon());
                            }
                            actualNode.setLeftSon(actualNode.getFather().getMiddleSon().getRightSon());
                            actualNode.getFather().getMiddleSon().setRightSon(actualNode.getFather().getMiddleSon().getMiddleSon());
                            actualNode.getFather().getMiddleSon().setMiddleSon(null);

                            actualNode.getLeftSon().setFather(actualNode);
                        }
                    } else {
                        actualNode.addKey(actualNode.getFather().getKeys().get(1));
                        actualNode.getFather().deleteKey(actualNode.getFather().getKeys().get(1));
                        actualNode.addKey(actualNode.getFather().getMiddleSon().getKeys().get(0));
                        actualNode.getFather().getMiddleSon().deleteKey(actualNode.getFather().getMiddleSon().getKeys().get(0));

                        if (!isLeaf(actualNode)) {
                            if (actualNode.getLeftSon() != null) {
                                actualNode.setRightSon(actualNode.getLeftSon());
                            }
                            actualNode.setMiddleSon(actualNode.getFather().getMiddleSon().getRightSon());
                            actualNode.setLeftSon(actualNode.getFather().getMiddleSon().getLeftSon());

                            actualNode.getLeftSon().setFather(actualNode);
                            actualNode.getMiddleSon().setFather(actualNode);
                        }
                        actualNode.getFather().setMiddleSon(null);
                    }
                }
                return true;
            } else {//otec je 2-vrchol
                if (son == 1) {//lavy syn
                    if (actualNode.getFather().getRightSon().getLengthKeys() == 2) {//
                        actualNode.addKey(actualNode.getFather().getKeys().get(0));
                        actualNode.getFather().deleteKey(actualNode.getFather().getKeys().get(0));
                        actualNode.getFather().addKey(actualNode.getFather().getRightSon().getKeys().get(0));
                        actualNode.getFather().getRightSon().deleteKey(actualNode.getFather().getRightSon().getKeys().get(0));

                        if (!isLeaf(actualNode)) {
                            if (actualNode.getRightSon() != null) {
                                actualNode.setLeftSon(actualNode.getRightSon());
                            }
                            actualNode.setRightSon(actualNode.getFather().getRightSon().getLeftSon());
                            actualNode.getFather().getRightSon().setLeftSon(actualNode.getFather().getRightSon().getMiddleSon());
                            actualNode.getFather().getRightSon().setMiddleSon(null);

                            actualNode.getRightSon().setFather(actualNode);
                        }
                        return true;
                    } else {
                        actualNode.addKey(actualNode.getFather().getKeys().get(0));
                        actualNode.getFather().deleteKey(actualNode.getFather().getKeys().get(0));
                        actualNode.addKey(actualNode.getFather().getRightSon().getKeys().get(0));
                        actualNode.getFather().getRightSon().deleteKey(actualNode.getFather().getRightSon().getKeys().get(0));

                        if (!isLeaf(actualNode)) {
                            if (actualNode.getRightSon() != null) {
                                actualNode.setLeftSon(actualNode.getRightSon());
                            }
                            actualNode.setMiddleSon(actualNode.getFather().getRightSon().getLeftSon());
                            actualNode.setRightSon(actualNode.getFather().getRightSon().getRightSon());

                            actualNode.getRightSon().setFather(actualNode);
                            actualNode.getMiddleSon().setFather(actualNode);
                        }
                        actualNode = actualNode.getFather();
                        actualNode.setRightSon(null);
                    }
                } else {//pravy syn
                    if (actualNode.getFather().getLeftSon().getLengthKeys() == 2) {
                        actualNode.addKey(actualNode.getFather().getKeys().get(0));
                        actualNode.getFather().deleteKey(actualNode.getFather().getKeys().get(0));
                        actualNode.getFather().addKey(actualNode.getFather().getLeftSon().getKeys().get(1));
                        actualNode.getFather().getLeftSon().deleteKey(actualNode.getFather().getLeftSon().getKeys().get(1));

                        if (!isLeaf(actualNode)) {
                            if (actualNode.getLeftSon() != null) {
                                actualNode.setRightSon(actualNode.getLeftSon());
                            }
                            actualNode.setLeftSon(actualNode.getFather().getLeftSon().getRightSon());

                            actualNode.getFather().getLeftSon().setRightSon(actualNode.getFather().getLeftSon().getMiddleSon());
                            actualNode.getFather().getLeftSon().setMiddleSon(null);
                            actualNode.getLeftSon().setFather(actualNode);
                        }
                        return true;
                    } else {
                        actualNode.addKey(actualNode.getFather().getKeys().get(0));
                        actualNode.getFather().deleteKey(actualNode.getFather().getKeys().get(0));
                        actualNode.addKey(actualNode.getFather().getLeftSon().getKeys().get(0));
                        actualNode.getFather().getLeftSon().deleteKey(actualNode.getFather().getLeftSon().getKeys().get(0));

                        if (!isLeaf(actualNode)) {
                            if (actualNode.getLeftSon() != null) {
                                actualNode.setRightSon(actualNode.getLeftSon());
                            }
                            actualNode.setMiddleSon(actualNode.getFather().getLeftSon().getRightSon());
                            actualNode.setLeftSon(actualNode.getFather().getLeftSon().getLeftSon());

                            actualNode.getLeftSon().setFather(actualNode);
                            actualNode.getMiddleSon().setFather(actualNode);

                        }
                        actualNode = actualNode.getFather();
                        actualNode.setLeftSon(null);
                    }
                }
            }
        }
    }

    //pri metode delete hladam list, z ktoreho vyberiem kluc pre nahradenie
    public TwoThreeNode<T> findLeaf(T key) {
        if (this.root == null) {
            return null;
        }

        TwoThreeNode<T> actualNode = this.root;
        while (true) {
            if (this.isLeaf(actualNode)) {
                return actualNode;
            }

            if (actualNode.getLengthKeys() == 2) {
                if (key.compareTo(actualNode.getKeys().get(0)) <= 0) {
                    actualNode = actualNode.getLeftSon();
                } else if (key.compareTo(actualNode.getKeys().get(1)) > 0) {
                    actualNode = actualNode.getRightSon();
                } else {
                    actualNode = actualNode.getMiddleSon();
                }
            } else {
                if (key.compareTo(actualNode.getKeys().get(0)) <= 0) {
                    actualNode = actualNode.getLeftSon();
                } else {
                    actualNode = actualNode.getRightSon();
                }
            }
        }
    }

    //hladam node v ktorom sa nachadza zadany kluc
    public TwoThreeNode<T> findNode(T key) {
        if (this.root == null) {
            return null;
        }
        TwoThreeNode<T> actualNode = this.root;

        while (true) {
            if (actualNode.getLengthKeys() == 2) { //3-vrchol
                if (actualNode.getKeys().get(0).compareTo(key) == 0 || actualNode.getKeys().get(1).compareTo(key) == 0) {
                    return actualNode;
                } else if (key.compareTo(actualNode.getKeys().get(0)) < 0 && actualNode.getLeftSon() != null) {
                    actualNode = actualNode.getLeftSon();
                } else if (key.compareTo(actualNode.getKeys().get(1)) > 0 && actualNode.getRightSon() != null) {
                    actualNode = actualNode.getRightSon();
                } else if (key.compareTo(actualNode.getKeys().get(0)) > 0 && key.compareTo(actualNode.getKeys().get(1)) < 0 && actualNode.getMiddleSon() != null) {
                    actualNode = actualNode.getMiddleSon();
                } else {
                    return null;
                }

            } else { //2-vrchol
                if (key.compareTo(actualNode.getKeys().get(0)) == 0) {
                    return actualNode;
                } else if (key.compareTo(actualNode.getKeys().get(0)) < 0 && actualNode.getLeftSon() != null) {
                    actualNode = actualNode.getLeftSon();
                } else if (key.compareTo(actualNode.getKeys().get(0)) > 0 && actualNode.getRightSon() != null) {
                    actualNode = actualNode.getRightSon();
                } else {
                    return null;
                }
            }
        }
    }

    //vytvorené podľa prednášky a vlastných poznámok
    public T find(T key) {
        TwoThreeNode<T> pomNode = findNode(key);
        if (pomNode == null) {
            return null;
        }
        if (this.findNode(key).getKeys().get(0).compareTo(key) == 0) {
            return pomNode.getKeys().get(0);
        } else {
            return pomNode.getKeys().get(1);
        }
    }

    public TwoThreeTree<T> findInterval(T min_, T max_) {
        TwoThreeTree<T> tree = new TwoThreeTree<T>();
        //najdem najmensi kluc do intervalu
        TwoThreeDataNode<T> dataNode = this.getMinKey(min_);
        if (dataNode == null) {
            return null;
        }
        T actualKey = dataNode.getKey();
        if (actualKey == null || actualKey.compareTo(max_) > 0) {
            return null;
        }

        //prehladavam az kym aktualny kluc nie je null a zaroven nie je vacsi ako maximalny kluc
        do {
            tree.add(actualKey);
            dataNode = this.getInOrder(dataNode);
            actualKey = dataNode != null ? dataNode.getKey() : null;
        } while (actualKey != null && actualKey.compareTo(max_) <= 0);

        return tree;
    }

    //pomocna metoda pre findInterval, najde dataNode obsahujuci kluc, ktory je najblizsi k minimu intervalu
    public TwoThreeDataNode<T> getMinKey(T key) {
        TwoThreeNode<T> node = this.findLeaf(key);
        if (node == null) {
            return null;
        }
        if (node.getLengthKeys() == 1) {
            if (node.getKeys().get(0).compareTo(key) >= 0) {
                return new TwoThreeDataNode<T>(node.getKeys().get(0), node);
            } else {
                return this.getInOrder(new TwoThreeDataNode<T>(node.getKeys().get(0), node));
            }
        } else {
            if (node.getKeys().get(1).compareTo(key) >= 0 && node.getKeys().get(0).compareTo(key) < 0) {
                return new TwoThreeDataNode<T>(node.getKeys().get(1), node);
            } else if (node.getKeys().get(0).compareTo(key) >= 0) {
                return new TwoThreeDataNode<T>(node.getKeys().get(0), node);
            } else {
                return this.getInOrder(new TwoThreeDataNode<T>(node.getKeys().get(1), node));
            }
        }
    }

    //najde in order nasledovnika zadanemu dataNode
    public TwoThreeDataNode<T> getInOrder(TwoThreeDataNode<T> dataNode) {
        TwoThreeNode<T> searchedNode = dataNode.getNode();
        T key = dataNode.getKey();
        if (searchedNode.getLengthKeys() == 2 && searchedNode.getKeys().get(0).compareTo(key) == 0) {
            if (isLeaf(searchedNode)) {
                dataNode.setNode(searchedNode);
                dataNode.setKey(searchedNode.getKeys().get(1));
                return dataNode;
            }
        }
        if (searchedNode == this.getLastNode()) {
            return null;
        }

        if (isLeaf(searchedNode)) {
            if (searchedNode.getFather().getLengthKeys() == 2) {
                switch (this.getSonPosition(searchedNode)) {
                    case 1:
                        dataNode.setNode(searchedNode.getFather());
                        dataNode.setKey(searchedNode.getFather().getKeys().get(0));
                        return dataNode/*searchedNode.getFather().getKeys().get(0)*/;
                    case 0:
                        dataNode.setNode(searchedNode.getFather());
                        dataNode.setKey(searchedNode.getFather().getKeys().get(1));
                        return dataNode/*searchedNode.getFather().getKeys().get(1)*/;
                    case -1:
                        do {
                            searchedNode = searchedNode.getFather();
                        } while (this.getSonPosition(searchedNode) == -1);

                        if (this.getSonPosition(searchedNode) == 1) {
                            dataNode.setNode(searchedNode.getFather());
                            dataNode.setKey(searchedNode.getFather().getKeys().get(0));
                            return dataNode/*searchedNode.getFather().getKeys().get(0)*/;
                        } else {
                            dataNode.setNode(searchedNode.getFather());
                            dataNode.setKey(searchedNode.getFather().getKeys().get(1));
                            return dataNode/*searchedNode.getFather().getKeys().get(1)*/;
                        }
                }
            } else {
                if (this.getSonPosition(searchedNode) == 1) {
                    dataNode.setNode(searchedNode.getFather());
                    dataNode.setKey(searchedNode.getFather().getKeys().get(0));
                    return dataNode/*searchedNode.getFather().getKeys().get(0)*/;
                } else {
                    do {
                        searchedNode = searchedNode.getFather();
                    } while (this.getSonPosition(searchedNode) == -1);

                    if (this.getSonPosition(searchedNode) == 1) {
                        dataNode.setNode(searchedNode.getFather());
                        dataNode.setKey(searchedNode.getFather().getKeys().get(0));
                        return dataNode/*searchedNode.getFather().getKeys().get(0)*/;
                    } else {
                        dataNode.setNode(searchedNode.getFather());
                        dataNode.setKey(searchedNode.getFather().getKeys().get(1));
                        return dataNode/*searchedNode.getFather().getKeys().get(1)*/;
                    }
                }
            }
        } else {
            if (searchedNode.getLengthKeys() == 2) {
                if (searchedNode.getKeys().get(0).compareTo(key) == 0) {
                    searchedNode = searchedNode.getMiddleSon();
                    while (!isLeaf(searchedNode)) {
                        searchedNode = searchedNode.getLeftSon();
                    }
                    dataNode.setNode(searchedNode);
                    dataNode.setKey(searchedNode.getKeys().get(0));
                    return dataNode/*searchedNode.getKeys().get(0)*/;
                } else {
                    searchedNode = searchedNode.getRightSon();
                    while (!isLeaf(searchedNode)) {
                        searchedNode = searchedNode.getLeftSon();
                    }
                    dataNode.setNode(searchedNode);
                    dataNode.setKey(searchedNode.getKeys().get(0));
                    return dataNode/*searchedNode.getKeys().get(0)*/;
                }
            } else {
                searchedNode = searchedNode.getRightSon();
                while (!isLeaf(searchedNode)) {
                    searchedNode = searchedNode.getLeftSon();
                }
                dataNode.setNode(searchedNode);
                dataNode.setKey(searchedNode.getKeys().get(0));
                return dataNode/*searchedNode.getKeys().get(0)*/;
            }
        }
        dataNode.setNode(searchedNode);
        dataNode.setKey(searchedNode.getKeys().get(0));
        return dataNode/*searchedNode.getKeys().get(0)*/;
    }

    //pomocna metoda pri kontrole hlbky stromu, najde inorder v liste
    public TwoThreeNode<T> findInOrder(TwoThreeNode<T> node, T key) {
        boolean isLeftKey = node.getKeys().get(0) == key;
        TwoThreeNode<T> searchedNode = node;

        if (isLeaf(searchedNode)) {
            if (searchedNode.getFather().getLengthKeys() == 2) {
                switch (this.getSonPosition(searchedNode)) {
                    case 1:
                        return findInOrder(searchedNode.getFather(), searchedNode.getFather().getKeys().get(0));
                    case 0:
                        return findInOrder(searchedNode.getFather(), searchedNode.getFather().getKeys().get(1));
                    case -1:
                        do {
                            searchedNode = searchedNode.getFather();
                        } while (this.getSonPosition(searchedNode) == -1);

                        if (this.getSonPosition(searchedNode) == 1) {
                            return findInOrder(searchedNode.getFather(), searchedNode.getFather().getKeys().get(0));
                        } else {
                            return findInOrder(searchedNode.getFather(), searchedNode.getFather().getKeys().get(1));
                        }
                }
            } else {
                if (this.getSonPosition(searchedNode) == 1) {
                    return findInOrder(searchedNode.getFather(), searchedNode.getFather().getKeys().get(0));
                } else {
                    do {
                        searchedNode = searchedNode.getFather();
                    } while (this.getSonPosition(searchedNode) == -1);

                    if (this.getSonPosition(searchedNode) == 1) {
                        return findInOrder(searchedNode.getFather(), searchedNode.getFather().getKeys().get(0));
                    } else {
                        return findInOrder(searchedNode.getFather(), searchedNode.getFather().getKeys().get(1));
                    }
                }
            }
        } else {
            if (searchedNode.getLengthKeys() == 1 || !isLeftKey) {
                searchedNode = searchedNode.getRightSon();
            } else {
                searchedNode = searchedNode.getMiddleSon();
            }
            while (true) {
                if (searchedNode.getLeftSon() != null) {
                    searchedNode = searchedNode.getLeftSon();
                } else {
                    return searchedNode;
                }
            }
        }
        return null;
    }

    //prvy node v strome
    public TwoThreeNode<T> getFirstNode() {
        if (this.root == null) {
            return null;
        }
        TwoThreeNode<T> actualNode = this.root;
        while (true) {
            if (!isLeaf(actualNode)) {
                actualNode = actualNode.getLeftSon();
            } else {
                return actualNode;
            }
        }
    }

    //posledny node v strome
    public TwoThreeNode<T> getLastNode() {
        if (this.root == null) {
            return null;
        }
        TwoThreeNode<T> actualNode = this.root;
        while (true) {
            if (!isLeaf(actualNode)) {
                actualNode = actualNode.getRightSon();
            } else {
                return actualNode;
            }
        }
    }

    //pretranformuje strom na arraylist, ktory ulahci pracu pri vypise
    public ArrayList<T> getInOrderArrayL() {
        ArrayList<T> array = new ArrayList<>(this.count);
        int x = 0;

        TwoThreeNode<T> actualNode = this.getFirstNode();

        if (actualNode == null) {
            System.out.println("Strom je prazdy.");
            return null;
        }

        TwoThreeDataNode<T> dataNode = new TwoThreeDataNode<>(actualNode.getKeys().get(0), actualNode);
        T actualKey = actualNode.getKeys().get(0);
        if (actualKey == null) {
            return null;
        }

        do {
            array.add(actualKey);
            dataNode = this.getInOrder(dataNode);
            actualKey = dataNode != null ? dataNode.getKey() : null;
        } while (actualKey != null);

        return array;
    }

    //kontrola hlbky stromu
    public boolean controlDepth() {
        TwoThreeNode<T> actualNode = getFirstNode();
        TwoThreeNode<T> pomNode = null;
        TwoThreeNode<T> lastNode = getLastNode();
        int count = 0;
        if (isLeaf(actualNode)) {
            count = 0;
            pomNode = actualNode;
            while (true) {
                if (isRoot(pomNode)) {
                    break;
                } else {
                    count++;
                    pomNode = pomNode.getFather();
                }
            }
        }
        while (actualNode != lastNode) {
            actualNode = findInOrder(actualNode, actualNode.getKeys().get(0));
            pomNode = actualNode;
            if (isLeaf(pomNode)) {
                int count2 = 0;
                while (true) {
                    if (isRoot(pomNode)) {
                        break;
                    } else {
                        count2++;
                        pomNode = pomNode.getFather();
                    }
                }
                if (count != count2) {
                    return false;
                }
            }
        }
        return true;
    }
}
