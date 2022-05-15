package Objects;

import structures.TwoThreeTree;

import java.util.Date;

public class Person implements Comparable<Person> {
    private String name;
    private String surname;
    private Date birthday;
    private String RC;
    private TwoThreeTree<PCR> PCR;

    public Person(String RC_) {
        this.RC = RC_;
        this.PCR = new TwoThreeTree<PCR>();
    }

    public Person(String name_, String surname_, Date birthday_, String RC_) {
        this.name = name_;
        this.surname = surname_;
        this.birthday = birthday_;
        this.RC = RC_;
        this.PCR = new TwoThreeTree<>();
    }

    @Override
    public int compareTo(Person o) {
        return this.RC.compareTo(o.getRC());
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getRC() {
        return RC;
    }

    public TwoThreeTree<PCR> getPCR() {
        return PCR;
    }
}
