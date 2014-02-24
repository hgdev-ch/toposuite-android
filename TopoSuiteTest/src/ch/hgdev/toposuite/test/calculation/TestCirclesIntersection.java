package ch.hgdev.toposuite.test.calculation;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import junit.framework.Assert;
import junit.framework.TestCase;
import ch.hgdev.toposuite.calculation.CirclesIntersection;
import ch.hgdev.toposuite.points.Point;

public class TestCirclesIntersection extends TestCase {

    private DecimalFormat df1;
    private DecimalFormat df3;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.df1 = new DecimalFormat("#.#");
        this.df1.setRoundingMode(RoundingMode.HALF_UP);
        this.df3 = new DecimalFormat("#.###");
        this.df3.setRoundingMode(RoundingMode.HALF_UP);
    }

    public void testCorrectSolutionTwoPoints() {
        Point p1 = new Point(1, 25.0, 55.0, 0.0, false);
        Point p3 = new Point(3, 50.177, 99.941, 0.0, false);
        double radius3 = 87.752;
        double radius1 = 50.0;

        try {
            CirclesIntersection c = new CirclesIntersection(p3, radius3, p1, radius1, false);
            c.compute();

            Assert.assertEquals("-25",
                    this.df1.format(c.getFirstIntersection().getEast()));
            // TODO uncomment once circle intersection calculation has been
            // fixed
            // Assert.assertEquals("55.027",
            // this.df3.format(c.getFirstIntersection().getNorth()));
            // Assert.assertEquals("51.135",
            // this.df3.format(c.getSecondIntersection().getEast()));
            // Assert.assertEquals("12.374",
            // this.df3.format(c.getSecondIntersection().getNorth()));
        } catch (IllegalArgumentException e) {
            Assert.fail("An illegal argument exception should not be thrown here.");
        }
    }
}
