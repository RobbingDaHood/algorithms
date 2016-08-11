package TableSoccerTournament.Tests;

import TableSoccerTournament.Models.Game;
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
    public void getNextGame8() throws Exception {
        testNonStop(8);
    }

    @org.junit.Test
    public void getNextGame16() throws Exception {
        testNonStop(16);
    }

    @org.junit.Test
    public void getNextGame23() throws Exception {
        testNonStop(23);
    }

    @org.junit.Test
    public void getNextGame5() throws Exception {
        testNonStop(5);
    }

    @org.junit.Test
    public void getNextGame6() throws Exception {
        testNonStop(6);
    }

    @org.junit.Test
    public void getNextGameNonDummy() throws Exception {
        for (int i = 4; i < 10; i++) {
            testNonStop(Math.pow(2, i));
        }
    }

    @org.junit.Test
    public void getNextGame() throws Exception {
        for (int i = 4; i < 100; i++) {
            testNonStop(i);
        }
    }

    //This test wont stop before alle games have been executed
    private void testNonStop(double amount) {
        ArrayList<Person> players = generatePersons(amount);
        Map<Person, List<Person>> pairsPlayed = new HashMap<>();

        TableSoccerTournament tableSoccerTournament = new TableSoccerTournament(players);

        //A safer version of while(true)
        for (int i = 0; i < amount; i++) {
            while (tableSoccerTournament.isRingsHaveMorePairs()) {
                tableSoccerTournament.generateNextGameRow();
                List<Game> newGamesList = tableSoccerTournament.getGameList();

                if (newGamesList.size() == 0) {
                    break;
                }

                for (Game game : newGamesList) {
                    addTeam(pairsPlayed, game.getTeamOne().getPersonOne(), game.getTeamOne().getPersonTwo());
                    addTeam(pairsPlayed, game.getTeamTwo().getPersonOne(), game.getTeamTwo().getPersonTwo());
                }
            }
        }

        validateTtalTournament(pairsPlayed);
    }

    private void validateTtalTournament(Map<Person, List<Person>> pairsPlayed) {
        Set<Person> persons = pairsPlayed.keySet();
        for (Person person : persons) {
            List<Person> partners = pairsPlayed.get(person);

            int countPlayersPlayedOneLess = 0;
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