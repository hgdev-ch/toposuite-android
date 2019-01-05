package ch.hgdev.toposuite.test.calculation;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.TriangleSolver;
import ch.hgdev.toposuite.test.testutils.CalculationTestRunner;
import ch.hgdev.toposuite.utils.MathUtils;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class TriangleSolverTest extends CalculationTestRunner {

    private final double a = 3.4560;
    private final double b = 1.3500;
    private final double c = 2.6540;
    private final double alpha = 128.2621;
    private final double beta = 22.9514;
    private final double gamma = 48.7865;

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void oneSolutionCases() {
        try {
            this.assertT(new TriangleSolver(this.a, this.b, this.c, 0.0, 0.0, 0.0, false));
            this.assertT(new TriangleSolver(this.a, this.b, 0.0, 0.0, 0.0, this.gamma, false));
            this.assertT(new TriangleSolver(0.0, this.b, this.c, this.alpha, 0.0, 0.0, false));
            this.assertT(new TriangleSolver(this.a, 0.0, this.c, 0.0, this.beta, 0.0, false));
            this.assertT(new TriangleSolver(this.a, 0.0, 0.0, 0.0, this.beta, this.gamma, false));
            this.assertT(new TriangleSolver(0.0, this.b, 0.0, this.alpha, 0.0, this.gamma, false));
            this.assertT(new TriangleSolver(0.0, 0.0, this.c, this.alpha, this.beta, 0.0, false));
            this.assertT(new TriangleSolver(this.a, 0.0, 0.0, this.alpha, this.beta, 0.0, false));
            this.assertT(new TriangleSolver(this.a, 0.0, 0.0, this.alpha, 0.0, this.gamma, false));
            this.assertT(new TriangleSolver(0.0, this.b, 0.0, this.alpha, this.beta, 0.0, false));
            this.assertT(new TriangleSolver(0.0, this.b, 0.0, 0.0, this.beta, this.gamma, false));
            this.assertT(new TriangleSolver(0.0, 0.0, this.c, this.alpha, 0.0, this.gamma, false));
            this.assertT(new TriangleSolver(0.0, 0.0, this.c, 0.0, this.beta, this.gamma, false));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Assert.fail("An illegal argument exception should not be thrown here");
        }
    }

    @Test
    public void twoSolutionsCases() {
        try {
            this.assertT(new TriangleSolver(this.a, this.b, 0.0, this.alpha, 0.0, 0.0, false));
            this.assertT2Ignorable(new TriangleSolver(this.a, this.b, 0.0, this.alpha, 0.0, 0.0, false));

            this.assertT(new TriangleSolver(this.a, this.b, 0.0, 0.0, this.beta, 0.0, false));
            this.assertT2A(new TriangleSolver(this.a, this.b, 0.0, 0.0, this.beta, 0.0, false));

            this.assertT(new TriangleSolver(0.0, this.b, this.c, 0.0, this.beta, 0.0, false));
            this.assertT2B(new TriangleSolver(0.0, this.b, this.c, 0.0, this.beta, 0.0, false));

            this.assertT(new TriangleSolver(0.0, this.b, this.c, 0.0, 0.0, this.gamma, false));
            this.assertT2Ignorable(new TriangleSolver(0.0, this.b, this.c, 0.0, 0.0, this.gamma, false));

            this.assertT(new TriangleSolver(this.a, 0.0, this.c, this.alpha, 0.0, 0.0, false));
            this.assertT2Ignorable(new TriangleSolver(this.a, 0.0, this.c, this.alpha, 0.0, 0.0, false));

            this.assertT(new TriangleSolver(this.a, 0.0, this.c, 0.0, 0.0, this.gamma, false));
            this.assertT2C(new TriangleSolver(this.a, 0.0, this.c, 0.0, 0.0, this.gamma, false));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Assert.fail("An illegal argument exception should not be thrown here");
        }
    }

    @Test
    public void invalidInput() {
        try {
            new TriangleSolver(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, false);
            Assert.fail("More than 3 values as zero should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // if we reach here, the test passed :-)
        }

        try {
            new TriangleSolver(0.0, 0.0, 0.0, 100.0, 57.0, 144.2, false);
            Assert.fail("Sum of the angles > 200 should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // if we reach here, the test passed :-)
        }

        try {
            new TriangleSolver(0.0, 0.0, 0.0, 100.0, 57.0, 23.2, false);
            Assert.fail("Giving 3 angles with a total sum different from 200 should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // if we reach here, the test passed :-)
        }

        try {
            new TriangleSolver(-3.3, 0.0, 0.0, 0.0, 0.0, 0.0, false);
            Assert.fail("Negative value for 'a' should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // if we reach here, the test passed :-)
        }

        try {
            new TriangleSolver(0.0, -3.3, 0.0, 0.0, 0.0, 0.0, false);
            Assert.fail("Negative value for 'b' should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // if we reach here, the test passed :-)
        }

        try {
            new TriangleSolver(0.0, 0.0, -3.3, 0.0, 0.0, 0.0, false);
            Assert.fail("Negative value for 'c' should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // if we reach here, the test passed :-)
        }

        try {
            new TriangleSolver(0.0, 0.0, 0.0, -3.3, 0.0, 0.0, false);
            Assert.fail("Negative value for 'alpha' should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // if we reach here, the test passed :-)
        }

        try {
            new TriangleSolver(0.0, 0.0, 0.0, 0.0, -3.3, 0.0, false);
            Assert.fail("Negative value for 'beta' should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // if we reach here, the test passed :-)
        }

        try {
            new TriangleSolver(0.0, 0.0, 0.0, 0.0, 0.0, -3.3, false);
            Assert.fail("Negative value for 'gamma' should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // if we reach here, the test passed :-)
        }
    }

    /**
     * This is a regression test for issue #760.
     */
    @Test
    public void issue760() {
        double a = 20.0;
        double c = 35.0;
        double gamma = 72.25;

        TriangleSolver t = new TriangleSolver(a, 0.0, c, 0.0, 0.0, gamma, false);
        try {
            t.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("20.0", this.df1.format(t.getA()));
        Assert.assertEquals("35.0", this.df1.format(t.getC()));
        Assert.assertEquals("72.25", this.df2.format(t.getGamma()));

        Assert.assertEquals("34.6643", this.df4.format(t.getAlpha()));
        Assert.assertEquals("93.0857", this.df4.format(t.getBeta()));
        Assert.assertEquals("38.383", this.df3.format(t.getB()));

        Assert.assertEquals("19.305", this.df3.format(t.getExcircleRadius().first));
    }

    /**
     * Assert resulting values for t computed with class parameters a, b, c,
     * alpha, beta and gamma. This tests only the first solution.
     *
     * @param t
     */
    private void assertT(TriangleSolver t) {
        try {
            t.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("7.46", this.df2.format(t.getPerimeter().first));
        Assert.assertEquals("0.9362", this.df4.format(t.getHeight().first));
        Assert.assertEquals("1.6178", this.df4.format(t.getSurface().first));
        Assert.assertEquals("0.4337", this.df4.format(t.getIncircleRadius().first));
        Assert.assertEquals("1.9135", this.df4.format(t.getExcircleRadius().first));
    }

    private void assertT2A(TriangleSolver t) {
        try {
            t.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("8.62", this.df2.format(t.getPerimeter().second));
        Assert.assertEquals("1.345", this.df3.format(t.getHeight().second));
        Assert.assertEquals("2.3247", this.df4.format(t.getSurface().second));
        Assert.assertEquals("0.539", this.df3.format(t.getIncircleRadius().second));
        Assert.assertEquals("1.9135", this.df4.format(t.getExcircleRadius().second));
    }

    private void assertT2B(TriangleSolver t) {
        try {
            t.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("5.515", this.df3.format(t.getPerimeter().second));
        Assert.assertEquals("0.936", this.df3.format(t.getHeight().second));
        Assert.assertEquals("0.7072", this.df4.format(t.getSurface().second));
        Assert.assertEquals("0.256", this.df3.format(t.getIncircleRadius().second));
        Assert.assertEquals("1.9135", this.df4.format(t.getExcircleRadius().second));
    }

    private void assertT2C(TriangleSolver t) {
        try {
            t.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("9.74", this.df2.format(t.getPerimeter().second));
        Assert.assertEquals("2.517", this.df3.format(t.getHeight().second));
        Assert.assertEquals("4.3498", this.df4.format(t.getSurface().second));
        Assert.assertEquals("0.893", this.df3.format(t.getIncircleRadius().second));
        Assert.assertEquals("1.9135", this.df4.format(t.getExcircleRadius().second));
    }

    private void assertT2Ignorable(TriangleSolver t) {
        try {
            t.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertTrue(MathUtils.isIgnorable(t.getPerimeter().second));
        Assert.assertTrue(MathUtils.isIgnorable(t.getHeight().second));
        Assert.assertTrue(MathUtils.isIgnorable(t.getSurface().second));
        Assert.assertTrue(MathUtils.isIgnorable(t.getIncircleRadius().second));
        Assert.assertTrue(MathUtils.isIgnorable(t.getExcircleRadius().second));
    }
}
