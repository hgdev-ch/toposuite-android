package ch.hgdev.toposuite.test.calculation;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hgdev.toposuite.calculation.AxisImplantation;
import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.test.testutils.CalculationTestRunner;
import ch.hgdev.toposuite.utils.MathUtils;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class AxisImplantationTest extends CalculationTestRunner {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void simple() {
        Point p210 = new Point("210", 556490.077, 172508.822, MathUtils.IGNORE_DOUBLE, true, false);
        Point p211 = new Point("211", 556517.541, 172491.482, MathUtils.IGNORE_DOUBLE, true, false);
        Point p111 = new Point("111", 556500.900, 172489.700, MathUtils.IGNORE_DOUBLE, true, false);
        double z0 = 0.0;

        AxisImplantation ai = new AxisImplantation(false);

        ai.initAttributes(p111, z0, p210, p211);
        ai.getMeasures().add(new Measure(null, 378.042, 100.0, 35.490,
                MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE,
                MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, "222"));
        try {
            ai.compute();
        } catch (CalculationException e) {
            Assert.fail(e.getMessage());
        }
        AxisImplantation.Result r = ai.getResults().get(0);

        Assert.assertEquals("222", r.getNumber());
        Assert.assertEquals("556488.9", this.df1.format(r.getEast()));
        Assert.assertEquals("172523.1", this.df1.format(r.getNorth()));
        Assert.assertEquals("-8.617", this.df3.format(r.getAbscissa()));
        Assert.assertEquals("-11.445", this.df3.format(r.getOrdinate()));
    }
}
