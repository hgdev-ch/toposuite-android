package ch.hgdev.toposuite.test.utils;

import junit.framework.Assert;
import junit.framework.TestCase;
import ch.hgdev.toposuite.utils.StringUtils;

public class TestStringUtils extends TestCase {

    public void testIncrementAsNumber() {
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
