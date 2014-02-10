package ch.hgdev.toposuite.calculation;

import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

public class OrthogonalBase {
    private Point  origin;
    private Point  extemity;
    private double calculatedDistance;
    private double measuredDistance;
    private double scaleFactor;

    public OrthogonalBase(Point _origin, Point _extremity, double _calculatedDistance,
            double _measuredDistance, double _scaleFactor) {
        this.origin = _origin;
        this.extemity = _extremity;
        this.calculatedDistance = _calculatedDistance;
        this.measuredDistance = _measuredDistance;
        this.scaleFactor = _scaleFactor;
    }

    public OrthogonalBase(Point _origin, Point _extremity, double _measuredDistance) {
        this(_origin, _extremity, MathUtils.euclideanDistance(_origin, _extremity),
                _measuredDistance, MathUtils.euclideanDistance(
                        _origin, _extremity) / _measuredDistance);
    }

    public Point getOrigin() {
        return this.origin;
    }

    public void setOrigin(Point origin) {
        this.origin = origin;
    }

    public Point getExtemity() {
        return this.extemity;
    }

    public void setExtemity(Point extemity) {
        this.extemity = extemity;
    }

    public double getCalculatedDistance() {
        return this.calculatedDistance;
    }

    public void setCalculatedDistance(double calculatedDistance) {
        this.calculatedDistance = calculatedDistance;
    }

    public double getMeasuredDistance() {
        return this.measuredDistance;
    }

    public void setMeasuredDistance(double measuredDistance) {
        this.measuredDistance = measuredDistance;
    }

    public double getScaleFactor() {
        return this.scaleFactor;
    }

    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }
}