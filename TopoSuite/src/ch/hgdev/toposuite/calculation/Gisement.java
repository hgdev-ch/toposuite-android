package ch.hgdev.toposuite.calculation;

import ch.hgdev.toposuite.points.Point;

/**
 * TODO add javadoc comments
 * 
 * @author HGdev
 */
public class Gisement extends Calculation {
    private Point origine;
    private Point orientation;
    
    private double gisement;
    private double distHoriz;
    private double altitude;
    private double slope;

    public Gisement(Point _origine, Point _orientation) {
        super("Gisement", "");
        
        this.origine = _origine;
        this.orientation = _orientation;
        
        this.compute();
    }
    
    public final void compute() {
        double deltaY = this.orientation.getEast() - this.origine.getEast();
        double deltaX = this.orientation.getNorth() - this.origine.getNorth();
        
        // complement remains 0.0 if deltaY is positive and deltaX is positive
        // and if deltaY is 0.0 and deltaX is positive.
        double complement = 0.0;
        
        if (deltaY > 0.0 && isZero(deltaX)) {
            complement = 100.0;
        } else if (deltaY < 0.0 && isZero(deltaX)) {
            complement = 300.0;
        } else if ((isZero(deltaY) && deltaX < 0.0) || (deltaY > 0.0 && deltaY < 0.0) || (deltaY < 0.0 && deltaX < 0.0)) {
            complement = 200.0;
        } else if (deltaY < 0.0 && deltaX > 0.0) {
            complement = 400.0;
        }
        
        // TODO create a separate helper for converting rad to grad
        this.gisement = (Math.atan(deltaY/deltaX)/Math.PI) * 200 + complement;
        
     // TODO create a separate helper for converting grad to rad
        this.distHoriz = deltaY / ((Math.sin((this.gisement * Math.PI) / 200)));
    }
    
    private boolean isZero(double d) {
        final double EPSILON = 0.0001; 
        return d < EPSILON && d > -EPSILON;
    }

    public Point getOrigine() {
        return origine;
    }

    public void setOrigine(Point _origine) {
        this.origine = _origine;
    }

    public Point getOrientation() {
        return orientation;
    }

    public void setOrientation(Point _orientation) {
        this.orientation = _orientation;
    }

    public double getGisement() {
        return gisement;
    }

    public double getDistHoriz() {
        return distHoriz;
    }

    public double getAltitude() {
        return altitude;
    }

    public double getSlope() {
        return slope;
    }
}