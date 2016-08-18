package TableSoccerTournament;

import TableSoccerTournament.Models.Pair;
import TableSoccerTournament.Models.Player;

import java.util.*;

/**
 * Created by super on 17/08/2016.
 */
public class BruteForcer {

    private Queue<Pair> allPossiblePairs = null;
    private LinkedList<Pair> currentTournament = new LinkedList<>();
    private int sequenceLength;

    private LinkedList<Pair> bestCandidate = new LinkedList<>();


    private int counter = 0;

    public int getCounter() {
        return counter;
    }

    public LinkedList<Pair> getBestCandidate() {
        return bestCandidate;
    }

    public BruteForcer(Queue<Pair> allPossiblePairs, int sequenceLength) {
        this.allPossiblePairs = allPossiblePairs;
        this.sequenceLength = sequenceLength;
        generateTournamentBrute();
    }

    public void generateTournamentBrute() {
        LinkedList<Pair> bestLocalCandidate = new LinkedList<>();
        for (int i = 0; i < allPossiblePairs.size(); i++) {
            counter++;

//            if (counter % 100 == 0) {
//                System.out.println("Got to: " + counter);
//            }

            Pair poll = allPossiblePairs.poll();
            currentTournament.add(poll);

            boolean makesSenseToContinue = compareAndReplaceCurrentBestStrict();
            if (bestCandidate.size() >= allPossiblePairs.size() + currentTournament.size()) {
                return;
            }

            if (makesSenseToContinue && !allPossiblePairs.isEmpty()) {
                generateTournamentBrute();
            }

            currentTournament.remove(poll);
            allPossiblePairs.add(poll);
        }
    }

    //STRICT no player is twice in any sequence
    //No max distance check
    private boolean compareAndReplaceCurrentBestStrict() {
        Map<Player, List<Player>> pairsPlayed = new HashMap<>();

        double maxGamesPlayedDistance = -Double.MAX_VALUE;
        int k = 0;
        for (int i = 0; i < currentTournament.size(); i += k) {
            List<Player> plaersPlayedInSequence = new LinkedList<>();
            boolean legalSequence = true;
            for (k = 0; k < sequenceLength && i + k < currentTournament.size(); k++) {
                Pair pair = currentTournament.get(i + k);

                if (plaersPlayedInSequence.contains(pair.getPlayerOne()) ||
                        plaersPlayedInSequence.contains(pair.getPlayerTwo())) {
                    legalSequence = false;
                    break;
                }

                plaersPlayedInSequence.add(pair.getPlayerOne());
                plaersPlayedInSequence.add(pair.getPlayerTwo());
                TournamentGeneratorHelper.addTeam(pairsPlayed, pair.getPlayerOne(), pair.getPlayerTwo());
            }

            if (!legalSequence && bestCandidate.size() < i) {
                return false;
            }
        }

        double maxAmountOfPlays = -Double.MAX_VALUE;
        double minAmountOfPlays = Double.MAX_VALUE;

        for (Player player : pairsPlayed.keySet()) {
            if (pairsPlayed.get(player) != null && !player.equals(new Player("null"))) {
                if (maxAmountOfPlays < pairsPlayed.get(player).size()) {
                    maxAmountOfPlays = pairsPlayed.get(player).size();
                }
                if (minAmountOfPlays > pairsPlayed.get(player).size()) {
                    minAmountOfPlays = pairsPlayed.get(player).size();
                }
            }
        }

        if (maxAmountOfPlays == minAmountOfPlays) {
            bestCandidate = currentTournament;
        }
        return true;
    }
}
