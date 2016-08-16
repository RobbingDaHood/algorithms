package TableSoccerTournament.Tests;

import TableSoccerTournament.Models.Game;
import TableSoccerTournament.Models.Pair;
import TableSoccerTournament.Models.Player;
import TableSoccerTournament.TableSoccerTournament;
import TableSoccerTournament.TournamentGeneratorHelper;
import org.junit.Assert;

import java.util.*;

/**
 * Created by super on 07/08/2016.
 */
public class TableSoccerTournamentTest {

    @org.junit.Test
    public void getNextGameTotalTournament8() throws Exception {
        testGenerateCycleRowTournament(8);
    }

    @org.junit.Test
    public void getNextGameTotalTournament16() throws Exception {
        testGenerateCycleRowTournament(16);
    }

    @org.junit.Test
    public void getNextGameTotalTournament23() throws Exception {
        testGenerateCycleRowTournament(23);
    }

    @org.junit.Test
    public void getNextGameTotalTournament5() throws Exception {
        testGenerateCycleRowTournament(5);
    }

    @org.junit.Test
    public void getNextGameTotalTournament6() throws Exception {
        testGenerateCycleRowTournament(6);
    }

    @org.junit.Test
    public void getNextGameTotalTournament7() throws Exception {
        testGenerateCycleRowTournament(7);
    }

    @org.junit.Test
    public void getNextGameEveryPairRow10() throws Exception {
        testGenerateCycleRowTournament(10);
    }

    @org.junit.Test
    public void getNextGameEveryPairRow11() throws Exception {
        testGenerateCycleRowTournament(11);
    }

    @org.junit.Test
    public void getNextGameEveryPairRow13() throws Exception {
        testGenerateCycleRowTournament(13);
    }

    @org.junit.Test
    public void getNextGameEveryPairRow17() throws Exception {
        testGenerateCycleRowTournament(17);
    }

    @org.junit.Test
    public void getNextGameEveryPairRow16() throws Exception {
        testGenerateCycleRowTournament(16);
    }

    @org.junit.Test
    public void getNextGameEveryPairRow18() throws Exception {
        testGenerateCycleRowTournament(18);
    }


    @org.junit.Test
    public void getNextGameEveryPairRow20() throws Exception {
        testGenerateCycleRowTournament(20);
    }


    @org.junit.Test
    public void getNextGameEveryPairRow21() throws Exception {
        testGenerateCycleRowTournament(21);
    }

    @org.junit.Test
    public void getNextGameEveryPairRow33() throws Exception {
        testGenerateCycleRowTournament(33);
    }

    @org.junit.Test
    public void getNextGameEveryPairRow65() throws Exception {
        testGenerateCycleRowTournament(65);
    }


    @org.junit.Test
    public void getNextGameEveryPairRow97() throws Exception {
        testGenerateCycleRowTournament(97);
    }


    @org.junit.Test
    public void getNextGameEveryPairRow98() throws Exception {
        testGenerateCycleRowTournament(98);
    }


    @org.junit.Test
    public void getNextGameEveryPairRow99() throws Exception {
        testGenerateCycleRowTournament(99);
    }


    @org.junit.Test
    public void getNextGameEveryPairRow5() throws Exception {
        testGenerateCycleRowTournament(5);
    }

    @org.junit.Test
    public void getNextGameTotalTournamentSimple() throws Exception {
        for (int i = 4; i < 10; i++) {
            testGenerateCycleRowTournament(Math.pow(2, i));
        }
    }

    @org.junit.Test
    public void getNextGameTotalTournamentTotal() throws Exception {
        for (int i = 4; i < 100; i++) {
            testGenerateCycleRowTournament(i);
        }
    }

    @org.junit.Test
    public void getFullTournamentFeaturesCustom() throws Exception {
        ArrayList<Player> players = TournamentGeneratorHelper.generatePersons(1000);
        TableSoccerTournament tableSoccerTournament = new TableSoccerTournament(players);
        Queue<Pair> pairs = tableSoccerTournament.generateTournament();
        Queue<Pair> pairs1 = TournamentGeneratorHelper.modifyTorunamentForSequenceLength(new LinkedList<>(pairs), 3 * 2, false);
        List<Game> games = TournamentGeneratorHelper.generateGameList(pairs1);
        List<Game> games1 = TournamentGeneratorHelper.modifyMaxAmountOfPlays(games, Integer.MAX_VALUE, 3, Integer.MAX_VALUE);
        TournamentGeneratorHelper.generateJson(games1, 3);

        System.out.println("Amount of players: " + 99 + " resulted in " + games1.size() + " Games. At sequence length " + 3 + " games:");

        testFillTournamnetFeatures(games1, 3, false);
        System.out.println("");
    }

    @org.junit.Test
    public void getFullTournamentFeatures10_2() throws Exception {
        //Max plays: i * gamelength / 4
        for (int i = 4; i < 100; i++) {
            ArrayList<Player> players = TournamentGeneratorHelper.generatePersons(i);
            TableSoccerTournament tableSoccerTournament = new TableSoccerTournament(players);
            Queue<Pair> pairs = tableSoccerTournament.generateTournament();
            Queue<Pair> pairs1 = TournamentGeneratorHelper.modifyTorunamentForSequenceLength(new LinkedList<>(pairs), 2 * 2, false);
            List<Game> games = TournamentGeneratorHelper.generateGameList(pairs1);
            List<Game> games1 = TournamentGeneratorHelper.modifyMaxAmountOfPlays(games, 10, 2, Integer.MAX_VALUE);
            TournamentGeneratorHelper.generateJson(games1, 2);

            System.out.println("Amount of players: " + i + " resulted in " + games1.size() + " Games. At sequence length " + 2 + " games:");

            testFillTournamnetFeatures(games1, 2, false);
            System.out.println("");
        }
    }

    @org.junit.Test
    public void getFullTournamentFeaturesMAX_2() throws Exception {
        //Max plays: i * gamelength / 4
        for (int i = 4; i < 100; i++) {
            ArrayList<Player> players = TournamentGeneratorHelper.generatePersons(i);
            TableSoccerTournament tableSoccerTournament = new TableSoccerTournament(players);
            Queue<Pair> pairs = tableSoccerTournament.generateTournament();
            Queue<Pair> pairs1 = TournamentGeneratorHelper.modifyTorunamentForSequenceLength(new LinkedList<>(pairs), 2 * 2, false);
            List<Game> games = TournamentGeneratorHelper.generateGameList(pairs1);
            List<Game> games1 = TournamentGeneratorHelper.modifyMaxAmountOfPlays(games, Integer.MAX_VALUE, 2, Integer.MAX_VALUE);
            TournamentGeneratorHelper.generateJson(games1, 2);

            System.out.println("Amount of players: " + i + " resulted in " + games1.size() + " Games. At sequence length " + 2 + " games:");

            testFillTournamnetFeatures(games1, 2, false);
            System.out.println("");
        }
    }

    @org.junit.Test
    public void getFullTournamentFeaturesMAX_3() throws Exception {
        //Max plays: i * gamelength / 4
        for (int i = 4; i < 100; i++) {
            ArrayList<Player> players = TournamentGeneratorHelper.generatePersons(i);
            TableSoccerTournament tableSoccerTournament = new TableSoccerTournament(players);
            Queue<Pair> pairs = tableSoccerTournament.generateTournament();
            Queue<Pair> pairs1 = TournamentGeneratorHelper.modifyTorunamentForSequenceLength(new LinkedList<>(pairs), 3 * 2, false);
            List<Game> games = TournamentGeneratorHelper.generateGameList(pairs1);
            List<Game> games1 = TournamentGeneratorHelper.modifyMaxAmountOfPlays(games, Integer.MAX_VALUE, 3, Integer.MAX_VALUE);
            TournamentGeneratorHelper.generateJson(games1, 3);

            System.out.println("Amount of players: " + i + " resulted in " + games1.size() + " Games. At sequence length " + 3 + " games:");

            testFillTournamnetFeatures(games1, 3, false);
            System.out.println("");
        }
    }


    private void testFillTournamnetFeatures(List<Game> games, int sequenceLength, boolean unsafe) {
        Map<Player, List<Player>> pairsPlayed = new HashMap<>();

        int dummyGames = 0;

        double maxGamesPlayedDistance = -Double.MAX_VALUE;
        for (int i = 0; i <= games.size() - sequenceLength; i += sequenceLength) {
            List<Player> plaersPlayedInSequence = new LinkedList<>();
            for (int k = 0; k < sequenceLength && i + k <= games.size(); k++) {
                Game game = games.get(i + k);

                if (!TournamentGeneratorHelper.isDummyGame(game.getTeamOne(), game.getTeamTwo())) {
                    Assert.assertFalse(!TournamentGeneratorHelper.isValidGame(game.getTeamOne(), game.getTeamTwo()));
                    Assert.assertFalse(!TournamentGeneratorHelper.isNotSamePairs(game.getTeamOne(), game.getTeamTwo()));

                    TournamentGeneratorHelper.addTeam(pairsPlayed, game.getTeamOne().getPlayerOne(), game.getTeamOne().getPlayerTwo());
                    TournamentGeneratorHelper.addTeam(pairsPlayed, game.getTeamTwo().getPlayerOne(), game.getTeamTwo().getPlayerTwo());

                    if (!unsafe && (plaersPlayedInSequence.contains(game.getTeamOne().getPlayerOne()) ||
                            plaersPlayedInSequence.contains(game.getTeamOne().getPlayerTwo()) ||
                            plaersPlayedInSequence.contains(game.getTeamTwo().getPlayerOne()) ||
                            plaersPlayedInSequence.contains(game.getTeamTwo().getPlayerTwo()))) {
                        System.out.print("Same:" + (i + k + 1) + " ");
                    }

                    plaersPlayedInSequence.add(game.getTeamOne().getPlayerOne());
                    plaersPlayedInSequence.add(game.getTeamOne().getPlayerTwo());
                    plaersPlayedInSequence.add(game.getTeamTwo().getPlayerOne());
                    plaersPlayedInSequence.add(game.getTeamTwo().getPlayerTwo());
                } else {
                    dummyGames++;
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

            if (maxGamesPlayedDistance < maxAmountOfPlays - minAmountOfPlays) {
                maxGamesPlayedDistance = maxAmountOfPlays - minAmountOfPlays;
                System.out.println("Max distance is now: " + maxGamesPlayedDistance + " at game: " + i + " ");
            }
        }

        if (dummyGames > 0)
            System.out.println("Dummies: " + dummyGames);
        else
            System.out.println(" ");


    }

    //This test wont stop before all games have been executed
    private void testGenerateCycleRowTournament(double amount) {
        ArrayList<Player> players = TournamentGeneratorHelper.generatePersons(amount);
        Map<Player, List<Player>> pairsPlayed = new HashMap<>();
        LinkedList<Pair> allPairs = new LinkedList<>();

        TableSoccerTournament tableSoccerTournament = new TableSoccerTournament(players);

        //Generate all rows
        while (tableSoccerTournament.isRingsHaveMorePairs()) {

//            System.out.println("Will generate this row, amount of pairs generated by now: " + allPairs.size());
//            for (DoubleRing doubleRing : tableSoccerTournament.getRings()) {
//                System.out.println(doubleRing);
//            }

            //Generate all cycles for this row
            while (tableSoccerTournament.isRingsHaveMorePairs()) {
                Queue<Pair> newGamesList = tableSoccerTournament.generateNextCycle();

                //Handle pairs
                while (!newGamesList.isEmpty()) {
                    Pair pair = newGamesList.poll();
                    allPairs.add(pair);
                    TournamentGeneratorHelper.addTeam(pairsPlayed, pair.getPlayerOne(), pair.getPlayerTwo());
                }

                validateCycle(pairsPlayed);
            }

            validateRow(pairsPlayed);

            tableSoccerTournament.generateNewRings();
        }

        int sequenceGameLength = 2;
        TournamentGeneratorHelper.modifyTorunamentForSequenceLength(allPairs, sequenceGameLength * 2, true);


        System.out.println("* Amount of players: " + amount + " resulted in " + TournamentGeneratorHelper.generateGameList(new LinkedList<>(allPairs)).size() + " Games. At sequence length " + sequenceGameLength + " games:");
        printHackItStatistics(TournamentGeneratorHelper.generateGameList(new LinkedList<>(allPairs)), sequenceGameLength);
        validateTotalTournament(pairsPlayed);
        notInSameGameSequence(TournamentGeneratorHelper.generateGameList(new LinkedList<>(allPairs)), amount, sequenceGameLength);
        System.out.println("");
    }

    private void notInSameGameSequence(List<Game> games, double amount, int sequenceGameLength) {
        System.out.print("At what game the sequance stops: ");

        boolean atLeastOnevalid = true;
//        int sequenceGameLength = 1;

//        while (atLeastOnevalid) {
//            atLeastOnevalid = false;

        outerwhile:
        for (int i = 0; i <= games.size() - sequenceGameLength; i += sequenceGameLength) {
            Map<Player, Integer> personCounts = new HashMap<>();
            for (int k = 0; k < sequenceGameLength && i + k < games.size(); k++) {
                Game game = games.get(i + k);

                Player player = game.getTeamOne().getPlayerOne();
                Integer personCount = personCounts.get(player);
                if (personCount == null) {
                    personCounts.put(player, 1);
                } else {
//                        System.out.print(i + ":" + sequenceGameLength + " ");
                    System.out.print(i);
                    break outerwhile;
                }

                player = game.getTeamOne().getPlayerTwo();
                personCount = personCounts.get(player);
                if (personCount == null) {
                    personCounts.put(player, 1);
                } else {
//                        System.out.print(i + ":" + sequenceGameLength + " ");
                    System.out.print(i);
                    break outerwhile;
                }


                player = game.getTeamTwo().getPlayerOne();
                personCount = personCounts.get(player);
                if (personCount == null) {
                    personCounts.put(player, 1);
                } else {
//                        System.out.print(i + ":" + sequenceGameLength + " ");
                    System.out.print(i);
                    break outerwhile;
                }


                player = game.getTeamTwo().getPlayerTwo();
                personCount = personCounts.get(player);
                if (personCount == null) {
                    personCounts.put(player, 1);
                } else {
//                        System.out.print(i + ":" + sequenceGameLength + " ");
                    System.out.print(i);
                    break outerwhile;
                }
            }

            atLeastOnevalid = true;
        }

//            sequenceGameLength++;
//        }

        System.out.println("   ");
//        System.out.println("GameDistance: " + (sequenceGameLength - 1));
    }


    private void countMaxDistanesInGames(List<Game> games, double amount) {
        Map<Player, Integer> distanceSinceLastGame = new HashMap<>();
        int minimumDistanceInGame = Integer.MAX_VALUE;

        System.out.print("(" + amount + "/" + games.size() + ") ");

        boolean minimumChanged;
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            minimumChanged = false;

            Player player = game.getTeamOne().getPlayerOne();
            Integer indexOfLastPlay = distanceSinceLastGame.get(player);
            if (indexOfLastPlay != null && minimumDistanceInGame > i - indexOfLastPlay) {
                minimumDistanceInGame = i - indexOfLastPlay;
                minimumChanged = true;
            }
            distanceSinceLastGame.put(player, i);

            player = game.getTeamOne().getPlayerTwo();
            indexOfLastPlay = distanceSinceLastGame.get(player);
            if (indexOfLastPlay != null && minimumDistanceInGame > i - indexOfLastPlay) {
                minimumDistanceInGame = i - indexOfLastPlay;
                minimumChanged = true;
            }
            distanceSinceLastGame.put(player, i);

            player = game.getTeamTwo().getPlayerOne();
            indexOfLastPlay = distanceSinceLastGame.get(player);
            if (indexOfLastPlay != null && minimumDistanceInGame > i - indexOfLastPlay) {
                minimumDistanceInGame = i - indexOfLastPlay;
                minimumChanged = true;
            }
            distanceSinceLastGame.put(player, i);

            player = game.getTeamTwo().getPlayerTwo();
            indexOfLastPlay = distanceSinceLastGame.get(player);
            if (indexOfLastPlay != null && minimumDistanceInGame > i - indexOfLastPlay) {
                minimumDistanceInGame = i - indexOfLastPlay;
                minimumChanged = true;
            }
            distanceSinceLastGame.put(player, i);

            if (minimumChanged) {
                System.out.print(i + ":" + minimumDistanceInGame + " ");
            }
        }
        System.out.println("");

    }


    private void validateGames(List<Game> games, double amount) {
        Map<Player, Integer> distanceSinceLastGame = new HashMap<>();
//        double minimumDistanceFromLastGame = Math.floor(amount / 6) - 1;
//        double minimumDistanceFromLastGame = Math.floor((amount - 6) / 4) + 1;
        double minimumDistanceFromLastGame = Math.floor((amount - 6) / 4) + 0;
        if (amount < 18) {
            minimumDistanceFromLastGame = 1;
        } else if (amount < 22) {
            minimumDistanceFromLastGame = 2;
        } else if (amount < 26) {
            minimumDistanceFromLastGame = 3;
        } else { //Og det er nok endnu bedre.
            minimumDistanceFromLastGame = Math.floor((amount - 10) / 4) + 2;
        }

        //games.size() * 0.84
        double validGamesMaxIndex;
        if (amount < 7) {
            validGamesMaxIndex = games.size() - 1;
        }
//        else if (amount < 8) {
//            validGamesMaxIndex = games.size() - 2;
//        }
        else { //Og det er nok endnu bedre.
            validGamesMaxIndex = games.size() * 0.84;
        }

        for (int i = 0; i < validGamesMaxIndex; i++) { //The last games can be quite broken.
            Game game = games.get(i);

            Player player = game.getTeamOne().getPlayerOne();
            Integer indexOfLastPlay = distanceSinceLastGame.get(player);
            if (indexOfLastPlay != null) {
                Assert.assertFalse("Should be: " + minimumDistanceFromLastGame + " but were: " + (i - indexOfLastPlay) + " at game number: " + i, i - indexOfLastPlay < minimumDistanceFromLastGame);
            }
            distanceSinceLastGame.put(player, i);

            player = game.getTeamOne().getPlayerTwo();
            indexOfLastPlay = distanceSinceLastGame.get(player);
            if (indexOfLastPlay != null) {
                Assert.assertFalse("Should be: " + minimumDistanceFromLastGame + " but were: " + (i - indexOfLastPlay) + " at game number: " + i, i - indexOfLastPlay < minimumDistanceFromLastGame);
            }
            distanceSinceLastGame.put(player, i);

            player = game.getTeamTwo().getPlayerOne();
            indexOfLastPlay = distanceSinceLastGame.get(player);
            if (indexOfLastPlay != null) {
                Assert.assertFalse("Should be: " + minimumDistanceFromLastGame + " but were: " + (i - indexOfLastPlay) + " at game number: " + i, i - indexOfLastPlay < minimumDistanceFromLastGame);
            }
            distanceSinceLastGame.put(player, i);

            player = game.getTeamTwo().getPlayerTwo();
            indexOfLastPlay = distanceSinceLastGame.get(player);
            if (indexOfLastPlay != null) {
                Assert.assertFalse("Should be: " + minimumDistanceFromLastGame + " but were: " + (i - indexOfLastPlay) + " at game number: " + i, i - indexOfLastPlay < minimumDistanceFromLastGame);
            }
            distanceSinceLastGame.put(player, i);
        }

    }

    private void printHackItStatistics(List<Game> games, int sequenceLength) {
        Map<Player, Integer> amountOfPlays = new HashMap<>();
        LinkedList<Integer> whenDidMaxGetBroken = new LinkedList<>();
        double maxDistanceEncountered = -Integer.MAX_VALUE;
        Map<Double, Integer> differentMaxCount = new HashMap<>();

        for (int i = 0; i < games.size(); i += sequenceLength) {

            //add all plays in nex sequence
            for (int k = 0; k < sequenceLength && i + k < games.size(); k++) {
                Game game = games.get(i + k);

                Player player = game.getTeamOne().getPlayerOne();
                Integer amountOfPlay = amountOfPlays.get(player);
                if (amountOfPlay != null) {
                    amountOfPlay++;
                    amountOfPlays.put(player, amountOfPlay);
                } else {
                    amountOfPlays.put(player, 1);
                }

                player = game.getTeamOne().getPlayerTwo();
                amountOfPlay = amountOfPlays.get(player);
                if (amountOfPlay != null) {
                    amountOfPlay++;
                    amountOfPlays.put(player, amountOfPlay);
                } else {
                    amountOfPlays.put(player, 1);
                }

                player = game.getTeamTwo().getPlayerOne();
                amountOfPlay = amountOfPlays.get(player);
                if (amountOfPlay != null) {
                    amountOfPlay++;
                    amountOfPlays.put(player, amountOfPlay);
                } else {
                    amountOfPlays.put(player, 1);
                }

                player = game.getTeamTwo().getPlayerTwo();
                amountOfPlay = amountOfPlays.get(player);
                if (amountOfPlay != null) {
                    amountOfPlay++;
                    amountOfPlays.put(player, amountOfPlay);
                } else {
                    amountOfPlays.put(player, 1);
                }
            }


            double distance = getMaxDistance(amountOfPlays);
            if (maxDistanceEncountered < distance) {
                maxDistanceEncountered = distance;
                whenDidMaxGetBroken.add(i + sequenceLength);
            }

            Integer distanceCount = differentMaxCount.get(distance);
            if (distanceCount == null) {
                differentMaxCount.put(distance, 1);
            } else {
                differentMaxCount.put(distance, distanceCount + 1);
            }
        }

        System.out.print("* GameIndex(maxValue): ");
        int count = 0;
        for (Integer integer : whenDidMaxGetBroken) {
            System.out.print(integer + "(" + count++ + "), ");
        }
        System.out.println("");

        System.out.print("* Distance(Count): ");
        for (Double distance : differentMaxCount.keySet()) {
            System.out.print(distance + "(" + differentMaxCount.get(distance) + "), ");
        }
        System.out.println("");
    }

    private double getMaxDistance(Map<Player, Integer> amountOfPlays) {
        double maxAmountOfPlays = -Double.MAX_VALUE;
        double minAmountOfPlays = Double.MAX_VALUE;

        for (Player player : amountOfPlays.keySet()) {
            if (amountOfPlays.get(player) != null) {
                if (maxAmountOfPlays < amountOfPlays.get(player)) {
                    maxAmountOfPlays = amountOfPlays.get(player);
                }
                if (minAmountOfPlays > amountOfPlays.get(player)) {
                    minAmountOfPlays = amountOfPlays.get(player);
                }
            }
        }

        return maxAmountOfPlays - minAmountOfPlays;
    }

    private void validateRow(Map<Player, List<Player>> pairsPlayed) {
        Set<Player> players = pairsPlayed.keySet();

        int firstSizeEncountered = 0;

        for (Player player : players) {
            List<Player> partners = pairsPlayed.get(player);

            if (firstSizeEncountered == 0) {
                firstSizeEncountered = partners.size();
            } else {
                int distanceFromFirstEncounter = firstSizeEncountered - partners.size();
                Assert.assertFalse("The distance in pairs pr. player may a max be -1 to 1 after each row, but were: " + distanceFromFirstEncounter, distanceFromFirstEncounter < -1);
                Assert.assertFalse("The distance in pairs pr. player may a max be -1 to 1 after each row, but were: " + distanceFromFirstEncounter, distanceFromFirstEncounter > 1);
            }
        }
    }


    private void validateCycle(Map<Player, List<Player>> pairsPlayed) {
        Set<Player> players = pairsPlayed.keySet();

        int firstSizeEncountered = 0;

        for (Player player : players) {
            List<Player> partners = pairsPlayed.get(player);

            if (firstSizeEncountered == 0) {
                firstSizeEncountered = partners.size();
            } else {
                int distanceFromFirstEncounter = firstSizeEncountered - partners.size();
                Assert.assertFalse("The distance in pairs pr. player may a max be -2 to 2 after each cycle, but were: " + distanceFromFirstEncounter, distanceFromFirstEncounter < -2);
                Assert.assertFalse("The distance in pairs pr. player may a max be -2 to 2 after each cycle, but were: " + distanceFromFirstEncounter, distanceFromFirstEncounter > 2);
            }
        }
    }

    private void validateTotalTournament(Map<Player, List<Player>> pairsPlayed) {
        Set<Player> players = pairsPlayed.keySet();
        int countPlayersPlayedOneLess = 0;
        for (Player player : players) {
            List<Player> partners = pairsPlayed.get(player);

            int distanceToTheoreticalMax = players.size() - partners.size();
            if (distanceToTheoreticalMax == 2) {
                //Sometimes the last pair does not have a partner, then up to two players can have played #Persons - 2
                //pairs.
                countPlayersPlayedOneLess++;
                Assert.assertFalse(countPlayersPlayedOneLess > 2);
            } else {
                //Max amount of pairs a player can be in is #Persons - 1
                Assert.assertFalse(distanceToTheoreticalMax != 1);
            }

            //No two same partners
            for (Player partner : partners) {
                int count = 0;
                for (Player partner1 : partners) {
                    if (partner == partner1) {
                        count++;
                    }

                    Assert.assertFalse(count > 1);
                }
            }
        }
    }

}