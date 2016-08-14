package TableSoccerTournament.Tests;

import TableSoccerTournament.Models.Game;
import TableSoccerTournament.Models.Pair;
import TableSoccerTournament.Models.Person;
import TableSoccerTournament.TableSoccerTournament;
import TableSoccerTournament.DoubleRing;
import org.junit.Assert;

import java.util.*;

/**
 * Created by super on 07/08/2016.
 */
public class TableSoccerTournamentTest {

    private ArrayList<Person> generatePersons(double amount) {
        ArrayList<Person> result = new ArrayList<Person>();
        for (int i = 0; i < amount; i++) {
            result.add(new Person(i + ""));
        }

        return result;
    }

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

    //This test wont stop before all games have been executed
    private void testGenerateCycleRowTournament(double amount) {
        ArrayList<Person> players = generatePersons(amount);
        Map<Person, List<Person>> pairsPlayed = new HashMap<>();
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
                    addTeam(pairsPlayed, pair.getPersonOne(), pair.getPersonTwo());
                }

                validateCycle(pairsPlayed);
            }

            validateRow(pairsPlayed);

            tableSoccerTournament.generateNewRings();
        }

        hackIT(allPairs, 4);

        validateTotalTournament(pairsPlayed);
        notInSameGameSequence(tableSoccerTournament.generateGameList(new LinkedList<Pair>(allPairs)), amount);

//        System.out.println("Amount of players: " + amount + " resulted in " + tableSoccerTournament.generateGameList(allPairs).size() + " Games.");
    }

    private void hackIT(LinkedList<Pair> allPairs, int sequencePairLength) {

        outerwhile:
        for (int i = 0; i <= allPairs.size() - sequencePairLength; i += sequencePairLength) {
            Map<Person, Integer> personCounts = new HashMap<>();
            for (int k = 0; k < sequencePairLength && i + k <= allPairs.size(); k++) {
                Pair game = allPairs.get(i + k);

                Person person = game.getPersonOne();
                Integer personCount = personCounts.get(person);
                if (personCount == null) {
                    personCounts.put(person, 1);
                } else {
                    //Find better candidate
                    int foundMatch = 0;
                    for (int l = i+k+1; l < allPairs.size() && foundMatch < sequencePairLength - k; l++) {
                        Pair candidate = allPairs.get(l);

                        Person personCandidate1 = candidate.getPersonOne();
                        Integer personCountCandidate = personCounts.get(personCandidate1);
                        if (personCountCandidate == null) {
                            Person personCandidate2 = candidate.getPersonTwo();
                            personCountCandidate = personCounts.get(personCandidate2);
                            if (personCountCandidate == null) {
                                personCounts.put(personCandidate1, 1);
                                personCounts.put(personCandidate2, 1);
                                foundMatch++;
                                Collections.swap(allPairs, i + k, l);
                                k++;
                            }
                        }
                    }
                }

                person = game.getPersonTwo();
                personCount = personCounts.get(person);
                if (personCount == null) {
                    personCounts.put(person, 1);
                } else {
                    //Find better candidate
                    int foundMatch = 0;
                    for (int l = i+k+1; l < allPairs.size() && foundMatch < sequencePairLength - k; l++) {
                        Pair candidate = allPairs.get(l);

                        Person personCandidate1 = candidate.getPersonOne();
                        Integer personCountCandidate = personCounts.get(personCandidate1);
                        if (personCountCandidate == null) {
                            Person personCandidate2 = candidate.getPersonTwo();
                            personCountCandidate = personCounts.get(personCandidate2);
                            if (personCountCandidate == null) {
                                personCounts.put(personCandidate1, 1);
                                personCounts.put(personCandidate2, 1);
                                foundMatch++;
                                Collections.swap(allPairs, i + k, l);
                                k++;
                            }
                        }
                    }
                }

            }
        }
    }

    private void notInSameGameSequence(List<Game> games, double amount) {
        System.out.print("(" + amount + "/" + games.size() + ") ");

        boolean atLeastOnevalid = true;
        int sequenceGameLength = 1;

        while (atLeastOnevalid) {
            atLeastOnevalid = false;

            outerwhile:
            for (int i = 0; i <= games.size() - sequenceGameLength; i += sequenceGameLength) {
                Map<Person, Integer> personCounts = new HashMap<>();
                for (int k = 0; k < sequenceGameLength && i + k < games.size(); k++) {
                    Game game = games.get(i + k);

                    Person person = game.getTeamOne().getPersonOne();
                    Integer personCount = personCounts.get(person);
                    if (personCount == null) {
                        personCounts.put(person, 1);
                    } else {
                        System.out.print(i + ":" + sequenceGameLength + " ");
                        break outerwhile;
                    }

                    person = game.getTeamOne().getPersonTwo();
                    personCount = personCounts.get(person);
                    if (personCount == null) {
                        personCounts.put(person, 1);
                    } else {
                        System.out.print(i + ":" + sequenceGameLength + " ");
                        break outerwhile;
                    }


                    person = game.getTeamTwo().getPersonOne();
                    personCount = personCounts.get(person);
                    if (personCount == null) {
                        personCounts.put(person, 1);
                    } else {
                        System.out.print(i + ":" + sequenceGameLength + " ");
                        break outerwhile;
                    }


                    person = game.getTeamTwo().getPersonTwo();
                    personCount = personCounts.get(person);
                    if (personCount == null) {
                        personCounts.put(person, 1);
                    } else {
                        System.out.print(i + ":" + sequenceGameLength + " ");
                        break outerwhile;
                    }
                }

                atLeastOnevalid = true;
            }

            sequenceGameLength++;
        }

        System.out.println("");
//        System.out.println("GameDistance: " + (sequenceGameLength - 1));
    }


    private void countMaxDistanesInGames(List<Game> games, double amount) {
        Map<Person, Integer> distanceSinceLastGame = new HashMap<>();
        int minimumDistanceInGame = Integer.MAX_VALUE;

        System.out.print("(" + amount + "/" + games.size() + ") ");

        boolean minimumChanged;
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            minimumChanged = false;

            Person person = game.getTeamOne().getPersonOne();
            Integer indexOfLastPlay = distanceSinceLastGame.get(person);
            if (indexOfLastPlay != null && minimumDistanceInGame > i - indexOfLastPlay) {
                minimumDistanceInGame = i - indexOfLastPlay;
                minimumChanged = true;
            }
            distanceSinceLastGame.put(person, i);

            person = game.getTeamOne().getPersonTwo();
            indexOfLastPlay = distanceSinceLastGame.get(person);
            if (indexOfLastPlay != null && minimumDistanceInGame > i - indexOfLastPlay) {
                minimumDistanceInGame = i - indexOfLastPlay;
                minimumChanged = true;
            }
            distanceSinceLastGame.put(person, i);

            person = game.getTeamTwo().getPersonOne();
            indexOfLastPlay = distanceSinceLastGame.get(person);
            if (indexOfLastPlay != null && minimumDistanceInGame > i - indexOfLastPlay) {
                minimumDistanceInGame = i - indexOfLastPlay;
                minimumChanged = true;
            }
            distanceSinceLastGame.put(person, i);

            person = game.getTeamTwo().getPersonTwo();
            indexOfLastPlay = distanceSinceLastGame.get(person);
            if (indexOfLastPlay != null && minimumDistanceInGame > i - indexOfLastPlay) {
                minimumDistanceInGame = i - indexOfLastPlay;
                minimumChanged = true;
            }
            distanceSinceLastGame.put(person, i);

            if (minimumChanged) {
                System.out.print(i + ":" + minimumDistanceInGame + " ");
            }
        }
        System.out.println("");

    }


    private void validateGames(List<Game> games, double amount) {
        Map<Person, Integer> distanceSinceLastGame = new HashMap<>();
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

            Person person = game.getTeamOne().getPersonOne();
            Integer indexOfLastPlay = distanceSinceLastGame.get(person);
            if (indexOfLastPlay != null) {
                Assert.assertFalse("Should be: " + minimumDistanceFromLastGame + " but were: " + (i - indexOfLastPlay) + " at game number: " + i, i - indexOfLastPlay < minimumDistanceFromLastGame);
            }
            distanceSinceLastGame.put(person, i);

            person = game.getTeamOne().getPersonTwo();
            indexOfLastPlay = distanceSinceLastGame.get(person);
            if (indexOfLastPlay != null) {
                Assert.assertFalse("Should be: " + minimumDistanceFromLastGame + " but were: " + (i - indexOfLastPlay) + " at game number: " + i, i - indexOfLastPlay < minimumDistanceFromLastGame);
            }
            distanceSinceLastGame.put(person, i);

            person = game.getTeamTwo().getPersonOne();
            indexOfLastPlay = distanceSinceLastGame.get(person);
            if (indexOfLastPlay != null) {
                Assert.assertFalse("Should be: " + minimumDistanceFromLastGame + " but were: " + (i - indexOfLastPlay) + " at game number: " + i, i - indexOfLastPlay < minimumDistanceFromLastGame);
            }
            distanceSinceLastGame.put(person, i);

            person = game.getTeamTwo().getPersonTwo();
            indexOfLastPlay = distanceSinceLastGame.get(person);
            if (indexOfLastPlay != null) {
                Assert.assertFalse("Should be: " + minimumDistanceFromLastGame + " but were: " + (i - indexOfLastPlay) + " at game number: " + i, i - indexOfLastPlay < minimumDistanceFromLastGame);
            }
            distanceSinceLastGame.put(person, i);
        }

    }

    private void validateRow(Map<Person, List<Person>> pairsPlayed) {
        Set<Person> persons = pairsPlayed.keySet();

        int firstSizeEncountered = 0;

        for (Person person : persons) {
            List<Person> partners = pairsPlayed.get(person);

            if (firstSizeEncountered == 0) {
                firstSizeEncountered = partners.size();
            } else {
                int distanceFromFirstEncounter = firstSizeEncountered - partners.size();
                Assert.assertFalse("The distance in pairs pr. player may a max be -1 to 1 after each row, but were: " + distanceFromFirstEncounter, distanceFromFirstEncounter < -1);
                Assert.assertFalse("The distance in pairs pr. player may a max be -1 to 1 after each row, but were: " + distanceFromFirstEncounter, distanceFromFirstEncounter > 1);
            }
        }
    }


    private void validateCycle(Map<Person, List<Person>> pairsPlayed) {
        Set<Person> persons = pairsPlayed.keySet();

        int firstSizeEncountered = 0;

        for (Person person : persons) {
            List<Person> partners = pairsPlayed.get(person);

            if (firstSizeEncountered == 0) {
                firstSizeEncountered = partners.size();
            } else {
                int distanceFromFirstEncounter = firstSizeEncountered - partners.size();
                Assert.assertFalse("The distance in pairs pr. player may a max be -2 to 2 after each cycle, but were: " + distanceFromFirstEncounter, distanceFromFirstEncounter < -2);
                Assert.assertFalse("The distance in pairs pr. player may a max be -2 to 2 after each cycle, but were: " + distanceFromFirstEncounter, distanceFromFirstEncounter > 2);
            }
        }
    }

    private void validateTotalTournament(Map<Person, List<Person>> pairsPlayed) {
        Set<Person> persons = pairsPlayed.keySet();
        int countPlayersPlayedOneLess = 0;
        for (Person person : persons) {
            List<Person> partners = pairsPlayed.get(person);

            int distanceToTheoreticalMax = persons.size() - partners.size();
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
            for (Person partner : partners) {
                int count = 0;
                for (Person partner1 : partners) {
                    if (partner == partner1) {
                        count++;
                    }

                    Assert.assertFalse(count > 1);
                }
            }
        }
    }

    private void addTeam(Map<Person, List<Person>> pairsPlayed, Person personOne, Person personTwo) {
        if (pairsPlayed.get(personOne) == null) {
            LinkedList<Person> persons = new LinkedList<>();
            persons.add(personTwo);
            pairsPlayed.put(personOne, persons);
        } else {
            pairsPlayed.get(personOne).add(personTwo);
        }

        if (pairsPlayed.get(personTwo) == null) {
            LinkedList<Person> persons = new LinkedList<>();
            persons.add(personOne);
            pairsPlayed.put(personTwo, persons);
        } else {
            pairsPlayed.get(personTwo).add(personOne);
        }

    }

}