package ch.hgdev.toposuite.test.calculation;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import junit.framework.Assert;
import junit.framework.TestCase;
import ch.hgdev.toposuite.calculation.Surface;

public class TestSurface extends TestCase {

    private DecimalFormat df4;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.df4 = new DecimalFormat("#.####");
        this.df4.setRoundingMode(RoundingMode.HALF_UP);
    }

    public void testSurfaceWithCurve() {

        Surface s1 = new Surface("Test 1", "Surface with curves", false);
        s1.getPoints().add(new Surface.PointWithRadius(
                1, 612.8181, 246.3561, 152.8449));
        s1.getPoints().add(new Surface.PointWithRadius(2, 469.4239, 95.0256));
        s1.getPoints().add(new Surface.PointWithRadius(3, 553.5582, -13.9031));
        s1.getPoints().add(new Surface.PointWithRadius(4, 666.9567, 73.0936));
        s1.getPoints().add(new Surface.PointWithRadius(5, 833.0305, -58.4981));
        s1.getPoints().add(new Surface.PointWithRadius(6, 881.3163, 142.5448));

        s1.compute();

        Assert.assertEquals("60795.8488", this.df4.format(s1.getSurface()));
        Assert.assertEquals("1216.4939", this.df4.format(s1.getPerimeter()));

        Surface s2 = new Surface("Test 2", "Surface with curves", false);
        s2.getPoints().add(new Surface.PointWithRadius(
                1, 1067.3017, 743.7669, -102.6444));
        s2.getPoints().add(new Surface.PointWithRadius(2, 988.6363, 616.6045));
        s2.getPoints().add(new Surface.PointWithRadius(3, 1047.7041, 439.5316));
        s2.getPoints().add(new Surface.PointWithRadius(4, 1182.134, 388.6486));
        s2.getPoints().add(new Surface.PointWithRadius(5, 1298.2327, 533.1563));
        s2.getPoints().add(new Surface.PointWithRadius(6, 1215.047, 690.6177));

        s2.compute();

        Assert.assertEquals("73339.6805", this.df4.format(s2.getSurface()));
        Assert.assertEquals("1018.3747", this.df4.format(s2.getPerimeter()));

    }

    public void testSurfaceWithoutCurve() {
        Surface s = new Surface("Test 3", "Surface without curve", false);
        s.getPoints().add(new Surface.PointWithRadius(1, 7.3959, 131.7282));
        s.getPoints().add(new Surface.PointWithRadius(2, 9.226, 234.5983));
        s.getPoints().add(new Surface.PointWithRadius(3, 169.3639, 262.9447));
        s.getPoints().add(new Surface.PointWithRadius(4, 315.7756, 223.6255));
        s.getPoints().add(new Surface.PointWithRadius(5, 307.0824, 112.0686));
        s.getPoints().add(new Surface.PointWithRadius(6, 167.9913, 69.0918));
        s.getPoints().add(new Surface.PointWithRadius(7, 148.7747, 171.5047));
        s.getPoints().add(new Surface.PointWithRadius(8, 73.7387, 126.699));

        s.compute();

        Assert.assertEquals("39662.5883", this.df4.format(s.getSurface()));
        Assert.assertEquals("932.7163", this.df4.format(s.getPerimeter()));
    }
}
