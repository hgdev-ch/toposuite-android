package ch.hgdev.toposuite.test.calculation;

import junit.framework.Assert;
import ch.hgdev.toposuite.calculation.Gisement;
import ch.hgdev.toposuite.dao.CalculationsDataSource;
import ch.hgdev.toposuite.points.Point;

public class TestGisement extends CalculationTest {

    public void testCompute() {
        Point p1 = new Point("1", 600.245, 200.729, 100.776, true);
        Point p2 = new Point("2", 634.087, 257.975, 134.876, true);

        // Test 1 - just a useless comment to make this test more readable
        Gisement g = new Gisement(p1, p2, false);
        g.removeDAO(CalculationsDataSource.getInstance());

        Assert.assertEquals("33.98913148", String.format("%.8f", g.getGisement()));
        Assert.assertEquals("66.50101864", String.format("%.8f", g.getHorizDist()));
        Assert.assertEquals("34.1000", String.format("%.4f", g.getAltitude()));
        Assert.assertEquals("51.2774", String.format("%.4f", g.getSlope()));

        // Test 2
        p2 = new Point("2", 683.064, 194.975, 145.125, true);
        g.setOrientation(p2);

        Assert.assertEquals("104.41593500", String.format("%.8f", g.getGisement()));
        Assert.assertEquals("83.01864415", String.format("%.8f", g.getHorizDist()));
        Assert.assertEquals("44.3490", String.format("%.4f", g.getAltitude()));
        Assert.assertEquals("53.4205", String.format("%.4f", g.getSlope()));

        // Test 3
        p2 = new Point("2", 593.075, 293.875, 97.865, true);
        g.setOrientation(p2);

        Assert.assertEquals("395.10920466", String.format("%.8f", g.getGisement()));
        Assert.assertEquals("93.42155113", String.format("%.8f", g.getHorizDist()));
        Assert.assertEquals("-2.9110", String.format("%.4f", g.getAltitude()));
        Assert.assertEquals("-3.1160", String.format("%.4f", g.getSlope()));

        // Test 4
        // reset origin without altitude
        p1 = new Point("1", 600.245, 200.729, 0.0, true);
        p2 = new Point("2", 528.964, 139.074, 0.0, true);
        g.setOrigin(p1);
        g.setOrientation(p2);

        Assert.assertEquals("254.60178203", String.format("%.8f", g.getGisement()));
        Assert.assertEquals("94.24606085", String.format("%.8f", g.getHorizDist()));

        // Test 5
        p2 = new Point("2", 600.245, 246.986, 0.0, true);
        g.setOrientation(p2);

        Assert.assertEquals("0.00000000", String.format("%.8f", g.getGisement()));
        Assert.assertEquals("46.25700000", String.format("%.8f", g.getHorizDist()));

        // Test 6
        p2 = new Point("2", 600.245, 176.086, 0.0, true);
        g.setOrientation(p2);

        Assert.assertEquals("200.00000000", String.format("%.8f", g.getGisement()));
        Assert.assertEquals("24.64300000", String.format("%.8f", g.getHorizDist()));

        // Test 7
        p2 = new Point("2", 589.907, 200.729, 0.0, true);
        g.setOrientation(p2);

        Assert.assertEquals("300.00000000", String.format("%.8f", g.getGisement()));
        Assert.assertEquals("10.33800000", String.format("%.8f", g.getHorizDist()));

        // Test 8
        p2 = new Point("2", 639.056, 200.729, 0.0, true);
        g.setOrientation(p2);

        Assert.assertEquals("100.00000000", String.format("%.8f", g.getGisement()));
        Assert.assertEquals("38.81100000", String.format("%.8f", g.getHorizDist()));
    }
}