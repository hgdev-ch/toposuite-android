package ch.hgdev.toposuite.test.calculation;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.CirclesIntersection;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.test.testutils.CalculationTestRunner;
import ch.hgdev.toposuite.utils.MathUtils;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class CirclesIntersectionTest extends CalculationTestRunner {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void simple() {
        Point p1 = new Point("1", 25.0, 55.0, 0.0, false);
        Point p3 = new Point("3", 50.177, 99.941, 0.0, false);
        double radius3 = 87.752;
        double radius1 = 50.0;

        try {
            CirclesIntersection c = new CirclesIntersection(p3, radius3, p1, radius1, false);
            c.compute();

            Assert.assertEquals("-24.999",
                    this.df3.format(c.getFirstIntersection().getEast()));
            Assert.assertEquals("54.675",
                    this.df3.format(c.getFirstIntersection().getNorth()));
            Assert.assertEquals("50.835",
                    this.df3.format(c.getSecondIntersection().getEast()));
            Assert.assertEquals("12.191",
                    this.df3.format(c.getSecondIntersection().getNorth()));
        } catch (IllegalArgumentException e) {
            Assert.fail("An illegal argument exception should not be thrown here.");
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }
    }

    // TD-NK_3.2.1.1
    @Test
    public void tdNK3211() {
        Point p1 = new Point("3201", 600.0, 375.0, MathUtils.IGNORE_DOUBLE, false);
        Point p2 = new Point("3202", 606.0, 374.0, MathUtils.IGNORE_DOUBLE, false);
        double radius1 = 5.5;
        double radius2 = 2.5;

        try {
            CirclesIntersection c = new CirclesIntersection(p1, radius1, p2, radius2, false);
            c.compute();

            Assert.assertEquals("604.574",
                    this.df3.format(c.getFirstIntersection().getEast()));
            Assert.assertEquals("371.946",
                    this.df3.format(c.getFirstIntersection().getNorth()));
            Assert.assertEquals("605.318",
                    this.df3.format(c.getSecondIntersection().getEast()));
            Assert.assertEquals("376.405",
                    this.df3.format(c.getSecondIntersection().getNorth()));
        } catch (IllegalArgumentException e) {
            Assert.fail("An illegal argument exception should not be thrown here.");
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }
    }

    // TD-NK_3.2.1.2
    @Test
    public void tdNK3212() {
        Point p1 = new Point("3205", 620.0, 375.0, MathUtils.IGNORE_DOUBLE, false);
        Point p2 = new Point("3206", 623.0, 374.0, MathUtils.IGNORE_DOUBLE, false);
        double radius1 = 2.5;
        double radius2 = 2.6;

        try {
            CirclesIntersection c = new CirclesIntersection(p1, radius1, p2, radius2, false);
            c.compute();

            Assert.assertEquals("620.791",
                    this.df3.format(c.getFirstIntersection().getEast()));
            Assert.assertEquals("372.628",
                    this.df3.format(c.getFirstIntersection().getNorth()));
            Assert.assertEquals("622.056",
                    this.df3.format(c.getSecondIntersection().getEast()));
            Assert.assertEquals("376.423",
                    this.df3.format(c.getSecondIntersection().getNorth()));
        } catch (IllegalArgumentException e) {
            Assert.fail("An illegal argument exception should not be thrown here.");
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }
    }

    // TD-NK_3.2.1.3
    @Test
    public void tdNK3213() {
        Point p1 = new Point("3209", 640.0, 375.0, MathUtils.IGNORE_DOUBLE, false);
        Point p2 = new Point("3210", 644.0, 375.0, MathUtils.IGNORE_DOUBLE, false);
        double radius1 = 5.5;
        double radius2 = 2.5;

        try {
            CirclesIntersection c = new CirclesIntersection(p1, radius1, p2, radius2, false);
            c.compute();

            Assert.assertEquals("645.000",
                    this.df3.format(c.getFirstIntersection().getEast()));
            Assert.assertEquals("372.709",
                    this.df3.format(c.getFirstIntersection().getNorth()));
            Assert.assertEquals("645.000",
                    this.df3.format(c.getSecondIntersection().getEast()));
            Assert.assertEquals("377.291",
                    this.df3.format(c.getSecondIntersection().getNorth()));
        } catch (IllegalArgumentException e) {
            Assert.fail("An illegal argument exception should not be thrown here.");
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }
    }

    // TD-NK_3.2.2.1
    @Test
    public void tdNK3221() {
        Point p1 = new Point("3213", 600.0, 390.0, MathUtils.IGNORE_DOUBLE, false);
        Point p2 = new Point("3214", 602.991, 394.616, MathUtils.IGNORE_DOUBLE, false);
        Point p3 = new Point("3215", 606.0, 389.0, MathUtils.IGNORE_DOUBLE, false);
        double radius3 = 2.5;

        try {
            CirclesIntersection c = new CirclesIntersection(p1, p2, p3, radius3, false);
            c.compute();

            Assert.assertEquals("604.575",
                    this.df3.format(c.getFirstIntersection().getEast()));
            Assert.assertEquals("386.946",
                    this.df3.format(c.getFirstIntersection().getNorth()));
            Assert.assertEquals("605.318",
                    this.df3.format(c.getSecondIntersection().getEast()));
            Assert.assertEquals("391.405",
                    this.df3.format(c.getSecondIntersection().getNorth()));
        } catch (IllegalArgumentException e) {
            Assert.fail("An illegal argument exception should not be thrown here.");
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }
    }

    // TD-NK_3.2.2.2
    @Test
    public void tdNK3222() {
        Point p1 = new Point("3218", 620.0, 390.0, MathUtils.IGNORE_DOUBLE, false);
        Point p2 = new Point("3219", 617.799, 391.185, MathUtils.IGNORE_DOUBLE, false);
        Point p3 = new Point("3220", 623.0, 389.0, MathUtils.IGNORE_DOUBLE, false);
        double radius3 = 2.6;

        try {
            CirclesIntersection c = new CirclesIntersection(p1, p2, p3, radius3, false);
            c.compute();

            Assert.assertEquals("620.791",
                    this.df3.format(c.getFirstIntersection().getEast()));
            Assert.assertEquals("387.629",
                    this.df3.format(c.getFirstIntersection().getNorth()));
            Assert.assertEquals("622.056",
                    this.df3.format(c.getSecondIntersection().getEast()));
            Assert.assertEquals("391.422",
                    this.df3.format(c.getSecondIntersection().getNorth()));
        } catch (IllegalArgumentException e) {
            Assert.fail("An illegal argument exception should not be thrown here.");
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }
    }

    // TD-NK_3.2.2.3
    @Test
    public void tdNK3223() {
        Point p1 = new Point("3223", 640.0, 390.0, MathUtils.IGNORE_DOUBLE, false);
        Point p2 = new Point("3244", 640.048, 384.5, MathUtils.IGNORE_DOUBLE, false);
        Point p3 = new Point("3225", 644.0, 390.0, MathUtils.IGNORE_DOUBLE, false);
        double radius3 = 2.5;

        try {
            CirclesIntersection c = new CirclesIntersection(p1, p2, p3, radius3, false);
            c.compute();

            Assert.assertEquals("645.000",
                    this.df3.format(c.getFirstIntersection().getEast()));
            Assert.assertEquals("387.709",
                    this.df3.format(c.getFirstIntersection().getNorth()));
            Assert.assertEquals("645.000",
                    this.df3.format(c.getSecondIntersection().getEast()));
            Assert.assertEquals("392.291",
                    this.df3.format(c.getSecondIntersection().getNorth()));
        } catch (IllegalArgumentException e) {
            Assert.fail("An illegal argument exception should not be thrown here.");
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }
    }

    // TD-NK_3.2.3.1
    @Test
    public void tdNK3231() {
        Point p1 = new Point("3228", 600.0, 405, MathUtils.IGNORE_DOUBLE, false);
        Point p2 = new Point("3229", 606.0, 404.0, MathUtils.IGNORE_DOUBLE, false);
        Point p3 = new Point("3230", 603.784, 405.158, MathUtils.IGNORE_DOUBLE, false);
        double radius1 = 5.5;

        try {
            CirclesIntersection c = new CirclesIntersection(p1, radius1, p2, p3, false);
            c.compute();

            Assert.assertEquals("604.574",
                    this.df3.format(c.getFirstIntersection().getEast()));
            Assert.assertEquals("401.946",
                    this.df3.format(c.getFirstIntersection().getNorth()));
            Assert.assertEquals("605.317",
                    this.df3.format(c.getSecondIntersection().getEast()));
            Assert.assertEquals("406.405",
                    this.df3.format(c.getSecondIntersection().getNorth()));
        } catch (IllegalArgumentException e) {
            Assert.fail("An illegal argument exception should not be thrown here.");
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }
    }

    // TD-NK_3.2.3.2
    @Test
    public void tdNK3232() {
        Point p1 = new Point("3233", 620.0, 405, MathUtils.IGNORE_DOUBLE, false);
        Point p2 = new Point("3234", 623.0, 404.0, MathUtils.IGNORE_DOUBLE, false);
        Point p3 = new Point("3235", 623.727, 401.504, MathUtils.IGNORE_DOUBLE, false);
        double radius1 = 2.5;

        try {
            CirclesIntersection c = new CirclesIntersection(p1, radius1, p2, p3, false);
            c.compute();

            Assert.assertEquals("620.791",
                    this.df3.format(c.getFirstIntersection().getEast()));
            Assert.assertEquals("402.629",
                    this.df3.format(c.getFirstIntersection().getNorth()));
            Assert.assertEquals("622.056",
                    this.df3.format(c.getSecondIntersection().getEast()));
            Assert.assertEquals("406.422",
                    this.df3.format(c.getSecondIntersection().getNorth()));
        } catch (IllegalArgumentException e) {
            Assert.fail("An illegal argument exception should not be thrown here.");
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }
    }

    // TD-NK_3.2.3.3
    @Test
    public void tdNK3233() {
        Point p1 = new Point("3238", 640.0, 405, MathUtils.IGNORE_DOUBLE, false);
        Point p2 = new Point("3239", 644.0, 405.0, MathUtils.IGNORE_DOUBLE, false);
        Point p3 = new Point("3240", 646.0, 405.775, MathUtils.IGNORE_DOUBLE, false);
        double radius1 = 5.5;

        try {
            CirclesIntersection c = new CirclesIntersection(p1, radius1, p2, p3, false);
            c.compute();

            Assert.assertEquals("645.206",
                    this.df3.format(c.getFirstIntersection().getEast()));
            Assert.assertEquals("403.226",
                    this.df3.format(c.getFirstIntersection().getNorth()));
            Assert.assertEquals("645.206",
                    this.df3.format(c.getSecondIntersection().getEast()));
            Assert.assertEquals("406.774",
                    this.df3.format(c.getSecondIntersection().getNorth()));
        } catch (IllegalArgumentException e) {
            Assert.fail("An illegal argument exception should not be thrown here.");
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }
    }

    // TD-NK_3.2.4.1
    @Test
    public void tdNK3241() {
        Point p1 = new Point("3243", 600.0, 420, MathUtils.IGNORE_DOUBLE, false);
        Point p2 = new Point("3244", 602.991, 424.616, MathUtils.IGNORE_DOUBLE, false);
        Point p3 = new Point("3245", 606.0, 419.0, MathUtils.IGNORE_DOUBLE, false);
        Point p4 = new Point("3246", 603.784, 420.158, MathUtils.IGNORE_DOUBLE, false);

        try {
            CirclesIntersection c = new CirclesIntersection(p1, p2, p3, p4, false);
            c.compute();

            Assert.assertEquals("604.574",
                    this.df3.format(c.getFirstIntersection().getEast()));
            Assert.assertEquals("416.946",
                    this.df3.format(c.getFirstIntersection().getNorth()));
            Assert.assertEquals("605.318",
                    this.df3.format(c.getSecondIntersection().getEast()));
            Assert.assertEquals("421.405",
                    this.df3.format(c.getSecondIntersection().getNorth()));
        } catch (IllegalArgumentException e) {
            Assert.fail("An illegal argument exception should not be thrown here.");
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }
    }

    // TD-NK_3.2.4.2
    @Test
    public void tdNK3242() {
        Point p1 = new Point("3249", 620.0, 420, MathUtils.IGNORE_DOUBLE, false);
        Point p2 = new Point("3250", 617.799, 421.185, MathUtils.IGNORE_DOUBLE, false);
        Point p3 = new Point("3251", 623.0, 419.0, MathUtils.IGNORE_DOUBLE, false);
        Point p4 = new Point("3252", 623.727, 416.504, MathUtils.IGNORE_DOUBLE, false);

        try {
            CirclesIntersection c = new CirclesIntersection(p1, p2, p3, p4, false);
            c.compute();

            Assert.assertEquals("620.791",
                    this.df3.format(c.getFirstIntersection().getEast()));
            Assert.assertEquals("417.629",
                    this.df3.format(c.getFirstIntersection().getNorth()));
            Assert.assertEquals("622.056",
                    this.df3.format(c.getSecondIntersection().getEast()));
            Assert.assertEquals("421.422",
                    this.df3.format(c.getSecondIntersection().getNorth()));
        } catch (IllegalArgumentException e) {
            Assert.fail("An illegal argument exception should not be thrown here.");
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }
    }

    // TD-NK_3.2.4.3
    @Test
    public void tdNK3243() {
        Point p1 = new Point("3255", 640.0, 420, MathUtils.IGNORE_DOUBLE, false);
        Point p2 = new Point("3256", 640.048, 414.5, MathUtils.IGNORE_DOUBLE, false);
        Point p3 = new Point("3257", 644.0, 420.0, MathUtils.IGNORE_DOUBLE, false);
        Point p4 = new Point("3258", 646.377, 420.775, MathUtils.IGNORE_DOUBLE, false);

        try {
            CirclesIntersection c = new CirclesIntersection(p1, p2, p3, p4, false);
            c.compute();

            Assert.assertEquals("645.000",
                    this.df3.format(c.getFirstIntersection().getEast()));
            Assert.assertEquals("417.709",
                    this.df3.format(c.getFirstIntersection().getNorth()));
            Assert.assertEquals("645.000",
                    this.df3.format(c.getSecondIntersection().getEast()));
            Assert.assertEquals("422.291",
                    this.df3.format(c.getSecondIntersection().getNorth()));
        } catch (IllegalArgumentException e) {
            Assert.fail("An illegal argument exception should not be thrown here.");
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }
    }
}
