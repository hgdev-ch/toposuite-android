package ch.hgdev.toposuite.test.calculation;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import junit.framework.Assert;
import junit.framework.TestCase;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.calculation.PolarImplantation;
import ch.hgdev.toposuite.calculation.PolarImplantation.Result;
import ch.hgdev.toposuite.points.Point;

public class TestPolarImplantation extends TestCase {
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

    public void testRealCase() {
        Point station = new Point(1, 0.0, 0.0, 323.45, false);
        Point p2 = new Point(2, 20.498, 21.703, 322.986, false);
        Point p3 = new Point(3, 62.853, 21.235, 324.831, false);
        Point p4 = new Point(4, 44.248, -8.891, 322.876, false);
        double z0 = 48.8732;
        double i = 1.58;

        Measure m2 = new Measure(p2, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, z0);
        Measure m3 = new Measure(p3, 0.0, 0.0, 0.0, 1.8, 0.0, 0.0, i, z0);
        Measure m4 = new Measure(p4, 0.0, 0.0, 0.0, 1.5, 0.0, 0.0, i, z0);

        PolarImplantation pi = new PolarImplantation(station, false);

        pi.getMeasures().add(m2);
        pi.getMeasures().add(m3);
        pi.getMeasures().add(m4);

        pi.compute();

        Result r2 = pi.getResults().get(0);
        Result r3 = pi.getResults().get(1);
        Result r4 = pi.getResults().get(2);

        Assert.assertEquals(3, r3.getPointNumber());
        Assert.assertEquals("30.385", this.df3.format(r3.getHorizDir()));
        Assert.assertEquals("66.343", this.df3.format(r3.getHorizDist()));
        Assert.assertEquals("98.464", this.df3.format(r3.getZenAngle()));
        Assert.assertEquals("66.366", this.df3.format(r3.getDistance()));
        Assert.assertEquals("79.258", this.df3.format(r3.getGisement()));

        Assert.assertEquals(4, r4.getPointNumber());
        Assert.assertEquals("63.7507", this.df4.format(r4.getHorizDir()));
        Assert.assertEquals("45.132", this.df3.format(r4.getHorizDist()));
        Assert.assertEquals("100.9224", this.df4.format(r4.getZenAngle()));
        Assert.assertEquals("45.139", this.df3.format(r4.getDistance()));
        Assert.assertEquals("112.6239", this.df4.format(r4.getGisement()));
    }
}
