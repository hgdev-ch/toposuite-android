package ch.hgdev.toposuite.test.calculation;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.Circle;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.test.testutils.CalculationTestRunner;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class CircleTest extends CalculationTestRunner {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void simple() {
        Point p1 = new Point("1", 25.0000, 55.0000, 0.0, true, false);
        Point p2 = new Point("2", 89.1570, 82.4730, 0.0, true, false);
        Point p3 = new Point("5", 113.2040, 37.4110, 0.0, true, false);

        Circle c = new Circle(p1, p2, p3, "42", false);

        try {
            c.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }
        // center
        Assert.assertEquals("68.347", this.df3.format(c.getCenter().getEast()));
        Assert.assertEquals("42.421", this.df3.format(c.getCenter().getNorth()));
        Assert.assertEquals("42", c.getCenter().getNumber());

        // radius
        Assert.assertEquals("45.136", this.df3.format(c.getRadius()));
    }
}
