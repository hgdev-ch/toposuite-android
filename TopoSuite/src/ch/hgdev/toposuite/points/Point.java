package ch.hgdev.toposuite.points;

import com.google.common.base.Preconditions;

/**
 * A point is defined by a number, its distance to the east and the north and
 * its altitude.
 * 
 * @author HGdev
 * 
 */
public class Point {

    private int     number;
    private double  east;
    private double  north;
    private double  altitude;
    private boolean basePoint;

    /**
     * A point is characterized by its number, distance to the east and north
     * and its altitude.
     * 
     * @param number
     *            Point number.
     * @param east
     *            Point distance to the east.
     * @param north
     *            Point distance to the north.
     * @param altitude
     *            Point altitude.
     * @param basePoint
     *            Determine if this point is a base point. A base point is a
     *            point that has been added as is and NOT computed.
     */
    public Point(int number, double east, double north, double altitude, boolean basePoint) {
        Preconditions.checkArgument(number >= 0, "A point number must be a positive integer: %s", number);

        this.number = number;
        this.east = east;
        this.north = north;
        this.altitude = altitude;
        this.basePoint = basePoint;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int _number) {
        this.number = _number;
    }

    public double getEast() {
        return this.east;
    }

    public void setEast(double _east) {
        this.east = _east;
    }

    public double getNorth() {
        return this.north;
    }

    public void setNorth(double _north) {
        this.north = _north;
    }

    public double getAltitude() {
        return this.altitude;
    }

    public void setAltitude(double _altitude) {
        this.altitude = _altitude;
    }

    public boolean getBasePoint() {
        return this.basePoint;
    }

    public void setBasePoint(boolean _basePoint) {
        this.basePoint = _basePoint;
    }

    @Override
    public String toString() {
        // the 0 number is used to put an empty item into the spinner
        if (this.number == 0) {
            return "";
        }
        return String.valueOf(this.number);
    }
}