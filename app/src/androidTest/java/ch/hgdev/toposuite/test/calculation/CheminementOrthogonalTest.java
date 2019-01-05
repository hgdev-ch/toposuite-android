package ch.hgdev.toposuite.test.calculation;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.CheminementOrthogonal;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.test.testutils.CalculationTestRunner;
import ch.hgdev.toposuite.utils.MathUtils;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class CheminementOrthogonalTest extends CalculationTestRunner {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void tdNK2211() {
        Point origin = new Point("2201", 600.0, 360.0, MathUtils.IGNORE_DOUBLE, true, false);
        Point extremity = new Point("2202", 616.0, 354.0, MathUtils.IGNORE_DOUBLE, true, false);

        CheminementOrthogonal co = new CheminementOrthogonal(origin, extremity, false);
        co.getMeasures().add(new CheminementOrthogonal.Measure("2203", 4.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2204", -3.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2205", 4.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2206", 5.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2207", -2.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2208", 3.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2209", -1.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2210", -5.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2211", 2.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2212", -3.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2213", 3.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2202", 9.0));

        try {
            co.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("1.00000", this.df5.format(co.getScale()));
        Assert.assertEquals("0.000", this.df3.format(co.getFs()));
        Assert.assertEquals("0.000", this.df3.format(co.getfE()));
        Assert.assertEquals("0.000", this.df3.format(co.getfN()));

        Assert.assertEquals("17.088", this.df3.format(co.getOrthogonalBase().getCalculatedDistance()));

        Assert.assertEquals("604.000", this.df3.format(co.getResults().get(0).getEast()));
        Assert.assertEquals("360.000", this.df3.format(co.getResults().get(0).getNorth()));

        Assert.assertEquals("604.000", this.df3.format(co.getResults().get(1).getEast()));
        Assert.assertEquals("363.000", this.df3.format(co.getResults().get(1).getNorth()));

        Assert.assertEquals("608.000", this.df3.format(co.getResults().get(2).getEast()));
        Assert.assertEquals("363.000", this.df3.format(co.getResults().get(2).getNorth()));

        Assert.assertEquals("608.000", this.df3.format(co.getResults().get(3).getEast()));
        Assert.assertEquals("358.000", this.df3.format(co.getResults().get(3).getNorth()));

        Assert.assertEquals("610.000", this.df3.format(co.getResults().get(4).getEast()));
        Assert.assertEquals("358.000", this.df3.format(co.getResults().get(4).getNorth()));

        Assert.assertEquals("610.000", this.df3.format(co.getResults().get(5).getEast()));
        Assert.assertEquals("355.000", this.df3.format(co.getResults().get(5).getNorth()));

        Assert.assertEquals("611.000", this.df3.format(co.getResults().get(6).getEast()));
        Assert.assertEquals("355.000", this.df3.format(co.getResults().get(6).getNorth()));

        Assert.assertEquals("611.000", this.df3.format(co.getResults().get(7).getEast()));
        Assert.assertEquals("360.000", this.df3.format(co.getResults().get(7).getNorth()));

        Assert.assertEquals("613.000", this.df3.format(co.getResults().get(8).getEast()));
        Assert.assertEquals("360.000", this.df3.format(co.getResults().get(8).getNorth()));

        Assert.assertEquals("613.000", this.df3.format(co.getResults().get(9).getEast()));
        Assert.assertEquals("363.000", this.df3.format(co.getResults().get(9).getNorth()));

        Assert.assertEquals("616.000", this.df3.format(co.getResults().get(10).getEast()));
        Assert.assertEquals("363.000", this.df3.format(co.getResults().get(10).getNorth()));

        Assert.assertEquals("616.000", this.df3.format(co.getResults().get(11).getEast()));
        Assert.assertEquals("354.000", this.df3.format(co.getResults().get(11).getNorth()));
    }

    // TD-NK_2.2.1.2
    @Test
    public void tdNK2212() {
        Point origin = new Point("2214", 625.0, 360.0, MathUtils.IGNORE_DOUBLE, true, false);
        Point extremity = new Point("2215", 630.0, 357.0, MathUtils.IGNORE_DOUBLE, true, false);

        CheminementOrthogonal co = new CheminementOrthogonal(origin, extremity, false);
        co.getMeasures().add(new CheminementOrthogonal.Measure("2216", 5.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2215", 3.0));

        try {
            co.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("1.00000", this.df5.format(co.getScale()));
        Assert.assertEquals("0.000", this.df3.format(co.getFs()));
        Assert.assertEquals("0.000", this.df3.format(co.getfE()));
        Assert.assertEquals("0.000", this.df3.format(co.getfN()));

        Assert.assertEquals("5.831", this.df3.format(co.getOrthogonalBase().getCalculatedDistance()));

        Assert.assertEquals("630.000", this.df3.format(co.getResults().get(0).getEast()));
        Assert.assertEquals("360.000", this.df3.format(co.getResults().get(0).getNorth()));

        Assert.assertEquals("630.000", this.df3.format(co.getResults().get(1).getEast()));
        Assert.assertEquals("357.000", this.df3.format(co.getResults().get(1).getNorth()));
    }

    // TD-NK_2.2.2.1
    @Test
    public void tdNK2221() {
        Point origin = new Point("2217", 600.0, 360.0, MathUtils.IGNORE_DOUBLE, true, false);
        Point extremity = new Point("2218", 616.0, 354.2, MathUtils.IGNORE_DOUBLE, true, false);

        CheminementOrthogonal co = new CheminementOrthogonal(origin, extremity, false);
        co.getMeasures().add(new CheminementOrthogonal.Measure("2219", 4.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2220", -3.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2221", 4.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2222", 5.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2223", -2.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2224", 3.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2225", -1.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2226", -5.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2227", 2.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2228", -3.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2229", 3.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2218", 9.0));

        try {
            co.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("0.99595", this.df5.format(co.getScale()));
        Assert.assertEquals("0.069", this.df3.format(co.getFs()));
        Assert.assertEquals("-0.065", this.df3.format(co.getfE()));
        Assert.assertEquals("0.024", this.df3.format(co.getfN()));

        Assert.assertEquals("17.019", this.df3.format(co.getOrthogonalBase().getCalculatedDistance()));

        Assert.assertEquals("603.984", this.df3.format(co.getResults().get(0).getEast()));
        Assert.assertEquals("360.044", this.df3.format(co.getResults().get(0).getNorth()));

        Assert.assertEquals("603.951", this.df3.format(co.getResults().get(1).getEast()));
        Assert.assertEquals("363.032", this.df3.format(co.getResults().get(1).getNorth()));

        Assert.assertEquals("607.934", this.df3.format(co.getResults().get(2).getEast()));
        Assert.assertEquals("363.075", this.df3.format(co.getResults().get(2).getNorth()));

        Assert.assertEquals("607.989", this.df3.format(co.getResults().get(3).getEast()));
        Assert.assertEquals("358.096", this.df3.format(co.getResults().get(3).getNorth()));

        Assert.assertEquals("609.981", this.df3.format(co.getResults().get(4).getEast()));
        Assert.assertEquals("358.118", this.df3.format(co.getResults().get(4).getNorth()));

        Assert.assertEquals("610.014", this.df3.format(co.getResults().get(5).getEast()));
        Assert.assertEquals("355.130", this.df3.format(co.getResults().get(5).getNorth()));

        Assert.assertEquals("611.010", this.df3.format(co.getResults().get(6).getEast()));
        Assert.assertEquals("355.141", this.df3.format(co.getResults().get(6).getNorth()));

        Assert.assertEquals("610.955", this.df3.format(co.getResults().get(7).getEast()));
        Assert.assertEquals("360.121", this.df3.format(co.getResults().get(7).getNorth()));

        Assert.assertEquals("612.947", this.df3.format(co.getResults().get(8).getEast()));
        Assert.assertEquals("360.142", this.df3.format(co.getResults().get(8).getNorth()));

        Assert.assertEquals("612.914", this.df3.format(co.getResults().get(9).getEast()));
        Assert.assertEquals("363.130", this.df3.format(co.getResults().get(9).getNorth()));

        Assert.assertEquals("615.901", this.df3.format(co.getResults().get(10).getEast()));
        Assert.assertEquals("363.163", this.df3.format(co.getResults().get(10).getNorth()));

        Assert.assertEquals("616.000", this.df3.format(co.getResults().get(11).getEast()));
        Assert.assertEquals("354.200", this.df3.format(co.getResults().get(11).getNorth()));
    }

    // TD-NK_2.2.2.2
    @Test
    public void tdNK2222() {
        Point origin = new Point("2230", 640.0, 360.0, MathUtils.IGNORE_DOUBLE, true, false);
        Point extremity = new Point("2231", 645.0, 357.0, MathUtils.IGNORE_DOUBLE, true, false);

        CheminementOrthogonal co = new CheminementOrthogonal(origin, extremity, false);
        co.getMeasures().add(new CheminementOrthogonal.Measure("2232", 4.99));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2231", 3.01));

        try {
            co.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("1.00059", this.df5.format(co.getScale()));
        Assert.assertEquals("0.003", this.df3.format(co.getFs()));
        Assert.assertEquals("0.003", this.df3.format(co.getfE()));
        Assert.assertEquals("-0.002", this.df3.format(co.getfN()));

        Assert.assertEquals("5.8310", this.df4.format(co.getOrthogonalBase().getCalculatedDistance()));

        Assert.assertEquals("644.993", this.df3.format(co.getResults().get(0).getEast()));
        Assert.assertEquals("360.012", this.df3.format(co.getResults().get(0).getNorth()));

        Assert.assertEquals("645.000", this.df3.format(co.getResults().get(1).getEast()));
        Assert.assertEquals("357.000", this.df3.format(co.getResults().get(1).getNorth()));
    }

    // TD-NK_2.2.2.3
    @Test
    public void tdNK2223() {
        Point origin = new Point("2233", 655.0, 360.0, MathUtils.IGNORE_DOUBLE, true, false);
        Point extremity = new Point("2234", 660.0, 357.0, MathUtils.IGNORE_DOUBLE, true, false);

        CheminementOrthogonal co = new CheminementOrthogonal(origin, extremity, false);
        co.getMeasures().add(new CheminementOrthogonal.Measure("2235", 4.99));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2234", 3.00));

        try {
            co.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("1.00147", this.df5.format(co.getScale()));
        Assert.assertEquals("0.009", this.df3.format(co.getFs()));
        Assert.assertEquals("0.007", this.df3.format(co.getfE()));
        Assert.assertEquals("-0.004", this.df3.format(co.getfN()));

        Assert.assertEquals("5.8310", this.df4.format(co.getOrthogonalBase().getCalculatedDistance()));

        Assert.assertEquals("659.997", this.df3.format(co.getResults().get(0).getEast()));
        Assert.assertEquals("360.004", this.df3.format(co.getResults().get(0).getNorth()));

        Assert.assertEquals("660.000", this.df3.format(co.getResults().get(1).getEast()));
        Assert.assertEquals("357.000", this.df3.format(co.getResults().get(1).getNorth()));
    }
}