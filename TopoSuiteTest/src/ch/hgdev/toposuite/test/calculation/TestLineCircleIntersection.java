package ch.hgdev.toposuite.test.calculation;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import junit.framework.Assert;
import junit.framework.TestCase;
import ch.hgdev.toposuite.calculation.LineCircleIntersection;
import ch.hgdev.toposuite.points.Point;

public class TestLineCircleIntersection extends TestCase {

    private DecimalFormat df1;
    private DecimalFormat df2;
    private DecimalFormat df3;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.df1 = new DecimalFormat("#.#");
        this.df1.setRoundingMode(RoundingMode.HALF_UP);
        this.df2 = new DecimalFormat("#.##");
        this.df2.setRoundingMode(RoundingMode.HALF_UP);
        this.df3 = new DecimalFormat("#.###");
        this.df3.setRoundingMode(RoundingMode.HALF_UP);
    }

    public void testCorrectSolution() {
        Point p1 = new Point(1, 25.0, 55.0, 0.0, false);
        Point p3 = new Point(3, 50.177, 99.941, 0.0, false);
        Point p4 = new Point(4, 67.0, 14.0, 0.0, false);
        Point p5 = new Point(5, 113.204, 37.411, 0.0, false);
        double displacement = 0.0;
        double radius = 87.572;

        LineCircleIntersection lci = new LineCircleIntersection(p1, p3, displacement, p3, radius);
        lci.compute();
        Assert.assertEquals("92.978", this.df3.format(lci.getFirstIntersection().getEast()));
        Assert.assertEquals("176.341", this.df3.format(lci.getFirstIntersection().getNorth()));
        Assert.assertEquals("7.376", this.df3.format(lci.getSecondIntersection().getEast()));
        Assert.assertEquals("23.541", this.df3.format(lci.getSecondIntersection().getNorth()));

        displacement = -0.875;
        lci = new LineCircleIntersection(p1, p3, displacement, p3, radius);
        lci.compute();
        Assert.assertEquals("92.212", this.df3.format(lci.getFirstIntersection().getEast()));
        Assert.assertEquals("176.765", this.df3.format(lci.getFirstIntersection().getNorth()));
        Assert.assertEquals("6.615", this.df3.format(lci.getSecondIntersection().getEast()));
        Assert.assertEquals("23.973", this.df3.format(lci.getSecondIntersection().getNorth()));

        displacement = 10.765;
        lci = new LineCircleIntersection(p1, p3, displacement, p3, radius);
        lci.compute();
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
        lci.compute();
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
        lci.compute();
        Assert.assertEquals("42.19",
                this.df2.format(lci.getFirstIntersection().getEast()));
        Assert.assertEquals("65.223",
                this.df3.format(lci.getFirstIntersection().getNorth()));
        Assert.assertEquals("25.259",
                this.df3.format(lci.getSecondIntersection().getEast()));
        Assert.assertEquals("35.002",
                this.df3.format(lci.getSecondIntersection().getNorth()));
    }
}
