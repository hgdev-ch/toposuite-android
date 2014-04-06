package ch.hgdev.toposuite.test.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import junit.framework.Assert;
import junit.framework.TestCase;
import ch.hgdev.toposuite.points.Point;
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

    public void testEuclideanDistance() {
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.HALF_UP);

        Point p1 = new Point("1", 643.238, 437.271, 0.0, true);
        Point p2 = new Point("2", 576.376, 285.267, 0.0, true);

        Assert.assertEquals("166.0595",
                df.format(MathUtils.euclideanDistance(p1, p2)));
    }

    public void testScaleToPPM() {
        Assert.assertEquals(-250, MathUtils.scaleToPPM(0.99975));
    }

    public void testEqualsForPointsWithTolerance() {
        final double TOLERANCE = 0.00000001;
        final double TOLERANCE_2 = 0.0000000001;
        Point p1 = new Point("421", 42.123456789, 24.1234, MathUtils.IGNORE_DOUBLE, true, false);
        Point p2 = new Point("422", 42.123456788, 24.1234, MathUtils.IGNORE_DOUBLE, true, false);
        Point p3 = new Point("423", 42.123456789, 24.1234, MathUtils.IGNORE_DOUBLE, true, false);

        Assert.assertTrue(MathUtils.equals(p1, p2, TOLERANCE));
        Assert.assertFalse(MathUtils.equals(p1, p2, TOLERANCE_2));
        Assert.assertTrue(MathUtils.equals(p1, p3, TOLERANCE));

        Assert.assertFalse(MathUtils.equals(null, null, TOLERANCE));
        Assert.assertFalse(MathUtils.equals(null, p1, TOLERANCE));
        Assert.assertFalse(MathUtils.equals(p1, null, TOLERANCE));
    }
}