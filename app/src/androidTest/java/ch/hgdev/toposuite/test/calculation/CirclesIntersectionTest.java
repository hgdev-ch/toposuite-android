package ch.hgdev.toposuite.test.calculation;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.CirclesIntersection;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.test.testutils.CalculationTestRunner;

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
}
