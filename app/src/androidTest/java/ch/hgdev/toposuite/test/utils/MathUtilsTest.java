package ch.hgdev.toposuite.test.utils;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.test.testutils.UtilsTestRunner;
import ch.hgdev.toposuite.utils.MathUtils;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class MathUtilsTest extends UtilsTestRunner {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void isZero() {
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

    @Test
    public void isPositive() {
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

    @Test
    public void isNegative() {
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

    @Test
    public void radToGrad() {
        Assert.assertEquals("218.89", this.df2.format(MathUtils.radToGrad(3.4383)));
        Assert.assertEquals("79.132", this.df3.format(MathUtils.radToGrad(1.243)));
    }

    @Test
    public void gadToRad() {
        Assert.assertEquals("3.438", this.df3.format(MathUtils.gradToRad(218.89)));
        Assert.assertEquals("1.243", this.df3.format(MathUtils.gradToRad(79.132)));
    }

    @Test
    public void modulo200() {
        Assert.assertEquals("100.0000", this.df4.format(MathUtils.modulo200(100.0)));
        Assert.assertEquals("100.0000", this.df4.format(MathUtils.modulo200(-100.0)));
        Assert.assertEquals("0.0000", this.df4.format(MathUtils.modulo200(200.0)));
        Assert.assertEquals("0.0000", this.df4.format(MathUtils.modulo200(0.0)));
        Assert.assertEquals("0.0000", this.df4.format(MathUtils.modulo200(-200.0)));
        Assert.assertEquals("0.0000", this.df4.format(MathUtils.modulo200(-0.0)));
        Assert.assertEquals("1.0000", this.df4.format(MathUtils.modulo200(201.0)));
        Assert.assertEquals("1.0000", this.df4.format(MathUtils.modulo200(401.0)));
        Assert.assertEquals("199.9999", this.df4.format(MathUtils.modulo200(199.9999)));
    }

    @Test
    public void modulo400() {
        Assert.assertEquals("200.0000", this.df4.format(MathUtils.modulo400(200.0)));
        Assert.assertEquals("200.0000", this.df4.format(MathUtils.modulo400(-200.0)));
        Assert.assertEquals("0.0000", this.df4.format(MathUtils.modulo400(400.0)));
        Assert.assertEquals("0.0000", this.df4.format(MathUtils.modulo400(0.0)));
        Assert.assertEquals("0.0000", this.df4.format(MathUtils.modulo400(-400.0)));
        Assert.assertEquals("0.0000", this.df4.format(MathUtils.modulo400(-0.0)));
        Assert.assertEquals("1.0000", this.df4.format(MathUtils.modulo400(401.0)));
        Assert.assertEquals("1.0000", this.df4.format(MathUtils.modulo400(801.0)));
        Assert.assertEquals("399.9999", this.df4.format(MathUtils.modulo400(399.9999)));

    }

    @Test
    public void euclideanDistance() {
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.HALF_UP);

        Point p1 = new Point("1", 643.238, 437.271, 0.0, true);
        Point p2 = new Point("2", 576.376, 285.267, 0.0, true);

        Assert.assertEquals("166.0595", df.format(MathUtils.euclideanDistance(p1, p2)));
    }

    @Test
    public void scaleToPPM() {
        Assert.assertEquals(-250, MathUtils.scaleToPPM(0.99975));
    }

    @Test
    public void equalsForPointsWithTolerance() {
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