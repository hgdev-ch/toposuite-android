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

    public void setNumber(int number) {
        this.number = number;
    }

    public double getEast() {
        return this.east;
    }

    public void setEast(double east) {
        this.east = east;
    }

    public double getNorth() {
        return this.north;
    }

    public void setNorth(double north) {
        this.north = north;
    }

    public double getAltitude() {
        return this.altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public int getBasePoint() {
        return this.basePoint;
    }

    public void setBasePoint(int basePoint) {
        this.basePoint = basePoint;
    }
}
