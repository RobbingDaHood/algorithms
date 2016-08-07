package TableSoccerTournament;

import TableSoccerTournament.Models.Game;
import TableSoccerTournament.Models.Pair;
import TableSoccerTournament.Models.Person;

import java.util.*;

/**
 * Alle skal spille lige mange kampe
 * Ingen må have spillet med hinanden 2 gange
 * <p>
 * Created by super on 07/08/2016.
 */
public class TableSoccerTournament {
    private List<DoubleRing> rings;
    private List<Game> gameList;
    private Iterator<Game> gameIterator;
    private boolean anyPlayersLeft;
    private ArrayList<Person> players;
    private boolean ringsHaveMorePairs = true;
    private Queue<Pair> nextPairRow = new LinkedList<>();

    public TableSoccerTournament(ArrayList<Person> players) {
        DoubleRing doubleRing = new DoubleRing(players);
        this.rings = new LinkedList<>();
        rings.add(doubleRing);
        Iterator<Game> gameIterator = new LinkedList<Game>().iterator();
        this.players = players;
    }

    public boolean isRingsHaveMorePairs() {
        return ringsHaveMorePairs;
    }

    public void generateNextGameList(boolean lastGameList) {
        gameList = new LinkedList<>();
        List<DoubleRing> newRings = new LinkedList<>();

        ringsHaveMorePairs = false;
        for (DoubleRing ring : rings) {

//            if (ring.maxSize() > 1) {
            if (ring.minSize() > 0 && ring.haveAnyPairsLeft()) { //TODO remove ring.haveAnyPairsLeft()
                Queue<Pair> nextPairRow1 = ring.getNextPairRow();
                this.nextPairRow.addAll(nextPairRow1);
            }

            if (!ringsHaveMorePairs) {
                ringsHaveMorePairs = ring.haveAnyPairsLeft();
            }
//            } else {
//                nextPairRow.add(ring.createDummyPair(players));
//            }

//            //TODO remove nextPairRow.size() == 0
//            if ((lastGameList || nextPairRow.size() == 0) && ring.anyPlayersLeft()) {
//                gameList.add(ring.createDummyGame(players));
//            } else {
//                newRings.addAll(ring.split());
//            }
        }

        if (!ringsHaveMorePairs) {
            for (DoubleRing ring : rings) {
                newRings.addAll(ring.split());
            }

            rings = newRings;
            ringsHaveMorePairs = true;
        }

        while (nextPairRow.size() > 1) {
            gameList.add(new Game(nextPairRow.poll(), nextPairRow.poll()));
        }

    }

    public List<Game> getGameList() {
        return gameList;
    }
}
