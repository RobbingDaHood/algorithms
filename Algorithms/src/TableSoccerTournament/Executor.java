package TableSoccerTournament;

import TableSoccerTournament.Models.Game;
import TableSoccerTournament.Models.Pair;
import TableSoccerTournament.Models.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by super on 15/08/2016.
 */
public class Executor {
    public static void main(String[] args) {
        int amountOfPlayers = 0;
        int sequenceLength = 0;

        if (args[0] != null) {
            amountOfPlayers = Integer.valueOf(args[0]);
        }

        if (args[1] != null) {
            sequenceLength = Integer.valueOf(args[1]);
        }

        List<Game> games = TournamentGeneratorHelper.generateTournamentGameList(amountOfPlayers, sequenceLength);

        String result = TournamentGeneratorHelper.generateJson(games, sequenceLength);

        System.out.println(result);

    }
}
