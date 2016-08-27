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
    private int sequenceLengthInPairs;
    private int amountOfPlayers;

//    private LinkedList<Pair> bestCandidate = new LinkedList<>();
    Map<Player, List<Player>> pairsPlayed = new HashMap<>();
    Map<Integer, HashSet<Player>> playersAmountOfPairs = new HashMap<>();
    int minimumMatches = 0;


    private int counter = 0;

    public int getCounter() {
        return counter;
    }

    public LinkedList<Pair> getBestCandidate() {
        return currentTournament;
    }

    public SelectivePair(int amountOfPlayers, int sequenceLengthInPairs) {
        this.amountOfPlayers = amountOfPlayers;
        this.sequenceLengthInPairs = sequenceLengthInPairs;
        initTournament();
        generateTournamentBrute();
    }

    public void generateTournamentBrute() {

        if (sequenceLengthInPairs * 2 > amountOfPlayers) {
            return;
        }

        boolean foundSequence = true;
        List<Player> playersPlayedInSequence = new LinkedList<>();
        outerwhile:
        while (foundSequence) {
            foundSequence = false;

            //need to create legal sequences.
            if (playersPlayedInSequence.size() == sequenceLengthInPairs * 2) {
                playersPlayedInSequence = new LinkedList<>();
            }

            sequenceloop:
            for (int k = 0; k < sequenceLengthInPairs; k++) { //Could end faster if there is many "Very nice candidates"

                Pair bestCandidate = null;
                int bestCandidateDistance = Integer.MAX_VALUE;
                boolean noVeryNiceCandidates = true;
                for (Pair pairCandidate : new LinkedList<>(allPossiblePairs)) {

                    //This could happen because of "Very nice candidates"
                    if (allPossiblePairs.isEmpty()) {
                        //No more pairs left at the end is a legal state.
                        break outerwhile;
                    } else if (playersPlayedInSequence.size() == sequenceLengthInPairs  * 2) {
                        break sequenceloop; //Created valid sequence
                    }

                    //Lets see if this is possible, else it could just be in the same game
                    //Could be tested with sequence 2
                    if (!playersPlayedInSequence.contains(pairCandidate.getPlayerOne()) &&
                            !playersPlayedInSequence.contains(pairCandidate.getPlayerTwo())) {

                        //Very nice candidate
                        //See if we can just take this greedy.
                        if (playersAmountOfPairs.get(minimumMatches).contains(pairCandidate.getPlayerOne()) &&
                                playersAmountOfPairs.get(minimumMatches).contains(pairCandidate.getPlayerTwo())) {
                            addPair(playersPlayedInSequence, pairCandidate);
                            noVeryNiceCandidates = false;
                        } else {
                            //Find best possible pair for this spot in the sequence
                            pairsPlayed.putIfAbsent(pairCandidate.getPlayerOne(), new LinkedList<>());
                            pairsPlayed.putIfAbsent(pairCandidate.getPlayerTwo(), new LinkedList<>());
                            int playerOnePairs = pairsPlayed.get(pairCandidate.getPlayerOne()).size();
                            int playerTwoPairs = pairsPlayed.get(pairCandidate.getPlayerTwo()).size();

                            if (bestCandidate != null) {
                                if (playerTwoPairs + playerOnePairs < bestCandidateDistance) {
                                    bestCandidate = pairCandidate;
                                    bestCandidateDistance = playerTwoPairs + playerOnePairs;
                                }
                            } else {
                                bestCandidate = pairCandidate;
                                bestCandidateDistance = playerTwoPairs + playerOnePairs;
                            }
                        }
                    }
                }

                if (noVeryNiceCandidates
                        && playersPlayedInSequence.size() != sequenceLengthInPairs * 2
                        && bestCandidate != null) {
                    addPair(playersPlayedInSequence, bestCandidate);
                    Assert.assertFalse(!playersPlayedInSequence.contains(bestCandidate.getPlayerOne()) &&
                            !playersPlayedInSequence.contains(bestCandidate.getPlayerTwo()));
                } else if (allPossiblePairs.isEmpty()) {
                    //No more pairs left at the end is a legal state.
                    break outerwhile;
                } else if (playersPlayedInSequence.size() == sequenceLengthInPairs * 2) {
                    //Found legal sequence
                    break sequenceloop;
                } else {
//                    Assert.assertFalse("We were not able to find a candidate :(", noVeryNiceCandidates);
                }
            }

            if (playersPlayedInSequence.size() == sequenceLengthInPairs * 2) {
                foundSequence = true;
            }
        }

        //Add one dummy game if there is an uneven amount of pairs.
        if (currentTournament.size() % 2 == 1) {
            currentTournament.add(new Pair(new Player("null"), new Player("null")));
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
        for (Integer integer : playersAmountOfPairs.keySet()) {
            movePlayerOneUp(pairCandidate.getPlayerOne(), integer);
            movePlayerOneUp(pairCandidate.getPlayerTwo(), integer);
        }
    }

    public void movePlayerOneUp(Player player, Integer integer) {
        if (playersAmountOfPairs.get(integer).contains(player)) {
            playersAmountOfPairs.get(integer).remove(player);
            playersAmountOfPairs.putIfAbsent(integer + 1, new HashSet<>());
            playersAmountOfPairs.get(integer + 1).add(player);
        }
    }

    public void initTournament() {
        ArrayList<Player> players = TournamentGeneratorHelper.generatePersons(amountOfPlayers);
        TableSoccerTournament tableSoccerTournament = new TableSoccerTournament(players);
        this.originalPairs = tableSoccerTournament.generateAllLegalPairs();
        this.allPossiblePairs = new LinkedList<>(originalPairs);

        playersAmountOfPairs.putIfAbsent(0, new HashSet());
        for (Player player : players) {
            playersAmountOfPairs.get(0).add(player);
        }
    }


}
