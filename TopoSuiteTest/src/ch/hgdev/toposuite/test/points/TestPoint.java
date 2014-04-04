package ch.hgdev.toposuite.test.points;

import junit.framework.Assert;
import android.test.AndroidTestCase;
import ch.hgdev.toposuite.points.Point;

/**
 * Test the Point object class.
 * 
 * @author HGdev
 * 
 */
public class TestPoint extends AndroidTestCase {

    public void testPoint() {
        // FIXME since the point number is now a String, this test must be adapted!
        /*try {
            new Point(null, 123.0, 1233431.1232, 1233124, true, false);
            Assert.fail("Point with negative number should not be instanciated!");
        } catch (IllegalArgumentException e) {
            // test passed
        }*/
        Assert.assertTrue(new Point("123", 425.065, 234545.01, 8765.12, false, false) != null);
    }

    public void testGetters() {
        Point p = new Point("1", 882332.0023, -1234312.123, 334299.013, false, false);
        Assert.assertEquals("1", p.getNumber());
        Assert.assertEquals(882332.0023, p.getEast());
        Assert.assertEquals(-1234312.123, p.getNorth());
        Assert.assertEquals(334299.013, p.getAltitude());
        Assert.assertFalse(p.isBasePoint());
        Assert.assertEquals("Computed", p.getBasePointAsString(this.getContext()));
        p = new Point("1", 882332.0023, -1234312.123, 334299.013, true);
        Assert.assertTrue(p.isBasePoint());
        Assert.assertEquals("Provided", p.getBasePointAsString(this.getContext()));
    }

    public void testSetters() {
        Point p = new Point("42", -12342.65424, 2345429.23434, 186032.9445345, false, false);
        p.setEast(453.67);
        Assert.assertEquals(453.67, p.getEast());
        p.setNorth(951134.98823);
        Assert.assertEquals(951134.98823, p.getNorth());
        p.setAltitude(-12051.234);
        Assert.assertEquals(-12051.234, p.getAltitude());
    }

    public void testToString() {
        Point p = new Point("", 23.1, 54.3, 99.5, false, false);
        Assert.assertTrue(p.toString().isEmpty());
        p = new Point("1", 23.1, 54.3, 99.5, false, false);
        Assert.assertEquals("1", p.toString());
    }
}