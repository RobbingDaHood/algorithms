package TableSoccerTournament.Models;

/**
 * Created by super on 07/08/2016.
 */
public class Pair {
    private Person personOne;
    private Person personTwo;

    public Pair() {
    }

    public Pair(Person personOne, Person personTwo) {
        this.personOne = personOne;
        this.personTwo = personTwo;
    }

    public Person getPersonOne() {
        return personOne;
    }

    public void setPersonOne(Person personOne) {
        this.personOne = personOne;
    }

    public Person getPersonTwo() {
        return personTwo;
    }

    public void setPersonTwo(Person personTwo) {
        this.personTwo = personTwo;
    }

    public String toString() {
        return personOne + ":" + personTwo;
    }
}
