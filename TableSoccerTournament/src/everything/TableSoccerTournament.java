package everything;

import everything.Models.Pair;
import everything.Models.Player;

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
    int numberOfCycles = 0;

    public TableSoccerTournament(ArrayList<Player> players) {
        DoubleRing doubleRing = new DoubleRing(players);
        this.rings = new LinkedList<>();
        rings.add(doubleRing);
    }


    public Queue<Pair> generateAllLegalPairs() {
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
        if (ringsHaveMorePairs) {
            ringsHaveMorePairs = false;

            int minSizeOfRings = Integer.MAX_VALUE;
            for (DoubleRing ring : rings) {
                int candidate = ring.minSize() + ring.maxSize();
                if (minSizeOfRings > candidate) {
                    minSizeOfRings = candidate;
                }
            }

            LinkedList<Queue<Pair>> cyclesFromUneven = new LinkedList<>();
            LinkedList<Queue<Pair>> secondCyclesFromUneven = new LinkedList<>();
            LinkedList<Queue<Pair>> cyclesFromEven = new LinkedList<>();
            for (DoubleRing ring : rings) {
                int candidate = ring.minSize() + ring.maxSize();
                if (minSizeOfRings < candidate && rings.size() > 1) {
                    cyclesFromUneven.add(ring.getNextCycle());

                    if (ring.getIndex() <= 1) {
                        secondCyclesFromUneven.add(ring.getNextCycle());
                    }
                } else {
                    cyclesFromEven.add(ring.getNextCycle());
                }

                if (!ringsHaveMorePairs) {
                    ringsHaveMorePairs = ring.haveAnyPairsLeft();
                }
            }

            if (cyclesFromUneven.isEmpty()) {
                boolean anyPairsLeft = true;
                while (anyPairsLeft) {
                    anyPairsLeft = false;
                    for (Queue<Pair> allNewCycle : cyclesFromEven) {
                        if (allNewCycle.size() > 0) {
                            this.nextPairRow.add(allNewCycle.poll());
                            anyPairsLeft = true;
                        }
                    }
                }
            } else if (cyclesFromEven.size() < 2) {
                for (Queue<Pair> allNewCycle : secondCyclesFromUneven) {
                    while (!allNewCycle.isEmpty()) {
                        this.nextPairRow.add(allNewCycle.poll());
                    }
                }

                for (Queue<Pair> allNewCycle : cyclesFromEven) {
                    while (!allNewCycle.isEmpty()) {
                        this.nextPairRow.add(allNewCycle.poll());
                    }
                }
                for (Queue<Pair> allNewCycle : cyclesFromUneven) {
                    while (!allNewCycle.isEmpty()) {
                        this.nextPairRow.add(allNewCycle.poll());
                    }
                }
            } else {
                double sizeOfEvenRoundDown = Math.floor(cyclesFromUneven.get(0).size() / 2.0);
                double sizeOfEvenRoundup = Math.ceil(cyclesFromUneven.get(0).size() / 2.0);
                Queue<Pair> mergeEvenPairs = new LinkedList<>();
                Queue<Pair> mergeUnevenPairs = new LinkedList<>();
                Queue<Pair> mergeSecondUnevenPairs = new LinkedList<>();

                //Merge all pairs from even cycles.
                boolean anyPairsLeft = true;
                while (anyPairsLeft) {
                    anyPairsLeft = false;
                    for (Queue<Pair> allNewCycle : cyclesFromEven) {
                        if (allNewCycle.size() > 0) {
                            mergeEvenPairs.add(allNewCycle.poll());
                            anyPairsLeft = true;
                        }
                    }
                }

                //Merge all pairs from uneven cycles.
                anyPairsLeft = true;
                while (anyPairsLeft) {
                    anyPairsLeft = false;
                    for (Queue<Pair> allNewCycle : cyclesFromUneven) {
                        if (allNewCycle.size() > 0) {
                            mergeUnevenPairs.add(allNewCycle.poll());
                            anyPairsLeft = true;
                        }
                    }
                }

                //Merge all pairs from seconduneven cycles.
                anyPairsLeft = true;
                while (anyPairsLeft) {
                    anyPairsLeft = false;
                    for (Queue<Pair> allNewCycle : secondCyclesFromUneven) {
                        if (allNewCycle.size() > 0) {
                            mergeSecondUnevenPairs.add(allNewCycle.poll());
                            anyPairsLeft = true;
                        }
                    }
                }

                if (numberOfCycles == 0) {
                    for (int i = 0; i < sizeOfEvenRoundDown; i++) {
                        this.nextPairRow.add(mergeEvenPairs.poll());
                    }

                    while (!mergeUnevenPairs.isEmpty()) {
                        this.nextPairRow.add(mergeUnevenPairs.poll());
                    }

                    while (!mergeEvenPairs.isEmpty()) {
                        this.nextPairRow.add(mergeEvenPairs.poll());
                    }

                    while (!mergeSecondUnevenPairs.isEmpty()) {
                        this.nextPairRow.add(mergeSecondUnevenPairs.poll());
                    }
                } else {
                    for (int i = 0; i < (numberOfCycles % 2 == 1 ? sizeOfEvenRoundup : sizeOfEvenRoundDown); i++) {
                        this.nextPairRow.add(mergeEvenPairs.poll());
                    }

                    while (!mergeUnevenPairs.isEmpty()) {
                        this.nextPairRow.add(mergeUnevenPairs.poll());
                    }

                    while (!mergeEvenPairs.isEmpty()) {
                        this.nextPairRow.add(mergeEvenPairs.poll());
                    }
                }
                numberOfCycles++;
            }
        }

        return nextPairRow;
    }

    public boolean isRingsHaveMorePairs() {
        return ringsHaveMorePairs;
    }

    public void generateNewRings() {
        List<DoubleRing> newRings = new LinkedList<>();

        if (!ringsHaveMorePairs) {

            for (DoubleRing ring : rings) {
                newRings.addAll(ring.split());
            }


            if (newRings.size() > 2) {
                ArrayList<Integer> indexesOfUneven = new ArrayList<>();

                for (int i = 0; i < newRings.size(); i++) {
                    DoubleRing ring = newRings.get(i);
                    if (ring.maxSize() != ring.minSize()) {
                        indexesOfUneven.add(i);
                    }
                }

                for (int i = 0; i < indexesOfUneven.size(); i++) {
                    Integer indexOfUneven = indexesOfUneven.get(i);

                    if (i + 1 < indexesOfUneven.size()) {
                        Collections.swap(newRings, indexOfUneven, indexesOfUneven.get(i + 1) - 1);
                    } else {
                        Collections.swap(newRings, indexOfUneven, newRings.size() - 2);
                    }
                }
            }

            rings = newRings;
            ringsHaveMorePairs = newRings.size() > 0;
            numberOfCycles = 0;
        }
    }

    public List<DoubleRing> getRings() {
        return rings;
    }
}
