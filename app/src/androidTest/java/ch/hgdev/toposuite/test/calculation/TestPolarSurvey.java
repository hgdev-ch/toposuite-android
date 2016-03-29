package ch.hgdev.toposuite.test.calculation;

import junit.framework.Assert;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.calculation.PolarSurvey;
import ch.hgdev.toposuite.calculation.PolarSurvey.Result;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

/**
 * Tests for the PolarSurvey class.
 * 
 * @author HGdev
 * 
 */
public class TestPolarSurvey extends CalculationTest {

    public void test1() {
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

        lp.compute();

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

    public void test2() {
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

        lp.compute();

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
}