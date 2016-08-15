package TableSoccerTournament;

import TableSoccerTournament.Models.Pair;
import TableSoccerTournament.Models.Player;
import org.junit.Assert;

import java.util.*;

/**
 * Created by super on 07/08/2016.
 */
public class DoubleRing {
    private ArrayList<Player> ringOne;
    private ArrayList<Player> ringTwo;
    private int index;

    public boolean haveAnyPairsLeft() {
        return index < maxSize();
    }

    //Ring one is always the bigger ring.
    public DoubleRing(ArrayList<Player> players) {
        this.ringTwo = new ArrayList<>(players.subList(0, players.size() / 2));
        this.ringOne = new ArrayList<>(players.subList(players.size() / 2, players.size()));
        this.index = 0;

        Assert.assertTrue(ringOne.size() >= ringTwo.size());
    }

    public Queue<Pair> getNextCycle() {
        Queue<Pair> pairs = new LinkedList<>();

        if (index < ringOne.size()) { //Else we already did one full Cycle
            for (int i = 0; i < ringTwo.size(); i++) { //Last player in the big ring should not be paired
                pairs.add(new Pair(ringOne.get((i + index) % ringOne.size()), ringTwo.get(i)));
            }
            index++;
        }

        return pairs;
    }

    public List<DoubleRing> split() {
        LinkedList<DoubleRing> doubleRings = new LinkedList<>();

        //The biggest ring will produce one more pair row if it is uneven.
        //If the biggest ring is the first ring, then there is a risk that the players
        //In the ring will play two games very close to each other.

        if (ringOne.size() > ringTwo.size() && ringOne.size() > 2) { //Then ring one last pair came from index = n - 2
            ArrayList<Player> firstPart = new ArrayList<>(ringOne.subList(0, ringOne.size() / 2));
            ArrayList<Player> lastPart = new ArrayList<>(ringOne.subList(ringOne.size() / 2, ringOne.size() - 2));
            ArrayList<Player> middlePart = new ArrayList<>(ringOne.subList(ringOne.size() - 2, ringOne.size()));
            firstPart.addAll(middlePart);
            firstPart.addAll(lastPart);

            doubleRings.add(new DoubleRing(firstPart));
        } else if (ringOne.size() > 2) {
            ArrayList<Player> firstPart = new ArrayList<>(ringOne.subList(0, ringOne.size() / 2));
            ArrayList<Player> lastPart = new ArrayList<>(ringOne.subList(ringOne.size() / 2, ringOne.size() - 1));
            ArrayList<Player> middlePart = new ArrayList<>(ringOne.subList(ringOne.size() - 1, ringOne.size()));
            firstPart.addAll(middlePart);
            firstPart.addAll(lastPart);

            doubleRings.add(new DoubleRing(firstPart));
        } else
            if (ringOne.size() > 1) //Else no pairs can be produced
            doubleRings.add(new DoubleRing(ringOne));

        if (ringTwo.size() > 1) //Else no pairs can be produced
            doubleRings.add(new DoubleRing(ringTwo));

        return doubleRings;


//        if (ringTwo.size() > 1) //Else no pairs can be produced
//            doubleRings.add(new DoubleRing(ringTwo));
//
//        ArrayList<Player> firstPart = new ArrayList<>(ringOne.subList(0, ringOne.size() / 2));
//        ArrayList<Player> lastPart = new ArrayList<>(ringOne.subList(ringOne.size() / 2, ringOne.size() - 2));
//        ArrayList<Player> middlePart = new ArrayList<>(ringOne.subList(ringOne.size() - 2, ringOne.size()));
//        firstPart.addAll(middlePart);
//        firstPart.addAll(lastPart);
//
//        doubleRings.add(new DoubleRing(firstPart));
//    } else {
//        if (ringOne.size() > 1) //Else no pairs can be produced
//            doubleRings.add(new DoubleRing(ringOne));
//
//        if (ringTwo.size() > 1) //Else no pairs can be produced
//            doubleRings.add(new DoubleRing(ringTwo));
//    }
//
//        return doubleRings;
    }

    public int maxSize() {
        return Math.max(ringOne.size(), ringTwo.size());
    }

    public int minSize() {
        return Math.min(ringOne.size(), ringTwo.size());
    }

    public String toString() {
        return ringOne.toString() + ringTwo.toString();
    }

    public int getIndex() {
        return index;
    }
}
