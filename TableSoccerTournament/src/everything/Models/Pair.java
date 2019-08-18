package everything.Models;

/**
 * Created by super on 07/08/2016.
 */
public class Pair {

    public enum Type {
        NORMAL, DUMMY, NOTAGAME;
    }

    private Player playerOne;
    private Player playerTwo;
    private Type type;

    public Pair() {
    }

    public Pair(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.type = Type.NORMAL;
    }

    public Pair(Player playerOne, Player playerTwo, Type type) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.type = type;
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

    public boolean isDummy() {
        return type.equals(Type.DUMMY);
    }

    public boolean isNotAGame() {
        return type.equals(Type.NOTAGAME);
    }
}
