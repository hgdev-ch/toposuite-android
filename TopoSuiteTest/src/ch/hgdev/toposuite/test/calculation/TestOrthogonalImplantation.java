package ch.hgdev.toposuite.test.calculation;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import junit.framework.Assert;
import junit.framework.TestCase;
import ch.hgdev.toposuite.calculation.OrthogonalImplantation;
import ch.hgdev.toposuite.points.Point;

public class TestOrthogonalImplantation extends TestCase {

    public void testOrthogonalImplantation() {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.HALF_UP);

        Point origin = new Point(210, 556490.077, 172508.822, 0.0, true, false);
        Point extremity = new Point(211, 556517.541, 172491.482, 0.0, true, false);

        OrthogonalImplantation oi = new OrthogonalImplantation(origin, extremity, false);

        oi.getMeasures().add(new Point(111, 556500.900, 172489.700, 0.0, true, false));
        oi.getMeasures().add(new Point(222, 556488.900, 172523.100, 0.0, true, false));
        oi.getMeasures().add(new Point(333, 556474.900, 172504.700, 0.0, true, false));
        oi.getMeasures().add(new Point(444, 556524.300, 172477.900, 0.0, true, false));
        oi.getMeasures().add(new Point(555, 556528.454, 172491.952, 0.0, true, false));
        oi.getMeasures().add(new Point(666, 556502.300, 172504.500, 0.0, true, false));

        oi.compute();

        Assert.assertEquals("19.36", df.format(oi.getResults().get(0).getAbscissa()));
        Assert.assertEquals("10.391", df.format(oi.getResults().get(0).getOrdinate()));

        Assert.assertEquals("-8.618", df.format(oi.getResults().get(1).getAbscissa()));
        Assert.assertEquals("-11.445", df.format(oi.getResults().get(1).getOrdinate()));

        Assert.assertEquals("-10.633", df.format(oi.getResults().get(2).getAbscissa()));
        Assert.assertEquals("11.588", df.format(oi.getResults().get(2).getOrdinate()));

        Assert.assertEquals("45.446", df.format(oi.getResults().get(3).getAbscissa()));
        Assert.assertEquals("7.876", df.format(oi.getResults().get(3).getOrdinate()));

        //Assert.assertEquals("41.457", df.format(oi.getResults().get(4).getAbscissa()));
        Assert.assertEquals("-6.224", df.format(oi.getResults().get(4).getOrdinate()));

        Assert.assertEquals("12.643", df.format(oi.getResults().get(5).getAbscissa()));
        Assert.assertEquals("-2.871", df.format(oi.getResults().get(5).getOrdinate()));
    }
}
