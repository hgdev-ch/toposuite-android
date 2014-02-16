package ch.hgdev.toposuite.test.calculation;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import junit.framework.Assert;
import junit.framework.TestCase;
import ch.hgdev.toposuite.calculation.Circle;
import ch.hgdev.toposuite.points.Point;

public class TestCircle extends TestCase {

    public void testCircle() {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.HALF_UP);

        Point p1 = new Point(1, 25.0000, 55.0000, 0.0, true, false);
        Point p2 = new Point(2, 89.1570, 82.4730, 0.0, true, false);
        Point p3 = new Point(5, 113.2040, 37.4110, 0.0, true, false);

        Circle c = new Circle(p1, p2, p3, 42, false);
        c.compute();

        // center
        Assert.assertEquals("68.347", df.format(c.getCenter().getEast()));
        Assert.assertEquals("42.421", df.format(c.getCenter().getNorth()));
        Assert.assertEquals(42, c.getCenter().getNumber());

        // radius
        Assert.assertEquals("45.136", df.format(c.getRadius()));
    }
}
