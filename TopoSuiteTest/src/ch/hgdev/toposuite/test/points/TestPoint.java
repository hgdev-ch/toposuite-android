package ch.hgdev.toposuite.test.points;

import android.test.AndroidTestCase;
import ch.hgdev.toposuite.points.Point;

/**
 * Test the Point object class.
 * 
 * @author HGdev
 * 
 */
public class TestPoint extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Test method for
     * {@link ch.hgdev.toposuite.points.Point#Point(int, double, double, double, boolean)}
     * .
     */
    public void testPoint() {
        try {
            new Point(-1, 123.0, 1233431.1232, 1233124, true);
            fail("Point with negative number should not be instanciated!");
        } catch (IllegalArgumentException e) {
            // test passed
        }
        assertTrue(new Point(123, 425.065, 234545.01, 8765.12, false) != null);
    }

    public void testGetters() {
        Point p = new Point(1, 882332.0023, -1234312.123, 334299.013, false);
        assertEquals(1, p.getNumber());
        assertEquals(882332.0023, p.getEast());
        assertEquals(-1234312.123, p.getNorth());
        assertEquals(334299.013, p.getAltitude());
        assertFalse(p.isBasePoint());
        assertEquals("Computed", p.getBasePointAsString(this.getContext()));
        p = new Point(1, 882332.0023, -1234312.123, 334299.013, true);
        assertTrue(p.isBasePoint());
        assertEquals("Provided", p.getBasePointAsString(this.getContext()));
    }

    public void testSetters() {
        Point p = new Point(42, -12342.65424, 2345429.23434, 186032.9445345, false);
        p.setEast(453.67);
        assertEquals(453.67, p.getEast());
        p.setNorth(951134.98823);
        assertEquals(951134.98823, p.getNorth());
        p.setAltitude(-12051.234);
        assertEquals(-12051.234, p.getAltitude());
    }

    public void testToString() {
        Point p = new Point(0, 23.1, 54.3, 99.5, false);
        assertTrue(p.toString().isEmpty());
        p = new Point(1, 23.1, 54.3, 99.5, false);
        assertEquals("1", p.toString());
    }
}