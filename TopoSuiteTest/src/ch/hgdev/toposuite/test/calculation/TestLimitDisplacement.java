package ch.hgdev.toposuite.test.calculation;

import junit.framework.Assert;
import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.LimitDisplacement;
import ch.hgdev.toposuite.points.Point;

public class TestLimitDisplacement extends CalculationTest {

    public void testLimitDisplacement1() {
        Point ptA = new Point("1", 96321.1527, 71470.5391, 0.0, true, false);
        Point ptB = new Point("2", 96331.2818, 71467.6509, 0.0, true, false);
        Point ptC = new Point("3", 96334.9793, 71477.2001, 0.0, true, false);
        Point ptD = new Point("4", 96328.0009, 71480.5567, 0.0, true, false);
        double surface = 25.0;

        LimitDisplacement ld = new LimitDisplacement(ptA, ptB, ptC, ptD,
                surface, "5", "6", false);
        try {
            ld.compute();
        } catch (CalculationException e) {
            Assert.fail("The calculation should be possible!");
        }

        Assert.assertEquals("96319.0953",
                this.df4.format(ld.getNewPointX().getEast()));
        Assert.assertEquals("71471.1257",
                this.df4.format(ld.getNewPointX().getNorth()));

        Assert.assertEquals("96326.1508",
                this.df4.format(ld.getNewPointY().getEast()));
        Assert.assertEquals("71481.4466",
                this.df4.format(ld.getNewPointY().getNorth()));

        Assert.assertEquals("2.029", this.df3.format(
                ld.getDistanceToSouthLimitAD()));
        Assert.assertEquals("2.139", this.df3.format(
                ld.getDistanceToWestLimitAX()));
        Assert.assertEquals("2.053", this.df3.format(
                ld.getDistanceToEastLimitDY()));
    }

    public void testLimitDisplacement2() {
        Assert.assertTrue(true);
    }
}