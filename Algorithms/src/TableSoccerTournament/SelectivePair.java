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
    private int maxAmountOfBattlesPrPlayer;

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
        this.maxAmountOfBattlesPrPlayer = Integer.MAX_VALUE;
        initTournament();
        generateTournamentBrute();
    }

    public SelectivePair(int amountOfPlayers, int sequenceLengthInPairs, int maxAmountOfBattlesPrPlayer) {
        this.amountOfPlayers = amountOfPlayers;
        this.sequenceLengthInPairs = sequenceLengthInPairs;
        this.maxAmountOfBattlesPrPlayer = maxAmountOfBattlesPrPlayer;
        initTournament();
        generateTournamentBrute();
    }

    public void generateTournamentBrute() {

        if (sequenceLengthInPairs * 2 > amountOfPlayers) {
            return;
        }

        boolean thereWereCandidatesWithMaxAmountOfPlays = false;
        boolean foundSequence = true;
        List<Player> playersPlayedInSequence = new LinkedList<>();
        outerwhile:
        while (foundSequence) {
            foundSequence = false;

            if (playersPlayedInSequence.size() == sequenceLengthInPairs * 2) {
                playersPlayedInSequence = new LinkedList<>();
            }

            sequenceloop:
            for (int k = 0; k < sequenceLengthInPairs; k++) { //Could end faster if there is many "Very nice candidates"

                Pair bestCandidate = null;
                int bestCandidateDistance = Integer.MAX_VALUE;
                boolean thereWereVeryNiceCandidates = false;
                for (Pair pairCandidate : new LinkedList<>(allPossiblePairs)) {

                    //This could happen because of "Very nice candidates"
                    if (allPossiblePairs.isEmpty()) {
                        //No more pairs left at the end is a legal state.
                        break outerwhile;
                    } else if (playersPlayedInSequence.size() == sequenceLengthInPairs * 2) {
                        break sequenceloop; //Created valid sequence
                    }

                    if (playersAmountOfPairs.get(maxAmountOfBattlesPrPlayer).contains(pairCandidate.getPlayerOne()) ||
                            playersAmountOfPairs.get(maxAmountOfBattlesPrPlayer).contains(pairCandidate.getPlayerTwo())) {
                        thereWereCandidatesWithMaxAmountOfPlays = true;
                        continue;
                    }

                    //Not in sequence and not played max amount og plays
                    if (!playersPlayedInSequence.contains(pairCandidate.getPlayerOne()) &&
                            !playersPlayedInSequence.contains(pairCandidate.getPlayerTwo())) {

                        //Very nice candidate
                        //See if we can just take this greedy.
                        if (playersAmountOfPairs.get(minimumMatches).contains(pairCandidate.getPlayerOne()) &&
                                playersAmountOfPairs.get(minimumMatches).contains(pairCandidate.getPlayerTwo())) {
                            addPair(playersPlayedInSequence, pairCandidate);
                            thereWereVeryNiceCandidates = true;
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

                if (allPossiblePairs.isEmpty()) {
                    //No more pairs left at the end is a legal state.
                    break outerwhile;
                } else if (playersPlayedInSequence.size() == sequenceLengthInPairs * 2) {
                    //Found legal sequence
                    break sequenceloop;
                } else if (!thereWereVeryNiceCandidates && bestCandidate != null &&
                        !playersAmountOfPairs.get(maxAmountOfBattlesPrPlayer).contains(bestCandidate.getPlayerOne()) &&
                        !playersAmountOfPairs.get(maxAmountOfBattlesPrPlayer).contains(bestCandidate.getPlayerTwo())) {
                    addPair(playersPlayedInSequence, bestCandidate);
                    Assert.assertFalse(!playersPlayedInSequence.contains(bestCandidate.getPlayerOne()) &&
                            !playersPlayedInSequence.contains(bestCandidate.getPlayerTwo()));
                } else if (playersPlayedInSequence.size() > 0 && !thereWereVeryNiceCandidates) {
                    if (currentTournament.size() % 2 == 1) {
                        currentTournament.add(new Pair(new Player("null"), new Player("null"), Pair.Type.DUMMY));
                    }

                    //We rather want one session with fewer tables, than stopping early.
                    while (currentTournament.size() % sequenceLengthInPairs != 0) {
                        currentTournament.add(new Pair(new Player("NOT A GAME"), new Player("NOT A GAME"), Pair.Type.NOTAGAME));
                    }
                    playersPlayedInSequence = new LinkedList<>();
                    foundSequence = true;
                    continue outerwhile;
                }
            }

            if (playersPlayedInSequence.size() > 0 && currentTournament.size() % sequenceLengthInPairs == 0) {
                foundSequence = true;
            }
        }

        finishTheTournament(playersPlayedInSequence, thereWereCandidatesWithMaxAmountOfPlays);
    }

    public void finishTheTournament(List<Player> playersPlayedInSequence, boolean thereWereCandidatesWithMaxAmountOfPlays) {

        if (thereWereCandidatesWithMaxAmountOfPlays) {
            addLegalPairsFinishTheTournament(playersPlayedInSequence);


            LinkedList<Player> playersMissingOneBattle = getPlayersMissingABattle();

            while (playersMissingOneBattle.size() > 0) {
                if (playersMissingOneBattle.size() == 1) {
                    if (!playersPlayedInSequence.contains(playersMissingOneBattle.get(0))) {
                        Pair dummyPair = new Pair(playersMissingOneBattle.get(0), new Player("null"), Pair.Type.DUMMY);
                        addPair(playersPlayedInSequence, dummyPair);
                    }
                } else if (playersMissingOneBattle.size() == 2) {
                    if (!playersPlayedInSequence.contains(playersMissingOneBattle.get(0)) &&
                            !playersPlayedInSequence.contains(playersMissingOneBattle.get(1))) {
                        //This way I am sure not to create a illegal pair.
                        Pair dummyPair = new Pair(playersMissingOneBattle.get(0), new Player("null"), Pair.Type.DUMMY);
                        addPair(playersPlayedInSequence, dummyPair);
                        dummyPair = new Pair(playersMissingOneBattle.get(1), new Player("null"), Pair.Type.DUMMY);
                        addPair(playersPlayedInSequence, dummyPair);
                    }
                } else if (playersMissingOneBattle.size() == 3) {
                    if (!playersPlayedInSequence.contains(playersMissingOneBattle.get(0)) &&
                            !playersPlayedInSequence.contains(playersMissingOneBattle.get(1)) &&
                            !playersPlayedInSequence.contains(playersMissingOneBattle.get(2))) {
                        //I Accept that one of these pairs are illegal
                        Pair dummyPair = new Pair(playersMissingOneBattle.get(0), playersMissingOneBattle.get(2), Pair.Type.DUMMY);
                        addPair(playersPlayedInSequence, dummyPair);
                        dummyPair = new Pair(playersMissingOneBattle.get(1), new Player("null"), Pair.Type.DUMMY);
                        addPair(playersPlayedInSequence, dummyPair);
                    }
                } else if (playersMissingOneBattle.size() >= 4) {
                    if (!playersPlayedInSequence.contains(playersMissingOneBattle.get(0)) &&
                            !playersPlayedInSequence.contains(playersMissingOneBattle.get(1))) {
                        //I Accept that one of these pairs are illegal
                        Player playerOne = playersMissingOneBattle.get(0);
                        Player playerTwo = playersMissingOneBattle.get(1);
                        Pair dummyPair = new Pair(playerOne, playerTwo, Pair.Type.DUMMY);
                        addPair(playersPlayedInSequence, dummyPair);

                        if (playersPlayedInSequence.size() == sequenceLengthInPairs * 2) {
                            playersPlayedInSequence = new LinkedList<>();
                        }
                    }

                    if (!playersPlayedInSequence.contains(playersMissingOneBattle.get(2)) &&
                            !playersPlayedInSequence.contains(playersMissingOneBattle.get(3))) {
                        //I Accept that one of these pairs are illegal
                        Player playerOne = playersMissingOneBattle.get(2);
                        Player playerTwo = playersMissingOneBattle.get(3);
                        Pair dummyPair = new Pair(playerOne, playerTwo, Pair.Type.DUMMY);
                        addPair(playersPlayedInSequence, dummyPair);

                        if (playersPlayedInSequence.size() == sequenceLengthInPairs * 2) {
                            playersPlayedInSequence = new LinkedList<>();
                        }
                    }
                }

                playersMissingOneBattle = getPlayersMissingABattle();
            }
        }

        //Add one dummy game if there is an uneven amount of pairs.
        if (currentTournament.size() % 2 == 1) {
            currentTournament.add(new Pair(new Player("null"), new Player("null"), Pair.Type.DUMMY));
        }
    }

    public void addLegalPairsFinishTheTournament(List<Player> playersPlayedInSequence) {
        LinkedList<Player> playersMissingOneBattle = getPlayersMissingABattle();
        if (playersMissingOneBattle.size() >= 2) {
            boolean atLeastCreatedOneSequence = true;
            while (atLeastCreatedOneSequence) {
                atLeastCreatedOneSequence = false;

                Map<Player, List<Pair>> possiblePairs = new HashMap<>();
                for (Player player : playersMissingOneBattle) {
                    for (Pair allPossiblePair : new LinkedList<>(allPossiblePairs)) {
                        if (allPossiblePair.getPlayerOne().equals(player) || allPossiblePair.getPlayerTwo().equals(player)) {
                            for (Player player2 : playersMissingOneBattle) {
                                if (!player2.equals(player)) {
                                    if (allPossiblePair.getPlayerOne().equals(player2) || allPossiblePair.getPlayerTwo().equals(player2)) {
                                        possiblePairs.putIfAbsent(player, new LinkedList<>());
                                        possiblePairs.get(player).add(allPossiblePair);

                                        possiblePairs.putIfAbsent(player2, new LinkedList<>());
                                        possiblePairs.get(player2).add(allPossiblePair);

                                        allPossiblePairs.remove(allPossiblePair);
                                    }
                                }
                            }
                        }
                    }
                }

                //Add legal games to this
                for (Player possiblePlayer : possiblePairs.keySet()) {
                    for (Pair possiblePair : possiblePairs.get(possiblePlayer)) {
                        if (!playersPlayedInSequence.contains(possiblePair.getPlayerOne()) &&
                                !playersPlayedInSequence.contains(possiblePair.getPlayerTwo())) {
                            currentTournament.add(possiblePair);
                            playersPlayedInSequence.add(possiblePair.getPlayerOne());
                            playersPlayedInSequence.add(possiblePair.getPlayerTwo());
                            movePlayersOneUp(possiblePair);
                            TournamentGeneratorHelper.addTeam(pairsPlayed, possiblePair.getPlayerOne(), possiblePair.getPlayerTwo());

                            if (playersAmountOfPairs.get(minimumMatches).isEmpty()) {
                                playersAmountOfPairs.remove(minimumMatches);
                                minimumMatches++;
                            }

                            possiblePairs.get(possiblePair.getPlayerTwo()).remove(possiblePair);

                            if (playersPlayedInSequence.size() == sequenceLengthInPairs * 2) {
                                playersPlayedInSequence = new LinkedList<>();
                                atLeastCreatedOneSequence = true;
                            }

                            Assert.assertFalse("I wonder if I ever needs this", true);
                        }
                    }
                }
            }
        }
    }

    public LinkedList<Player> getPlayersMissingABattle() {
        //Players missing more than one battle is mentioned more times.
        LinkedList<Player> playersMissingOneBattle = new LinkedList<>();
        for (int i = 0; i + minimumMatches < maxAmountOfBattlesPrPlayer; i++) {
            for (int times = 0; times < i + 1; times++) {
                if (playersAmountOfPairs.get(i + minimumMatches) != null) {
                    playersMissingOneBattle.addAll(playersAmountOfPairs.get(i + minimumMatches));
                }
            }
        }
        return playersMissingOneBattle;
    }

    public void addPair(List<Player> playersPlayedInSequence, Pair pairCandidate) {
        currentTournament.add(pairCandidate);
        playersPlayedInSequence.add(pairCandidate.getPlayerOne());
        playersPlayedInSequence.add(pairCandidate.getPlayerTwo());
        allPossiblePairs.remove(pairCandidate);
        movePlayersOneUp(pairCandidate);
        TournamentGeneratorHelper.addTeam(pairsPlayed, pairCandidate.getPlayerOne(), pairCandidate.getPlayerTwo());

        if (playersAmountOfPairs.get(minimumMatches).isEmpty()) {
            playersAmountOfPairs.remove(minimumMatches);
            minimumMatches++;
        }
    }

    public void movePlayersOneUp(Pair pairCandidate) {
        if (!pairCandidate.getPlayerOne().equals(new Player("null"))) {
            List<Player> pairsPlayedOne = pairsPlayed.get(pairCandidate.getPlayerOne());
            movePlayerOneUp(pairCandidate.getPlayerOne(), pairsPlayedOne != null ? pairsPlayedOne.size() : 0);
        }

        if (!pairCandidate.getPlayerTwo().equals(new Player("null"))) {
            List<Player> pairsPlayedTwo = pairsPlayed.get(pairCandidate.getPlayerTwo());
            movePlayerOneUp(pairCandidate.getPlayerTwo(), pairsPlayedTwo != null ? pairsPlayedTwo.size() : 0);
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
        playersAmountOfPairs.put(maxAmountOfBattlesPrPlayer, new HashSet());

        playersAmountOfPairs.put(0, new HashSet());
        for (Player player : players) {
            playersAmountOfPairs.get(0).add(player);
        }
    }


}
