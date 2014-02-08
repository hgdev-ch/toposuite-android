package ch.hgdev.toposuite.calculation;

import ch.hgdev.toposuite.points.Point;

public class Measure {
    private Point  orientation;
    private double horizDir;
    private double zenAngle;
    private double distance;
    private double s;
    private double latDepl;
    private double lonDepl;

    public Measure(Point _orientation, double _horizDir, double _zenAngle,
            double _distance, double _s, double _latDepl, double _lonDepl) {
        this.orientation = _orientation;
        this.horizDir = _horizDir;
        this.zenAngle = _zenAngle;
        this.distance = _distance;
        this.s = _s;
        this.latDepl = _latDepl;
        this.lonDepl = _lonDepl;
    }

    public Measure(Point _orientation, double _horizDir, double _zenAngle,
            double _distance, double _s) {
        this(_orientation, _horizDir, _zenAngle, _distance, _s, 0.0, 0.0);
    }

    public Measure(Point _orientation, double _horizDir) {
        this(_orientation, _horizDir, 100.0, 0.0, 0.0);
    }

    public Point getOrientation() {
        return this.orientation;
    }

    public void setOrientation(Point orientation) {
        this.orientation = orientation;
    }

    public double getHorizDir() {
        return this.horizDir;
    }

    public void setHorizDir(double horizDir) {
        this.horizDir = horizDir;
    }

    public double getZenAngle() {
        return this.zenAngle;
    }

    public void setZenAngle(double zenAngle) {
        this.zenAngle = zenAngle;
    }

    public double getDistance() {
        return this.distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getS() {
        return this.s;
    }

    public void setS(double s) {
        this.s = s;
    }

    public double getLatDepl() {
        return this.latDepl;
    }

    public void setLatDepl(double latDepl) {
        this.latDepl = latDepl;
    }

    public double getLonDepl() {
        return this.lonDepl;
    }

    public void setLonDepl(double lonDepl) {
        this.lonDepl = lonDepl;
    }
}