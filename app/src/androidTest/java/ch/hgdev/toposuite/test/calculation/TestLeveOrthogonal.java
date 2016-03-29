package ch.hgdev.toposuite.test.calculation;

import junit.framework.Assert;
import ch.hgdev.toposuite.calculation.LeveOrthogonal;
import ch.hgdev.toposuite.points.Point;

public class TestLeveOrthogonal extends CalculationTest {
    public void testLeveOrthogonal() {

        Point origin = new Point("45", 556495.160, 172493.912, 623.37, true);
        Point extremity = new Point("46", 556517.541, 172491.482, 624.14, true);
        double measuredDistance = 22.58;

        LeveOrthogonal lo = new LeveOrthogonal(origin, extremity, measuredDistance, false);

        lo.getMeasures().add(new LeveOrthogonal.Measure("1", -3.5, -7.3));
        lo.getMeasures().add(new LeveOrthogonal.Measure("4", 13.82, 10.97));

        lo.compute();

        Assert.assertEquals("556492.476", this.df3.format(lo.getResults().get(0).getAbscissa()));
        Assert.assertEquals("172501.524", this.df3.format(lo.getResults().get(0).getOrdinate()));

        Assert.assertEquals("556507.678", this.df3.format(lo.getResults().get(1).getAbscissa()));
        Assert.assertEquals("172481.551", this.df3.format(lo.getResults().get(1).getOrdinate()));
    }
}