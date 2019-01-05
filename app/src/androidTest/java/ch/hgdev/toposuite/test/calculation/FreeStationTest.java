package ch.hgdev.toposuite.test.calculation;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.FreeStation;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.test.testutils.CalculationTestRunner;
import ch.hgdev.toposuite.utils.MathUtils;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class FreeStationTest extends CalculationTestRunner {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void simple1() {
        Point p1 = new Point("1", 542430.11, 151989.66, 0.0, true, false);
        Point p2 = new Point("2", 542610.79, 151979.94, 0.0, true, false);
        Point p3 = new Point("3", 542624.36, 151873.24, 0.0, true, false);
        Point p4 = new Point("4", 542495.94, 151847.05, 0.0, true, false);

        Measure m1 = new Measure(p1, 271.234, 100, 162.154);
        Measure m2 = new Measure(p2, 356.627, 100, 125.149);
        Measure m3 = new Measure(p3, 21.493, 100, 80.431);
        Measure m4 = new Measure(p4, 188.014, 100, 55.128);

        FreeStation fs = new FreeStation("42", false);
        fs.getMeasures().add(m1);
        fs.getMeasures().add(m2);
        fs.getMeasures().add(m3);
        fs.getMeasures().add(m4);

        try {
            fs.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

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

    @Test
    public void simple2() {
        Point p1 = new Point("1", 600.0, 200.0, 0.0, true, false);
        Point p2 = new Point("2", 620.0, 215.0, 416.3, true, false);
        Point p3 = new Point("3", 610.0, 185.0, 417.17, true, false);
        Point p104 = new Point("104", 635.0, 180.0, 0.0, true, false);
        Point p105 = new Point("105", 595.0, 170.0, 0.0, true, false);

        Measure m1 = new Measure(p1, 252.0, 100, 18.015);
        Measure m2 = new Measure(p2, 309.91, 100, 31.61);
        Measure m3 = new Measure(p3, 0.0, 100, 0.0);
        Measure m4 = new Measure(p104, 2.0, 100, 25.5);
        Measure m5 = new Measure(p105, 139.43, 100, 21.22);

        FreeStation fs = new FreeStation("9001", 1.650, false);
        fs.getMeasures().add(m1);
        fs.getMeasures().add(m2);
        fs.getMeasures().add(m3);
        fs.getMeasures().add(m4);
        fs.getMeasures().add(m5);

        try {
            fs.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("609.999", this.df3.format(fs.getStationResult().getEast()));
        Assert.assertEquals("185.004", this.df3.format(fs.getStationResult().getNorth()));

        Assert.assertEquals("110.565", this.df3.format(fs.getUnknownOrientation()));
        Assert.assertEquals("0.3", this.df1.format(fs.getsE()));
    }

    @Test
    public void simple3() {
        Point p6 = new Point("6", 622.475, 210.990, 100.400, true, false);
        Point p7 = new Point("7", 636.236, 145.773, 99.964, true, false);
        Point p8 = new Point("8", 635.417, 177.289, 99.144, true, false);
        Point p9 = new Point("9", 595.012, 210.991, 100.068, true, false);
        Point p10 = new Point("10", 598.055, 218.982, 100.189, true, false);

        Measure m1 = new Measure(p6, 10.562, 99.124, 25.030, 1.570);
        Measure m2 = new Measure(p7, 102.070, 100.068, 65.200, 1.620);
        Measure m3 = new Measure(p8, 75.852, 101.162, 42.070, 1.740);
        Measure m4 = new Measure(p9, 312.411, 99.724, 12.070, 1.600);
        Measure m5 = new Measure(p10, 333.020, 98.180, 19.080, 2.000);

        FreeStation fs = new FreeStation("42", 1.6, false);
        fs.getMeasures().add(m1);
        fs.getMeasures().add(m2);
        fs.getMeasures().add(m3);
        fs.getMeasures().add(m4);
        fs.getMeasures().add(m5);

        try {
            fs.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

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

    @Test
    public void simple4() {
        Point p182 = new Point("182", 559729.53, 147799.62, 0.00, true, false);
        Point p188 = new Point("188", 559750.21, 147772.29, 0.00, true, false);
        Point p189 = new Point("189", 559748.07, 147775.80, 0.00, true, false);
        Point p190 = new Point("190", 559750.55, 147777.23, 0.00, true, false);
        Point p284 = new Point("284", 559701.24, 147751.08, 0.00, true, false);
        Point p969 = new Point("969", 559772.81, 147851.25, 0.00, true, false);
        Point p970 = new Point("970", 559727.00, 147754.64, 0.00, true, false);
        Point p8001 = new Point("8001", 559694.50, 147719.23, 0.00, true, false);

        Measure m1 = new Measure(p182, 6.5060, 100, 46.120);
        Measure m2 = new Measure(p188, 58.8310, 100, 31.801);
        Measure m3 = new Measure(p189, 51.7400, 100, 32.051);
        Measure m4 = new Measure(p190, 52.9910, 100, 34.877);
        Measure m5 = new Measure(p284, 292.6390, 100, 23.343);
        Measure m6 = new Measure(p969, 29.0700, 100, 108.656);
        Measure m7 = new Measure(p970, 77.7880, 100, 2.476);
        Measure m8 = new Measure(p8001, 245.5710, 100, 45.949);

        FreeStation fs = new FreeStation("42", MathUtils.IGNORE_DOUBLE, false);
        fs.getMeasures().add(m1);
        fs.getMeasures().add(m2);
        fs.getMeasures().add(m3);
        fs.getMeasures().add(m4);
        fs.getMeasures().add(m5);
        fs.getMeasures().add(m6);
        fs.getMeasures().add(m7);
        fs.getMeasures().add(m8);

        try {
            fs.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("559724.59", this.df2.format(
                fs.getStationResult().getEast()));
        Assert.assertEquals("147753.696", this.df3.format(
                fs.getStationResult().getNorth()));

        Assert.assertEquals("8.4", this.df1.format(fs.getsE()));
        Assert.assertEquals("8.4", this.df1.format(fs.getsN()));

        Assert.assertEquals("0.352", this.df3.format(fs.getUnknownOrientation()));
    }

    @Test
    public void deactivation() {
        Point p1 = new Point("1", 542430.11, 151989.66, 0.0, true, false);
        Point p2 = new Point("2", 542610.79, 151979.94, 0.0, true, false);
        Point p3 = new Point("3", 542624.36, 151873.24, 0.0, true, false);
        Point p4 = new Point("4", 542495.94, 151847.05, 0.0, true, false);
        Point p5 = new Point("970", 559727.00, 147754.64, 0.00, true, false);

        Measure m1 = new Measure(p1, 271.234, 100, 162.154);
        Measure m2 = new Measure(p2, 356.627, 100, 125.149);
        Measure m3 = new Measure(p3, 21.493, 100, 80.431);
        Measure m4 = new Measure(p4, 188.014, 100, 55.128);
        Measure m5 = new Measure(p5, 292.6390, 100, 23.343);

        FreeStation fs = new FreeStation("42", false);
        fs.getMeasures().add(m1);
        fs.getMeasures().add(m2);
        fs.getMeasures().add(m3);
        fs.getMeasures().add(m4);
        fs.getMeasures().add(m5);

        try {
            fs.compute();
            m5.deactivate();
            fs.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

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

    /**
     * This test is a regression test for bug #628 (wrong computation of the
     * horizontal distance).
     */
    @Test
    public void issue628() {
        Point p47 = new Point("47", 529076.015, 182644.074, 933.350, true, false);
        Point p48 = new Point("48", 529133.008, 182679.620, 918.950, true, false);
        Point p9001 = new Point("9001", 529137.864, 182649.391, 919.770, true, false);
        Point p9002 = new Point("9002", 529112.403, 182631.705, 924.720, true, false);
        Point p9005 = new Point("9005", 529120.829, 182647.374, 923.684, true, false);
        Point p9010gps = new Point("9010gps", 529115.194, 182633.175, 923.785, true, false);

        Measure m1 = new Measure(p47, 288.8624, 85.8420, 43.215, 2.0);
        Measure m2 = new Measure(p48, 31.9616, 109.5593, 32.559, 2.0);
        Measure m3 = new Measure(p9001, 106.3770, 112.4151, 20.890, 2.00);
        Measure m4 = new Measure(p9002, 216.0699, 97.2887, 20.360, 2.000);
        Measure m5 = new Measure(p9005, 155.9136, 101.5850, 5.250, 2.000);
        Measure m6 = new Measure(p9010gps, 208.0706, 100.1894, 18.340, 2.000);

        FreeStation fs = new FreeStation("42", 0.846, false);
        fs.getMeasures().add(m1);
        fs.getMeasures().add(m2);
        fs.getMeasures().add(m3);
        fs.getMeasures().add(m4);
        fs.getMeasures().add(m5);
        fs.getMeasures().add(m6);

        try {
            fs.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("529117.495", this.df3.format(fs.getStationResult().getEast()));
        Assert.assertEquals("182651.405", this.df3.format(fs.getStationResult().getNorth()));
        Assert.assertEquals("924.973", this.df3.format(fs.getStationResult().getAltitude()));

        Assert.assertEquals("-0.7", this.df1.format(fs.getResults().get(0).getvE()));
        Assert.assertEquals("-4.1", this.df1.format(fs.getResults().get(1).getvE()));
        Assert.assertEquals("1.5", this.df1.format(fs.getResults().get(2).getvE()));
        Assert.assertEquals("2.3", this.df1.format(fs.getResults().get(3).getvE()));

        Assert.assertEquals("-2.2", this.df1.format(fs.getResults().get(0).getvN()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(1).getvN()));
        Assert.assertEquals("-2.5", this.df1.format(fs.getResults().get(2).getvN()));
        Assert.assertEquals("0.9", this.df1.format(fs.getResults().get(3).getvN()));

        Assert.assertEquals("0.8", this.df1.format(fs.getsE()));
        Assert.assertEquals("0.8", this.df1.format(fs.getsN()));

        Assert.assertEquals("399.9697", this.df4.format(fs.getUnknownOrientation()));
    }

    /**
     * Test with Vz values provided.
     */
    @Test
    public void withVz() {
        Point p2 = new Point("2", 634.6482, 236.0624, MathUtils.IGNORE_DOUBLE, true);
        Point p3 = new Point("3", 643.1335, 159.6949, MathUtils.IGNORE_DOUBLE, true);
        Point p4 = new Point("4", 576.2674, 169.0361, MathUtils.IGNORE_DOUBLE, true);

        Measure m2 = new Measure(p2, 50.0, 98.505, 50.0138, MathUtils.IGNORE_DOUBLE, -1.0);
        Measure m3 = new Measure(p3, 150.0, 99.0011, 55.0068, MathUtils.IGNORE_DOUBLE, -2.0, 4.0);
        Measure m4 = new Measure(p4, 240.0, 102.1020, 40.0218, MathUtils.IGNORE_DOUBLE, 1.0, -1.0);


        FreeStation fs = new FreeStation("testVz", MathUtils.IGNORE_DOUBLE, false);
        fs.getMeasures().add(m2);
        fs.getMeasures().add(m3);
        fs.getMeasures().add(m4);

        try {
            fs.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("-0.0", this.df1.format(fs.getResults().get(0).getvE()));
        Assert.assertEquals("-0.0", this.df1.format(fs.getResults().get(0).getvN()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(0).getfS()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(0).getvA()));

        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(1).getvE()));
        Assert.assertEquals("-0.0", this.df1.format(fs.getResults().get(1).getvN()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(1).getfS()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(1).getvA()));

        Assert.assertEquals("-0.0", this.df1.format(fs.getResults().get(2).getvE()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(2).getvN()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(2).getfS()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(2).getvA()));

        Assert.assertEquals("400.0000", this.df4.format(fs.getUnknownOrientation()));
        Assert.assertEquals("0.0007", this.df4.format(fs.getsE()));
        Assert.assertEquals("0.0007", this.df4.format(fs.getsN()));
        Assert.assertTrue(MathUtils.isIgnorable(fs.getsA()));

        Assert.assertEquals("600.0000", this.df4.format(fs.getStationResult().getEast()));
        Assert.assertEquals("200.0000", this.df4.format(fs.getStationResult().getNorth()));
    }

    /**
     * This is a regression test for bug #731.
     */
    @Test
    public void issue731() {
        Point p9000 = new Point("1436", 615.0, 740.0, 554.0, true);
        Point p9001 = new Point("1437", 615.0, 810.0, 556.34, true);
        Point p9002 = new Point("1438", 687.0, 804.0, 558.33, true);
        Point p9003 = new Point("1439", 676.0, 743.0, 550.5, true);

        Measure m1 = new Measure(p9000, 0.0, 101.561, 52.525, 1.42);
        Measure m2 = new Measure(p9001, 101.1868, 98.238, 45.34, 1.4);
        Measure m3 = new Measure(p9002, 219.3087, 95.3558, 45.069, 1.5);
        Measure m4 = new Measure(p9003, 315.1179, 106.286, 46.055, 1.5);

        FreeStation fs = new FreeStation("test731", 1.52, false);
        fs.getMeasures().add(m1);
        fs.getMeasures().add(m2);
        fs.getMeasures().add(m3);
        fs.getMeasures().add(m4);

        try {
            fs.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("649.001", this.df3.format(fs.getStationResult().getEast()));
        Assert.assertEquals("780.023", this.df3.format(fs.getStationResult().getNorth()));
        Assert.assertEquals("555.04", this.df2.format(fs.getStationResult().getAltitude()));

        Assert.assertEquals("244.8155", this.df4.format(fs.getUnknownOrientation()));

        Assert.assertEquals("0.6", this.df1.format(fs.getsE()));
        Assert.assertEquals("0.6", this.df1.format(fs.getsN()));
        Assert.assertEquals("4.5", this.df1.format(fs.getsA()));

        Assert.assertEquals("1.8", this.df1.format(fs.getResults().get(0).getvE()));
        Assert.assertEquals("-0.2", this.df1.format(fs.getResults().get(0).getvN()));
        Assert.assertEquals("1.8", this.df1.format(fs.getResults().get(0).getfS()));
        Assert.assertEquals("-14.8", this.df1.format(fs.getResults().get(0).getvA()));

        Assert.assertEquals("0.8", this.df1.format(fs.getResults().get(1).getvE()));
        Assert.assertEquals("-0.6", this.df1.format(fs.getResults().get(1).getvN()));
        Assert.assertEquals("1.0", this.df1.format(fs.getResults().get(1).getfS()));
        Assert.assertEquals("7.5", this.df1.format(fs.getResults().get(1).getvA()));

        Assert.assertEquals("-0.2", this.df1.format(fs.getResults().get(2).getvE()));
        Assert.assertEquals("3.2", this.df1.format(fs.getResults().get(2).getvN()));
        Assert.assertEquals("3.2", this.df1.format(fs.getResults().get(2).getfS()));
        Assert.assertEquals("1.5", this.df1.format(fs.getResults().get(2).getvA()));

        Assert.assertEquals("-2.3", this.df1.format(fs.getResults().get(3).getvE()));
        Assert.assertEquals("-2.4", this.df1.format(fs.getResults().get(3).getvN()));
        Assert.assertEquals("3.4", this.df1.format(fs.getResults().get(3).getfS()));
        Assert.assertEquals("2.0", this.df1.format(fs.getResults().get(3).getvA()));

        try {
            // Results NEED to be the same when re-computing again
            fs.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("649.001", this.df3.format(fs.getStationResult().getEast()));
        Assert.assertEquals("780.023", this.df3.format(fs.getStationResult().getNorth()));
        Assert.assertEquals("555.04", this.df2.format(fs.getStationResult().getAltitude()));

        Assert.assertEquals("244.8155", this.df4.format(fs.getUnknownOrientation()));

        Assert.assertEquals("0.6", this.df1.format(fs.getsE()));
        Assert.assertEquals("0.6", this.df1.format(fs.getsN()));
        Assert.assertEquals("4.5", this.df1.format(fs.getsA()));

        Assert.assertEquals("1.8", this.df1.format(fs.getResults().get(0).getvE()));
        Assert.assertEquals("-0.2", this.df1.format(fs.getResults().get(0).getvN()));
        Assert.assertEquals("1.8", this.df1.format(fs.getResults().get(0).getfS()));
        Assert.assertEquals("-14.8", this.df1.format(fs.getResults().get(0).getvA()));

        Assert.assertEquals("0.8", this.df1.format(fs.getResults().get(1).getvE()));
        Assert.assertEquals("-0.6", this.df1.format(fs.getResults().get(1).getvN()));
        Assert.assertEquals("1.0", this.df1.format(fs.getResults().get(1).getfS()));
        Assert.assertEquals("7.5", this.df1.format(fs.getResults().get(1).getvA()));

        Assert.assertEquals("-0.2", this.df1.format(fs.getResults().get(2).getvE()));
        Assert.assertEquals("3.2", this.df1.format(fs.getResults().get(2).getvN()));
        Assert.assertEquals("3.2", this.df1.format(fs.getResults().get(2).getfS()));
        Assert.assertEquals("1.5", this.df1.format(fs.getResults().get(2).getvA()));

        Assert.assertEquals("-2.3", this.df1.format(fs.getResults().get(3).getvE()));
        Assert.assertEquals("-2.4", this.df1.format(fs.getResults().get(3).getvN()));
        Assert.assertEquals("3.4", this.df1.format(fs.getResults().get(3).getfS()));
        Assert.assertEquals("2.0", this.df1.format(fs.getResults().get(3).getvA()));
    }

    /**
     * This is a regression test for issue #759: calculation is wrong when DM1 and DM2 are provided.
     */
    @Test
    public void issue759() {
        Point p2 = new Point("2", 634.6482, 236.0624, MathUtils.IGNORE_DOUBLE, true);
        Point p3 = new Point("3", 643.1335, 159.6949, MathUtils.IGNORE_DOUBLE, true);
        Point p4 = new Point("4", 576.2674, 169.0361, MathUtils.IGNORE_DOUBLE, true);

        Measure m2 = new Measure(p2, 50.0, 100.0, 50.0, MathUtils.IGNORE_DOUBLE, -1.0);
        Measure m3 = new Measure(p3, 150.0, 100.0, 55.0, MathUtils.IGNORE_DOUBLE, -2.0, 4.0);
        Measure m4 = new Measure(p4, 240.0, 100.0, 40.0, MathUtils.IGNORE_DOUBLE, 1.0, -1.0);


        FreeStation fs = new FreeStation("test759", MathUtils.IGNORE_DOUBLE, false);
        fs.getMeasures().add(m2);
        fs.getMeasures().add(m3);
        fs.getMeasures().add(m4);

        try {
            fs.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(0).getvE()));
        Assert.assertEquals("-0.0", this.df1.format(fs.getResults().get(0).getvN()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(0).getfS()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(0).getvA()));

        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(1).getvE()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(1).getvN()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(1).getfS()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(1).getvA()));

        Assert.assertEquals("-0.0", this.df1.format(fs.getResults().get(2).getvE()));
        Assert.assertEquals("-0.0", this.df1.format(fs.getResults().get(2).getvN()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(2).getfS()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(2).getvA()));

        Assert.assertEquals("400.0000", this.df4.format(fs.getUnknownOrientation()));
        Assert.assertEquals("0.0008", this.df4.format(fs.getsE()));
        Assert.assertEquals("0.0008", this.df4.format(fs.getsN()));
        Assert.assertTrue(MathUtils.isIgnorable(fs.getsA()));

        Assert.assertEquals("600.0000", this.df4.format(fs.getStationResult().getEast()));
        Assert.assertEquals("200.0000", this.df4.format(fs.getStationResult().getNorth()));
    }

    /**
     * This is a regression test for issue #769: altitude calculation with conditions.
     */
    @Test
    public void issue769() {
        Point p2 = new Point("2", 634.6482, 236.0624, 501.0, true);
        Point p3 = new Point("3", 643.1335, 159.6949, MathUtils.IGNORE_DOUBLE, true);
        Point p4 = new Point("4", 576.2674, 169.0361, 500.3772, true);
        Point p5 = new Point("5", 600.0, 220.0, 501.4864, true);
        Point p6 = new Point("6", 600.0, 180.0, MathUtils.IGNORE_DOUBLE, true);
        double i = 1.625;

        Measure m2 = new Measure(p2, 50.0, 98.505, 50.0138, 1.524, -1.0);
        Measure m3 = new Measure(p3, 150.0, 99.0011, 55.0068, MathUtils.IGNORE_DOUBLE, -2.0, 4.0);
        Measure m4 = new Measure(p4, 240.0, 102.1020, 40.0218, 2.0, 1.0, -1.0);
        Measure m5 = new Measure(p5, 0.0, 100.0, 20.0, 1.212);
        Measure m6 = new Measure(p6, 200.0, 99.558, 20.0, 1.415);

        m2.setMeasureNumber("2");
        m3.setMeasureNumber("3");
        m4.setMeasureNumber("4");
        m5.setMeasureNumber("5");
        m6.setMeasureNumber("6");

        FreeStation fs = new FreeStation("test769", i, false);
        fs.getMeasures().add(m2);
        fs.getMeasures().add(m3);
        fs.getMeasures().add(m4);
        fs.getMeasures().add(m5);
        fs.getMeasures().add(m6);

        try {
            fs.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("-0.0", this.df1.format(fs.getResults().get(0).getvE()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(0).getvN()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(0).getfS()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(0).getvA()));

        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(1).getvE()));
        Assert.assertEquals("-0.0", this.df1.format(fs.getResults().get(1).getvN()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(1).getfS()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(1).getvA()));

        Assert.assertEquals("-0.0", this.df1.format(fs.getResults().get(2).getvE()));
        Assert.assertEquals("-0.0", this.df1.format(fs.getResults().get(2).getvN()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(2).getfS()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(2).getvA()));

        Assert.assertEquals("399.9998", this.df4.format(fs.getUnknownOrientation()));
        Assert.assertEquals("0.0095", this.df4.format(fs.getsE()));
        Assert.assertEquals("0.0095", this.df4.format(fs.getsN()));
        Assert.assertEquals("0.0000", this.df4.format(fs.getsA()));

        Assert.assertEquals("599.9999", this.df4.format(fs.getStationResult().getEast()));
        Assert.assertEquals("199.9999", this.df4.format(fs.getStationResult().getNorth()));
        Assert.assertEquals("501.0734", this.df4.format(fs.getStationResult().getAltitude()));
    }

    @Test
    public void issue769bis() {
        Point p6 = new Point("6", 622.475, 210.99, 100.4, true);
        Point p77 = new Point("77", 635.223, 145.831, 99.931, true);
        Point p8 = new Point("8", 635.417, 177.289, 99.144, true);
        Point p9 = new Point("9", 595.012, 210.991, 100.068, true);
        Point p10 = new Point("10", 598.055, 218.982, MathUtils.IGNORE_DOUBLE, true);
        double i = 1.6;
        Measure m6 = new Measure(p6, 10.562, 99.124, 25.03, 1.57);
        Measure m77 = new Measure(p77, 102.07, 100.068, 65.2, 1.62, 0.8, -0.6);
        Measure m8 = new Measure(p8, 75.852, 101.162, 42.07, 1.74);
        Measure m9 = new Measure(p9, 312.411, 99.724, 12.07, 1.6);
        Measure m10 = new Measure(p10, 333.02, 98.18, 19.08, 2.0);

        FreeStation fs = new FreeStation("9001", i, false);
        fs.getMeasures().add(m6);
        fs.getMeasures().add(m77);
        fs.getMeasures().add(m8);
        fs.getMeasures().add(m9);
        fs.getMeasures().add(m10);

        try {
            fs.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("0.8", this.df1.format(fs.getResults().get(0).getvE()));
        Assert.assertEquals("1.4", this.df1.format(fs.getResults().get(0).getvN()));
        Assert.assertEquals("1.6", this.df1.format(fs.getResults().get(0).getfS()));
        Assert.assertEquals("-0.6", this.df1.format(fs.getResults().get(0).getvA()));

        Assert.assertEquals("-0.8", this.df1.format(fs.getResults().get(1).getvE()));
        Assert.assertEquals("0.1", this.df1.format(fs.getResults().get(1).getvN()));
        Assert.assertEquals("0.8", this.df1.format(fs.getResults().get(1).getfS()));
        Assert.assertTrue(MathUtils.isIgnorable(fs.getResults().get(1).getvA()));

        Assert.assertEquals("-0.3", this.df1.format(fs.getResults().get(2).getvE()));
        Assert.assertEquals("0.4", this.df1.format(fs.getResults().get(2).getvN()));
        Assert.assertEquals("0.5", this.df1.format(fs.getResults().get(2).getfS()));
        Assert.assertEquals("-3.2", this.df1.format(fs.getResults().get(2).getvA()));

        Assert.assertEquals("0.4", this.df1.format(fs.getResults().get(3).getvE()));
        Assert.assertEquals("-0.5", this.df1.format(fs.getResults().get(3).getvN()));
        Assert.assertEquals("0.6", this.df1.format(fs.getResults().get(3).getfS()));
        Assert.assertEquals("0.4", this.df1.format(fs.getResults().get(3).getvA()));

        Assert.assertEquals("-0.1", this.df1.format(fs.getResults().get(4).getvE()));
        Assert.assertEquals("-1.4", this.df1.format(fs.getResults().get(4).getvN()));
        Assert.assertEquals("1.4", this.df1.format(fs.getResults().get(4).getfS()));
        Assert.assertTrue(MathUtils.isIgnorable(fs.getResults().get(4).getvA()));

        Assert.assertEquals("60.4478", this.df4.format(fs.getUnknownOrientation()));
        Assert.assertEquals("0.5", this.df1.format(fs.getsE()));
        Assert.assertEquals("0.5", this.df1.format(fs.getsN()));
        Assert.assertEquals("0.6", this.df1.format(fs.getsA()));

        Assert.assertEquals("600.007", this.df3.format(fs.getStationResult().getEast()));
        Assert.assertEquals("199.997", this.df3.format(fs.getStationResult().getNorth()));
        Assert.assertEquals("100.020", this.df3.format(fs.getStationResult().getAltitude()));
    }

    /**
     * This is a regression test for bug #831. The calculation must change the zenithal angle of
     * measures to 100.0 if not provided.
     * This test is the same as testFreeStation750 but without using 100.0 for the zenithal angle
     * values of the measures.
     */
    @Test
    public void issue831() {
        Point p2 = new Point("2", 634.6482, 236.0624, MathUtils.IGNORE_DOUBLE, true);
        Point p3 = new Point("3", 643.1335, 159.6949, MathUtils.IGNORE_DOUBLE, true);
        Point p4 = new Point("4", 576.2674, 169.0361, MathUtils.IGNORE_DOUBLE, true);

        Measure m2 = new Measure(p2, 50.0, MathUtils.IGNORE_DOUBLE, 50.0, MathUtils.IGNORE_DOUBLE, -1.0);
        Measure m3 = new Measure(p3, 150.0, MathUtils.IGNORE_DOUBLE, 55.0, MathUtils.IGNORE_DOUBLE, -2.0, 4.0);
        Measure m4 = new Measure(p4, 240.0, MathUtils.IGNORE_DOUBLE, 40.0, MathUtils.IGNORE_DOUBLE, 1.0, -1.0);


        FreeStation fs = new FreeStation("test759", MathUtils.IGNORE_DOUBLE, false);
        fs.getMeasures().add(m2);
        fs.getMeasures().add(m3);
        fs.getMeasures().add(m4);

        try {
            fs.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(0).getvE()));
        Assert.assertEquals("-0.0", this.df1.format(fs.getResults().get(0).getvN()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(0).getfS()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(0).getvA()));

        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(1).getvE()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(1).getvN()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(1).getfS()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(1).getvA()));

        Assert.assertEquals("-0.0", this.df1.format(fs.getResults().get(2).getvE()));
        Assert.assertEquals("-0.0", this.df1.format(fs.getResults().get(2).getvN()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(2).getfS()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(2).getvA()));

        Assert.assertEquals("400.0000", this.df4.format(fs.getUnknownOrientation()));
        Assert.assertEquals("0.0008", this.df4.format(fs.getsE()));
        Assert.assertEquals("0.0008", this.df4.format(fs.getsN()));
        Assert.assertTrue(MathUtils.isIgnorable(fs.getsA()));

        Assert.assertEquals("600.0000", this.df4.format(fs.getStationResult().getEast()));
        Assert.assertEquals("200.0000", this.df4.format(fs.getStationResult().getNorth()));
    }


    // TD-NK_1.4.1
    @Test
    public void tdNK141() {
        Point p1 = new Point("1401", 615.0, 740.0, 0.0, true, false);
        Point p2 = new Point("1402", 615.0, 810.0, 0.0, true, false);
        Point p3 = new Point("1403", 687.0, 804.0, 0.0, true, false);
        Point p4 = new Point("1404", 676.0, 743.0, 0.0, true, false);

        Measure m1 = new Measure(p1, 0.0000, 100, 52.498);
        Measure m2 = new Measure(p2, 101.1768, 100, 45.343);
        Measure m3 = new Measure(p3, 219.2887, 100, 44.944);
        Measure m4 = new Measure(p4, 315.0179, 100, 45.804);

        FreeStation fs = new FreeStation("1405", false);
        fs.getMeasures().add(m1);
        fs.getMeasures().add(m2);
        fs.getMeasures().add(m3);
        fs.getMeasures().add(m4);

        try {
            fs.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("649.000", this.df3.format(fs.getStationResult().getEast()));
        Assert.assertEquals("780.000", this.df3.format(fs.getStationResult().getNorth()));

        Assert.assertEquals("244.8495", this.df4.format(fs.getUnknownOrientation()));

        Assert.assertEquals("-0.0", this.df1.format(fs.getResults().get(0).getvE()));
        Assert.assertEquals("-0.0", this.df1.format(fs.getResults().get(0).getvN()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(0).getfS()));

        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(1).getvE()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(1).getvN()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(1).getfS()));

        Assert.assertEquals("-0.0", this.df1.format(fs.getResults().get(2).getvE()));
        Assert.assertEquals("-0.0", this.df1.format(fs.getResults().get(2).getvN()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(2).getfS()));

        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(3).getvE()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(3).getvN()));
        Assert.assertEquals("0.0", this.df1.format(fs.getResults().get(3).getfS()));

    }

    // TD-NK_1.4.2
    @Test
    public void tdNK142() {
        Point p1 = new Point("1406", 615.0, 740.0, MathUtils.IGNORE_DOUBLE, true, false);
        Point p2 = new Point("1407", 615.0, 810.0, MathUtils.IGNORE_DOUBLE, true, false);
        Point p3 = new Point("1408", 687.0, 804.0, MathUtils.IGNORE_DOUBLE, true, false);
        Point p4 = new Point("1409", 676.0, 743.0, MathUtils.IGNORE_DOUBLE, true, false);

        Measure m1 = new Measure(p1, 0.0, MathUtils.IGNORE_DOUBLE, 52.618);
        Measure m2 = new Measure(p2, 101.218, MathUtils.IGNORE_DOUBLE, 45.353);
        Measure m3 = new Measure(p3, 219.3067, MathUtils.IGNORE_DOUBLE, 44.984);
        Measure m4 = new Measure(p4, 315.1130, MathUtils.IGNORE_DOUBLE, 45.924);

        FreeStation fs = new FreeStation("1410", false);
        fs.getMeasures().add(m1);
        fs.getMeasures().add(m2);
        fs.getMeasures().add(m3);
        fs.getMeasures().add(m4);

        try {
            fs.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("648.998", this.df3.format(fs.getStationResult().getEast()));
        Assert.assertEquals("780.039", this.df3.format(fs.getStationResult().getNorth()));

        Assert.assertEquals("244.8102", this.df4.format(fs.getUnknownOrientation()));

        Assert.assertEquals("-0.4", this.df1.format(fs.getResults().get(0).getvE()));
        Assert.assertEquals("-1.3", this.df1.format(fs.getResults().get(0).getvN()));
        Assert.assertEquals("1.4", this.df1.format(fs.getResults().get(0).getfS()));

        Assert.assertEquals("4.2", this.df1.format(fs.getResults().get(1).getvE()));
        Assert.assertEquals("0.2", this.df1.format(fs.getResults().get(1).getvN()));
        Assert.assertEquals("4.2", this.df1.format(fs.getResults().get(1).getfS()));

        Assert.assertEquals("-3.4", this.df1.format(fs.getResults().get(2).getvE()));
        Assert.assertEquals("3.7", this.df1.format(fs.getResults().get(2).getvN()));
        Assert.assertEquals("5.0", this.df1.format(fs.getResults().get(2).getfS()));

        Assert.assertEquals("-0.4", this.df1.format(fs.getResults().get(3).getvE()));
        Assert.assertEquals("-2.6", this.df1.format(fs.getResults().get(3).getvN()));
        Assert.assertEquals("2.6", this.df1.format(fs.getResults().get(3).getfS()));
    }
}
