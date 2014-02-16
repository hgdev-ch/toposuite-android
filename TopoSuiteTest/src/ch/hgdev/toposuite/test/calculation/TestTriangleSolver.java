package ch.hgdev.toposuite.test.calculation;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import junit.framework.TestCase;
import ch.hgdev.toposuite.calculation.TriangleSolver;

public class TestTriangleSolver extends TestCase {

    private DecimalFormat df2;
    private DecimalFormat df4;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.df2 = new DecimalFormat("#.##");
        this.df2.setRoundingMode(RoundingMode.HALF_UP);
        this.df4 = new DecimalFormat("#.####");
        this.df4.setRoundingMode(RoundingMode.HALF_UP);
    }

    public void testTriangleValidInput() {
        double a = 3.4560;
        double b = 1.3500;
        double c = 2.6540;
        double alpha = 0.0;
        double beta = 0.0;
        double gamma = 0.0;

        try {
            TriangleSolver t = new TriangleSolver(a, b, c, alpha, beta, gamma, false);
            t.compute();

            assertEquals("7.46", this.df2.format(t.getPerimeter()));
            assertEquals("0.9362", this.df4.format(t.getHeight()));
            assertEquals("1.6178", this.df4.format(t.getSurface()));
            assertEquals("0.4337", this.df4.format(t.getIncircleRadius()));
            assertEquals("1.9135", this.df4.format(t.getExcircleRadius()));
        } catch (IllegalArgumentException e) {
            fail("An illegal argument exception should not be thrown here");
        }
    }

    public void testTriangleInvalidInput() {

        try {
            new TriangleSolver(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, false);
            fail("More than 3 values as zero should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // if we reach here, the test passed :-)
        }

        try {
            new TriangleSolver(0.0, 0.0, 0.0, 100.0, 57.0, 144.2, false);
            fail("Sum of the angles > 200 should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // if we reach here, the test passed :-)
        }

        try {
            new TriangleSolver(0.0, 0.0, 0.0, 100.0, 57.0, 23.2, false);
            fail("Giving 3 angles with a total sum different from 200 should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // if we reach here, the test passed :-)
        }

        try {
            new TriangleSolver(-3.3, 0.0, 0.0, 0.0, 0.0, 0.0, false);
            fail("Negative value for 'a' should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // if we reach here, the test passed :-)
        }

        try {
            new TriangleSolver(0.0, -3.3, 0.0, 0.0, 0.0, 0.0, false);
            fail("Negative value for 'b' should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // if we reach here, the test passed :-)
        }

        try {
            new TriangleSolver(0.0, 0.0, -3.3, 0.0, 0.0, 0.0, false);
            fail("Negative value for 'c' should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // if we reach here, the test passed :-)
        }

        try {
            new TriangleSolver(0.0, 0.0, 0.0, -3.3, 0.0, 0.0, false);
            fail("Negative value for 'alpha' should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // if we reach here, the test passed :-)
        }

        try {
            new TriangleSolver(0.0, 0.0, 0.0, 0.0, -3.3, 0.0, false);
            fail("Negative value for 'beta' should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // if we reach here, the test passed :-)
        }

        try {
            new TriangleSolver(0.0, 0.0, 0.0, 0.0, 0.0, -3.3, false);
            fail("Negative value for 'gamma' should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // if we reach here, the test passed :-)
        }
    }
}
