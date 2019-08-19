package everything;

import everything.ThisOneWillWork;
import org.junit.Assert;
import org.junit.Test;


public class ThisOneWillWorkTest {

    @Test
    public void fourPoneTmaxRounds() throws Exception {
        ThisOneWillWork thisOneWillWork = new ThisOneWillWork(4, 1);
        Assert.assertEquals(3, thisOneWillWork.getTournament().size());
    }

    @Test
    public void fivePoneTmaxRounds() throws Exception {
        ThisOneWillWork thisOneWillWork = new ThisOneWillWork(5, 1);
        Assert.assertEquals(5, thisOneWillWork.getTournament().size());
    }

    @Test
    public void sixPoneTmaxRounds() throws Exception {
        ThisOneWillWork thisOneWillWork = new ThisOneWillWork(6, 1);
        Assert.assertEquals(7, thisOneWillWork.getTournament().size());
    }

    @Test
    public void sevenPoneTmaxRounds() throws Exception {
        ThisOneWillWork thisOneWillWork = new ThisOneWillWork(7, 1);
        Assert.assertEquals(11, thisOneWillWork.getTournament().size());
    }

    @Test
    public void BiggerTestOneTableMaxRounds() throws Exception {

        for (int i = 4; i < 200; i++) {
            ThisOneWillWork thisOneWillWork = new ThisOneWillWork(i, 1);
            int uniquePairs = (i*(i-1))/2;
            int amountOfGames = (uniquePairs - 1) / 2 + 1;;

            System.out.println("Asserting game with: " + i + " players.");
            Assert.assertEquals(amountOfGames, thisOneWillWork.getTournament().size());
        }

    }

}