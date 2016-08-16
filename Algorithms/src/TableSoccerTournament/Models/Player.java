package TableSoccerTournament.Models;

/**
 * Created by super on 07/08/2016.
 */
public class Player {
    private String name;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public boolean equals(Object o) {
        return o instanceof Player && o.toString().equals(name);
    }
}
