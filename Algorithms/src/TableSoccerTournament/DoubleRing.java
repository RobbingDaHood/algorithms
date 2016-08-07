package TableSoccerTournament;

import TableSoccerTournament.Models.Game;
import TableSoccerTournament.Models.Pair;
import TableSoccerTournament.Models.Person;
import org.junit.Assert;

import java.util.*;

/**
 * Created by super on 07/08/2016.
 */
public class DoubleRing {
    private ArrayList<Person> ringOne;
    private ArrayList<Person> ringTwo;
    private int index;

    public boolean haveAnyPairsLeft() {
        return index < maxSize();
    }

    public DoubleRing(ArrayList<Person> players) {
        this.ringTwo = new ArrayList<>(players.subList(0, players.size() / 2));
        this.ringOne = new ArrayList<>(players.subList(players.size() / 2, players.size()));
        this.index = 0;

        Assert.assertTrue(ringOne.size() >= ringTwo.size());
    }

    public Queue<Pair> getNextPairRow() {
        Queue<Pair> pairs = new LinkedList<>();

        Assert.assertTrue(index < ringOne.size());
        for (int i = 0; i < ringOne.size(); i++) { //Last player in the big ring should not be paired
            pairs.add(new Pair(ringOne.get((i + index) % ringOne.size()), ringTwo.get(i % ringTwo.size())));
        }
        index++;

        return pairs;
    }

    public boolean anyPlayersLeft() {
        return index < ringOne.size() || index < ringTwo.size();
    }

    public int maxSize() {
        return Math.max(ringOne.size(), ringTwo.size());
    }

    public List<DoubleRing> split() {
        LinkedList<DoubleRing> doubleRings = new LinkedList<>();
        doubleRings.add(new DoubleRing(ringOne));
        doubleRings.add(new DoubleRing(ringTwo));
        return doubleRings;
    }


    //None of the people in any on the rings have been paired with each other yet
    public Game createDummyGame(ArrayList<Person> players) {
        Game game = new Game();

        List<Person> playersLeft = new LinkedList<>();
        for (int i = index; i < ringOne.size(); i++) {
            playersLeft.add(ringOne.get(index));
        }
        for (int i = index; i < ringTwo.size(); i++) {
            playersLeft.add(ringTwo.get(index));
        }

        //There is betweeen 1 and 3 real players, else it would not be a dummy game
        List<Person> nonDummyPlayers = new LinkedList<>(playersLeft);
        if (playersLeft.size() == 1) {
            Person dummyPlayer = getDummyPlayer(players, nonDummyPlayers);
            nonDummyPlayers.add(dummyPlayer);
            game.addDummyPlayer(dummyPlayer);
            Person dummyPlayer1 = getDummyPlayer(players, nonDummyPlayers);
            nonDummyPlayers.add(dummyPlayer1);
            game.addDummyPlayer(dummyPlayer1);
            Person dummyPlayer2 = getDummyPlayer(players, nonDummyPlayers);
            game.addDummyPlayer(dummyPlayer2);

            game.setTeamOne(new Pair(playersLeft.get(0), dummyPlayer));
            game.setTeamTwo(new Pair(dummyPlayer1, dummyPlayer2));
        } else if (playersLeft.size() == 2) {
            Person dummyPlayer = getDummyPlayer(players, nonDummyPlayers);
            nonDummyPlayers.add(dummyPlayer);
            game.addDummyPlayer(dummyPlayer);
            Person dummyPlayer1 = getDummyPlayer(players, nonDummyPlayers);
            game.addDummyPlayer(dummyPlayer1);

            game.setTeamOne(new Pair(playersLeft.get(0), playersLeft.get(1)));
            game.setTeamTwo(new Pair(dummyPlayer, dummyPlayer1));
        } else {
            Person dummyPlayer = getDummyPlayer(players, nonDummyPlayers);
            game.addDummyPlayer(dummyPlayer);

            game.setTeamOne(new Pair(playersLeft.get(0), playersLeft.get(1)));
            game.setTeamTwo(new Pair(dummyPlayer, playersLeft.get(2)));
        }

        return game;
    }

    private Person getDummyPlayer(ArrayList<Person> players, List<Person> noneOfThesePlayers) {
        ArrayList<Person> oneOfThesePlayers = new ArrayList<>(players);
        oneOfThesePlayers.removeAll(noneOfThesePlayers);
        int randomIndex = new Random().nextInt(oneOfThesePlayers.size());
        return oneOfThesePlayers.get(randomIndex);
    }

    public Pair createDummyPair(ArrayList<Person> players) {
        LinkedList<Person> noneOfThesePlayers = new LinkedList<>();
        noneOfThesePlayers.add(ringOne.get(0));
        return new Pair(ringOne.get(0), getDummyPlayer(players, noneOfThesePlayers));
    }

    public int minSize() {
        return Math.min(ringOne.size(), ringTwo.size());
    }

    public int getIndex() {
        return index;
    }
}
