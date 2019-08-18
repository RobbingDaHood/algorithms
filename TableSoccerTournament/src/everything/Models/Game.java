package everything.Models;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by super on 07/08/2016.
 */
public class Game {
    private Pair teamOne;
    private Pair teamTwo;
    private List<Player> dummyPlayers;

    public Game() {
        this.dummyPlayers = new LinkedList<>();
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

    public List<Player> getDummyPlayers() {
        return dummyPlayers;
    }

    public void setDummyPlayers(List<Player> dummyPlayers) {
        this.dummyPlayers = dummyPlayers;
    }

    public void addDummyPlayer(Player player) {
        dummyPlayers.add(player);
    }

    public String toString() {
        return "[" + teamOne + ";" + teamTwo + "]";
    }
}
