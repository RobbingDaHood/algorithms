package TableSoccerTournament.Tests;

import TableSoccerTournament.Models.Game;
import TableSoccerTournament.Models.Pair;
import TableSoccerTournament.Models.Person;
import TableSoccerTournament.TableSoccerTournament;
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
    public void getNextGameEveryPairRow17() throws Exception {
        testGenerateCycleRowTournament(17);
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

        validateTotalTournament(pairsPlayed);

//        System.out.println("Amount of players: " + amount + " resulted in " + tableSoccerTournament.generateGameList(allPairs).size() + " Games.");
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
                Assert.assertFalse("Max amount of pairs a player can be in is #Persons - 1, but were: " + distanceFromFirstEncounter, distanceFromFirstEncounter < -1);
                Assert.assertFalse("Max amount of pairs a player can be in is #Persons - 1, but were: " + distanceFromFirstEncounter, distanceFromFirstEncounter > 1);
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
                Assert.assertFalse("Max amount of pairs a player can be in is #Persons - 1, but were: " + distanceFromFirstEncounter, distanceFromFirstEncounter < -2);
                Assert.assertFalse("Max amount of pairs a player can be in is #Persons - 1, but were: " + distanceFromFirstEncounter, distanceFromFirstEncounter > 2);
            }
        }
    }

    private void validateTotalTournament(Map<Person, List<Person>> pairsPlayed) {
        Set<Person> persons = pairsPlayed.keySet();
        int countPlayersPlayedOneLess = 0;
        for (Person person : persons) {
            List<Person> partners = pairsPlayed.get(person);

            int i = persons.size() - partners.size();
            if (i == 2) {
                //Sometimes the last pair does not have a partner, then up to two players can have played #Persons - 2
                //pairs.
                countPlayersPlayedOneLess++;
                Assert.assertTrue(countPlayersPlayedOneLess < 3);
            } else {
                //Max amount of pairs a player can be in is #Persons - 1
                Assert.assertTrue(i == 1);
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