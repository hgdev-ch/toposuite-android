package ch.hgdev.toposuite.test.calculation;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import junit.framework.Assert;
import junit.framework.TestCase;
import ch.hgdev.toposuite.calculation.PointProjectionOnALine;
import ch.hgdev.toposuite.points.Point;

public class TestPointProjectionOnALine extends TestCase {

    public void testPointProjectionOnALine() {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.HALF_UP);

        Point p1 = new Point(1, 25.0000, 55.0000, 0.0, false, false);
        Point p2 = new Point(2, 89.1570, 82.4730, 0.0, false, false);

        Point ptToProj = new Point(5, 113.2040, 37.4110, 0.0, false, false);

        double inputGisement = 74.243;

        double dist = 10.05;
        PointProjectionOnALine pp = new PointProjectionOnALine(42, p1, p2, ptToProj, dist, false);
        pp.compute();
        Assert.assertEquals("97.128", df.format(pp.getProjPt().getEast()));
        Assert.assertEquals("74.954", df.format(pp.getProjPt().getNorth()));
        Assert.assertEquals("40.84", df.format(pp.getDistPtToLine()));
        Assert.assertEquals("74.159", df.format(pp.getDistPtToP1()));
        Assert.assertEquals("4.367", df.format(pp.getDistPtToP2()));

        dist = -5.08;
        pp = new PointProjectionOnALine(42, p1, 74.243, ptToProj, dist, false);
        pp.compute();
        Assert.assertEquals("91.172", df.format(pp.getProjPt().getEast()));
        Assert.assertEquals("88.862", df.format(pp.getProjPt().getNorth()));
        Assert.assertEquals("55.97", df.format(pp.getDistPtToLine()));
        Assert.assertEquals("74.159", df.format(pp.getDistPtToP1()));
        Assert.assertEquals("54.159", df.format(pp.getDistPtToP2()));

        dist = 0.0;
        pp = new PointProjectionOnALine(42, p1, p2, ptToProj, dist, false);
        pp.compute();
        Assert.assertEquals("93.172", df.format(pp.getProjPt().getEast()));
        Assert.assertEquals("84.192", df.format(pp.getProjPt().getNorth()));
        Assert.assertEquals("50.89", df.format(pp.getDistPtToLine()));
        Assert.assertEquals("74.159", df.format(pp.getDistPtToP1()));
        Assert.assertEquals("4.367", df.format(pp.getDistPtToP2()));
    }
}