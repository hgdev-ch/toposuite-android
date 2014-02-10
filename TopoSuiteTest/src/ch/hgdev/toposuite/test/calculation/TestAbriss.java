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

    private DecimalFormat df3;
    private DecimalFormat df4;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.df3 = new DecimalFormat("#.###");
        this.df3.setRoundingMode(RoundingMode.HALF_UP);
        this.df4 = new DecimalFormat("#.####");
        this.df4.setRoundingMode(RoundingMode.HALF_UP);
    }

    public void testRandom() {
        Point p1 = new Point(1, 600.245, 200.729, 0.0, true);
        Point p2 = new Point(2, 623.487, 528.371, 0.0, true);
        Point p3 = new Point(3, 476.331, 534.228, 0.0, true);
        Point p4 = new Point(4, 372.472, 257.326, 0.0, true);

        Abriss a = new Abriss(p1, false);
        a.removeDAO(CalculationsDataSource.getInstance());

        a.getMeasures().add(new Measure(p2, 257.748));
        a.getMeasures().add(new Measure(p3, 254.558));
        a.getMeasures().add(new Measure(p4, 247.655));

        a.compute();

        Assert.assertEquals("328.465", this.df3.format(
                a.getResults().get(0).getDistance()));
        Assert.assertEquals("146.7604", this.df4.format(
                a.getResults().get(0).getUnknownOrientation()));
        Assert.assertEquals("370.2162", this.df4.format(
                a.getResults().get(0).getOrientedDirection()));
        Assert.assertEquals("-365.7077", this.df4.format(
                a.getResults().get(0).getErrAngle()));
        Assert.assertEquals("-1886.876", this.df3.format(
                a.getResults().get(0).getErrTrans()));

        Assert.assertEquals("355.776", this.df3.format(
                a.getResults().get(1).getDistance()));
        Assert.assertEquals("122.7943", this.df4.format(
                a.getResults().get(1).getUnknownOrientation()));
        Assert.assertEquals("367.0262", this.df4.format(
                a.getResults().get(1).getOrientedDirection()));
        Assert.assertEquals("10.3262", this.df4.format(
                a.getResults().get(1).getErrAngle()));
        Assert.assertEquals("57.708", this.df3.format(
                a.getResults().get(1).getErrTrans()));

        Assert.assertEquals("234.699", this.df3.format(
                a.getResults().get(2).getDistance()));
        Assert.assertEquals("67.8497", this.df4.format(
                a.getResults().get(2).getUnknownOrientation()));
        Assert.assertEquals("360.1232", this.df4.format(
                a.getResults().get(2).getOrientedDirection()));
        Assert.assertEquals("-44.6184", this.df4.format(
                a.getResults().get(2).getErrAngle()));
        Assert.assertEquals("-164.492", this.df3.format(
                a.getResults().get(2).getErrTrans()));

        Assert.assertEquals("112.4682", this.df4.format(
                a.getMean()));
        Assert.assertEquals("260.6142", this.df4.format(
                a.getMSE()));
        Assert.assertEquals("150.4657", this.df4.format(
                a.getMeanErrCompDir()));
    }

    public void testRealCase() {
        Point p34 = new Point(34, 556506.667, 172513.91, 620.34, true);
        Point p45 = new Point(45, 556495.16, 172493.912, 623.37, true);
        Point p47 = new Point(47, 556612.21, 172489.274, 0.0, true);
        Abriss a = new Abriss(p34, false);
        a.removeDAO(CalculationsDataSource.getInstance());
        a.getMeasures().add(new Measure(p45, 0.0, 91.6892, 23.277, 1.63));
        a.getMeasures().add(new Measure(p47, 281.3521, 100.0471, 108.384, 1.63));
        a.compute();

        Assert.assertEquals("233.2435", this.df4.format(
                a.getResults().get(0).getUnknownOrientation()));
        Assert.assertEquals("0.043", this.df3.format(
                a.getResults().get(0).getErrAngle()));
        Assert.assertEquals("0.030", this.df3.format(
                a.getResults().get(0).getErrLong()));
    }
}