package ch.hgdev.toposuite.test.utils;

import junit.framework.Assert;
import junit.framework.TestCase;
import ch.hgdev.toposuite.utils.MathUtils;

public class TestMathUtils extends TestCase {
    public void testIsZero() {
        double d1 = 0.0;
        double d2 = 0.0000000000001;
        double d3 = (0.479374 + 0.209093) - 0.688467;
        double d4 = -0.0;
        double d5 = -0.0000000001;

        Assert.assertTrue(MathUtils.isZero(d1));
        Assert.assertTrue(MathUtils.isZero(d3));
        Assert.assertTrue(MathUtils.isZero(d4));

        Assert.assertFalse(MathUtils.isZero(d2));
        Assert.assertFalse(MathUtils.isZero(d5));
    }

    public void testIsPositive() {

    }

    public void testIsNegative() {

    }

    public void testRadToGrad() {

    }

    public void testGradToRad() {

    }
}