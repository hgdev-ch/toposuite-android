package ch.hgdev.toposuite.test.utils;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hgdev.toposuite.test.testutils.UtilsTestRunner;
import ch.hgdev.toposuite.utils.StringUtils;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class StringUtilsTest extends UtilsTestRunner {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void incrementAsNumber() {
        String validInput1 = "123.456";
        String validInput2 = "foo42";
        String validInput3 = "Foo42Bar42.123";
        String validInput4 = "42";

        Assert.assertEquals("123.457",
                StringUtils.incrementAsNumber(validInput1));
        Assert.assertEquals("foo43",
                StringUtils.incrementAsNumber(validInput2));
        Assert.assertEquals("Foo42Bar42.124",
                StringUtils.incrementAsNumber(validInput3));
        Assert.assertEquals("43",
                StringUtils.incrementAsNumber(validInput4));

        String invalidInput1 = "foo";
        String invalidInput2 = "123foo";
        String invalidInput3 = "";
        String invalidInput4 = null;

        try {
            StringUtils.incrementAsNumber(invalidInput1);
            Assert.fail("incrementAsNumber should fail with input: " + invalidInput1);
        } catch (IllegalArgumentException e) {
            // should be thrown
        }

        try {
            StringUtils.incrementAsNumber(invalidInput2);
            Assert.fail("incrementAsNumber should fail with input: " + invalidInput2);
        } catch (IllegalArgumentException e) {
            // should be thrown
        }

        try {
            StringUtils.incrementAsNumber(invalidInput3);
            Assert.fail("incrementAsNumber should fail with input: " + invalidInput3);
        } catch (IllegalArgumentException e) {
            // should be thrown
        }

        try {
            StringUtils.incrementAsNumber(invalidInput4);
            Assert.fail("incrementAsNumber should fail with input: " + invalidInput4);
        } catch (IllegalArgumentException e) {
            // should be thrown
        }
    }
}
