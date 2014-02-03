package ch.hgdev.toposuite.test.calculation;

import junit.framework.TestCase;
import ch.hgdev.toposuite.calculation.Gisement;
import ch.hgdev.toposuite.points.Point;

public class TestGisement extends TestCase {

    public void testCompute() {
        Point p1 = new Point(1, 600.245, 200.729, 0.0, false);
        Point p2 = new Point(2, 634.087, 257.975, 0.0, false);
        
        Gisement z0 = new Gisement(p1, p2);

        assertEquals("33.98913148", String.format("%.8f", z0.getGisement()));
        assertEquals("66.50101864", String.format("%.8f", z0.getDistHoriz()));
        
        p2 = new Point(2, 589.907, 200.729, 0.0, false);
        z0.setOrientation(p2);
        z0.compute();
        
        assertEquals("300.00000000", String.format("%.8f", z0.getGisement()));
        assertEquals("10.33800000", String.format("%.8f", z0.getDistHoriz()));
        
        p2 = new Point(2, 639.056, 200.729, 0.0, false);
        z0.setOrientation(p2);
        z0.compute();
        
        assertEquals("100.00000000", String.format("%.8f", z0.getGisement()));
        assertEquals("38.81100000", String.format("%.8f", z0.getDistHoriz()));
    }
}
