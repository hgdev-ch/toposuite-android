package ch.hgdev.toposuite.test.calculation;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.OrthogonalBase;
import ch.hgdev.toposuite.calculation.OrthogonalImplantation;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.test.testutils.CalculationTestRunner;
import ch.hgdev.toposuite.utils.MathUtils;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class OrthogonalImplantationTest extends CalculationTestRunner {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void simple() {
        Point origin = new Point("210", 556490.077, 172508.822, MathUtils.IGNORE_DOUBLE, true, false);
        Point extremity = new Point("211", 556517.541, 172491.482, MathUtils.IGNORE_DOUBLE, true, false);
        OrthogonalBase base = new OrthogonalBase(origin, extremity);

        OrthogonalImplantation oi = new OrthogonalImplantation(base, false);

        oi.getMeasures().add(new Point("111", 556500.900, 172489.700, MathUtils.IGNORE_DOUBLE, true, false));
        oi.getMeasures().add(new Point("222", 556488.900, 172523.100, MathUtils.IGNORE_DOUBLE, true, false));
        oi.getMeasures().add(new Point("333", 556474.900, 172504.700, MathUtils.IGNORE_DOUBLE, true, false));
        oi.getMeasures().add(new Point("444", 556524.300, 172477.900, MathUtils.IGNORE_DOUBLE, true, false));
        oi.getMeasures().add(new Point("555", 556528.454, 172491.952, MathUtils.IGNORE_DOUBLE, true, false));
        oi.getMeasures().add(new Point("666", 556502.300, 172504.500, MathUtils.IGNORE_DOUBLE, true, false));

        try {
            oi.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("19.360", this.df3.format(oi.getResults().get(0).getAbscissa()));
        Assert.assertEquals("10.391", this.df3.format(oi.getResults().get(0).getOrdinate()));

        Assert.assertEquals("-8.618", this.df3.format(oi.getResults().get(1).getAbscissa()));
        Assert.assertEquals("-11.445", this.df3.format(oi.getResults().get(1).getOrdinate()));

        Assert.assertEquals("-10.633", this.df3.format(oi.getResults().get(2).getAbscissa()));
        Assert.assertEquals("11.588", this.df3.format(oi.getResults().get(2).getOrdinate()));

        Assert.assertEquals("45.446", this.df3.format(oi.getResults().get(3).getAbscissa()));
        Assert.assertEquals("7.876", this.df3.format(oi.getResults().get(3).getOrdinate()));

        Assert.assertEquals("41.457", this.df3.format(oi.getResults().get(4).getAbscissa()));
        Assert.assertEquals("-6.224", this.df3.format(oi.getResults().get(4).getOrdinate()));

        Assert.assertEquals("12.643", this.df3.format(oi.getResults().get(5).getAbscissa()));
        Assert.assertEquals("-2.871", this.df3.format(oi.getResults().get(5).getOrdinate()));
    }

    // TD-NK_2.3.1
    @Test
    public void tdNK231() {
        Point origin = new Point("2301", 600.000, 200.000, MathUtils.IGNORE_DOUBLE, true, false);
        Point extremity = new Point("2302", 665.000, 200.000, MathUtils.IGNORE_DOUBLE, true, false);
        OrthogonalBase base = new OrthogonalBase(origin, extremity);

        OrthogonalImplantation oi = new OrthogonalImplantation(base, false);

        oi.getMeasures().add(new Point("2303", 593.750, 205.000, MathUtils.IGNORE_DOUBLE, true, false));
        oi.getMeasures().add(new Point("2304", 605.000, 207.500, MathUtils.IGNORE_DOUBLE, true, false));
        oi.getMeasures().add(new Point("2305", 620.000, 215.000, MathUtils.IGNORE_DOUBLE, true, false));
        oi.getMeasures().add(new Point("2306", 637.350, 210.000, MathUtils.IGNORE_DOUBLE, true, false));
        oi.getMeasures().add(new Point("2307", 652.500, 220.000, MathUtils.IGNORE_DOUBLE, true, false));
        oi.getMeasures().add(new Point("2308", 660.000, 212.500, MathUtils.IGNORE_DOUBLE, true, false));
        oi.getMeasures().add(new Point("2309", 666.250, 267.500, MathUtils.IGNORE_DOUBLE, true, false));
        oi.getMeasures().add(new Point("2310", 672.500, 193.750, MathUtils.IGNORE_DOUBLE, true, false));
        oi.getMeasures().add(new Point("2311", 655.000, 190.000, MathUtils.IGNORE_DOUBLE, true, false));
        oi.getMeasures().add(new Point("2312", 640.000, 185.000, MathUtils.IGNORE_DOUBLE, true, false));
        oi.getMeasures().add(new Point("2313", 630.000, 195.000, MathUtils.IGNORE_DOUBLE, true, false));
        oi.getMeasures().add(new Point("2314", 617.500, 197.500, MathUtils.IGNORE_DOUBLE, true, false));
        oi.getMeasures().add(new Point("2315", 610.000, 190.000, MathUtils.IGNORE_DOUBLE, true, false));
        oi.getMeasures().add(new Point("2316", 601.250, 195.000, MathUtils.IGNORE_DOUBLE, true, false));

        try {
            oi.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("-6.250", this.df3.format(oi.getResults().get(0).getAbscissa()));
        Assert.assertEquals("-5.000", this.df3.format(oi.getResults().get(0).getOrdinate()));

        Assert.assertEquals("5.000", this.df3.format(oi.getResults().get(1).getAbscissa()));
        Assert.assertEquals("-7.500", this.df3.format(oi.getResults().get(1).getOrdinate()));

        Assert.assertEquals("20.000", this.df3.format(oi.getResults().get(2).getAbscissa()));
        Assert.assertEquals("-15.000", this.df3.format(oi.getResults().get(2).getOrdinate()));

        Assert.assertEquals("37.350", this.df3.format(oi.getResults().get(3).getAbscissa()));
        Assert.assertEquals("-10.000", this.df3.format(oi.getResults().get(3).getOrdinate()));

        Assert.assertEquals("52.500", this.df3.format(oi.getResults().get(4).getAbscissa()));
        Assert.assertEquals("-20.000", this.df3.format(oi.getResults().get(4).getOrdinate()));

        Assert.assertEquals("60.000", this.df3.format(oi.getResults().get(5).getAbscissa()));
        Assert.assertEquals("-12.500", this.df3.format(oi.getResults().get(5).getOrdinate()));

        Assert.assertEquals("66.250", this.df3.format(oi.getResults().get(6).getAbscissa()));
        Assert.assertEquals("-67.500", this.df3.format(oi.getResults().get(6).getOrdinate()));

        Assert.assertEquals("72.500", this.df3.format(oi.getResults().get(7).getAbscissa()));
        Assert.assertEquals("6.250", this.df3.format(oi.getResults().get(7).getOrdinate()));

        Assert.assertEquals("55.000", this.df3.format(oi.getResults().get(8).getAbscissa()));
        Assert.assertEquals("10.000", this.df3.format(oi.getResults().get(8).getOrdinate()));

        Assert.assertEquals("40.000", this.df3.format(oi.getResults().get(9).getAbscissa()));
        Assert.assertEquals("15.000", this.df3.format(oi.getResults().get(9).getOrdinate()));

        Assert.assertEquals("30.000", this.df3.format(oi.getResults().get(10).getAbscissa()));
        Assert.assertEquals("5.000", this.df3.format(oi.getResults().get(10).getOrdinate()));

        Assert.assertEquals("17.500", this.df3.format(oi.getResults().get(11).getAbscissa()));
        Assert.assertEquals("2.500", this.df3.format(oi.getResults().get(11).getOrdinate()));

        Assert.assertEquals("10.000", this.df3.format(oi.getResults().get(12).getAbscissa()));
        Assert.assertEquals("10.000", this.df3.format(oi.getResults().get(12).getOrdinate()));

        Assert.assertEquals("1.250", this.df3.format(oi.getResults().get(13).getAbscissa()));
        Assert.assertEquals("5.000", this.df3.format(oi.getResults().get(13).getOrdinate()));
    }
}
