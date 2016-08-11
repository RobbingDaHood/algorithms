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
        for (int i = 0; i < ringTwo.size(); i++) { //Last player in the big ring should not be paired
            pairs.add(new Pair(ringOne.get((i + index) % ringOne.size()), ringTwo.get(i)));
        }
        index++;

        return pairs;
    }

    public List<DoubleRing> split() {
        LinkedList<DoubleRing> doubleRings = new LinkedList<>();
        doubleRings.add(new DoubleRing(ringOne));
        doubleRings.add(new DoubleRing(ringTwo));
        return doubleRings;
    }

    public int maxSize() {
        return Math.max(ringOne.size(), ringTwo.size());
    }

    public int minSize() {
        return Math.min(ringOne.size(), ringTwo.size());
    }
}
