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
    private boolean ringsHaveMorePairs = true;
    private Queue<Pair> nextPairRow = new LinkedList<>();

    public TableSoccerTournament(ArrayList<Person> players) {
        DoubleRing doubleRing = new DoubleRing(players);
        this.rings = new LinkedList<>();
        rings.add(doubleRing);
    }


    public Queue<Pair> generateTournament() {
        while (ringsHaveMorePairs) {
            generateNextRow();
        }

        return nextPairRow;
    }

    public Queue<Pair> generateNextRow() {
        while (ringsHaveMorePairs) { //TODO could probably be replaced with a for loop on the max length of the biggest innter ring.
            generateNextCycle();
        }

        generateNewRings();

        return nextPairRow;
    }

    public Queue<Pair> generateNextCycle() {
        ringsHaveMorePairs = false;
        for (DoubleRing ring : rings) {
            this.nextPairRow.addAll(ring.getNextCycle());

            if (!ringsHaveMorePairs) {
                ringsHaveMorePairs = ring.haveAnyPairsLeft();
            }
        }

        return nextPairRow;
    }

    public boolean isRingsHaveMorePairs() {
        return ringsHaveMorePairs;
    }


    public List<Game> generateGameList(Queue<Pair> tournamentOfPairs) {
        List<Game> gameList = new LinkedList<>();
        while (tournamentOfPairs.size() > 1) {
            gameList.add(new Game(tournamentOfPairs.poll(), tournamentOfPairs.poll()));
        }
        return gameList;
    }

    public void generateNewRings() {
        List<DoubleRing> newRings = new LinkedList<>();

        if (!ringsHaveMorePairs) {

            for (DoubleRing ring : rings) {
                newRings.addAll(ring.split());
            }

            rings = newRings;
            ringsHaveMorePairs = newRings.size() > 0;
        }
    }
}
