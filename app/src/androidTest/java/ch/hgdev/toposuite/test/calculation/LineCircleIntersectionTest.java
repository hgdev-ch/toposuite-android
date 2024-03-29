package ch.hgdev.toposuite.test.calculation;

import androidx.test.filters.SmallTest;
import androidx.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.LineCircleIntersection;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.test.testutils.CalculationTestRunner;
import ch.hgdev.toposuite.utils.MathUtils;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class LineCircleIntersectionTest extends CalculationTestRunner {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void simple() {
        Point p1 = new Point("1", 25.0, 55.0, MathUtils.IGNORE_DOUBLE, true, false);
        Point p3 = new Point("3", 50.177, 99.941, MathUtils.IGNORE_DOUBLE, true, false);
        double displacement = 0.0;
        double radius = 87.572;

        LineCircleIntersection lci = new LineCircleIntersection(p1, p3, displacement, p3, radius);

        try {
            lci.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("92.978", this.df3.format(lci.getFirstIntersection().getEast()));
        Assert.assertEquals("176.341", this.df3.format(lci.getFirstIntersection().getNorth()));
        Assert.assertEquals("7.376", this.df3.format(lci.getSecondIntersection().getEast()));
        Assert.assertEquals("23.541", this.df3.format(lci.getSecondIntersection().getNorth()));

        displacement = -0.875;
        lci = new LineCircleIntersection(p1, p3, displacement, p3, radius);

        try {
            lci.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("92.212", this.df3.format(lci.getFirstIntersection().getEast()));
        Assert.assertEquals("176.765", this.df3.format(lci.getFirstIntersection().getNorth()));
        Assert.assertEquals("6.615", this.df3.format(lci.getSecondIntersection().getEast()));
        Assert.assertEquals("23.973", this.df3.format(lci.getSecondIntersection().getNorth()));

        displacement = 10.765;

        lci = new LineCircleIntersection(p1, p3, displacement, p3, radius);
        try {
            lci.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("102.045",
                this.df3.format(lci.getFirstIntersection().getEast()));
        Assert.assertEquals("170.5",
                this.df1.format(lci.getFirstIntersection().getNorth()));
        Assert.assertEquals("17.092",
                this.df3.format(lci.getSecondIntersection().getEast()));
        Assert.assertEquals("18.859",
                this.df3.format(lci.getSecondIntersection().getNorth()));

        displacement = 24.875;
        lci = new LineCircleIntersection(p1, p3, displacement, p3, radius);

        try {
            lci.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("112.916",
                this.df3.format(lci.getFirstIntersection().getEast()));
        Assert.assertEquals("161.036",
                this.df3.format(lci.getFirstIntersection().getNorth()));
        Assert.assertEquals("30.841",
                this.df3.format(lci.getSecondIntersection().getEast()));
        Assert.assertEquals("14.53",
                this.df2.format(lci.getSecondIntersection().getNorth()));

        displacement = 10.0;
        radius = 20.0;
        lci = new LineCircleIntersection(p1, p3, displacement, p1, radius);

        try {
            lci.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("42.19",
                this.df2.format(lci.getFirstIntersection().getEast()));
        Assert.assertEquals("65.223",
                this.df3.format(lci.getFirstIntersection().getNorth()));
        Assert.assertEquals("25.259",
                this.df3.format(lci.getSecondIntersection().getEast()));
        Assert.assertEquals("35.002",
                this.df3.format(lci.getSecondIntersection().getNorth()));
    }

    /**
     * Test for bug report #434
     */
    @Test
    public void issue434() {
        Point p1 = new Point("1", 600, 200, MathUtils.IGNORE_DOUBLE, true, false);
        Point p4 = new Point("4", 638.9498, 198.0212, MathUtils.IGNORE_DOUBLE, true, false);
        Point p5 = new Point("5", 604, 203.8019, MathUtils.IGNORE_DOUBLE, true, false);
        double displacement = -4.0;
        double radius = 11.0;

        LineCircleIntersection lci = new LineCircleIntersection(p1, p4, displacement, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, p5, radius, false);

        try {
            lci.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("614.986", this.df3.format(lci.getFirstIntersection().getEast()));
        Assert.assertEquals("203.244", this.df3.format(lci.getFirstIntersection().getNorth()));
        Assert.assertEquals("593.0142", this.df4.format(lci.getSecondIntersection().getEast()));
        Assert.assertEquals("204.3601", this.df4.format(lci.getSecondIntersection().getNorth()));
    }

    /**
     * Test for bug report #476
     */
    @Test
    public void issue476() {
        Point pA = new Point("1", 14.4172, 7.8539, MathUtils.IGNORE_DOUBLE, true, false);
        Point pB = new Point("4", 19.627, 12.659, MathUtils.IGNORE_DOUBLE, true, false);
        double distToPtL = 1.978;
        double radius = 3.9317;

        LineCircleIntersection lci = new LineCircleIntersection(pA, pB, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, distToPtL, pA, radius, false);

        try {
            lci.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("13.5675", this.df4.format(lci.getSecondIntersection().getEast()));
        Assert.assertEquals("11.6927", this.df4.format(lci.getSecondIntersection().getNorth()));
        Assert.assertEquals("18.1749", this.df4.format(lci.getFirstIntersection().getEast()));
        Assert.assertEquals("6.6972", this.df4.format(lci.getFirstIntersection().getNorth()));
    }

    /**
     * Test for bug report #622
     */
    @Test
    public void issue622() {
        Point p1 = new Point("1", 600.0, 200.0, MathUtils.IGNORE_DOUBLE, true, false);
        Point p2 = new Point("2", 630.0, 225.0, MathUtils.IGNORE_DOUBLE, true, false);
        double distance = 5.0;
        double radius = 10.0;

        LineCircleIntersection lci = new LineCircleIntersection(p1, p2, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, distance, p1, radius, false);

        try {
            lci.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("598.297", this.df3.format(lci.getSecondIntersection().getEast()));
        Assert.assertEquals("209.854", this.df3.format(lci.getSecondIntersection().getNorth()));
        Assert.assertEquals("609.385", this.df3.format(lci.getFirstIntersection().getEast()));
        Assert.assertEquals("196.548", this.df3.format(lci.getFirstIntersection().getNorth()));
    }
}
