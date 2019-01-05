package ch.hgdev.toposuite.test.calculation;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.LeveOrthogonal;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.test.testutils.CalculationTestRunner;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class LeveOrthogonalTest extends CalculationTestRunner {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void simple() {
        Point origin = new Point("45", 556495.160, 172493.912, 623.37, true);
        Point extremity = new Point("46", 556517.541, 172491.482, 624.14, true);
        double measuredDistance = 22.58;

        LeveOrthogonal lo = new LeveOrthogonal(origin, extremity, measuredDistance, false);

        lo.getMeasures().add(new LeveOrthogonal.Measure("1", -3.5, -7.3));
        lo.getMeasures().add(new LeveOrthogonal.Measure("4", 13.82, 10.97));

        try {
            lo.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("22.513", this.df3.format(lo.getOrthogonalBase().getCalculatedDistance()));
        Assert.assertEquals("0.99701", this.df5.format(lo.getOrthogonalBase().getScaleFactor()));
        // TODO: uncomment once gap is implemented
        //Assert.assertEquals("0.067", this.df3.format(lo.getOrthogonalBase().getFS()));

        Assert.assertEquals("556492.476", this.df3.format(lo.getResults().get(0).getAbscissa()));
        Assert.assertEquals("172501.524", this.df3.format(lo.getResults().get(0).getOrdinate()));

        Assert.assertEquals("556507.678", this.df3.format(lo.getResults().get(1).getAbscissa()));
        Assert.assertEquals("172481.551", this.df3.format(lo.getResults().get(1).getOrdinate()));
    }

    // TD-NK_2.1.1
    @Test
    public void tdNK211() {
        Point origin = new Point("2101", 600.000, 200.000, 0.0, true);
        Point extremity = new Point("2102", 665.000, 200.000, 0.0, true);
        double measuredDistance = 65.00;

        LeveOrthogonal lo = new LeveOrthogonal(origin, extremity, measuredDistance, false);

        lo.getMeasures().add(new LeveOrthogonal.Measure("2103", -6.25, -5.0));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2104", 5.00, -7.50));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2105", 20.00, -15.00));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2106", 37.50, -10.00));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2107", 52.50, -20.00));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2108", 60.00, -12.50));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2109", 66.25, -7.50));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2110", 72.50, 6.25));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2111", 55.00, 10.00));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2112", 40.00, 15.00));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2113", 30.00, 5.00));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2114", 17.50, 2.50));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2115", 10.00, 10.00));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2116", 1.25, 5.00));

        try {
            lo.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("65.000", this.df3.format(lo.getOrthogonalBase().getCalculatedDistance()));
        Assert.assertEquals("1.000", this.df3.format(lo.getOrthogonalBase().getScaleFactor()));
        Assert.assertEquals(0, lo.getOrthogonalBase().getScaleFactorPPM());
        // TODO: uncomment once gap is implemented
        //Assert.assertEquals("0.000", this.df3.format(lo.getOrthogonalBase().getFS()));

        Assert.assertEquals("593.750", this.df3.format(lo.getResults().get(0).getAbscissa()));
        Assert.assertEquals("205.000", this.df3.format(lo.getResults().get(0).getOrdinate()));

        Assert.assertEquals("605.000", this.df3.format(lo.getResults().get(1).getAbscissa()));
        Assert.assertEquals("207.500", this.df3.format(lo.getResults().get(1).getOrdinate()));

        Assert.assertEquals("620.000", this.df3.format(lo.getResults().get(2).getAbscissa()));
        Assert.assertEquals("215.000", this.df3.format(lo.getResults().get(2).getOrdinate()));

        Assert.assertEquals("637.500", this.df3.format(lo.getResults().get(3).getAbscissa()));
        Assert.assertEquals("210.000", this.df3.format(lo.getResults().get(3).getOrdinate()));

        Assert.assertEquals("652.500", this.df3.format(lo.getResults().get(4).getAbscissa()));
        Assert.assertEquals("220.000", this.df3.format(lo.getResults().get(4).getOrdinate()));

        Assert.assertEquals("660.000", this.df3.format(lo.getResults().get(5).getAbscissa()));
        Assert.assertEquals("212.500", this.df3.format(lo.getResults().get(5).getOrdinate()));

        Assert.assertEquals("666.250", this.df3.format(lo.getResults().get(6).getAbscissa()));
        Assert.assertEquals("207.500", this.df3.format(lo.getResults().get(6).getOrdinate()));

        Assert.assertEquals("672.500", this.df3.format(lo.getResults().get(7).getAbscissa()));
        Assert.assertEquals("193.750", this.df3.format(lo.getResults().get(7).getOrdinate()));

        Assert.assertEquals("655.000", this.df3.format(lo.getResults().get(8).getAbscissa()));
        Assert.assertEquals("190.000", this.df3.format(lo.getResults().get(8).getOrdinate()));

        Assert.assertEquals("640.000", this.df3.format(lo.getResults().get(9).getAbscissa()));
        Assert.assertEquals("185.000", this.df3.format(lo.getResults().get(9).getOrdinate()));

        Assert.assertEquals("630.000", this.df3.format(lo.getResults().get(10).getAbscissa()));
        Assert.assertEquals("195.000", this.df3.format(lo.getResults().get(10).getOrdinate()));

        Assert.assertEquals("617.500", this.df3.format(lo.getResults().get(11).getAbscissa()));
        Assert.assertEquals("197.500", this.df3.format(lo.getResults().get(11).getOrdinate()));

        Assert.assertEquals("610.000", this.df3.format(lo.getResults().get(12).getAbscissa()));
        Assert.assertEquals("190.000", this.df3.format(lo.getResults().get(12).getOrdinate()));

        Assert.assertEquals("601.250", this.df3.format(lo.getResults().get(13).getAbscissa()));
        Assert.assertEquals("195.000", this.df3.format(lo.getResults().get(13).getOrdinate()));
    }

    // TD-NK_2.1.2
    @Test
    public void tdNK212() {
        Point origin = new Point("2117", 600.000, 200.000, 0.0, true);
        Point extremity = new Point("2118", 665.000, 200.000, 0.0, true);
        double measuredDistance = 65.40;

        LeveOrthogonal lo = new LeveOrthogonal(origin, extremity, measuredDistance, false);

        lo.getMeasures().add(new LeveOrthogonal.Measure("2119", -6.25, -5.0));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2120", 5.00, -7.50));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2121", 20.00, -15.00));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2122", 37.50, -10.00));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2123", 52.50, -20.00));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2124", 60.00, -12.50));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2125", 66.25, -7.50));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2126", 72.50, 6.25));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2127", 55.00, 10.00));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2128", 40.00, 15.00));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2129", 30.00, 5.00));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2130", 17.50, 2.50));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2131", 10.00, 10.00));
        lo.getMeasures().add(new LeveOrthogonal.Measure("2132", 1.25, 5.00));

        try {
            lo.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("65.000", this.df3.format(lo.getOrthogonalBase().getCalculatedDistance()));
        Assert.assertEquals("0.99388", this.df5.format(lo.getOrthogonalBase().getScaleFactor()));
        Assert.assertEquals(-6117, lo.getOrthogonalBase().getScaleFactorPPM());
        // TODO: uncomment once gap is implemented
        //Assert.assertEquals("-0.400", this.df3.format(lo.getOrthogonalBase().getFS()));

        Assert.assertEquals("593.788", this.df3.format(lo.getResults().get(0).getAbscissa()));
        Assert.assertEquals("204.969", this.df3.format(lo.getResults().get(0).getOrdinate()));

        Assert.assertEquals("604.969", this.df3.format(lo.getResults().get(1).getAbscissa()));
        Assert.assertEquals("207.454", this.df3.format(lo.getResults().get(1).getOrdinate()));

        Assert.assertEquals("619.878", this.df3.format(lo.getResults().get(2).getAbscissa()));
        Assert.assertEquals("214.908", this.df3.format(lo.getResults().get(2).getOrdinate()));

        Assert.assertEquals("637.271", this.df3.format(lo.getResults().get(3).getAbscissa()));
        Assert.assertEquals("209.939", this.df3.format(lo.getResults().get(3).getOrdinate()));

        Assert.assertEquals("652.179", this.df3.format(lo.getResults().get(4).getAbscissa()));
        Assert.assertEquals("219.878", this.df3.format(lo.getResults().get(4).getOrdinate()));

        Assert.assertEquals("659.633", this.df3.format(lo.getResults().get(5).getAbscissa()));
        Assert.assertEquals("212.424", this.df3.format(lo.getResults().get(5).getOrdinate()));

        Assert.assertEquals("665.845", this.df3.format(lo.getResults().get(6).getAbscissa()));
        Assert.assertEquals("207.454", this.df3.format(lo.getResults().get(6).getOrdinate()));

        Assert.assertEquals("672.057", this.df3.format(lo.getResults().get(7).getAbscissa()));
        Assert.assertEquals("193.788", this.df3.format(lo.getResults().get(7).getOrdinate()));

        Assert.assertEquals("654.664", this.df3.format(lo.getResults().get(8).getAbscissa()));
        Assert.assertEquals("190.061", this.df3.format(lo.getResults().get(8).getOrdinate()));

        Assert.assertEquals("639.755", this.df3.format(lo.getResults().get(9).getAbscissa()));
        Assert.assertEquals("185.092", this.df3.format(lo.getResults().get(9).getOrdinate()));

        Assert.assertEquals("629.817", this.df3.format(lo.getResults().get(10).getAbscissa()));
        Assert.assertEquals("195.031", this.df3.format(lo.getResults().get(10).getOrdinate()));

        Assert.assertEquals("617.393", this.df3.format(lo.getResults().get(11).getAbscissa()));
        Assert.assertEquals("197.515", this.df3.format(lo.getResults().get(11).getOrdinate()));

        Assert.assertEquals("609.939", this.df3.format(lo.getResults().get(12).getAbscissa()));
        Assert.assertEquals("190.061", this.df3.format(lo.getResults().get(12).getOrdinate()));

        Assert.assertEquals("601.242", this.df3.format(lo.getResults().get(13).getAbscissa()));
        Assert.assertEquals("195.031", this.df3.format(lo.getResults().get(13).getOrdinate()));
    }
}