package ch.hgdev.toposuite.test.calculation;

import junit.framework.Assert;
import junit.framework.TestCase;
import ch.hgdev.toposuite.calculation.Gisement;
import ch.hgdev.toposuite.points.Point;

public class TestGisement extends TestCase {

    public void testCompute() {
        Point p1 = new Point(1, 600.245, 200.729, 0.0, false);
        Point p2 = new Point(2, 634.087, 257.975, 0.0, false);

        Gisement z0 = new Gisement(p1, p2);

        Assert.assertEquals("33.98913148", String.format("%.8f", z0.getGisement()));
        Assert.assertEquals("66.50101864", String.format("%.8f", z0.getHorizDist()));

        p2 = new Point(2, 589.907, 200.729, 0.0, false);
        z0.setOrientation(p2);
        z0.compute();

        Assert.assertEquals("300.00000000", String.format("%.8f", z0.getGisement()));
        Assert.assertEquals("10.33800000", String.format("%.8f", z0.getHorizDist()));

        p2 = new Point(2, 639.056, 200.729, 0.0, false);
        z0.setOrientation(p2);
        z0.compute();

        Assert.assertEquals("100.00000000", String.format("%.8f", z0.getGisement()));
        Assert.assertEquals("38.81100000", String.format("%.8f", z0.getHorizDist()));
    }
}
