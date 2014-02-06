package ch.hgdev.toposuite.test.utils;

import junit.framework.TestCase;
import ch.hgdev.toposuite.utils.DisplayUtils;

/**
 * Test DisplayUtils functions.
 * 
 * @author HGdev
 * 
 */
public class TestDisplayUtils extends TestCase {

    /**
     * Test method for
     * {@link ch.hgdev.toposuite.utils.DisplayUtils#toString(double)}.
     */
    public void testToStringDouble() {
        assertEquals("12.0043", DisplayUtils.toString(12.0043));
        assertEquals("0.0000", DisplayUtils.toString(0.0));
        assertEquals("9832.1230", DisplayUtils.toString(9832.1230));
        assertEquals("-12.0021", DisplayUtils.toString(-12.0021));
        assertEquals("42.0000", DisplayUtils.toString(42));
    }

    /**
     * Test method for
     * {@link ch.hgdev.toposuite.utils.DisplayUtils#toString(android.content.Context, boolean)}
     * .
     */
    public void testToStringContextBoolean() {
        // TODO test it
    }

    /**
     * Test method for
     * {@link ch.hgdev.toposuite.utils.DisplayUtils#formatDate(java.util.Date)}.
     */
    public void testFormatDate() {
        // TODO test it
    }

    /**
     * Test method for
     * {@link ch.hgdev.toposuite.utils.DisplayUtils#dpToPx(android.content.Context, int)}
     * .
     */
    public void testDpToPx() {
        // TODO test it
    }
}