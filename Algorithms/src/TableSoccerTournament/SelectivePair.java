package TableSoccerTournament;

import TableSoccerTournament.Models.Pair;
import TableSoccerTournament.Models.Player;
import org.junit.Assert;

import java.util.*;

/**
 * Created by super on 18/08/2016.
 */
public class SelectivePair {

    private Queue<Pair> allPossiblePairs = null;
    private Queue<Pair> originalPairs = null; //Testing
    private LinkedList<Pair> currentTournament = new LinkedList<>();
    private int sequenceLength;
    private int amountOfPlayers;

    private LinkedList<Pair> bestCandidate = new LinkedList<>();
    Map<Player, List<Player>> pairsPlayed = new HashMap<>();
    Map<Integer, List<Player>> playersAmountOfPairs = new HashMap<>();
    int minimumMatches = 0;


    private int counter = 0;

    public int getCounter() {
        return counter;
    }

    public LinkedList<Pair> getBestCandidate() {
        return bestCandidate;
    }

    public SelectivePair(int amountOfPlayers, int sequenceLength) {
        this.amountOfPlayers = amountOfPlayers;
        this.sequenceLength = sequenceLength;
        initTournament();
        generateTournamentBrute();
    }

    public void generateTournamentBrute() {

        boolean foundSequence = true;
        while (foundSequence) {
            foundSequence = false;

            //need to create legal sequences.
            List<Player> playersPlayedInSequence = new LinkedList<>();

            if (allPossiblePairs.size() < sequenceLength) {
                if (allPossiblePairs.size() > 1) {
                    for (Pair pairCandidate : new LinkedList<>(allPossiblePairs)) {
                        if (!playersPlayedInSequence.contains(pairCandidate.getPlayerOne()) &&
                                !playersPlayedInSequence.contains(pairCandidate.getPlayerTwo())) {
                            addPair(playersPlayedInSequence, pairCandidate);
                        }
                    }
                }

                Assert.assertFalse(allPossiblePairs.size() == 1);

                currentTournament.add(allPossiblePairs.poll());
                currentTournament.add(new Pair(new Player("null"), new Player("null")));

                return;
            }

            sequenceloop:
            for (int k = 0; k < sequenceLength; k++) {

                Pair bestCandidate = null;
                int bestCandidateDistance = -1;
                for (Pair pairCandidate : new LinkedList<>(allPossiblePairs)) {

                    if (playersPlayedInSequence.size() / 2 == sequenceLength) {
                        break sequenceloop;
                    }

                    //Lets see if this is possible, else it could just be in the same game
                    //Could be tested with sequence 1
                    if (!playersPlayedInSequence.contains(pairCandidate.getPlayerOne()) &&
                            !playersPlayedInSequence.contains(pairCandidate.getPlayerTwo())) {

                        //Very nice candidate
                        //See if we can just take this greedy.
                        if (playersAmountOfPairs.get(minimumMatches).contains(pairCandidate.getPlayerOne()) &&
                                playersAmountOfPairs.get(minimumMatches).contains(pairCandidate.getPlayerTwo())) {
                            addPair(playersPlayedInSequence, pairCandidate);
                        } else {
                            //Find best possible pair for this spot in the sequence
                            pairsPlayed.putIfAbsent(pairCandidate.getPlayerOne(), new LinkedList<>());
                            pairsPlayed.putIfAbsent(pairCandidate.getPlayerTwo(), new LinkedList<>());
                            int playerOnePairs = pairsPlayed.get(pairCandidate.getPlayerOne()).size();
                            int playerTwoPairs = pairsPlayed.get(pairCandidate.getPlayerTwo()).size();

                            int maxDistance = minimumMatches - playerOnePairs;
                            if (maxDistance < minimumMatches - playerTwoPairs) {
                                maxDistance = minimumMatches - playerTwoPairs;
                            }

                            if (bestCandidate != null) {
                                if (maxDistance < bestCandidateDistance) {
                                    bestCandidate = pairCandidate;
                                    bestCandidateDistance = maxDistance;
                                }
                            } else {
                                bestCandidate = pairCandidate;
                                bestCandidateDistance = maxDistance;
                            }
                        }
                    }
                }

                if (bestCandidate != null &&
                        !playersPlayedInSequence.contains(bestCandidate.getPlayerOne()) &&
                        !playersPlayedInSequence.contains(bestCandidate.getPlayerTwo())) {
                    addPair(playersPlayedInSequence, bestCandidate);
                } else {
                    Assert.assertTrue("We were not able to find a candidate :(", false);
                }
            }

            if (playersPlayedInSequence.size() / 2 == sequenceLength) {
                foundSequence = true;
            }
        }


    }

    public void addPair(List<Player> playersPlayedInSequence, Pair pairCandidate) {
        currentTournament.add(pairCandidate);
        playersPlayedInSequence.add(pairCandidate.getPlayerOne());
        playersPlayedInSequence.add(pairCandidate.getPlayerTwo());
        TournamentGeneratorHelper.addTeam(pairsPlayed, pairCandidate.getPlayerOne(), pairCandidate.getPlayerTwo());
        allPossiblePairs.remove(pairCandidate);
        movePlayersOneUp(pairCandidate);

        if (playersAmountOfPairs.get(minimumMatches).isEmpty()) {
            minimumMatches++;
        }
    }

    public void movePlayersOneUp(Pair pairCandidate) {
        playersAmountOfPairs.get(minimumMatches).remove(pairCandidate.getPlayerOne());
        playersAmountOfPairs.get(minimumMatches).remove(pairCandidate.getPlayerTwo());
        playersAmountOfPairs.putIfAbsent(minimumMatches + 1, new LinkedList<>());
        playersAmountOfPairs.get(minimumMatches + 1).add(pairCandidate.getPlayerOne());
        playersAmountOfPairs.get(minimumMatches + 1).add(pairCandidate.getPlayerTwo());
    }

    public void initTournament() {
        ArrayList<Player> players = TournamentGeneratorHelper.generatePersons(amountOfPlayers);
        TableSoccerTournament tableSoccerTournament = new TableSoccerTournament(players);
        this.originalPairs = tableSoccerTournament.generateAllLegalPairs();
        this.allPossiblePairs = new LinkedList<>(originalPairs);

        playersAmountOfPairs.putIfAbsent(0, new LinkedList());
        for (Player player : players) {
            playersAmountOfPairs.get(0).add(player);
        }
    }


}
