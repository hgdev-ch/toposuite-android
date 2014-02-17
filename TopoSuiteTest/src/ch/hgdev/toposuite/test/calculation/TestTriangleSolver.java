package ch.hgdev.toposuite.test.calculation;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import junit.framework.Assert;
import junit.framework.TestCase;
import ch.hgdev.toposuite.calculation.TriangleSolver;

public class TestTriangleSolver extends TestCase {

    private DecimalFormat df2;
    private DecimalFormat df4;

    private final double  a     = 3.4560;
    private final double  b     = 1.3500;
    private final double  c     = 2.6540;
    private final double  alpha = 128.2621;
    private final double  beta  = 22.9514;
    private final double  gamma = 48.7865;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.df2 = new DecimalFormat("#.##");
        this.df2.setRoundingMode(RoundingMode.HALF_UP);
        this.df4 = new DecimalFormat("#.####");
        this.df4.setRoundingMode(RoundingMode.HALF_UP);

    }

    public void testTriangleValidInput() {
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
            // TODO test second solution of cases with 2 solutions (followings)
            // this.assertT(new TriangleSolver(this.a, this.b, 0.0, this.alpha,
            // 0.0, 0.0, false));
            // this.assertT(new TriangleSolver(this.a, this.b, 0.0, 0.0,
            // this.beta, 0.0, false));
            // this.assertT(new TriangleSolver(0.0, this.b, this.c, 0.0,
            // this.beta, 0.0, false));
            // this.assertT(new TriangleSolver(0.0, this.b, this.c, 0.0, 0.0,
            // this.gamma, false));
            // this.assertT(new TriangleSolver(this.a, 0.0, this.c, this.alpha,
            // 0.0, 0.0, false));
            // this.assertT(new TriangleSolver(this.a, 0.0, this.c, 0.0, 0.0,
            // this.gamma, false));
        } catch (IllegalArgumentException e) {
            Assert.fail("An illegal argument exception should not be thrown here");
        }
    }

    public void testTriangleInvalidInput() {

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
     * Assert resulting values for t computed with class parameters a, b, c,
     * alpha, beta and gamma.
     * 
     * @param t
     */
    private void assertT(TriangleSolver t) {
        t.compute();

        Assert.assertEquals("7.46", this.df2.format(t.getPerimeter().first));
        Assert.assertEquals("0.9362", this.df4.format(t.getHeight().first));
        Assert.assertEquals("1.6178", this.df4.format(t.getSurface().first));
        Assert.assertEquals("0.4337", this.df4.format(t.getIncircleRadius().first));
        Assert.assertEquals("1.9135", this.df4.format(t.getExcircleRadius().first));
    }
}
