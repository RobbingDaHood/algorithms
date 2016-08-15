package TableSoccerTournament.Models;

/**
 * Created by super on 07/08/2016.
 */
public class Pair {
    private Player playerOne;
    private Player playerTwo;

    public Pair() {
    }

    public Pair(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

    public String toString() {
        return playerOne + ":" + playerTwo;
    }
}
