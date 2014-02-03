/**
 * 
 */
package ch.hgdev.toposuite.points;

/**
 * A point is defined by a number, its distance to the east and the north and
 * its altitude.
 * 
 * @author HGdev
 * 
 */
public class Point {

    private int    number;
    private double east;
    private double north;
    private double altitude;
    private int    basePoint;

    /**
     * A point is characterized by its number, distance to the east and north
     * and its altitude. A base point is a referrer point.
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
     *            Referrer point.
     */
    public Point(int number, double east, double north, double altitude, int basePoint) {
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

    public int getBasePoint() {
        return this.basePoint;
    }

    public void setBasePoint(int _basePoint) {
        this.basePoint = _basePoint;
    }
}
