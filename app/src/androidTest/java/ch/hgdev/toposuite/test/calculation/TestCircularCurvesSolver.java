package ch.hgdev.toposuite.test.calculation;

import junit.framework.Assert;
import ch.hgdev.toposuite.calculation.CircularCurvesSolver;

public class TestCircularCurvesSolver extends CalculationTest {

    public void testRadiusAlpha() {
        // radius / alpha (central angle)
        CircularCurvesSolver ccs = new CircularCurvesSolver(false);
        ccs.setRadius(40.0);
        ccs.setAlphaAngle(120.0);
        ccs.compute();

        Assert.assertEquals("40", this.df4.format(ccs.getRadius()));
        Assert.assertEquals("120", this.df4.format(ccs.getAlphaAngle()));
        Assert.assertEquals("64.7214", this.df4.format(ccs.getChordOF()));
        Assert.assertEquals("55.0553", this.df4.format(ccs.getTangent()));
        Assert.assertEquals("16.4886", this.df4.format(ccs.getArrow()));

        Assert.assertEquals("28.0521", this.df4.format(ccs.getBisector()));
        Assert.assertEquals("75.3982", this.df4.format(ccs.getArc()));
        Assert.assertEquals("251.3274", this.df4.format(ccs.getCircumference()));
        Assert.assertEquals("36.3192", this.df4.format(ccs.getChordOM()));
        Assert.assertEquals("80", this.df4.format(ccs.getBetaAngle()));
        Assert.assertEquals("5026.5482", this.df4.format(ccs.getCircleSurface()));
        Assert.assertEquals("1507.9645", this.df4.format(ccs.getSectorSurface()));
        Assert.assertEquals("747.1193", this.df4.format(ccs.getSegmentSurface()));
    }

    public void testRadiusTangent() {
        // radius / tangent
        CircularCurvesSolver ccs = new CircularCurvesSolver(false);
        ccs.setRadius(40.0);
        ccs.setTangent(55.0553);
        ccs.compute();

        Assert.assertEquals("40", this.df4.format(ccs.getRadius()));
        Assert.assertEquals("120", this.df4.format(ccs.getAlphaAngle()));
        Assert.assertEquals("64.7214", this.df4.format(ccs.getChordOF()));
        Assert.assertEquals("55.0553", this.df4.format(ccs.getTangent()));
        Assert.assertEquals("16.4886", this.df4.format(ccs.getArrow()));

        Assert.assertEquals("28.0521", this.df4.format(ccs.getBisector()));
        Assert.assertEquals("75.3982", this.df4.format(ccs.getArc()));
        Assert.assertEquals("251.3274", this.df4.format(ccs.getCircumference()));
        Assert.assertEquals("36.3192", this.df4.format(ccs.getChordOM()));
        Assert.assertEquals("80", this.df4.format(ccs.getBetaAngle()));
        Assert.assertEquals("5026.5482", this.df4.format(ccs.getCircleSurface()));
        Assert.assertEquals("1507.9648", this.df4.format(ccs.getSectorSurface()));
        Assert.assertEquals("747.1197", this.df4.format(ccs.getSegmentSurface()));
    }

    public void testRadiusArrow() {
        // radius / arrow
        CircularCurvesSolver ccs = new CircularCurvesSolver(false);
        ccs.setRadius(40.0);
        ccs.setArrow(16.4886);
        ccs.compute();

        Assert.assertEquals("40", this.df4.format(ccs.getRadius()));
        Assert.assertEquals("120", this.df4.format(ccs.getAlphaAngle()));
        Assert.assertEquals("64.7214", this.df4.format(ccs.getChordOF()));
        Assert.assertEquals("55.0553", this.df4.format(ccs.getTangent()));
        Assert.assertEquals("16.4886", this.df4.format(ccs.getArrow()));

        Assert.assertEquals("28.0521", this.df4.format(ccs.getBisector()));
        Assert.assertEquals("75.3982", this.df4.format(ccs.getArc()));
        Assert.assertEquals("251.3274", this.df4.format(ccs.getCircumference()));
        Assert.assertEquals("36.3193", this.df4.format(ccs.getChordOM()));
        Assert.assertEquals("80", this.df4.format(ccs.getBetaAngle()));
        Assert.assertEquals("5026.5482", this.df4.format(ccs.getCircleSurface()));
        Assert.assertEquals("1507.965", this.df4.format(ccs.getSectorSurface()));
        Assert.assertEquals("747.1199", this.df4.format(ccs.getSegmentSurface()));
    }

    public void testRadiusChordOF() {
        // radius / chord OF
        CircularCurvesSolver ccs = new CircularCurvesSolver(false);
        ccs.setRadius(40.0);
        ccs.setChordOF(64.7214);
        ccs.compute();

        Assert.assertEquals("40", this.df3.format(ccs.getRadius()));
        Assert.assertEquals("120", this.df3.format(ccs.getAlphaAngle()));
        Assert.assertEquals("64.721", this.df3.format(ccs.getChordOF()));
        Assert.assertEquals("55.055", this.df3.format(ccs.getTangent()));
        Assert.assertEquals("16.489", this.df3.format(ccs.getArrow()));

        Assert.assertEquals("28.052", this.df3.format(ccs.getBisector()));
        Assert.assertEquals("75.398", this.df3.format(ccs.getArc()));
        Assert.assertEquals("251.327", this.df3.format(ccs.getCircumference()));
        Assert.assertEquals("36.319", this.df3.format(ccs.getChordOM()));
        Assert.assertEquals("80", this.df3.format(ccs.getBetaAngle()));
        Assert.assertEquals("5026.548", this.df3.format(ccs.getCircleSurface()));
        Assert.assertEquals("1507.966", this.df3.format(ccs.getSectorSurface()));
        Assert.assertEquals("747.121", this.df3.format(ccs.getSegmentSurface()));
    }

    public void testChordOFAlpha() {
        // chord OF / alpha
        CircularCurvesSolver ccs = new CircularCurvesSolver(false);
        ccs.setChordOF(64.7214);
        ccs.setAlphaAngle(120.0);
        ccs.compute();

        Assert.assertEquals("40", this.df3.format(ccs.getRadius()));
        Assert.assertEquals("120", this.df3.format(ccs.getAlphaAngle()));
        Assert.assertEquals("64.721", this.df3.format(ccs.getChordOF()));
        Assert.assertEquals("55.055", this.df3.format(ccs.getTangent()));
        Assert.assertEquals("16.489", this.df3.format(ccs.getArrow()));

        Assert.assertEquals("28.052", this.df3.format(ccs.getBisector()));
        Assert.assertEquals("75.398", this.df3.format(ccs.getArc()));
        Assert.assertEquals("251.328", this.df3.format(ccs.getCircumference()));
        Assert.assertEquals("36.319", this.df3.format(ccs.getChordOM()));
        Assert.assertEquals("80", this.df3.format(ccs.getBetaAngle()));
        Assert.assertEquals("5026.555", this.df3.format(ccs.getCircleSurface()));
        Assert.assertEquals("1507.966", this.df3.format(ccs.getSectorSurface()));
        Assert.assertEquals("747.12", this.df3.format(ccs.getSegmentSurface()));
    }

    public void testChordOFTengent() {
        // chord OF / tangent
        CircularCurvesSolver ccs = new CircularCurvesSolver(false);
        ccs.setChordOF(64.7214);
        ccs.setTangent(55.0553);
        ccs.compute();

        Assert.assertEquals("40", this.df3.format(ccs.getRadius()));
        Assert.assertEquals("120", this.df3.format(ccs.getAlphaAngle()));
        Assert.assertEquals("64.721", this.df3.format(ccs.getChordOF()));
        Assert.assertEquals("55.055", this.df3.format(ccs.getTangent()));
        Assert.assertEquals("16.489", this.df3.format(ccs.getArrow()));

        Assert.assertEquals("28.052", this.df3.format(ccs.getBisector()));
        Assert.assertEquals("75.398", this.df3.format(ccs.getArc()));
        Assert.assertEquals("251.328", this.df3.format(ccs.getCircumference()));
        Assert.assertEquals("36.319", this.df3.format(ccs.getChordOM()));
        Assert.assertEquals("80", this.df3.format(ccs.getBetaAngle()));
        Assert.assertEquals("5026.556", this.df3.format(ccs.getCircleSurface()));
        Assert.assertEquals("1507.966", this.df3.format(ccs.getSectorSurface()));
        Assert.assertEquals("747.12", this.df3.format(ccs.getSegmentSurface()));
    }

    public void testChordOFArrow() {
        // chord OF / arrow
        CircularCurvesSolver ccs = new CircularCurvesSolver(false);
        ccs.setChordOF(64.7214);
        ccs.setArrow(16.4886);
        ccs.compute();

        Assert.assertEquals("40", this.df3.format(ccs.getRadius()));
        Assert.assertEquals("120", this.df3.format(ccs.getAlphaAngle()));
        Assert.assertEquals("64.721", this.df3.format(ccs.getChordOF()));
        Assert.assertEquals("55.055", this.df3.format(ccs.getTangent()));
        Assert.assertEquals("16.489", this.df3.format(ccs.getArrow()));

        Assert.assertEquals("28.052", this.df3.format(ccs.getBisector()));
        Assert.assertEquals("75.398", this.df3.format(ccs.getArc()));
        Assert.assertEquals("251.328", this.df3.format(ccs.getCircumference()));
        Assert.assertEquals("36.319", this.df3.format(ccs.getChordOM()));
        Assert.assertEquals("80", this.df3.format(ccs.getBetaAngle()));
        Assert.assertEquals("5026.555", this.df3.format(ccs.getCircleSurface()));
        Assert.assertEquals("1507.966", this.df3.format(ccs.getSectorSurface()));
        Assert.assertEquals("747.12", this.df3.format(ccs.getSegmentSurface()));
    }

    public void testTangentAlpha() {
        // tangent / alpha
        CircularCurvesSolver ccs = new CircularCurvesSolver(false);
        ccs.setTangent(55.0553);
        ccs.setAlphaAngle(120.0);
        ccs.compute();

        Assert.assertEquals("40", this.df3.format(ccs.getRadius()));
        Assert.assertEquals("120", this.df3.format(ccs.getAlphaAngle()));
        Assert.assertEquals("64.721", this.df3.format(ccs.getChordOF()));
        Assert.assertEquals("55.055", this.df3.format(ccs.getTangent()));
        Assert.assertEquals("16.489", this.df3.format(ccs.getArrow()));

        Assert.assertEquals("28.052", this.df3.format(ccs.getBisector()));
        Assert.assertEquals("75.398", this.df3.format(ccs.getArc()));
        Assert.assertEquals("251.328", this.df3.format(ccs.getCircumference()));
        Assert.assertEquals("36.319", this.df3.format(ccs.getChordOM()));
        Assert.assertEquals("80", this.df3.format(ccs.getBetaAngle()));
        Assert.assertEquals("5026.552", this.df3.format(ccs.getCircleSurface()));
        Assert.assertEquals("1507.966", this.df3.format(ccs.getSectorSurface()));
        Assert.assertEquals("747.12", this.df3.format(ccs.getSegmentSurface()));
    }
}