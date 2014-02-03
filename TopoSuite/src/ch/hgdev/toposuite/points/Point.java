/**
 * 
 */
package ch.hgdev.toposuite.points;

/**
 * A point is defined by a number, its orientation to the east and the north and
 * its altitude.
 * 
 * @author HGdev
 * 
 */
public class Point {

	private int number;
	private double east;
	private double north;
	private double altitude;

	/**
	 * A point is characterized by its number, orientation to the east and north
	 * and its altitude.
	 * 
	 * @param number
	 * @param east
	 * @param north
	 * @param altitude
	 */
	public Point(int number, double east, double north, double altitude) {
		this.number = number;
		this.east = east;
		this.north = north;
		this.altitude = altitude;
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
}
