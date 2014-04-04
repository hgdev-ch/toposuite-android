package ch.hgdev.toposuite.test.calculation;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import junit.framework.Assert;
import junit.framework.TestCase;
import ch.hgdev.toposuite.calculation.CheminementOrthogonal;
import ch.hgdev.toposuite.points.Point;

public class TestCheminementOrthogonal extends TestCase {
    public void testCheminementOrthogonal() {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.HALF_UP);

        Point origin = new Point("1", 600.0, 200.0, 0.0, true, false);
        Point extremity = new Point("2", 620.0, 215.0, 0.0, true, false);

        CheminementOrthogonal co = new CheminementOrthogonal(origin, extremity, false);
        co.getMeasures().add(new CheminementOrthogonal.Measure("11", 10.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("12", 3.0));
        co.getMeasures().add(new CheminementOrthogonal.Measure("13", -5.02));
        co.getMeasures().add(new CheminementOrthogonal.Measure("14", 3.02));
        co.getMeasures().add(new CheminementOrthogonal.Measure("2", 10.01));

        co.compute();

        Assert.assertEquals("601.614", df.format(co.getResults().get(0).getEast()));
        Assert.assertEquals("231.879", df.format(co.getResults().get(0).getNorth()));

        Assert.assertEquals("611.178", df.format(co.getResults().get(1).getEast()));
        Assert.assertEquals("231.395", df.format(co.getResults().get(1).getNorth()));

        Assert.assertEquals("611.988", df.format(co.getResults().get(2).getEast()));
        Assert.assertEquals("247.399", df.format(co.getResults().get(2).getNorth()));

        Assert.assertEquals("621.616", df.format(co.getResults().get(3).getEast()));
        Assert.assertEquals("246.911", df.format(co.getResults().get(3).getNorth()));

        Assert.assertEquals("620", df.format(co.getResults().get(4).getEast()));
        Assert.assertEquals("215", df.format(co.getResults().get(4).getNorth()));
    }
}