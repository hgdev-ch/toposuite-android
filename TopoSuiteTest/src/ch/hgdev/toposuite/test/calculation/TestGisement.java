package ch.hgdev.toposuite.test.calculation;

import junit.framework.Assert;
import junit.framework.TestCase;
import ch.hgdev.toposuite.calculation.Gisement;
import ch.hgdev.toposuite.points.Point;

public class TestGisement extends TestCase {

    public void testCompute() {
        Point p1 = new Point(1, 600.245, 200.729, 0.0, true);
        Point p2 = new Point(2, 634.087, 257.975, 0.0, true);

        // Test 1 - just a useless comment to make this test more readable
        Gisement g = new Gisement(p1, p2);

        Assert.assertEquals("33.98913148", String.format("%.8f", g.getGisement()));
        Assert.assertEquals("66.50101864", String.format("%.8f", g.getHorizDist()));

        // Test 2
        p2 = new Point(2, 683.064, 194.975, 0.0, true);
        g.setOrientation(p2);

        Assert.assertEquals("104.41593500", String.format("%.8f", g.getGisement()));
        Assert.assertEquals("83.01864415", String.format("%.8f", g.getHorizDist()));

        // Test 3
        p2 = new Point(2, 593.075, 293.875, 0.0, true);
        g.setOrientation(p2);

        Assert.assertEquals("395.10920466", String.format("%.8f", g.getGisement()));
        Assert.assertEquals("93.42155113", String.format("%.8f", g.getHorizDist()));

        // Test 4
        p2 = new Point(2, 528.964, 139.074, 0.0, true);
        g.setOrientation(p2);

        Assert.assertEquals("254.60178203", String.format("%.8f", g.getGisement()));
        Assert.assertEquals("94.24606085", String.format("%.8f", g.getHorizDist()));

        // Test 5
        p2 = new Point(2, 600.245, 246.986, 0.0, true);
        g.setOrientation(p2);

        Assert.assertEquals("0.00000000", String.format("%.8f", g.getGisement()));
        Assert.assertEquals("46.25700000", String.format("%.8f", g.getHorizDist()));

        // Test 6
        p2 = new Point(2, 600.245, 176.086, 0.0, true);
        g.setOrientation(p2);

        Assert.assertEquals("200.00000000", String.format("%.8f", g.getGisement()));
        Assert.assertEquals("24.64300000", String.format("%.8f", g.getHorizDist()));

        // Test 7
        p2 = new Point(2, 589.907, 200.729, 0.0, true);
        g.setOrientation(p2);

        Assert.assertEquals("300.00000000", String.format("%.8f", g.getGisement()));
        Assert.assertEquals("10.33800000", String.format("%.8f", g.getHorizDist()));

        // Test 8
        p2 = new Point(2, 639.056, 200.729, 0.0, true);
        g.setOrientation(p2);

        Assert.assertEquals("100.00000000", String.format("%.8f", g.getGisement()));
        Assert.assertEquals("38.81100000", String.format("%.8f", g.getHorizDist()));
    }
}