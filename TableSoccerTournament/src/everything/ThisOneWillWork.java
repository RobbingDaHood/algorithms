package everything;

import everything.Models.Game;
import everything.Models.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class ThisOneWillWork {
    private LinkedList<Game> tournament = new LinkedList<>();

    public LinkedList<Game> getTournament() {
        return tournament;
    }

    public ThisOneWillWork(int numberOfPlayers, int numberOfTables) {

        int maxAmountOfPairs = (numberOfPlayers * (numberOfPlayers - 1)) / 2;

        //Create structured list of pairs
        PriorityQueue<Player> playerCounts = new PriorityQueue<Player>((a,b) -> a.getCount() - b.getCount());

        int totalAmountOfPairs = 0;
        HashMap<Player, HashSet<Player>> pairsNotUsed = new HashMap<>(); //If I could use some treemap that autocalculated priority based on sum of player count, that would be ideal :)
        for (int i = 0; i < numberOfPlayers; i++) {
            Player player = new Player(i + "");

            playerCounts.add(player);

            //Should create all pairs
            for (HashSet<Player> otherPlayerPairs : pairsNotUsed.values()) {
                otherPlayerPairs.add(player);
                totalAmountOfPairs++;
            }
            pairsNotUsed.put(player, new HashSet<>());
        }


        //Loop until done
        int count = 0;
        while (count < maxAmountOfPairs*2 && totalAmountOfPairs != 0) { //we cannot have more games than maximum amount of pairs
            Game game = new Game();
            if (findingOnePairOne(playerCounts, pairsNotUsed, game) == null) {
                System.out.println("COULD NOT FIND PAIR ONE!");
                return;
            }
            else if (!anyMorePairs(pairsNotUsed)) {
                tournament.add(game);
                System.out.println("DONE, ONE DUMMY BATTLE!");
                return;
            } else if (findingOnePairTwo(playerCounts, pairsNotUsed, game) == null) {
                System.out.println("COULD NOT FIND PAIR TWO!");
                return;
            } else if (!anyMorePairs(pairsNotUsed)) {
                tournament.add(game);
                System.out.println("DONE, NO DUMMY BATTLES!");
                return;
            } else
                totalAmountOfPairs -= 2;

            if (game.getTeamOne() == null || game.getTeamTwo() == null) {
                System.out.println("GAME MISSING AT LEAST ONE TEAM!");
            }

            if (playerCounts.size() != numberOfPlayers) {
                System.out.println("playerCounts.size() != numberOfPlayers");
            }

            tournament.add(game);
            //List<Integer> counts = playerCounts.stream().map(player -> player.getCount()).collect(Collectors.toList());
            count++;
        }

    }

    private boolean anyMorePairs(HashMap<Player, HashSet<Player>> pairsNotUsed) {
        for (HashSet<Player> pairs: pairsNotUsed.values()) {
            if (pairs.size() > 0)
                return true;
        }
        return false;
    }

    private Pair findingOnePairOne(PriorityQueue<Player> playerCounts, HashMap<Player, HashSet<Player>> pairsNotUsed, Game game) {
        Pair result = null;

        Queue<Player> usedPlayerOnes = new LinkedList<>();
        Queue<Player> usedPlayerTwos = new LinkedList<>();

        while (!playerCounts.isEmpty() && result == null) {
            Player playerOne = playerCounts.poll();
            while (!playerCounts.isEmpty() && result == null) {
                Player playerTwo = playerCounts.poll();
                if (pairsNotUsed.get(playerOne).contains(playerTwo) || pairsNotUsed.get(playerTwo).contains(playerOne)) {
                    //Got a pair Keep player one and two and remove them as candidates for later.
                    pairsNotUsed.get(playerOne).remove(playerTwo);
                    pairsNotUsed.get(playerTwo).remove(playerOne);
                    playerOne.addOneToCount();
                    playerTwo.addOneToCount();
                    playerCounts.add(playerTwo);
                    game.setTeamOne(new Pair(playerOne, playerTwo));
                    result = new Pair(playerOne, playerTwo);
                } else {
                    usedPlayerTwos.add(playerTwo);
                }
            }
            playerCounts.addAll(usedPlayerTwos);
            usedPlayerTwos = new LinkedList<>();
            usedPlayerOnes.add(playerOne);
        }

        playerCounts.addAll(usedPlayerTwos);
        playerCounts.addAll(usedPlayerOnes);
        return result;
    }

    private Pair findingOnePairTwo(PriorityQueue<Player> playerCounts, HashMap<Player, HashSet<Player>> pairsNotUsed, Game game) {
        Pair result = null;

        Queue<Player> usedPlayerOnes = new LinkedList<>();
        Queue<Player> usedPlayerTwos = new LinkedList<>();

        while (!playerCounts.isEmpty() && result == null) {
            Player playerOne = playerCounts.poll();
            if (!game.getTeamOne().getPlayerOne().equals(playerOne) && !game.getTeamOne().getPlayerTwo().equals(playerOne)) {
                while (!playerCounts.isEmpty() && result == null) {
                    Player playerTwo = playerCounts.poll();
                    if ((pairsNotUsed.get(playerOne).contains(playerTwo) || pairsNotUsed.get(playerTwo).contains(playerOne)) &&
                            !game.getTeamOne().getPlayerOne().equals(playerTwo) && !game.getTeamOne().getPlayerTwo().equals(playerTwo)) {
                        //Got a pair Keep player one and two and remove them as candidates for later.
                        pairsNotUsed.get(playerOne).remove(playerTwo);
                        pairsNotUsed.get(playerTwo).remove(playerOne);
                        playerOne.addOneToCount();
                        playerTwo.addOneToCount();
                        playerCounts.add(playerTwo);
                        game.setTeamTwo(new Pair(playerOne, playerTwo));
                        result = new Pair(playerOne, playerTwo);
                    } else {
                        usedPlayerTwos.add(playerTwo);
                    }
                }
            }
            playerCounts.addAll(usedPlayerTwos);
            usedPlayerTwos = new LinkedList<>();
            usedPlayerOnes.add(playerOne);
        }

        playerCounts.addAll(usedPlayerTwos);
        playerCounts.addAll(usedPlayerOnes);
        return result;
    }

    public class Player extends everything.Models.Player {
        private int count;

        public Player(String name) {
            super(name);
            this.count = 0;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void addOneToCount() { this.count++; }

        public String toString() {
            return this.getName();
        }

        public boolean equals(Object o) {
            return o instanceof Player && ((Player) o).getName() == this.getName();
        }
    }
}
