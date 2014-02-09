package ch.hgdev.toposuite.test.calculation;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import junit.framework.Assert;
import junit.framework.TestCase;
import ch.hgdev.toposuite.calculation.Abriss;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.dao.CalculationsDataSource;
import ch.hgdev.toposuite.points.Point;

public class TestAbriss extends TestCase {
    public void testCompute() {
        DecimalFormat df4 = new DecimalFormat("#.####");
        df4.setRoundingMode(RoundingMode.HALF_UP);
        DecimalFormat df3 = new DecimalFormat("#.###");
        df3.setRoundingMode(RoundingMode.HALF_UP);

        Point p1 = new Point(1, 600.245, 200.729, 0.0, true);
        Point p2 = new Point(2, 623.487, 528.371, 0.0, true);
        Point p3 = new Point(3, 476.331, 534.228, 0.0, true);
        Point p4 = new Point(4, 372.472, 257.326, 0.0, true);

        Abriss a = new Abriss(p1);
        a.removeDAO(CalculationsDataSource.getInstance());

        a.getMeasures().add(new Measure(p2, 257.748));
        a.getMeasures().add(new Measure(p3, 254.558));
        a.getMeasures().add(new Measure(p4, 247.655));

        a.compute();

        Assert.assertEquals("328.465", df3.format(
                a.getResults().get(0).getDistance()));
        Assert.assertEquals("146.7604", df4.format(
                a.getResults().get(0).getUnknownOrientation()));
        Assert.assertEquals("370.2162", df4.format(
                a.getResults().get(0).getOrientedDirection()));
        Assert.assertEquals("-365.7077", df4.format(
                a.getResults().get(0).getErrAngle()));
        Assert.assertEquals("-1886.876", df3.format(
                a.getResults().get(0).getErrTrans()));

        Assert.assertEquals("355.776", df3.format(
                a.getResults().get(1).getDistance()));
        Assert.assertEquals("122.7943", df4.format(
                a.getResults().get(1).getUnknownOrientation()));
        Assert.assertEquals("367.0262", df4.format(
                a.getResults().get(1).getOrientedDirection()));
        Assert.assertEquals("10.3262", df4.format(
                a.getResults().get(1).getErrAngle()));
        Assert.assertEquals("57.708", df3.format(
                a.getResults().get(1).getErrTrans()));

        Assert.assertEquals("234.699", df3.format(
                a.getResults().get(2).getDistance()));
        Assert.assertEquals("67.8497", df4.format(
                a.getResults().get(2).getUnknownOrientation()));
        Assert.assertEquals("360.1232", df4.format(
                a.getResults().get(2).getOrientedDirection()));
        Assert.assertEquals("-44.6184", df4.format(
                a.getResults().get(2).getErrAngle()));
        Assert.assertEquals("-164.492", df3.format(
                a.getResults().get(2).getErrTrans()));

        Assert.assertEquals("112.4682", df4.format(
                a.getMean()));
        Assert.assertEquals("260.6142", df4.format(
                a.getMSE()));
        Assert.assertEquals("150.4657", df4.format(
                a.getMeanErrCompDir()));
    }
}