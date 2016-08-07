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
        testNonStop(8, true);
    }

    @org.junit.Test
    public void getNextGame16() throws Exception {
        testNonStop(16, true);
    }

    @org.junit.Test
    public void getNextGame23() throws Exception {
        testNonStop(23, true);
    }

    @org.junit.Test
    public void getNextGame5() throws Exception {
        testNonStop(5, true);
    }

    @org.junit.Test
    public void getNextGame6() throws Exception {
        testNonStop(6, true);
    }

    @org.junit.Test
    public void getNextGameNonDummy() throws Exception {
        for (int i = 4; i < 10; i++) {
            testNonStop(Math.pow(2, i), true);
        }
    }

    @org.junit.Test
    public void getNextGame() throws Exception {
        for (int i = 4; i < 100; i++) {
            testNonStop(i, true);
//            System.out.println("Doing: " + i);
        }
    }

    //This test wont stop before alle games have been executed
    private void testNonStop(double amount, boolean withoutDummy) {
        ArrayList<Person> players = generatePersons(amount);
        Map<Person, List<Person>> pairsPlayed = new HashMap<>();
        int numberOfDummies = 0;
        int gamesTotal = 0;
        List<Game> gameList = new LinkedList<>();

        TableSoccerTournament tableSoccerTournament = new TableSoccerTournament(players);

        //A safer version of while(true)
        for (int i = 0; i < amount; i++) {

            while (tableSoccerTournament.isRingsHaveMorePairs()) {
//            System.out.println("Doing: " + amount + " Generating: " + i);
                tableSoccerTournament.generateNextGameList(false);
                List<Game> newGamesList = tableSoccerTournament.getGameList();

//            System.out.println("Doing: " + amount + " Validating: " + i);

                if (newGamesList.size() == 0) {
                    break;
                }

                for (Game game : newGamesList) {
                    if (withoutDummy) {
                        addTeam(pairsPlayed, game.getTeamOne().getPersonOne(), game.getTeamOne().getPersonTwo());
                        addTeam(pairsPlayed, game.getTeamTwo().getPersonOne(), game.getTeamTwo().getPersonTwo());
                    } else {
                        Person personOneTeamOne = game.getTeamOne().getPersonOne();
                        Person personTwoTeamOne = game.getTeamOne().getPersonTwo();
                        Person personOneTeamTwo = game.getTeamTwo().getPersonOne();
                        Person personTwoTeamTwo = game.getTeamTwo().getPersonTwo();

                        if (game.getDummyPersons().contains(personOneTeamOne)) {
                            personOneTeamOne = new Person("DUMMY");
                            System.out.println("DUMMY");
                            numberOfDummies++;
                        }
                        if (game.getDummyPersons().contains(personTwoTeamOne)) {
                            personTwoTeamOne = new Person("DUMMY");
                            System.out.println("DUMMY");
                            numberOfDummies++;
                        }
                        if (game.getDummyPersons().contains(personOneTeamTwo)) {
                            personOneTeamTwo = new Person("DUMMY");
                            System.out.println("DUMMY");
                            numberOfDummies++;
                        }
                        if (game.getDummyPersons().contains(personTwoTeamTwo)) {
                            personTwoTeamTwo = new Person("DUMMY");
                            System.out.println("DUMMY");
                            numberOfDummies++;
                        }

                        addTeam(pairsPlayed, personOneTeamOne, personTwoTeamOne);
                        addTeam(pairsPlayed, personOneTeamTwo, personTwoTeamTwo);
                    }
                    gamesTotal++;
                }

                gameList.addAll(newGamesList);
                validateParisWithoutDummyPlayers(pairsPlayed, withoutDummy);
            }
        }

        System.out.println("Did: " + amount + " Games total: " + gamesTotal + " Dummies: " + numberOfDummies);
    }

    private void validateParisWithoutDummyPlayers(Map<Person, List<Person>> pairsPlayed, boolean withoutDummy) {
        Integer gamesPlayed = null;
        for (Person person : pairsPlayed.keySet()) {
            if (!person.getName().equals("DUMMY")) {
                List<Person> partners = pairsPlayed.get(person);

                //Every player played the same amount of games
                if (gamesPlayed == null) {
                    gamesPlayed = partners.size();
                } else {
                    if (!(gamesPlayed.intValue() == partners.size() ||
                            gamesPlayed.intValue() + 1 == partners.size() ||
                            gamesPlayed.intValue() == partners.size() + 1)) {

                        //TODO: TEst 6 fejler her fordi det sidste par mangler en dummy kamp.
                        Assert.assertTrue(gamesPlayed.intValue() == partners.size() ||
                                gamesPlayed.intValue() + 1 == partners.size() ||
                                gamesPlayed.intValue() == partners.size() + 1);
                    }
                }

                //No two same partners
                for (Person partner : partners) {
                    int count = 0;

                    if (!partner.getName().equals("DUMMY")) {
                        for (Person partner1 : partners) {
                            if (partner == partner1) {
                                count++;
                            }

                            if (count > 1) {
                                System.out.println("FAILED!");
                            }

                            Assert.assertFalse(count > 1);
                        }
                    }
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