package ch.hgdev.toposuite.test.calculation;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import junit.framework.Assert;
import junit.framework.TestCase;
import ch.hgdev.toposuite.calculation.LinesIntersection;
import ch.hgdev.toposuite.points.Point;

public class TestLinesIntersection extends TestCase {
    private Point         p1;
    private Point         p3;
    private Point         p4;
    private Point         p5;
    private Point         p10;
    private Point         p20;
    private Point         p30;
    private Point         p40;

    private DecimalFormat df;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.p1 = new Point(1, 25.0, 55.0, 0.0, true, false);
        this.p3 = new Point(3, 50.177, 99.941, 0.0, true, false);
        this.p4 = new Point(4, 67.0, 14.0, 0.0, true, false);
        this.p5 = new Point(5, 113.204, 37.411, 0.0, true, false);

        this.p10 = new Point(10, 43.5816, 144.4225, 0.0, true, false);
        this.p20 = new Point(20, 357.7832, 48.6002, 0.0, true, false);
        this.p30 = new Point(30, 140.1251, 249.2679, 0.0, true, false);
        this.p40 = new Point(40, -29.2174, 39.2745, 0.0, true, false);

        this.df = new DecimalFormat("#.###");
        this.df.setRoundingMode(RoundingMode.HALF_UP);
    }

    public void testCompute1() {
        LinesIntersection li = new LinesIntersection(
                this.p1, 50.876, 0.0, 0.0, this.p3, 350.35,
                0.0, 0.0, 42, false);
        li.compute();

        Assert.assertEquals("60.484", this.df.format(
                li.getIntersectionPoint().getEast()));
        Assert.assertEquals("89.52", this.df.format(
                li.getIntersectionPoint().getNorth()));
    }

    public void testCompute2() {
        LinesIntersection li = new LinesIntersection(
                this.p1, 50.876, 0.763, 0.0, this.p3, 350.35,
                21.87, 0.0, 42, false);
        li.compute();

        Assert.assertEquals("76.697", this.df.format(
                li.getIntersectionPoint().getEast()));
        Assert.assertEquals("104.229", this.df.format(
                li.getIntersectionPoint().getNorth()));
    }

    public void testCompute3() {
        LinesIntersection li = new LinesIntersection(
                this.p1, 50.876, -0.763, 0.0, this.p3, 350.35,
                -21.87, 0.0, 42, false);
        li.compute();

        Assert.assertEquals("44.271", this.df.format(
                li.getIntersectionPoint().getEast()));
        Assert.assertEquals("74.812", this.df.format(
                li.getIntersectionPoint().getNorth()));
    }

    public void testCompute4() {
        LinesIntersection li = new LinesIntersection(
                this.p1, 50.876, 0.763, 0.0, this.p3, 350.35,
                -21.87, 0.0, 42, false);
        li.compute();

        Assert.assertEquals("45.344", this.df.format(
                li.getIntersectionPoint().getEast()));
        Assert.assertEquals("73.727", this.df.format(
                li.getIntersectionPoint().getNorth()));
    }

    public void testCompute5() {
        LinesIntersection li = new LinesIntersection(
                this.p1, 50.876, -0.763, 0.0, this.p3, 350.35,
                21.87, 0.0, 42, false);
        li.compute();

        Assert.assertEquals("75.623", this.df.format(
                li.getIntersectionPoint().getEast()));
        Assert.assertEquals("105.314", this.df.format(
                li.getIntersectionPoint().getNorth()));
    }

    public void testCompute6() {
        LinesIntersection li = new LinesIntersection(
                this.p1, this.p4, -0.763, 0.0, this.p3, 250.35,
                21.87, 0.0, 42, false);
        li.compute();

        Assert.assertEquals("-0.306", this.df.format(
                li.getIntersectionPoint().getEast()));
        Assert.assertEquals("80.77", this.df.format(
                li.getIntersectionPoint().getNorth()));
    }

    public void testCompute7() {
        LinesIntersection li = new LinesIntersection(
                this.p1, this.p5, 0.0, 0.0, this.p3, this.p4,
                0.0, 0.0, 42, false);
        li.compute();

        Assert.assertEquals("60.354", this.df.format(
                li.getIntersectionPoint().getEast()));
        Assert.assertEquals("47.95", this.df.format(
                li.getIntersectionPoint().getNorth()));
    }

    public void testCompute8() {
        LinesIntersection li = new LinesIntersection(
                this.p1, this.p5, -0.65, 0.0, this.p3, this.p4,
                -13.872, 0.0, 42, false);
        li.compute();

        Assert.assertEquals("74.929", this.df.format(
                li.getIntersectionPoint().getEast()));
        Assert.assertEquals("45.706", this.df.format(
                li.getIntersectionPoint().getNorth()));
    }

    public void testCompute9() {
        LinesIntersection li = new LinesIntersection(
                this.p10, this.p20, 0.0, -21.954, this.p30,
                this.p40, 0.0, -1.569, 42, false);
        li.compute();

        /*Assert.assertEquals("69.386", this.df.format(
                li.getIntersectionPoint().getEast()));*/
        Assert.assertEquals("70.373", this.df.format(
                li.getIntersectionPoint().getEast()));
        /*Assert.assertEquals("304.297", this.df.format(
                li.getIntersectionPoint().getNorth()));*/
        Assert.assertEquals("307.533", this.df.format(
                li.getIntersectionPoint().getNorth()));
    }
}