package ch.hgdev.toposuite.test.calculation;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.CircularSegmentation;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

public class TestCircularSegmentation extends TestCase {

    private DecimalFormat df4;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.df4 = new DecimalFormat("#.####");
        this.df4.setRoundingMode(RoundingMode.HALF_UP);

    }

    public void testCircularSegmentation1() {
        Point circleCenter = new Point(1, 444.1609, -713.9844, MathUtils.IGNORE_DOUBLE, true, false);
        Point startPoint = new Point(2, 557.3641, -207.0243, MathUtils.IGNORE_DOUBLE, true, false);
        Point endPoint = new Point(3, 192.8039, -259.4042, MathUtils.IGNORE_DOUBLE, true, false);
        int nbOfSegments = 5;

        CircularSegmentation cs = new CircularSegmentation();
        try {
            cs.initAttributes(
                    circleCenter, startPoint, endPoint, nbOfSegments, MathUtils.IGNORE_DOUBLE);
            cs.compute();
        } catch (CalculationException e) {
            Assert.fail("A calculation exception should not be thrown here");
        } catch (IllegalArgumentException e) {
            Assert.fail("An illegal argument exception should not be thrown here");
        }

        List<Point> points = cs.getPoints();
        Assert.assertEquals("261.105", this.df4.format(points.get(0).getEast()));
        Assert.assertEquals("-227.863", this.df4.format(points.get(0).getNorth()));
        Assert.assertEquals("333.246", this.df4.format(points.get(1).getEast()));
        Assert.assertEquals("-206.5188", this.df4.format(points.get(1).getNorth()));
        Assert.assertEquals("407.7135", this.df4.format(points.get(2).getEast()));
        Assert.assertEquals("-195.8193", this.df4.format(points.get(2).getNorth()));
        Assert.assertEquals("482.9456", this.df4.format(points.get(3).getEast()));
        Assert.assertEquals("-195.989", this.df4.format(points.get(3).getNorth()));
    }

    public void testCircularSegmentation2() {
        Point circleCenter = new Point(1, 444.169, -714.0406, MathUtils.IGNORE_DOUBLE, true, false);
        Point startPoint = new Point(2, 19425.882, 12829.6439, MathUtils.IGNORE_DOUBLE, true, false);
        Point endPoint = new Point(3, 19037.8865, 13344.078, MathUtils.IGNORE_DOUBLE, true, false);
        double arcLength = 250.0;

        CircularSegmentation cs = new CircularSegmentation();
        try {
            cs.initAttributes(circleCenter, startPoint, endPoint, MathUtils.IGNORE_INT, arcLength);
            cs.compute();
        } catch (CalculationException e) {
            Assert.fail("A calculation exception should not be thrown here");
        } catch (IllegalArgumentException e) {
            Assert.fail("An illegal argument exception should not be thrown here");
        }

        List<Point> points = cs.getPoints();
        Assert.assertEquals("18489.6210", this.df4.format(points.get(0).getEast()));
        Assert.assertEquals("13005.5696", this.df4.format(points.get(0).getNorth()));
        Assert.assertEquals("18402.3055", this.df4.format(points.get(1).getEast()));
        Assert.assertEquals("13238.1548", this.df4.format(points.get(1).getNorth()));
        Assert.assertEquals("18409.4735", this.df4.format(points.get(2).getEast()));
        Assert.assertEquals("13486.4862", this.df4.format(points.get(2).getNorth()));
        Assert.assertEquals("18510.0597", this.df4.format(points.get(3).getEast()));
        Assert.assertEquals("13713.6477", this.df4.format(points.get(3).getNorth()));
        Assert.assertEquals("18689.1110", this.df4.format(points.get(4).getEast()));
        Assert.assertEquals("13885.8700", this.df4.format(points.get(4).getNorth()));
        Assert.assertEquals("18920.0102", this.df4.format(points.get(5).getEast()));
        Assert.assertEquals("13977.5510", this.df4.format(points.get(5).getNorth()));
        Assert.assertEquals("19168.4326", this.df4.format(points.get(6).getEast()));
        Assert.assertEquals("13975.0618", this.df4.format(points.get(6).getNorth()));
        Assert.assertEquals("19397.4484", this.df4.format(points.get(7).getEast()));
        Assert.assertEquals("13878.7724", this.df4.format(points.get(7).getNorth()));
        Assert.assertEquals("19573.0127", this.df4.format(points.get(8).getEast()));
        Assert.assertEquals("13702.9969", this.df4.format(points.get(8).getNorth()));
        Assert.assertEquals("19669.0268", this.df4.format(points.get(9).getEast()));
        Assert.assertEquals("13473.8655", this.df4.format(points.get(9).getNorth()));
        Assert.assertEquals("19671.2173", this.df4.format(points.get(10).getEast()));
        Assert.assertEquals("13225.4403", this.df4.format(points.get(10).getNorth()));
        Assert.assertEquals("19579.2588", this.df4.format(points.get(11).getEast()));
        Assert.assertEquals("12994.6514", this.df4.format(points.get(11).getNorth()));
    }

    public void testCircularSegmentation3() {
        Point circleCenter = new Point(
                1, 19863.9616, 1890.3261, MathUtils.IGNORE_DOUBLE, true, false);
        Point startPoint = new Point(2, 17473.4117, 2638.7761, MathUtils.IGNORE_DOUBLE, true, false);
        Point endPoint = new Point(3, 21376.5743, 3887.0507, MathUtils.IGNORE_DOUBLE, true, false);
        int nbOfSegments = 3;

        CircularSegmentation cs = new CircularSegmentation();
        try {
            cs.initAttributes(
                    circleCenter, startPoint, endPoint, nbOfSegments, MathUtils.IGNORE_DOUBLE);
            cs.compute();
        } catch (CalculationException e) {
            Assert.fail("A calculation exception should not be thrown here");
        } catch (IllegalArgumentException e) {
            Assert.fail("An illegal argument exception should not be thrown here");
        }

        List<Point> points = cs.getPoints();
        Assert.assertEquals("18390.5709", this.df4.format(points.get(0).getEast()));
        Assert.assertEquals("3916.1658", this.df4.format(points.get(0).getNorth()));
        Assert.assertEquals("19888.3852", this.df4.format(points.get(1).getEast()));
        Assert.assertEquals("4395.1833", this.df4.format(points.get(1).getNorth()));
    }
}
