package ch.hgdev.toposuite.test.calculation;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.LinesIntersection;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.test.testutils.CalculationTestRunner;
import ch.hgdev.toposuite.utils.MathUtils;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class LinesIntersectionTest extends CalculationTestRunner {
    private Point p1;
    private Point p3;
    private Point p4;
    private Point p5;
    private Point p10;
    private Point p20;
    private Point p30;
    private Point p40;

    @Before
    public void setUp() {
        super.setUp();

        this.p1 = new Point("1", 25.0, 55.0, 0.0, true, false);
        this.p3 = new Point("3", 50.177, 99.941, 0.0, true, false);
        this.p4 = new Point("4", 67.0, 14.0, 0.0, true, false);
        this.p5 = new Point("5", 113.204, 37.411, 0.0, true, false);

        this.p10 = new Point("10", 43.5816, 144.4225, 0.0, true, false);
        this.p20 = new Point("20", 357.7832, 48.6002, 0.0, true, false);
        this.p30 = new Point("30", 140.1251, 249.2679, 0.0, true, false);
        this.p40 = new Point("40", -29.2174, 39.2745, 0.0, true, false);
    }

    @Test
    public void simple1() {
        LinesIntersection li = new LinesIntersection(
                this.p1, 50.876, 0.0, MathUtils.IGNORE_DOUBLE, this.p3, 350.35,
                0.0, MathUtils.IGNORE_DOUBLE, "42", false);
        try {
            li.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("60.484", this.df3.format(
                li.getIntersectionPoint().getEast()));
        Assert.assertEquals("89.520", this.df3.format(
                li.getIntersectionPoint().getNorth()));
    }

    @Test
    public void simple2() {
        LinesIntersection li = new LinesIntersection(
                this.p1, 50.876, 0.763, MathUtils.IGNORE_DOUBLE, this.p3, 350.35,
                21.87, MathUtils.IGNORE_DOUBLE, "42", false);
        try {
            li.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("76.697", this.df3.format(
                li.getIntersectionPoint().getEast()));
        Assert.assertEquals("104.229", this.df3.format(
                li.getIntersectionPoint().getNorth()));
    }

    @Test
    public void simple3() {
        LinesIntersection li = new LinesIntersection(
                this.p1, 50.876, -0.763, MathUtils.IGNORE_DOUBLE, this.p3, 350.35,
                -21.87, MathUtils.IGNORE_DOUBLE, "42", false);
        try {
            li.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("44.271", this.df3.format(
                li.getIntersectionPoint().getEast()));
        Assert.assertEquals("74.812", this.df3.format(
                li.getIntersectionPoint().getNorth()));
    }

    @Test
    public void simple4() {
        LinesIntersection li = new LinesIntersection(
                this.p1, 50.876, 0.763, MathUtils.IGNORE_DOUBLE, this.p3, 350.35,
                -21.87, MathUtils.IGNORE_DOUBLE, "42", false);
        try {
            li.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("45.344", this.df3.format(
                li.getIntersectionPoint().getEast()));
        Assert.assertEquals("73.727", this.df3.format(
                li.getIntersectionPoint().getNorth()));
    }

    @Test
    public void simple5() {
        LinesIntersection li = new LinesIntersection(
                this.p1, 50.876, -0.763, MathUtils.IGNORE_DOUBLE, this.p3, 350.35,
                21.87, MathUtils.IGNORE_DOUBLE, "42", false);
        try {
            li.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("75.623", this.df3.format(
                li.getIntersectionPoint().getEast()));
        Assert.assertEquals("105.314", this.df3.format(
                li.getIntersectionPoint().getNorth()));
    }

    @Test
    public void simple6() {
        LinesIntersection li = new LinesIntersection(
                this.p1, this.p4, -0.763, MathUtils.IGNORE_DOUBLE, this.p3, 250.35,
                21.87, MathUtils.IGNORE_DOUBLE, "42", false);
        try {
            li.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("-0.306", this.df3.format(
                li.getIntersectionPoint().getEast()));
        Assert.assertEquals("80.770", this.df3.format(
                li.getIntersectionPoint().getNorth()));
    }

    @Test
    public void simple7() {
        LinesIntersection li = new LinesIntersection(
                this.p1, this.p5, 0.0, MathUtils.IGNORE_DOUBLE, this.p3, this.p4,
                0.0, MathUtils.IGNORE_DOUBLE, "42", false);
        try {
            li.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("60.354", this.df3.format(
                li.getIntersectionPoint().getEast()));
        Assert.assertEquals("47.950", this.df3.format(
                li.getIntersectionPoint().getNorth()));
    }

    @Test
    public void simple8() {
        LinesIntersection li = new LinesIntersection(
                this.p1, this.p5, -0.65, MathUtils.IGNORE_DOUBLE, this.p3, this.p4,
                -13.872, MathUtils.IGNORE_DOUBLE, "42", false);
        try {
            li.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("74.929", this.df3.format(
                li.getIntersectionPoint().getEast()));
        Assert.assertEquals("45.706", this.df3.format(
                li.getIntersectionPoint().getNorth()));
    }

    @Test
    public void simple9() {
        LinesIntersection li = new LinesIntersection(
                this.p10, this.p20, 0.0, -21.954, this.p30,
                this.p40, 0.0, -1.569, "42", false);
        try {
            li.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("70.373", this.df3.format(
                li.getIntersectionPoint().getEast()));
        Assert.assertEquals("307.533", this.df3.format(
                li.getIntersectionPoint().getNorth()));
    }

    @Test
    public void simple10() {
        LinesIntersection li = new LinesIntersection(
                this.p10, this.p20, 0.0, 1.697, this.p30,
                this.p40, 0.0, MathUtils.IGNORE_DOUBLE, "42", false);
        try {
            li.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("39.140", this.df3.format(
                li.getIntersectionPoint().getEast()));
        Assert.assertEquals("124.041", this.df3.format(
                li.getIntersectionPoint().getNorth()));
    }

    @Test
    public void simple11() {
        Point localP1 = new Point("1", 600, 200, 0.0, true, false);
        Point localP2 = new Point("2", 620, 215, 0.0, true, false);
        Point localP3 = new Point("3", 610, 185, 0.0, true, false);
        Point localP104 = new Point("104", 635, 180, 0.0, true, false);

        LinesIntersection li = new LinesIntersection(
                localP1, localP2, 0, 5.0, localP3, localP104, -5.0,
                MathUtils.IGNORE_DOUBLE, "42", false);
        try {
            li.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("614.324", this.df3.format(
                li.getIntersectionPoint().getEast()));
        Assert.assertEquals("189.234", this.df3.format(
                li.getIntersectionPoint().getNorth()));
    }
}
