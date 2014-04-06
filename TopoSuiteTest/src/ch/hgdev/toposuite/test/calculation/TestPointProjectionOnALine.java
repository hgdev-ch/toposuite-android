package ch.hgdev.toposuite.test.calculation;

import junit.framework.Assert;
import ch.hgdev.toposuite.calculation.PointProjectionOnALine;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

public class TestPointProjectionOnALine extends CalculationTest {

    public void testPointProjectionOnALine1() {
        Point p1 = new Point("1", 25.0000, 55.0000, MathUtils.IGNORE_DOUBLE, false, false);
        Point p2 = new Point("2", 89.1570, 82.4730, MathUtils.IGNORE_DOUBLE, false, false);

        Point ptToProj = new Point("5", 113.2040, 37.4110, MathUtils.IGNORE_DOUBLE, false, false);

        double inputGisement = 74.243;

        double dist = 10.05;
        PointProjectionOnALine pp = new PointProjectionOnALine("42", p1, p2, ptToProj, dist, false);
        pp.compute();
        Assert.assertEquals("97.128", this.df3.format(pp.getProjPt().getEast()));
        Assert.assertEquals("74.954", this.df3.format(pp.getProjPt().getNorth()));
        Assert.assertEquals("40.84", this.df3.format(pp.getDistPtToLine()));
        Assert.assertEquals("74.159", this.df3.format(pp.getDistPtToP1()));
        Assert.assertEquals("4.367", this.df3.format(pp.getDistPtToP2()));

        dist = -5.08;
        pp = new PointProjectionOnALine("42", p1, 74.243, ptToProj, dist, false);
        pp.compute();
        Assert.assertEquals("91.172", this.df3.format(pp.getProjPt().getEast()));
        Assert.assertEquals("88.862", this.df3.format(pp.getProjPt().getNorth()));
        Assert.assertEquals("55.97", this.df3.format(pp.getDistPtToLine()));
        Assert.assertEquals("74.159", this.df3.format(pp.getDistPtToP1()));
        Assert.assertEquals("54.159", this.df3.format(pp.getDistPtToP2()));

        dist = MathUtils.IGNORE_DOUBLE;
        pp = new PointProjectionOnALine("42", p1, p2, ptToProj, dist, false);
        pp.compute();
        Assert.assertEquals("93.172", this.df3.format(pp.getProjPt().getEast()));
        Assert.assertEquals("84.192", this.df3.format(pp.getProjPt().getNorth()));
        Assert.assertEquals("50.89", this.df3.format(pp.getDistPtToLine()));
        Assert.assertEquals("74.159", this.df3.format(pp.getDistPtToP1()));
        Assert.assertEquals("4.367", this.df3.format(pp.getDistPtToP2()));
    }

    public void testPointProjectionOnALine2() {
        Point p1 = new Point("101", 35.8967, 237.0131, MathUtils.IGNORE_DOUBLE, false, false);
        Point p2 = new Point("102", 271.8654, 149.7584, MathUtils.IGNORE_DOUBLE, false, false);

        Point ptToProj = new Point("10", 149.7584, 241.3083, MathUtils.IGNORE_DOUBLE, false, false);

        double dist = MathUtils.IGNORE_DOUBLE;
        PointProjectionOnALine pp = new PointProjectionOnALine("42", p1, p2, ptToProj, dist, false);
        pp.compute();
        Assert.assertEquals("134.665", this.df3.format(pp.getProjPt().getEast()));
        Assert.assertEquals("200.491", this.df3.format(pp.getProjPt().getNorth()));
        Assert.assertEquals("43.518", this.df3.format(pp.getDistPtToLine()));
        Assert.assertEquals("105.305", this.df3.format(pp.getDistPtToP1()));
        Assert.assertEquals("146.279", this.df3.format(pp.getDistPtToP2()));

        dist = -5.8940;
        pp = new PointProjectionOnALine("42", p1, p2, ptToProj, dist, false);
        pp.compute();
        Assert.assertEquals("136.71", this.df3.format(pp.getProjPt().getEast()));
        Assert.assertEquals("206.019", this.df3.format(pp.getProjPt().getNorth()));
        Assert.assertEquals("37.624", this.df3.format(pp.getDistPtToLine()));
        Assert.assertEquals("105.305", this.df3.format(pp.getDistPtToP1()));
        Assert.assertEquals("146.279", this.df3.format(pp.getDistPtToP2()));

        dist = 17.8900;
        pp = new PointProjectionOnALine("42", p1, p2, ptToProj, dist, false);
        pp.compute();
        Assert.assertEquals("128.461", this.df3.format(pp.getProjPt().getEast()));
        Assert.assertEquals("183.712", this.df3.format(pp.getProjPt().getNorth()));
        Assert.assertEquals("61.408", this.df3.format(pp.getDistPtToLine()));
        Assert.assertEquals("105.305", this.df3.format(pp.getDistPtToP1()));
        Assert.assertEquals("146.279", this.df3.format(pp.getDistPtToP2()));
    }

    public void testPointProjectionOnALine3() {
        Point p1 = new Point("101", 600, 200, MathUtils.IGNORE_DOUBLE, false, false);
        Point p2 = new Point("102", 630, 200, MathUtils.IGNORE_DOUBLE, false, false);

        Point ptToProj = new Point("10", 615, 210, MathUtils.IGNORE_DOUBLE, false, false);

        double dist = -2;
        PointProjectionOnALine pp = new PointProjectionOnALine("42", p1, p2, ptToProj, dist, false);
        pp.compute();
        Assert.assertEquals("615", this.df3.format(pp.getProjPt().getEast()));
        Assert.assertEquals("202", this.df3.format(pp.getProjPt().getNorth()));
        Assert.assertEquals("8", this.df3.format(pp.getDistPtToLine()));
        Assert.assertEquals("15", this.df3.format(pp.getDistPtToP1()));
        Assert.assertEquals("15", this.df3.format(pp.getDistPtToP2()));
    }
}