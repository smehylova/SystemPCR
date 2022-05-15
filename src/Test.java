import structures.TwoThreeTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Test {
    private final Scanner scanner = new Scanner(System.in);

    public Test() {
        System.out.println("Vitajte v aplikacii. Zadajte ktory test chcete vykonat.");
        System.out.println("1: Test podla pradepodobnosti vykonania prikazu pridaj zmaz a hladaj. Zadava sa aj pocet vykonavania operacii.");
        System.out.println("2: pridanie zadaneho poctu prvkov prvkov, najdenie vsetkych prvkov a ich vymazanie. No konci zostane pocet zadanych prvkov.");
        System.out.println("3: kontrola hladania nahodnych intervalov.");
        System.out.println("4: kontrola konecnej hlbky stromu pri vlozeni 1000000 prvkov.");
        int druhTestu = scanner.nextInt();

        while (druhTestu != 1 && druhTestu != 2 && druhTestu != 3 && druhTestu != 4) {
            System.out.print("Zadaj znova: ");
            druhTestu = scanner.nextInt();
        }
        if (druhTestu == 1) {
            this.firstTest();
        } else if (druhTestu == 2) {
            this.secondTest();
        } else if (druhTestu == 3) {
            this.testInterval();
        } else {
            this.testDepth();
        }
    }

    public void firstTest() {
        TwoThreeTree<Integer> tree = new TwoThreeTree<Integer>();
        ArrayList<Integer> polePrvkov = new ArrayList<Integer>();
        ArrayList<Integer> poleVlozenychPrvkov = new ArrayList<Integer>();
        for (int i = 0; i < 1000000; i++) {
            polePrvkov.add(i);
        }
        Collections.shuffle(polePrvkov);
        int pVloz = 0, pMaz = 0, pHladaj = 0;

        while (pVloz+pMaz+pHladaj != 100) {
            System.out.print("Zadaj pravdepodobnost pre vkladanie: ");
            pVloz = scanner.nextInt();
            System.out.print("Zadaj pravdepodobnost pre mazanie: ");
            pMaz = scanner.nextInt();
            System.out.print("Zadaj pravdepodobnost pre hladanie: ");
            pHladaj = scanner.nextInt();

            if ((pVloz+pMaz+pHladaj) != 100) {
                System.out.println("Suma nedava hodnotu 100. Zadaj znova!" + (pVloz+pMaz+pHladaj));
            }
        }
        System.out.print("Zadaj pocet pokusov: ");
        int pocetPokusov = scanner.nextInt();
        System.out.println("Proces zacina.");
        int pocetV = 0, pocetM = 0, pocetHTrue = 0, pocetHFalse = 0, pocetMNull = 0;

        for (int i = 0; i < pocetPokusov; i++) {
            double metoda = Math.random() * 100;
            if (metoda < pVloz) {
                //System.out.println("Vklada sa prvok: " + polePrvkov.get(0));
                tree.add(polePrvkov.get(0));
                poleVlozenychPrvkov.add(polePrvkov.get(0));
                polePrvkov.remove(0);
                Collections.shuffle(poleVlozenychPrvkov);
                pocetV++;
            } else if (metoda < (pVloz + pMaz)) {
                //System.out.println(poleVlozenychPrvkov);
                if (poleVlozenychPrvkov.size() != 0) {
                    tree.delete(poleVlozenychPrvkov.get(0));
                    poleVlozenychPrvkov.remove(0);
                    pocetM++;
                    pocetMNull--;
                }
                pocetMNull++;
            } else {
                if (poleVlozenychPrvkov.size() != 0) {
                    int randomNum = (int)Math.random()*(poleVlozenychPrvkov.size());
                    if (tree.find(poleVlozenychPrvkov.get(randomNum)) != null) {
                        pocetHTrue++;
                    } else {
                        pocetHFalse++;
                    }
                }
            }
        }
        System.out.println(tree.getInOrderArrayL());
        System.out.println("Pocet vlozeni: " + pocetV);
        System.out.println("Pocet mazani: " + pocetM);
        System.out.println("Pocet mazani pri prazdnom strome: " + pocetMNull);
        System.out.println("Pocet najdeni(true): " + pocetHTrue);
        System.out.println("Pocet najdeni(false): " + pocetHFalse);
        System.out.println("Pocet prvkov v konecnom strome: " + (pocetV - pocetM));
        System.out.println("Pocet prvkov: " + tree.getCount());
    }

    public void secondTest() {
        TwoThreeTree<Integer> tree = new TwoThreeTree<Integer>();
        ArrayList<Integer> pole = new ArrayList<Integer>();
        int pocetVlozeni = 0;
        System.out.println("Zadaj pocet: ");
        int pocetOperacii = scanner.nextInt();
        System.out.println("Zadaj pocet prvkov, ktory ostane na konci testovania.");
        int pocetPrvkovNaKonci = scanner.nextInt();
        for (int i = 0; i < pocetOperacii; i++) {
            pole.add(i);
        }
        Collections.shuffle(pole);
        //pole = {8, 7, 6, 4, 1, 3, 9, 2, 5, 0};
        //System.out.println("\nShuffled List : \n" + pole);
        for (int i = 0; i < pole.size(); i++) {
            tree.add(pole.get(i));
            pocetVlozeni++;
        }

        System.out.println("Vkladalo sa " + pole.size() + " prvkov a vlozilo sa " + pocetVlozeni + " prvkov.");
        System.out.println();


        int pocetNajdeni = 0;
        for (int i = 0; i < pole.size(); i++) {
            if (tree.find(pole.get(i)) != null) {
                pocetNajdeni++;
            }
        }
        System.out.println("Naslo sa " + pocetNajdeni + " prvkov z " + pocetVlozeni);
        System.out.println();

        Collections.shuffle(pole);
        int pocetMazani = 0;
        for (int i = 0; i < pole.size() - pocetPrvkovNaKonci; i++) {
            if (!tree.delete(pole.get(i))) {
                System.out.println("Cislo " + i + "sa nepodarilo vymazat!");
            } else {
                //System.out.println("Cislo " + i);
                pocetMazani++;
            }
        }
        System.out.println("Vymazalo sa " + pocetMazani + " prvkov.");
        System.out.println(tree.getInOrderArrayL());
        System.out.println("Pocet prvkov: " + tree.getCount());
    }

    public void testInterval() {
        TwoThreeTree<Integer> tree = new TwoThreeTree<Integer>();
        ArrayList<Integer> pole = new ArrayList<Integer>();

        for (int i = 0; i < 10; i++) {
            pole.add(i);
        }
        Collections.shuffle(pole);
        for (int i = 0; i < pole.size(); i++) {
            tree.add(pole.get(i));
        }
        System.out.println(pole);

        for (int i = 0; i < 1000
                ; i++) {
            double rand = Math.random()*(pole.size());
            int first = (int) rand;
            rand = Math.random()*pole.size()
            ;
            int second = (int) rand;
            System.out.println(first);
            System.out.println(second);
            TwoThreeTree<Integer> treeA= new TwoThreeTree<Integer>();
            if (first < second) {
                treeA = tree.findInterval(first, second);
            } else {
                treeA = tree.findInterval(second, first);
            }
            System.out.println(treeA.getInOrderArrayL());
        }
    }

    public void testDepth() {
        TwoThreeTree<Integer> tree = new TwoThreeTree<Integer>();
        ArrayList<Integer> pole = new ArrayList<Integer>();

        for (int i = 0; i < 1000000; i++) {
            pole.add(i);
        }
        Collections.shuffle(pole);
        for (int i = 0; i < pole.size(); i++) {
            tree.add(pole.get(i));
        }

        System.out.println(tree.controlDepth());
    }
}
