package TableSoccerTournament;

import TableSoccerTournament.Models.Game;
import TableSoccerTournament.Models.Pair;
import TableSoccerTournament.Models.Player;
import org.junit.Assert;

import java.util.*;

/**
 * Created by super on 15/08/2016.
 */
public class TournamentGeneratorHelper {

    public static List<Game> modifyMaxAmountOfPlays(List<Game> games, int maxLegalAmountOfPlays, int sequenceLength, int stopAfterGame) {
        Map<Player, List<Player>> pairsPlayed = new HashMap<>();
        Map<Player, Integer> totalCount = new HashMap<>();
        int maxPairsYet = -Integer.MAX_VALUE;
        List<Game> result = new LinkedList<>();

        if (games.isEmpty())
            return result;

        for (int i1 = 0; i1 < games.size(); i1++) {
            Game game = games.get(i1);

            Integer count = totalCount.get(game.getTeamOne().getPlayerOne());
            if (count == null) {
                totalCount.put(game.getTeamOne().getPlayerOne(), 1);
            } else {
                totalCount.put(game.getTeamOne().getPlayerOne(), count + 1);
            }

            count = totalCount.get(game.getTeamOne().getPlayerTwo());
            if (count == null) {
                totalCount.put(game.getTeamOne().getPlayerTwo(), 1);
            } else {
                totalCount.put(game.getTeamOne().getPlayerTwo(), count + 1);
            }

            count = totalCount.get(game.getTeamTwo().getPlayerOne());
            if (count == null) {
                totalCount.put(game.getTeamTwo().getPlayerOne(), 1);
            } else {
                totalCount.put(game.getTeamTwo().getPlayerOne(), count + 1);
            }

            count = totalCount.get(game.getTeamTwo().getPlayerTwo());
            if (count == null) {
                totalCount.put(game.getTeamTwo().getPlayerTwo(), 1);
            } else {
                totalCount.put(game.getTeamTwo().getPlayerTwo(), count + 1);
            }
        }

        int minAmountOfPlays = maxLegalAmountOfPlays;

        for (Player player : totalCount.keySet()) {
            if (totalCount.get(player) != null && !player.equals(new Player("null"))) {
                if (minAmountOfPlays > totalCount.get(player)) {
                    minAmountOfPlays = totalCount.get(player);
                }
            }
        }

        maxLegalAmountOfPlays = minAmountOfPlays;
//        System.out.println("maxLegalAmountOfPlays is now: " + maxLegalAmountOfPlays);

        int k = 0;
        outerLoop:
        for (int i = 0; i < games.size() && i <= stopAfterGame; i += k) {

            List<Game> sequence = new LinkedList<>();
            for (k = 0; k < sequenceLength && i + k < games.size(); k++) {
                Game game = games.get(i + k);

                List<Player> players = pairsPlayed.get(game.getTeamOne().getPlayerOne());
                if (players != null && maxPairsYet < players.size() + 1) {
                    maxPairsYet = players.size() + 1;
                }
                players = pairsPlayed.get(game.getTeamOne().getPlayerTwo());
                if (players != null && maxPairsYet < players.size() + 1) {
                    maxPairsYet = players.size() + 1;
                }
                players = pairsPlayed.get(game.getTeamTwo().getPlayerOne());
                if (players != null && maxPairsYet < players.size() + 1) {
                    maxPairsYet = players.size() + 1;
                }
                players = pairsPlayed.get(game.getTeamTwo().getPlayerTwo());
                if (players != null && maxPairsYet < players.size() + 1) {
                    maxPairsYet = players.size() + 1;
                }

                sequence.add(game);
            }

            if (maxPairsYet <= maxLegalAmountOfPlays) {
                result.addAll(sequence);
                addGames(pairsPlayed, sequence);
                for (Game game : sequence) {
                    int count = totalCount.get(game.getTeamTwo().getPlayerOne());
                    totalCount.put(game.getTeamOne().getPlayerOne(), count - 1);
                    totalCount.put(game.getTeamOne().getPlayerTwo(), count - 1);
                    totalCount.put(game.getTeamTwo().getPlayerOne(), count - 1);
                    totalCount.put(game.getTeamTwo().getPlayerTwo(), count - 1);
                }
            } else {
                addAlreadyLegalGames(games, maxLegalAmountOfPlays, sequenceLength, pairsPlayed, result, i, sequence);

                //See if there are any players
                List<Player> playersMissingOneOrMoreGames = getPlayersMissingGames(maxLegalAmountOfPlays, pairsPlayed);
                if (playersMissingOneOrMoreGames.size() < 1) {
                    break outerLoop;
                }

                if (playersMissingOneOrMoreGames.size() >= 4) {
                    if (addNewGamesWithoutDummies(maxLegalAmountOfPlays, pairsPlayed, result, playersMissingOneOrMoreGames, true))
                        break outerLoop;

                    //See if there are any players
                    playersMissingOneOrMoreGames = getPlayersMissingGames(maxLegalAmountOfPlays, pairsPlayed, playersMissingOneOrMoreGames);
                    if (playersMissingOneOrMoreGames.size() < 1) {
                        break outerLoop;
                    }

                    if (playersMissingOneOrMoreGames.size() >= 4) {
                        if (addNewGamesWithoutDummies(maxLegalAmountOfPlays, pairsPlayed, result, playersMissingOneOrMoreGames, false))
                            break outerLoop;
                    }
                }

                //See if there are any players
                playersMissingOneOrMoreGames = getPlayersMissingGames(maxLegalAmountOfPlays, pairsPlayed, playersMissingOneOrMoreGames);
                if (playersMissingOneOrMoreGames.size() < 1) {
                    break outerLoop;
                }

                if (playersMissingOneOrMoreGames.size() >= 3) {
                    playersMissingOneOrMoreGames = generateOneDummyGames(maxLegalAmountOfPlays, pairsPlayed, result, playersMissingOneOrMoreGames);
                    if (playersMissingOneOrMoreGames == null) break outerLoop;
                }

                if (playersMissingOneOrMoreGames.size() >= 2) {
                    playersMissingOneOrMoreGames = generateDoubleDummyGames(maxLegalAmountOfPlays, pairsPlayed, result, playersMissingOneOrMoreGames);
                    if (playersMissingOneOrMoreGames == null) break outerLoop;
                }

                //Triple dummy games are only considered if
                for (Player playersMissingOneOrMoreGame : playersMissingOneOrMoreGames) {
                    Game game = new Game(new Pair(new Player("null"), new Player("null")), new Pair(playersMissingOneOrMoreGame, new Player("null")));
                    result.add(game);
                    addTeam(pairsPlayed, game.getTeamOne().getPlayerOne(), game.getTeamOne().getPlayerTwo());
                    addTeam(pairsPlayed, game.getTeamTwo().getPlayerOne(), game.getTeamTwo().getPlayerTwo());
                }

                //See if there are any players
                playersMissingOneOrMoreGames = getPlayersMissingGames(maxLegalAmountOfPlays, pairsPlayed, playersMissingOneOrMoreGames);
                if (playersMissingOneOrMoreGames.size() < 1) {
                    break outerLoop;
                }

                Assert.assertFalse(!playersMissingOneOrMoreGames.isEmpty());

                break outerLoop;
            }
        }

        return result;
    }

    public static List<Player> generateDoubleDummyGames(int maxAmountOfPlays, Map<Player, List<Player>> pairsPlayed, List<Game> result, List<Player> playersMissingOneOrMoreGames) {
        boolean addedPair = true;
        while (addedPair) {
            addedPair = false;
            //Try to generate all possible pairs with the last players
            //Where it is okay they played with each other
            generatorLoop:
            for (Player playersMissingOneOrMoreGame : playersMissingOneOrMoreGames) {
                for (Player missingOneOrMoreGame : playersMissingOneOrMoreGames) {
                    if (!playersMissingOneOrMoreGame.equals(missingOneOrMoreGame)) {
                        Game game = new Game(new Pair(playersMissingOneOrMoreGame, missingOneOrMoreGame), new Pair(new Player("null"), new Player("null")));
                        result.add(game);
                        addTeam(pairsPlayed, game.getTeamOne().getPlayerOne(), game.getTeamOne().getPlayerTwo());
                        addTeam(pairsPlayed, game.getTeamTwo().getPlayerOne(), game.getTeamTwo().getPlayerTwo());
                        addedPair = true;
                        break generatorLoop;
                    }
                }
            }

            playersMissingOneOrMoreGames = getPlayersMissingGames(maxAmountOfPlays, pairsPlayed, playersMissingOneOrMoreGames);
            if (playersMissingOneOrMoreGames.size() < 1) {
                return null;
            }
        }
        return playersMissingOneOrMoreGames;
    }

    public static List<Player> generateOneDummyGames(int maxAmountOfPlays, Map<Player, List<Player>> pairsPlayed, List<Game> result, List<Player> playersMissingOneOrMoreGames) {
        boolean addedPair = true;
        while (addedPair) {
            addedPair = false;
            //Try to generate all possible pairs with the last players
            //Where it is okay they played with each other
            List<Pair> validPairs = new LinkedList<>();
            for (Player playersMissingOneOrMoreGame : playersMissingOneOrMoreGames) {
                for (Player missingOneOrMoreGame : playersMissingOneOrMoreGames) {
                    if (!playersMissingOneOrMoreGame.equals(missingOneOrMoreGame)) {
                        validPairs.add(new Pair(playersMissingOneOrMoreGame, missingOneOrMoreGame));
                    }
                }
            }

            //Create any one dummy player games?
            if (validPairs.size() > 0) {
                generatorLoop:
                for (Pair validPair : new LinkedList<>(validPairs)) {
                    for (Player playersMissingOneOrMoreGame : new LinkedList<>(playersMissingOneOrMoreGames)) {
                        if (isNotSamePairs(validPair, new Pair(playersMissingOneOrMoreGame, playersMissingOneOrMoreGame))) {
                            Game game = new Game(validPair, new Pair(playersMissingOneOrMoreGame, new Player("null")));
                            result.add(game);
                            addTeam(pairsPlayed, game.getTeamOne().getPlayerOne(), game.getTeamOne().getPlayerTwo());
                            addTeam(pairsPlayed, game.getTeamTwo().getPlayerOne(), game.getTeamTwo().getPlayerTwo());
                            addedPair = true;
                            break generatorLoop;
                        }
                    }
                }
            }

            playersMissingOneOrMoreGames = getPlayersMissingGames(maxAmountOfPlays, pairsPlayed, playersMissingOneOrMoreGames);
            if (playersMissingOneOrMoreGames.size() < 1) {
                return null;
            }
        }
        return playersMissingOneOrMoreGames;
    }

    public static boolean addNewGamesWithoutDummies(int maxAmountOfPlays, Map<Player, List<Player>> pairsPlayed, List<Game> result, List<Player> playersMissingOneOrMoreGames, boolean considerPastGames) {
        boolean addedPair = true;
        while (addedPair) {
            addedPair = false;

            //Try to generate all possible pairs with the last players
            //Where they have not played with each other before
            LinkedList<Pair> validPairs = new LinkedList<>();
            for (Player playersMissingOneOrMoreGame : playersMissingOneOrMoreGames) {
                for (Player missingOneOrMoreGame : playersMissingOneOrMoreGames) {
                    if (!playersMissingOneOrMoreGame.equals(missingOneOrMoreGame) && !(considerPastGames && pairsPlayed.get(playersMissingOneOrMoreGame).contains(missingOneOrMoreGame))) {
                        validPairs.add(new Pair(playersMissingOneOrMoreGame, missingOneOrMoreGame));
                    }
                }
            }

            //try to generate valid games
            generatorLoop:
            for (Pair validPair : new LinkedList<>(validPairs)) {
                for (Pair pair : new LinkedList<>(validPairs)) {
                    if (isNotSamePairs(validPair, pair)) {
                        Game game = new Game(validPair, pair);
                        result.add(game);
                        addTeam(pairsPlayed, game.getTeamOne().getPlayerOne(), game.getTeamOne().getPlayerTwo());
                        addTeam(pairsPlayed, game.getTeamTwo().getPlayerOne(), game.getTeamTwo().getPlayerTwo());
                        addedPair = true;
                        break generatorLoop;
                    }
                }
            }

            playersMissingOneOrMoreGames = getPlayersMissingGames(maxAmountOfPlays, pairsPlayed, playersMissingOneOrMoreGames);
            if (playersMissingOneOrMoreGames.size() < 1) {
                return true;
            }
        }
        return false;
    }

    public static void addAlreadyLegalGames(List<Game> games, int maxAmountOfPlays, int sequenceLength, Map<Player, List<Player>> pairsPlayed, List<Game> result, int i, List<Game> sequence) {
        //Add valid games from this sequence
        for (Game game : sequence) {
            if (maxAmountOfPlays > pairsPlayed.get(game.getTeamOne().getPlayerOne()).size()) {
                if (maxAmountOfPlays > pairsPlayed.get(game.getTeamOne().getPlayerTwo()).size()) {
                    if (maxAmountOfPlays > pairsPlayed.get(game.getTeamTwo().getPlayerOne()).size()) {
                        if (maxAmountOfPlays > pairsPlayed.get(game.getTeamTwo().getPlayerTwo()).size()) {
                            result.add(game);
                            addTeam(pairsPlayed, game.getTeamOne().getPlayerOne(), game.getTeamOne().getPlayerTwo());
                            addTeam(pairsPlayed, game.getTeamTwo().getPlayerOne(), game.getTeamTwo().getPlayerTwo());
                        }
                    }
                }
            }
        }

        //Add valid games from the rest of the generated games
        for (int k = sequence.size(); i + k < games.size(); k++) {
            Game game = games.get(i + k);
            if (maxAmountOfPlays > pairsPlayed.get(game.getTeamOne().getPlayerOne()).size()) {
                if (maxAmountOfPlays > pairsPlayed.get(game.getTeamOne().getPlayerTwo()).size()) {
                    if (maxAmountOfPlays > pairsPlayed.get(game.getTeamTwo().getPlayerOne()).size()) {
                        if (maxAmountOfPlays > pairsPlayed.get(game.getTeamTwo().getPlayerTwo()).size()) {
                            result.add(game);
                            addTeam(pairsPlayed, game.getTeamOne().getPlayerOne(), game.getTeamOne().getPlayerTwo());
                            addTeam(pairsPlayed, game.getTeamTwo().getPlayerOne(), game.getTeamTwo().getPlayerTwo());
                        }
                    }
                }
            }
        }
    }

    public static void generateValidGames(List<Game> result, LinkedList<Pair> validPairs) {
        //try to generate valid games
        for (Pair validPair : new LinkedList<>(validPairs)) {
            for (Pair pair : new LinkedList<>(validPairs)) {
                if (isNotSamePairs(validPair, pair)) {
                    result.add(new Game(validPair, pair));
                    validPairs.remove(validPair);
                    validPairs.remove(pair);
                }
            }
        }
    }

    public static List<Player> getPlayersMissingGames(int maxAmountOfPlays, Map<Player, List<Player>> pairsPlayed) {
        //Get list of players missing battles
        List<Player> playersMissingOneOrMoreGames = new LinkedList<>();
        for (Player player : pairsPlayed.keySet()) {
            if (pairsPlayed.get(player).size() < maxAmountOfPlays) {
                playersMissingOneOrMoreGames.add(player);
            }
        }
        return playersMissingOneOrMoreGames;
    }

    public static List<Player> getPlayersMissingGames(int maxAmountOfPlays, Map<Player, List<Player>> pairsPlayed, List<Player> missingPlayers) {
        //Get list of players missing battles
        List<Player> playersMissingOneOrMoreGames = new LinkedList<>();
        for (Player player : missingPlayers) {
            if (pairsPlayed.get(player).size() < maxAmountOfPlays) {
                playersMissingOneOrMoreGames.add(player);
            }
        }
        return playersMissingOneOrMoreGames;
    }

    public static boolean isNotSamePairs(Pair validPair, Pair pair) {
        return validPair.getPlayerOne() != pair.getPlayerOne() &&
                validPair.getPlayerOne() != pair.getPlayerTwo() &&
                validPair.getPlayerTwo() != pair.getPlayerOne() &&
                validPair.getPlayerTwo() != pair.getPlayerTwo();
    }

    public static boolean isValidGame(Pair validPair, Pair pair) {
        return validPair.getPlayerOne() != validPair.getPlayerTwo() &&
                pair.getPlayerTwo() != pair.getPlayerOne();
    }

    public static boolean isDummyGame(Pair validPair, Pair pair) {
        return validPair.getPlayerOne().equals(new Player("null")) ||
                pair.getPlayerOne().equals(new Player("null")) ||
                validPair.getPlayerTwo().equals(new Player("null")) ||
                pair.getPlayerTwo().equals(new Player("null"));
    }

    public static boolean isNoGame(Pair validPair, Pair pair) {

        //They all need to be "NOT A GAME".
        if (validPair.getPlayerOne().equals(new Player("NOT A GAME")) ||
                pair.getPlayerOne().equals(new Player("NOT A GAME")) ||
                validPair.getPlayerTwo().equals(new Player("NOT A GAME")) ||
                pair.getPlayerTwo().equals(new Player("NOT A GAME"))) {
            Assert.assertFalse(!validPair.getPlayerOne().equals(new Player("NOT A GAME")) ||
                    !pair.getPlayerOne().equals(new Player("NOT A GAME")) ||
                    !validPair.getPlayerTwo().equals(new Player("NOT A GAME")) ||
                    !pair.getPlayerTwo().equals(new Player("NOT A GAME")));
        }

        return validPair.getPlayerOne().equals(new Player("NOT A GAME")) ||
                pair.getPlayerOne().equals(new Player("NOT A GAME")) ||
                validPair.getPlayerTwo().equals(new Player("NOT A GAME")) ||
                pair.getPlayerTwo().equals(new Player("NOT A GAME"));
    }


    private static void addGames(Map<Player, List<Player>> pairsPlayed, List<Game> games) {
        for (Game game : games) {
            addTeam(pairsPlayed, game.getTeamOne().getPlayerOne(), game.getTeamOne().getPlayerTwo());
            addTeam(pairsPlayed, game.getTeamTwo().getPlayerOne(), game.getTeamTwo().getPlayerTwo());
        }
    }

    public static void addTeam(Map<Player, List<Player>> pairsPlayed, Player playerOne, Player playerTwo) {
       if (isCompetingPlayer(playerOne)) {
           if (pairsPlayed.get(playerOne) == null) {
               LinkedList<Player> players = new LinkedList<>();
               players.add(playerTwo);
               pairsPlayed.put(playerOne, players);
           } else {
               pairsPlayed.get(playerOne).add(playerTwo);
           }
       }

        if (isCompetingPlayer(playerTwo)) {
            if (pairsPlayed.get(playerTwo) == null) {
                LinkedList<Player> players = new LinkedList<>();
                players.add(playerOne);
                pairsPlayed.put(playerTwo, players);
            } else {
                pairsPlayed.get(playerTwo).add(playerOne);
            }
        }
    }


    public static boolean isCompetingPlayer(Player player) {
        return !player.equals(new Player("null"));
    }

    public static LinkedList<Pair> modifyTorunamentForSequenceLength(LinkedList<Pair> allPairs, int sequencePairLength, boolean unsafe) {
        outerwhile:
        for (int i = 0; i < allPairs.size(); i += sequencePairLength) {
            Map<Player, Integer> personCounts = new HashMap<>();
            boolean foundSequence = true;
            innerwhile:
            for (int k = 0; k < sequencePairLength && i + k < allPairs.size(); k++) {
                foundSequence = false;
                Pair game = allPairs.get(i + k);

                Player player = game.getPlayerOne();
                Integer personCount = personCounts.get(player);
                Player player2 = game.getPlayerTwo();
                Integer personCoun2 = personCounts.get(player2);
                if (personCount == null && personCoun2 == null) { //The pair fits into the sequence
                    personCounts.put(player, 1);
                    personCounts.put(player2, 1);
                    foundSequence = true;
                } else {
                    //Find better pair
                    findNewPairWhile:
                    for (int l = i + k + 1; l < allPairs.size(); l++) {
                        Pair candidate = allPairs.get(l);

                        Player playerCandidate1 = candidate.getPlayerOne();
                        if (personCounts.get(playerCandidate1) == null) {
                            Player playerCandidate2 = candidate.getPlayerTwo();
                            if (personCounts.get(playerCandidate2) == null) {
                                personCounts.put(playerCandidate1, 1);
                                personCounts.put(playerCandidate2, 1);
                                Collections.swap(allPairs, i + k, l);
                                foundSequence = true;
                                break findNewPairWhile;
                            }
                        }
                    }
                }
            }

            if (!foundSequence && !unsafe) {
                return new LinkedList<>(allPairs.subList(0, i));
            }
        }

        return allPairs;
    }


    public static ArrayList<Player> generatePersons(double amount) {
        ArrayList<Player> result = new ArrayList<Player>();
        for (int i = 0; i < amount; i++) {
            result.add(new Player(i + ""));
        }

        return result;
    }


    public static List<Game> generateGameList(Queue<Pair> tournamentOfPairs) {
        List<Game> gameList = new LinkedList<>();
        while (tournamentOfPairs.size() > 1) {
            gameList.add(new Game(tournamentOfPairs.poll(), tournamentOfPairs.poll()));
        }
        return gameList;
    }


    public static List<Game> generateTournamentGameList(int amountOfPlayers) {
        ArrayList<Player> players = TournamentGeneratorHelper.generatePersons(amountOfPlayers);
        TableSoccerTournament tableSoccerTournament = new TableSoccerTournament(players);
        Queue<Pair> pairs = tableSoccerTournament.generateAllLegalPairs();
        return TournamentGeneratorHelper.generateGameList(pairs);
    }


    public static List<Game> generateTournamentGameList(int amountOfPlayers, int sequenceLength) {
        ArrayList<Player> players = TournamentGeneratorHelper.generatePersons(amountOfPlayers);
        TableSoccerTournament tableSoccerTournament = new TableSoccerTournament(players);
        Queue<Pair> pairs = tableSoccerTournament.generateAllLegalPairs();
        TournamentGeneratorHelper.modifyTorunamentForSequenceLength((LinkedList) pairs, sequenceLength, true);
        return TournamentGeneratorHelper.generateGameList(pairs);
    }


    public static String generateTournamentGameList(int amountOfPlayers, int sequenceLength, int maxGames) {
        ArrayList<Player> players = TournamentGeneratorHelper.generatePersons(amountOfPlayers);
        TableSoccerTournament tableSoccerTournament = new TableSoccerTournament(players);
        Queue<Pair> pairs = tableSoccerTournament.generateAllLegalPairs();
        Queue<Pair> pairs1 = TournamentGeneratorHelper.modifyTorunamentForSequenceLength(new LinkedList<>(pairs), sequenceLength * 2, false);
        List<Game> games = TournamentGeneratorHelper.generateGameList(pairs1);
        List<Game> games1 = TournamentGeneratorHelper.modifyMaxAmountOfPlays(games, maxGames, sequenceLength, Integer.MAX_VALUE);
        return TournamentGeneratorHelper.generateJson(games1, sequenceLength);
    }


    public static String generateTournamentGameListBrute(int amountOfPlayers, int sequenceLength, int maxGames) {
        ArrayList<Player> players = TournamentGeneratorHelper.generatePersons(amountOfPlayers);
        TableSoccerTournament tableSoccerTournament = new TableSoccerTournament(players);
        Queue<Pair> pairs = tableSoccerTournament.generateAllLegalPairs();

        BruteForcer bruteForcer = new BruteForcer(pairs, sequenceLength);
        LinkedList<Pair> bestCandidate = bruteForcer.getBestCandidate();
        List<Game> games = TournamentGeneratorHelper.generateGameList(bestCandidate);

        return TournamentGeneratorHelper.generateJson(games, sequenceLength);
    }





    public static String generateJson(List<Game> games, int sequenceLength) {
        int sequenceNumber = 1;
        int gameNumber = 1;
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("[");

        for (int i = 0; i < games.size(); i += sequenceLength) {
            if (i > 0) {
                stringBuffer.append(",");
            }

            stringBuffer.append("{\"rundenr\":" + sequenceNumber++ + ",\"kampe\":[");
            for (int k = 0; k < sequenceLength && i + k < games.size(); k++) {
                Game game = games.get(i + k);

                if (k > 0) {
                    stringBuffer.append(",");
                }

                stringBuffer.append("{");

                stringBuffer.append("\"kampnr\":" + gameNumber++ + ",\"par1\":{\"spiller1\":\"");
                stringBuffer.append(game.getTeamOne().getPlayerOne());
                stringBuffer.append("\",\"spiller2\":\"");
                stringBuffer.append(game.getTeamOne().getPlayerTwo());
                stringBuffer.append("\"},\"par2\":{\"spiller1\":\"");
                stringBuffer.append(game.getTeamTwo().getPlayerOne());
                stringBuffer.append("\",\"spiller2\":\"");
                stringBuffer.append(game.getTeamTwo().getPlayerTwo());
                stringBuffer.append("\"}");

                stringBuffer.append("}");
            }
            stringBuffer.append("]}");
        }


        stringBuffer.append("]");

        return stringBuffer.toString();
    }
}
