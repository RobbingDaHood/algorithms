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

    public static List<Game> modifyMaxAmountOfPlays(List<Game> games, int maxAmountOfPlays, int sequenceLength, int stopAfterGame) {
        Map<Player, List<Player>> pairsPlayed = new HashMap<>();
        int maxPairsYet = -Integer.MAX_VALUE;
        List<Game> result = new LinkedList<>();

        for (int i = 0; i <= games.size() - sequenceLength && i <= stopAfterGame; i += sequenceLength) {
            List<Game> sequence = new LinkedList<>();
            for (int k = 0; k < sequenceLength && i + k <= games.size(); k++) {
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

            if (maxPairsYet <= maxAmountOfPlays) {
                result.addAll(sequence);
                addGames(pairsPlayed, sequence);
            } else {

                //Add valid games from this sequence
                for (Game game : sequence) {
                    if (maxAmountOfPlays >= pairsPlayed.get(game.getTeamOne().getPlayerOne()).size() + 1) {
                        if (maxAmountOfPlays >= pairsPlayed.get(game.getTeamOne().getPlayerTwo()).size() + 1) {
                            if (maxAmountOfPlays >= pairsPlayed.get(game.getTeamTwo().getPlayerOne()).size() + 1) {
                                if (maxAmountOfPlays >= pairsPlayed.get(game.getTeamTwo().getPlayerTwo()).size() + 1) {
                                    result.add(game);
                                    addTeam(pairsPlayed, game.getTeamOne().getPlayerOne(), game.getTeamOne().getPlayerTwo());
                                    addTeam(pairsPlayed, game.getTeamTwo().getPlayerOne(), game.getTeamTwo().getPlayerTwo());
                                }
                            }
                        }
                    }
                }

                List<Player> playersMissingOneOrMoreGames = new LinkedList<>();
                for (Player player : pairsPlayed.keySet()) {
                    if (pairsPlayed.get(player).size() < maxAmountOfPlays) {
                        playersMissingOneOrMoreGames.add(player);
                    }
                }

                //Try to generate valid pairs
                LinkedList<Pair> validPairs = new LinkedList<>();
                for (Player playersMissingOneOrMoreGame : playersMissingOneOrMoreGames) {
                    if (pairsPlayed.get(playersMissingOneOrMoreGame).size() < maxAmountOfPlays) {
                        for (Player missingOneOrMoreGame : playersMissingOneOrMoreGames) {
                            if (pairsPlayed.get(missingOneOrMoreGame).size() < maxAmountOfPlays) {
                                if (playersMissingOneOrMoreGame != missingOneOrMoreGame && pairsPlayed.get(playersMissingOneOrMoreGame).contains(missingOneOrMoreGame)) {
                                    addTeam(pairsPlayed, missingOneOrMoreGame, playersMissingOneOrMoreGame);
                                    validPairs.add(new Pair(playersMissingOneOrMoreGame, missingOneOrMoreGame));
                                }
                            }
                        }
                    }
                }

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

                //See if there are any players left outside valid pairs
                playersMissingOneOrMoreGames = new LinkedList<>();
                for (Player player : pairsPlayed.keySet()) {
                    if (pairsPlayed.get(player).size() < maxAmountOfPlays) {
                        playersMissingOneOrMoreGames.add(player);
                    }
                }

                if (playersMissingOneOrMoreGames.size() > 0 && validPairs.size() > 0) {
                    for (Pair validPair : new LinkedList<>(validPairs)) {
                        for (Player playersMissingOneOrMoreGame : new LinkedList<>(playersMissingOneOrMoreGames)) {
                            if(isNotSamePairs(validPair, new Pair(playersMissingOneOrMoreGame, playersMissingOneOrMoreGame))) {
                                result.add(new Game(validPair, new Pair(playersMissingOneOrMoreGame, new Player("null"))));
                                validPairs.remove(validPair);
                                playersMissingOneOrMoreGames.remove(playersMissingOneOrMoreGame);
                                break;
                            }
                        }
                    }
                }

                if (playersMissingOneOrMoreGames.size() > 0) {
                    for (Player playersMissingOneOrMoreGame : new LinkedList<>(playersMissingOneOrMoreGames)) {
                        result.add(new Game(new Pair(new Player("null"), new Player("null")), new Pair(playersMissingOneOrMoreGame, new Player("null"))));
                        playersMissingOneOrMoreGames.remove(playersMissingOneOrMoreGame);
                    }

                }

                if (validPairs.size() > 0) {
                    for (Pair validPair : new LinkedList<>(validPairs)) {
                        result.add(new Game(validPair, new Pair(new Player("null"), new Player("null"))));
                        validPairs.remove(validPair);
                    }
                }

                Assert.assertFalse(!validPairs.isEmpty() || !playersMissingOneOrMoreGames.isEmpty());

                break;
            }
        }

        return result;
    }

    public static boolean isNotSamePairs(Pair validPair, Pair pair) {
        return validPair.getPlayerOne() != pair.getPlayerOne() &&
                validPair.getPlayerOne() != pair.getPlayerTwo() &&
                validPair.getPlayerTwo() != pair.getPlayerOne() &&
                validPair.getPlayerTwo() != pair.getPlayerTwo();
    }


    private static void addGames(Map<Player, List<Player>> pairsPlayed, List<Game> games) {
        for (Game game : games) {
            addTeam(pairsPlayed, game.getTeamOne().getPlayerOne(), game.getTeamOne().getPlayerTwo());
            addTeam(pairsPlayed, game.getTeamTwo().getPlayerOne(), game.getTeamTwo().getPlayerTwo());
        }
    }

    public static void addTeam(Map<Player, List<Player>> pairsPlayed, Player playerOne, Player playerTwo) {
        if (pairsPlayed.get(playerOne) == null) {
            LinkedList<Player> players = new LinkedList<>();
            players.add(playerTwo);
            pairsPlayed.put(playerOne, players);
        } else {
            pairsPlayed.get(playerOne).add(playerTwo);
        }

        if (pairsPlayed.get(playerTwo) == null) {
            LinkedList<Player> players = new LinkedList<>();
            players.add(playerOne);
            pairsPlayed.put(playerTwo, players);
        } else {
            pairsPlayed.get(playerTwo).add(playerOne);
        }
    }

    public static void modifyTorunamentForSequenceLength(LinkedList<Pair> allPairs, int sequencePairLength) {
        outerwhile:
        for (int i = 0; i <= allPairs.size() - sequencePairLength; i += sequencePairLength) {
            Map<Player, Integer> personCounts = new HashMap<>();
            innerwhile:
            for (int k = 0; k < sequencePairLength && i + k <= allPairs.size(); k++) {
                Pair game = allPairs.get(i + k);

                Player player = game.getPlayerOne();
                Integer personCount = personCounts.get(player);
                Player player2 = game.getPlayerTwo();
                Integer personCoun2 = personCounts.get(player);
                if (personCount == null && personCoun2 == null) { //The pair fits into the sequence
                    personCounts.put(player, 1);
                    personCounts.put(player2, 1);
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
                                break findNewPairWhile;
                            }
                        }
                    }
                }
            }
        }
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
        Queue<Pair> pairs = tableSoccerTournament.generateTournament();
        return TournamentGeneratorHelper.generateGameList(pairs);
    }


    public static List<Game> generateTournamentGameList(int amountOfPlayers, int sequenceLength) {
        ArrayList<Player> players = TournamentGeneratorHelper.generatePersons(amountOfPlayers);
        TableSoccerTournament tableSoccerTournament = new TableSoccerTournament(players);
        Queue<Pair> pairs = tableSoccerTournament.generateTournament();
        TournamentGeneratorHelper.modifyTorunamentForSequenceLength((LinkedList) pairs, sequenceLength);
        return TournamentGeneratorHelper.generateGameList(pairs);
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

                stringBuffer.append("\"kampnr\":" + gameNumber++ + ",\"par1\":{\"spiller1\":");
                stringBuffer.append(game.getTeamOne().getPlayerOne());
                stringBuffer.append(",\"spiller2\":");
                stringBuffer.append(game.getTeamOne().getPlayerTwo());
                stringBuffer.append("},\"par2\":{\"spiller1\":");
                stringBuffer.append(game.getTeamTwo().getPlayerOne());
                stringBuffer.append(",\"spiller2\":");
                stringBuffer.append(game.getTeamTwo().getPlayerTwo());
                stringBuffer.append("}");

                stringBuffer.append("}");
            }
            stringBuffer.append("]}");
        }


        stringBuffer.append("]");

        return stringBuffer.toString();
    }
}
