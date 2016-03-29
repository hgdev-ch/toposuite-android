package ch.hgdev.toposuite.test.calculation;

import java.util.List;

import junit.framework.Assert;
import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.CircularSegmentation;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

public class TestCircularSegmentation extends CalculationTest {

    public void testCircularSegmentation1() {
        Point circleCenter = new Point("1", 444.1609, -713.9844, MathUtils.IGNORE_DOUBLE, true,
                false);
        Point startPoint = new Point("2", 557.3641, -207.0243, MathUtils.IGNORE_DOUBLE, true, false);
        Point endPoint = new Point("3", 192.8039, -259.4042, MathUtils.IGNORE_DOUBLE, true, false);
        int nbOfSegments = 5;

        CircularSegmentation cs = new CircularSegmentation(false);
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
        Assert.assertEquals(4, points.size());

        Assert.assertEquals("948.7889", this.df4.format(points.get(0).getEast()));
        Assert.assertEquals("-590.8014", this.df4.format(points.get(0).getNorth()));
        Assert.assertEquals("778.2173", this.df4.format(points.get(1).getEast()));
        Assert.assertEquals("-1111.7656", this.df4.format(points.get(1).getNorth()));
        Assert.assertEquals("235.6121", this.df4.format(points.get(2).getEast()));
        Assert.assertEquals("-1189.7269", this.df4.format(points.get(2).getNorth()));
        Assert.assertEquals("-74.7354", this.df4.format(points.get(3).getEast()));
        Assert.assertEquals("-737.8611", this.df4.format(points.get(3).getNorth()));
    }

    public void testCircularSegmentation2() {
        Point circleCenter = new Point(
                "1", 352.9166, 288.3151, MathUtils.IGNORE_DOUBLE, true, false);
        Point startPoint = new Point(
                "2", 283.7735, 299.7822, MathUtils.IGNORE_DOUBLE, true, false);
        Point endPoint = new Point("3", 416.2355, 318.3647, MathUtils.IGNORE_DOUBLE, true, false);
        double arcLength = 50.0;

        CircularSegmentation cs = new CircularSegmentation(false);
        try {
            cs.initAttributes(circleCenter, startPoint, endPoint, MathUtils.IGNORE_INT, arcLength);
            cs.compute();
        } catch (CalculationException e) {
            Assert.fail("A calculation exception should not be thrown here");
        } catch (IllegalArgumentException e) {
            Assert.fail("An illegal argument exception should not be thrown here");
        }

        List<Point> points = cs.getPoints();
        Assert.assertEquals(3, points.size());

        Assert.assertEquals("308.1385", this.df4.format(points.get(0).getEast()));
        Assert.assertEquals("342.2334", this.df4.format(points.get(0).getNorth()));
        Assert.assertEquals("354.3421", this.df4.format(points.get(1).getEast()));
        Assert.assertEquals("358.3881", this.df4.format(points.get(1).getNorth()));
        Assert.assertEquals("399.8506", this.df4.format(points.get(2).getEast()));
        Assert.assertEquals("340.3676", this.df4.format(points.get(2).getNorth()));

    }

    public void testCircularSegmentation3() {
        Point circleCenter = new Point(
                "1", 19863.9616, 1890.3261, MathUtils.IGNORE_DOUBLE, true, false);
        Point startPoint = new Point("2", 17473.4117, 2638.7761, MathUtils.IGNORE_DOUBLE, true,
                false);
        Point endPoint = new Point("3", 21376.5743, 3887.0507, MathUtils.IGNORE_DOUBLE, true, false);
        int nbOfSegments = 3;

        CircularSegmentation cs = new CircularSegmentation(false);
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
        Assert.assertEquals(2, points.size());

        Assert.assertEquals("18390.571", this.df3.format(points.get(0).getEast()));
        Assert.assertEquals("3916.1658", this.df4.format(points.get(0).getNorth()));
        Assert.assertEquals("19888.3852", this.df4.format(points.get(1).getEast()));
        Assert.assertEquals("4395.1833", this.df4.format(points.get(1).getNorth()));
    }
}
