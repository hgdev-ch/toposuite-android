package ch.hgdev.toposuite.test.points;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.AndroidJUnitRunner;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hgdev.toposuite.points.Point;

/**
 * Test the Point object class.
 * 
 * @author HGdev
 * 
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class PointTest extends AndroidJUnitRunner {
    private Context context;

    @Before
    public void setUp() {
        this.context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void constructor() {
        // FIXME since the point number is now a String, this test must be adapted!
        /*try {
            new Point(null, 123.0, 1233431.1232, 1233124, true, false);
            Assert.fail("Point with negative number should not be instanciated!");
        } catch (IllegalArgumentException e) {
            // test passed
        }*/
        Assert.assertTrue(new Point("123", 425.065, 234545.01, 8765.12, false, false) != null);
    }

    @Test
    public void getters() {
        Point p = new Point("1", 882332.0023, -1234312.12345, 334299.01345, false, false);
        Assert.assertEquals("1", p.getNumber());
        Assert.assertEquals(882332.0023, p.getEast());
        Assert.assertEquals(-1234312.12345, p.getNorth());
        Assert.assertEquals(334299.01345, p.getAltitude());
        Assert.assertFalse(p.isBasePoint());
        Assert.assertEquals("Computed", p.getBasePointAsString(this.context));
        p = new Point("1", 882332.0023, -1234312.123, 334299.013, true);
        Assert.assertTrue(p.isBasePoint());
        Assert.assertEquals("Provided", p.getBasePointAsString(this.context));
    }

    @Test
    public void setters() {
        Point p = new Point("42", -12342.65424, 2345429.23434, 186032.9445345, false, false);
        p.setEast(453.67);
        Assert.assertEquals(453.67, p.getEast());
        p.setNorth(951134.988);
        Assert.assertEquals(951134.988, p.getNorth());
        p.setAltitude(-12051.234);
        Assert.assertEquals(-12051.234, p.getAltitude());
    }

    @Test
    public void pointToString() {
        Point p = new Point("", 23.1, 54.3, 99.5, false, false);
        Assert.assertTrue(p.toString().isEmpty());
        p = new Point("1", 23.1, 54.3, 99.5, false, false);
        Assert.assertEquals("1", p.toString());
    }
}