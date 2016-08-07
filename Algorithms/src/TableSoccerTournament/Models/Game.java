package TableSoccerTournament.Models;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by super on 07/08/2016.
 */
public class Game {
    private Pair teamOne;
    private Pair teamTwo;
    private List<Person> dummyPersons;

    public Game() {
        this.dummyPersons = new LinkedList<>();
    }

    public Game(Pair teamOne, Pair teamTwo) {
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
    }

    public Pair getTeamOne() {
        return teamOne;
    }

    public void setTeamOne(Pair teamOne) {
        this.teamOne = teamOne;
    }

    public Pair getTeamTwo() {
        return teamTwo;
    }

    public void setTeamTwo(Pair teamTwo) {
        this.teamTwo = teamTwo;
    }

    public List<Person> getDummyPersons() {
        return dummyPersons;
    }

    public void setDummyPersons(List<Person> dummyPersons) {
        this.dummyPersons = dummyPersons;
    }

    public void addDummyPlayer(Person person) {
        dummyPersons.add(person);
    }

    public String toString() {
        return "[" + teamOne + ";" + teamTwo + "]";
    }
}
