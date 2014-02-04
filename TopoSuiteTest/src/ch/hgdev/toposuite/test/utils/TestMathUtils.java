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
        double d1 = 0.0;
        double d2 = -0.0;
        double d3 = -0.000000000001;
        double d4 = 42.0;
        double d5 = 0.00000000001;

        Assert.assertTrue(MathUtils.isPositive(d4));
        Assert.assertTrue(MathUtils.isPositive(d5));

        Assert.assertFalse(MathUtils.isPositive(d1));
        Assert.assertFalse(MathUtils.isPositive(d2));
        Assert.assertFalse(MathUtils.isPositive(d3));
    }

    public void testIsNegative() {
        double d1 = 0.0;
        double d2 = -0.0;
        double d3 = -0.00000000000001;
        double d4 = 1.43433;
        double d5 = -42.0;

        Assert.assertTrue(MathUtils.isNegative(d3));
        Assert.assertTrue(MathUtils.isNegative(d5));

        Assert.assertFalse(MathUtils.isNegative(d1));
        Assert.assertFalse(MathUtils.isNegative(d2));
        Assert.assertFalse(MathUtils.isNegative(d4));
    }

    public void testRadToGrad() {
        Assert.assertEquals("218.89", String.format("%.2f", MathUtils.radToGrad(3.4383)));
        Assert.assertEquals("79.132", String.format("%.3f", MathUtils.radToGrad(1.243)));
    }

    public void testGradToRad() {
        Assert.assertEquals("3.438", String.format("%.3f", MathUtils.gradToRad(218.89)));
        Assert.assertEquals("1.243", String.format("%.3f", MathUtils.gradToRad(79.132)));
    }
}