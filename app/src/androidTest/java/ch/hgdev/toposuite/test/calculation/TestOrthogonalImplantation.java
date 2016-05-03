package ch.hgdev.toposuite.test.calculation;

import junit.framework.Assert;

import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.OrthogonalBase;
import ch.hgdev.toposuite.calculation.OrthogonalImplantation;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

public class TestOrthogonalImplantation extends CalculationTest {

    public void testOrthogonalImplantation() {
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
}
