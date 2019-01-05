package ch.hgdev.toposuite.test.calculation;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.calculation.PolarSurvey;
import ch.hgdev.toposuite.calculation.PolarSurvey.Result;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.test.testutils.CalculationTestRunner;
import ch.hgdev.toposuite.utils.MathUtils;

/**
 * Tests for the PolarSurvey class.
 *
 * @author HGdev
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class PolarSurveyTest extends CalculationTestRunner {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void simple1() {
        Point station = new Point("34", 556506.667, 172513.91, 620.34, true);
        double i = 1.63;
        double z0 = 233.2435;

        Measure m2 = new Measure(
                null, 288.833, 96.1645, 12.621, 1.40, MathUtils.IGNORE_DOUBLE,
                MathUtils.IGNORE_DOUBLE);
        Measure m3 = new Measure(
                null, 317.0352, 95.2922, 30.996, 1.63, MathUtils.IGNORE_DOUBLE,
                MathUtils.IGNORE_DOUBLE);
        Measure m5 = new Measure(
                null, 5.9274, 107.3266, 32.265, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE,
                1.1);
        Measure m6 = new Measure(
                null, 45.9760, 96.9650, 15.864, MathUtils.IGNORE_DOUBLE, 0.5, 1.5);

        PolarSurvey lp = new PolarSurvey(station, z0, i, false);

        lp.getDeterminations().add(m2);
        lp.getDeterminations().add(m3);
        lp.getDeterminations().add(m5);
        lp.getDeterminations().add(m6);

        try {
            lp.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Result r2 = lp.getResults().get(0);
        Result r3 = lp.getResults().get(1);
        Result r5 = lp.getResults().get(2);
        Result r6 = lp.getResults().get(3);

        Assert.assertEquals("556518.515", this.df3.format(r2.getEast()));
        Assert.assertEquals("172509.628", this.df3.format(r2.getNorth()));
        Assert.assertEquals("621.33", this.df2.format(r2.getAltitude()));

        Assert.assertEquals("556528.429", this.df3.format(r3.getEast()));
        Assert.assertEquals("172491.957", this.df3.format(r3.getNorth()));
        Assert.assertEquals("622.63", this.df2.format(r3.getAltitude()));

        Assert.assertEquals("556487.532", this.df3.format(r5.getEast()));
        Assert.assertEquals("172486.838", this.df3.format(r5.getNorth()));
        Assert.assertEquals(MathUtils.IGNORE_DOUBLE, r5.getAltitude());

        Assert.assertEquals("556490.077", this.df3.format(r6.getEast()));
        Assert.assertEquals("172508.822", this.df3.format(r6.getNorth()));
        Assert.assertEquals(MathUtils.IGNORE_DOUBLE, r6.getAltitude());
    }

    @Test
    public void simple2() {
        Point station = new Point("46", 556517.541, 172491.482, 624.14, true);
        double i = 1.58;
        double z0 = 371.2579;

        Measure m1 = new Measure(
                null, 353.0032, 102.6626, 27.032, 1.60, MathUtils.IGNORE_DOUBLE,
                MathUtils.IGNORE_DOUBLE);
        Measure m2 = new Measure(
                null, 32.205, 109.2742, 18.393, 1.70, MathUtils.IGNORE_DOUBLE,
                MathUtils.IGNORE_DOUBLE);
        Measure m3 = new Measure(
                null, 126.0412, 108.9541, 11.056, 1.58, MathUtils.IGNORE_DOUBLE,
                MathUtils.IGNORE_DOUBLE);
        Measure m4 = new Measure(
                null, 278.5222, 91.7697, 14.117, 1.60, MathUtils.IGNORE_DOUBLE,
                MathUtils.IGNORE_DOUBLE);
        Measure m5 = new Measure(
                null, 321.489, 115.1289, 31.219, MathUtils.IGNORE_DOUBLE, -1.2,
                MathUtils.IGNORE_DOUBLE);

        PolarSurvey lp = new PolarSurvey(station, z0, i, false);

        lp.getDeterminations().add(m1);
        lp.getDeterminations().add(m2);
        lp.getDeterminations().add(m3);
        lp.getDeterminations().add(m4);
        lp.getDeterminations().add(m5);

        try {
            lp.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Result r1 = lp.getResults().get(0);
        Result r2 = lp.getResults().get(1);
        Result r3 = lp.getResults().get(2);
        Result r4 = lp.getResults().get(3);
        Result r5 = lp.getResults().get(4);

        Assert.assertEquals("556492.47", this.df2.format(r1.getEast()));
        Assert.assertEquals("172501.527", this.df3.format(r1.getNorth()));
        Assert.assertEquals("622.99", this.df2.format(r1.getAltitude()));

        Assert.assertEquals("556518.53", this.df2.format(r2.getEast()));
        Assert.assertEquals("172509.653", this.df3.format(r2.getNorth()));
        Assert.assertEquals("621.35", this.df2.format(r2.getAltitude()));

        Assert.assertEquals("556528.478", this.df3.format(r3.getEast()));
        Assert.assertEquals("172491.946", this.df3.format(r3.getNorth()));
        Assert.assertEquals("622.59", this.df2.format(r3.getAltitude()));

        Assert.assertEquals("556507.676", this.df3.format(r4.getEast()));
        Assert.assertEquals("172481.549", this.df3.format(r4.getNorth()));
        Assert.assertEquals("625.94", this.df2.format(r4.getAltitude()));

        Assert.assertEquals("556487.533", this.df3.format(r5.getEast()));
        Assert.assertEquals("172486.84", this.df2.format(r5.getNorth()));
    }

    // This is a regression test for bug #826
    // The calculation must change the zenithal angle of measures to 100.0 if not provided.
    @Test
    public void issue826() {
        Point station = new Point("station", 1205.0, 2470.0, MathUtils.IGNORE_DOUBLE, true);
        double z0 = 120.02;

        Measure m1 = new Measure(null, 41.741, 100.0, 36.0);

        PolarSurvey lp = new PolarSurvey(station, z0, MathUtils.IGNORE_DOUBLE, false);
        lp.getDeterminations().add(m1);

        try {
            lp.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Result r1 = lp.getResults().get(0);
        Assert.assertEquals("1225.347", this.df3.format(r1.getEast()));
        Assert.assertEquals("2440.301", this.df3.format(r1.getNorth()));
        Assert.assertTrue(MathUtils.isIgnorable(r1.getAltitude()));

        // now test with zenithal angle set to MathUtils.IGNORE_DOUBLE
        // we should obtain the same results
        m1 = new Measure(null, 41.741, MathUtils.IGNORE_DOUBLE, 36.0);

        lp = new PolarSurvey(station, z0, MathUtils.IGNORE_DOUBLE, false);
        lp.getDeterminations().add(m1);

        try {
            lp.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        r1 = lp.getResults().get(0);
        Assert.assertEquals("1225.347", this.df3.format(r1.getEast()));
        Assert.assertEquals("2440.301", this.df3.format(r1.getNorth()));
        Assert.assertTrue(MathUtils.isIgnorable(r1.getAltitude()));
    }

    // TD-NK_1.2.1.1
    @Test
    public void tdNK1211() {
        Point station = new Point("1201", 600.000, 200.000, MathUtils.IGNORE_DOUBLE, true);
        double i = 1.63;
        double z0 = 344.0897;

        Measure m2 = new Measure(
                null, 35.2973, 102.2463, 29.675, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, -0.07);
        Measure m3 = new Measure(
                null, 21.695, 101.2956, 18.597, MathUtils.IGNORE_DOUBLE, -1.63, MathUtils.IGNORE_DOUBLE);
        Measure m4 = new Measure(
                null, 395.7896, 104.5542, 28.263, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE);
        Measure m5 = new Measure(
                null, 390.2218, 96.2145, 33.514, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, 0.1);
        Measure m6 = new Measure(
                null, 382.6538, 98.2586, 44.784, MathUtils.IGNORE_DOUBLE, 0.39, MathUtils.IGNORE_DOUBLE);
        Measure m7 = new Measure(
                null, 399.0187, 99.9143, 50.312, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE);
        Measure m8 = new Measure(
                null, 9.0583, 97.1589, 40.697, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE);
        Measure m9 = new Measure(
                null, 15.9901, 101.1111, 36.362, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE);

        PolarSurvey lp = new PolarSurvey(station, z0, i, false);

        lp.getDeterminations().add(m2);
        lp.getDeterminations().add(m3);
        lp.getDeterminations().add(m4);
        lp.getDeterminations().add(m5);
        lp.getDeterminations().add(m6);
        lp.getDeterminations().add(m7);
        lp.getDeterminations().add(m8);
        lp.getDeterminations().add(m9);

        try {
            lp.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Result r2 = lp.getResults().get(0);
        Result r3 = lp.getResults().get(1);
        Result r4 = lp.getResults().get(2);
        Result r5 = lp.getResults().get(3);
        Result r6 = lp.getResults().get(4);
        Result r7 = lp.getResults().get(5);
        Result r8 = lp.getResults().get(6);
        Result r9 = lp.getResults().get(7);

        Assert.assertEquals("590.587", this.df3.format(r2.getEast()));
        Assert.assertEquals("228.049", this.df3.format(r2.getNorth()));
        Assert.assertEquals(MathUtils.IGNORE_DOUBLE, r2.getAltitude());

        Assert.assertEquals("589.081", this.df3.format(r3.getEast()));
        Assert.assertEquals("215.137", this.df3.format(r3.getNorth()));
        Assert.assertEquals(MathUtils.IGNORE_DOUBLE, r3.getAltitude());

        Assert.assertEquals("577.162", this.df3.format(r4.getEast()));
        Assert.assertEquals("216.527", this.df3.format(r4.getNorth()));
        Assert.assertEquals(MathUtils.IGNORE_DOUBLE, r4.getAltitude());

        Assert.assertEquals("571.202", this.df3.format(r5.getEast()));
        Assert.assertEquals("217.222", this.df3.format(r5.getNorth()));
        Assert.assertEquals(MathUtils.IGNORE_DOUBLE, r5.getAltitude());

        Assert.assertEquals("559.284", this.df3.format(r6.getEast()));
        Assert.assertEquals("218.614", this.df3.format(r6.getNorth()));
        Assert.assertEquals(MathUtils.IGNORE_DOUBLE, r6.getAltitude());

        Assert.assertEquals("560.789", this.df3.format(r7.getEast()));
        Assert.assertEquals("231.524", this.df3.format(r7.getNorth()));
        Assert.assertEquals(MathUtils.IGNORE_DOUBLE, r7.getAltitude());

        Assert.assertEquals("572.708", this.df3.format(r8.getEast()));
        Assert.assertEquals("230.134", this.df3.format(r8.getNorth()));
        Assert.assertEquals(MathUtils.IGNORE_DOUBLE, r8.getAltitude());

        Assert.assertEquals("578.667", this.df3.format(r9.getEast()));
        Assert.assertEquals("229.440", this.df3.format(r9.getNorth()));
        Assert.assertEquals(MathUtils.IGNORE_DOUBLE, r9.getAltitude());
    }

    /**
     * This is a regression test. We want to make sure that we obtain the same results when we use
     * setters and a parameterless constructor as when we use a comprehensive constructor.
     */
    @Test
    public void testSetters() {
        Point station = new Point("1", 600.0, 200.0, MathUtils.IGNORE_DOUBLE, true);
        double i = MathUtils.IGNORE_DOUBLE;
        double z0 = 59.03;

        Measure m = new Measure(null, 120.01, MathUtils.IGNORE_DOUBLE, 20.002);

        PolarSurvey ps = new PolarSurvey(false);
        ps.setStation(station);
        ps.setInstrumentHeight(i);
        ps.setUnknownOrientation(z0);
        ps.getDeterminations().add(m);

        try {
            ps.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Result r = ps.getResults().get(0);

        Assert.assertEquals("606.467", this.df3.format(r.getEast()));
        Assert.assertEquals("181.072", this.df3.format(r.getNorth()));
        Assert.assertEquals(MathUtils.IGNORE_DOUBLE, r.getAltitude());

        ps = new PolarSurvey(station, z0, i, false);
        ps.getDeterminations().add(m);

        try {
            ps.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        r = ps.getResults().get(0);

        Assert.assertEquals("606.467", this.df3.format(r.getEast()));
        Assert.assertEquals("181.072", this.df3.format(r.getNorth()));
        Assert.assertEquals(MathUtils.IGNORE_DOUBLE, r.getAltitude());
    }
}