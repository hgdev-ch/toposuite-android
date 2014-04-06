package ch.hgdev.toposuite.test.calculation;

import junit.framework.Assert;
import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.CirclesIntersection;
import ch.hgdev.toposuite.points.Point;

public class TestCirclesIntersection extends CalculationTest {

    public void testCorrectSolutionTwoPoints() {
        Point p1 = new Point("1", 25.0, 55.0, 0.0, false);
        Point p3 = new Point("3", 50.177, 99.941, 0.0, false);
        double radius3 = 87.752;
        double radius1 = 50.0;

        try {
            CirclesIntersection c = new CirclesIntersection(p3, radius3, p1, radius1, false);
            c.compute();

            Assert.assertEquals("-24.999",
                    this.df3.format(c.getFirstIntersection().getEast()));
            Assert.assertEquals("54.675",
                    this.df3.format(c.getFirstIntersection().getNorth()));
            Assert.assertEquals("50.835",
                    this.df3.format(c.getSecondIntersection().getEast()));
            Assert.assertEquals("12.191",
                    this.df3.format(c.getSecondIntersection().getNorth()));
        } catch (IllegalArgumentException e) {
            Assert.fail("An illegal argument exception should not be thrown here.");
        } catch (CalculationException e) {
            Assert.fail("The calculation should be possible!");
        }
    }
}
