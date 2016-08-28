package TableSoccerTournament;


import TableSoccerTournament.Models.Game;
import TableSoccerTournament.Models.Pair;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by super on 15/08/2016.
 */
public class Executor {
    public static void main(String[] args) {
        int amountOfPlayers = 0;
        int sequenceLength = 0;
        int maxGames = 0;
        String result = "";

        if (args.length < 2) {
            printError();
            return;
        }

        if (args[0] != null) {
            amountOfPlayers = Integer.valueOf(args[0]);
        } else {
            printError();
            return;
        }

        if (args[1] != null) {
            sequenceLength = Integer.valueOf(args[1]);
        } else {
            printError();
            return;
        }

        if (sequenceLength != 1 && sequenceLength != 2 && sequenceLength != 3) {
            System.out.println("Right now it only supports 1, 2 or 3 tables.");
            return;
        }

        if (args.length >= 3 && args[2] != null) {
//            maxGames = Integer.valueOf(args[2]);
//            result = TournamentGeneratorHelper.generateTournamentGameListBrute(amountOfPlayers, sequenceLength, maxGames);

            System.out.println("Right now it only support a full turnament, not stopping in the middle.");


        } else {
            SelectivePair selectivePair = new SelectivePair(amountOfPlayers, sequenceLength * 2);
            List<Pair> bestCandidate = selectivePair.getBestCandidate();
            List<Game> games = TournamentGeneratorHelper.generateGameList(new LinkedList<>(bestCandidate));
            result = TournamentGeneratorHelper.generateJson(games, sequenceLength);
        }

        System.out.println(result);
    }

    private static void printError() {
        System.out.println("YOU DID WRONG, YOU NOT SMART!");
        System.out.println("amount of players, amount of tables, optional: Max amount of plays pr. player");
    }
}
