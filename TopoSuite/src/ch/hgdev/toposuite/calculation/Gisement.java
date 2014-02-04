package ch.hgdev.toposuite.calculation;

import ch.hgdev.toposuite.points.Point;

/**
 * TODO add javadoc comments
 * 
 * @author HGdev
 */
public class Gisement extends Calculation {
    private final static double EPSILON = Double.MIN_VALUE;

    private Point               origin;
    private Point               orientation;

    private double              gisement;
    private double              horizDist;
    private double              altitude;
    private double              slope;

    public Gisement(Point _origin, Point _orientation) {
        super("Gisement", "");

        this.origin = _origin;
        this.orientation = _orientation;

        this.compute();
    }

    public final void compute() {
        double deltaY = this.orientation.getEast() - this.origin.getEast();
        double deltaX = this.orientation.getNorth() - this.origin.getNorth();

        // complement remains 0.0 if deltaY is positive and deltaX is positive
        // and if deltaY is 0.0 and deltaX is positive.
        double complement = 0.0;

        // FIXME precision problem
        if (this.isPositive(deltaY) && this.isZero(deltaX)) {
            complement = 100.0;
        } else if (this.isNegative(deltaY) && this.isZero(deltaX)) {
            complement = 300.0;
        } else if (this.isZero(deltaY) && this.isNegative(deltaX) || this.isPositive(deltaY) && this.isNegative(deltaY)
                || this.isNegative(deltaY) && this.isNegative(deltaX)) {
            complement = 200.0;
        } else if (this.isNegative(deltaY) && this.isPositive(deltaX)) {
            complement = 400.0;
        }

        // handle division by zero
        double tmp = 0.0;
        if (deltaX > 0.0) {
            tmp = Math.atan(deltaY / deltaX);
        }
        // TODO create a separate helper for converting rad to grad
        this.gisement = tmp / Math.PI * 200 + complement;

        // TODO create a separate helper for converting grad to rad
        this.horizDist = deltaY / Math.sin(this.gisement * Math.PI / 200);
    }

    private boolean isZero(double d) {
        return d < Gisement.EPSILON && d > -Gisement.EPSILON;
    }

    private boolean isPositive(double d) {
        return d > Gisement.EPSILON;
    }

    private boolean isNegative(double d) {
        return d < Gisement.EPSILON;
    }

    public Point getOrigin() {
        return this.origin;
    }

    public void setOrigin(Point _origin) {
        this.origin = _origin;
        this.compute();
    }

    public Point getOrientation() {
        return this.orientation;
    }

    public void setOrientation(Point _orientation) {
        this.orientation = _orientation;
        this.compute();
    }

    public double getGisement() {
        return this.gisement;
    }

    public double getHorizDist() {
        return this.horizDist;
    }

    public double getAltitude() {
        return this.altitude;
    }

    public double getSlope() {
        return this.slope;
    }
}