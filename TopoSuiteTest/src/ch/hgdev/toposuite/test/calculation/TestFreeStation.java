package ch.hgdev.toposuite.test.calculation;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import junit.framework.Assert;
import junit.framework.TestCase;
import ch.hgdev.toposuite.calculation.FreeStation;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

public class TestFreeStation extends TestCase {
    private DecimalFormat df3;
    private DecimalFormat df2;
    private DecimalFormat df1;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.df3 = new DecimalFormat("#.###");
        this.df3.setRoundingMode(RoundingMode.HALF_UP);

        this.df2 = new DecimalFormat("#.##");
        this.df2.setRoundingMode(RoundingMode.HALF_UP);

        this.df1 = new DecimalFormat("#.#");
        this.df1.setRoundingMode(RoundingMode.HALF_UP);
    }

    public void testFreeStation1() {
        Point p1 = new Point(1, 542430.11, 151989.66, 0.0, true, false);
        Point p2 = new Point(2, 542610.79, 151979.94, 0.0, true, false);
        Point p3 = new Point(3, 542624.36, 151873.24, 0.0, true, false);
        Point p4 = new Point(4, 542495.94, 151847.05, 0.0, true, false);

        Measure m1 = new Measure(p1, 271.234, 100, 162.154);
        Measure m2 = new Measure(p2, 356.627, 100, 125.149);
        Measure m3 = new Measure(p3, 21.493, 100, 80.431);
        Measure m4 = new Measure(p4, 188.014, 100, 55.128);

        FreeStation fs = new FreeStation(42, false);
        fs.getMeasures().add(m1);
        fs.getMeasures().add(m2);
        fs.getMeasures().add(m3);
        fs.getMeasures().add(m4);
        fs.compute();

        Assert.assertEquals("542543.93", this.df2.format(
                fs.getStationResult().getEast()));
        Assert.assertEquals("151874.16", this.df2.format(
                fs.getStationResult().getNorth()));

        Assert.assertEquals("2.6", this.df1.format(fs.getResults().get(0).getvE()));
        Assert.assertEquals("0.9", this.df1.format(fs.getResults().get(0).getvN()));
        Assert.assertEquals("2.8", this.df1.format(fs.getResults().get(0).getfS()));

        Assert.assertEquals("-1.8", this.df1.format(fs.getResults().get(1).getvE()));
        Assert.assertEquals("1.7", this.df1.format(fs.getResults().get(1).getvN()));
        Assert.assertEquals("2.5", this.df1.format(fs.getResults().get(1).getfS()));

        Assert.assertEquals("-0.5", this.df1.format(fs.getResults().get(2).getvE()));
        Assert.assertEquals("-1.4", this.df1.format(fs.getResults().get(2).getvN()));
        Assert.assertEquals("1.4", this.df1.format(fs.getResults().get(2).getfS()));

        Assert.assertEquals("-0.3", this.df1.format(fs.getResults().get(3).getvE()));
        Assert.assertEquals("-1.3", this.df1.format(fs.getResults().get(3).getvN()));
        Assert.assertEquals("1.3", this.df1.format(fs.getResults().get(3).getfS()));
    }

    public void testFreeStation2() {
        Point p1 = new Point(1, 600.0, 200.0, 0.0, true, false);
        Point p2 = new Point(2, 620.0, 215.0, 416.3, true, false);
        Point p3 = new Point(3, 610.0, 185.0, 417.17, true, false);
        Point p104 = new Point(104, 635.0, 180.0, 0.0, true, false);
        Point p105 = new Point(105, 595.0, 170.0, 0.0, true, false);

        Measure m1 = new Measure(p1, 252.0, 100, 18.015);
        Measure m2 = new Measure(p2, 309.91, 100, 31.61);
        Measure m3 = new Measure(p3, 0.0, 100, 0.0);
        Measure m4 = new Measure(p104, 2.0, 100, 25.5);
        Measure m5 = new Measure(p105, 139.43, 100, 21.22);

        FreeStation fs = new FreeStation(9001, 1.650, false);
        fs.getMeasures().add(m1);
        fs.getMeasures().add(m2);
        fs.getMeasures().add(m3);
        fs.getMeasures().add(m4);
        fs.getMeasures().add(m5);
        fs.compute();

        Assert.assertEquals("609.999", this.df3.format(fs.getStationResult().getEast()));
        Assert.assertEquals("185.004", this.df3.format(fs.getStationResult().getNorth()));

        Assert.assertEquals("110.565", this.df3.format(fs.getUnknownOrientation()));
        Assert.assertEquals("0.3", this.df1.format(fs.getsE()));
    }

    public void testFreeStation3() {
        Point p6 = new Point(6, 622.475, 210.990, 100.400, true, false);
        Point p7 = new Point(7, 636.236, 145.773, 99.964, true, false);
        Point p8 = new Point(8, 635.417, 177.289, 99.144, true, false);
        Point p9 = new Point(9, 595.012, 210.991, 100.068, true, false);
        Point p10 = new Point(10, 598.055, 218.982, 100.189, true, false);

        Measure m1 = new Measure(p6, 10.562, 99.124, 25.030, 1.570);
        Measure m2 = new Measure(p7, 102.070, 100.068, 65.200, 1.620);
        Measure m3 = new Measure(p8, 75.852, 101.162, 42.070, 1.740);
        Measure m4 = new Measure(p9, 312.411, 99.724, 12.070, 1.600);
        Measure m5 = new Measure(p10, 333.020, 98.180, 19.080, 2.000);

        FreeStation fs = new FreeStation(42, 1.6, false);
        fs.getMeasures().add(m1);
        fs.getMeasures().add(m2);
        fs.getMeasures().add(m3);
        fs.getMeasures().add(m4);
        fs.getMeasures().add(m5);
        fs.compute();

        Assert.assertEquals("600.009", this.df3.format(
                fs.getStationResult().getEast()));
        Assert.assertEquals("199.995", this.df3.format(
                fs.getStationResult().getNorth()));
        Assert.assertEquals("100.026", this.df3.format(
                fs.getStationResult().getAltitude()));

        Assert.assertEquals("0.03", this.df2.format(fs.getResults().get(0).getvA()));
        Assert.assertEquals("-2.76", this.df2.format(fs.getResults().get(1).getvA()));
        Assert.assertEquals("-2.59", this.df2.format(fs.getResults().get(2).getvA()));
        Assert.assertEquals("1.01", this.df2.format(fs.getResults().get(3).getvA()));
        Assert.assertEquals("-1.78", this.df2.format(fs.getResults().get(4).getvA()));

        Assert.assertEquals("0.6", this.df1.format(fs.getsE()));
        Assert.assertEquals("0.6", this.df1.format(fs.getsN()));
        Assert.assertEquals("0.7", this.df1.format(fs.getsA()));

        Assert.assertEquals("60.443", this.df3.format(fs.getUnknownOrientation()));
    }

    public void testFreeStation4() {
        Point p182 = new Point(182, 559729.53, 147799.62, 0.00, true, false);
        Point p188 = new Point(188, 559750.21, 147772.29, 0.00, true, false);
        Point p189 = new Point(189, 559748.07, 147775.80, 0.00, true, false);
        Point p190 = new Point(190, 559750.55, 147777.23, 0.00, true, false);
        Point p284 = new Point(284, 559701.24, 147751.08, 0.00, true, false);
        Point p969 = new Point(969, 559772.81, 147851.25, 0.00, true, false);
        Point p970 = new Point(970, 559727.00, 147754.64, 0.00, true, false);
        Point p8001 = new Point(8001, 559694.50, 147719.23, 0.00, true, false);

        Measure m1 = new Measure(p182, 6.5060, 100, 46.120);
        Measure m2 = new Measure(p188, 58.8310, 100, 31.801);
        Measure m3 = new Measure(p189, 51.7400, 100, 32.051);
        Measure m4 = new Measure(p190, 52.9910, 100, 34.877);
        Measure m5 = new Measure(p284, 292.6390, 100, 23.343);
        Measure m6 = new Measure(p969, 29.0700, 100, 108.656);
        Measure m7 = new Measure(p970, 77.7880, 100, 2.476);
        Measure m8 = new Measure(p8001, 245.5710, 100, 45.949);

        FreeStation fs = new FreeStation(42, MathUtils.IGNORE_DOUBLE, false);
        fs.getMeasures().add(m1);
        fs.getMeasures().add(m2);
        fs.getMeasures().add(m3);
        fs.getMeasures().add(m4);
        fs.getMeasures().add(m5);
        fs.getMeasures().add(m6);
        fs.getMeasures().add(m7);
        fs.getMeasures().add(m8);
        fs.compute();

        Assert.assertEquals("559724.677", this.df3.format(
                fs.getStationResult().getEast()));
        Assert.assertEquals("147753.711", this.df3.format(
                fs.getStationResult().getNorth()));

        Assert.assertEquals("10", this.df1.format(fs.getsE()));
        Assert.assertEquals("10", this.df1.format(fs.getsN()));

        Assert.assertEquals("0.2054", this.df3.format(fs.getUnknownOrientation()));
    }

    public void testFreeStationDeactivation1() {
        Point p1 = new Point(1, 542430.11, 151989.66, 0.0, true, false);
        Point p2 = new Point(2, 542610.79, 151979.94, 0.0, true, false);
        Point p3 = new Point(3, 542624.36, 151873.24, 0.0, true, false);
        Point p4 = new Point(4, 542495.94, 151847.05, 0.0, true, false);
        Point p5 = new Point(970, 559727.00, 147754.64, 0.00, true, false);

        Measure m1 = new Measure(p1, 271.234, 100, 162.154);
        Measure m2 = new Measure(p2, 356.627, 100, 125.149);
        Measure m3 = new Measure(p3, 21.493, 100, 80.431);
        Measure m4 = new Measure(p4, 188.014, 100, 55.128);
        Measure m5 = new Measure(p5, 292.6390, 100, 23.343);

        FreeStation fs = new FreeStation(42, false);
        fs.getMeasures().add(m1);
        fs.getMeasures().add(m2);
        fs.getMeasures().add(m3);
        fs.getMeasures().add(m4);
        fs.getMeasures().add(m5);
        fs.compute();
        m5.deactivate();
        fs.compute();

        Assert.assertEquals("542543.93", this.df2.format(
                fs.getStationResult().getEast()));
        Assert.assertEquals("151874.16", this.df2.format(
                fs.getStationResult().getNorth()));

        Assert.assertEquals("2.6", this.df1.format(fs.getResults().get(0).getvE()));
        Assert.assertEquals("0.9", this.df1.format(fs.getResults().get(0).getvN()));
        Assert.assertEquals("2.8", this.df1.format(fs.getResults().get(0).getfS()));

        Assert.assertEquals("-1.8", this.df1.format(fs.getResults().get(1).getvE()));
        Assert.assertEquals("1.7", this.df1.format(fs.getResults().get(1).getvN()));
        Assert.assertEquals("2.5", this.df1.format(fs.getResults().get(1).getfS()));

        Assert.assertEquals("-0.5", this.df1.format(fs.getResults().get(2).getvE()));
        Assert.assertEquals("-1.4", this.df1.format(fs.getResults().get(2).getvN()));
        Assert.assertEquals("1.4", this.df1.format(fs.getResults().get(2).getfS()));

        Assert.assertEquals("-0.3", this.df1.format(fs.getResults().get(3).getvE()));
        Assert.assertEquals("-1.3", this.df1.format(fs.getResults().get(3).getvN()));
        Assert.assertEquals("1.3", this.df1.format(fs.getResults().get(3).getfS()));
    }

}