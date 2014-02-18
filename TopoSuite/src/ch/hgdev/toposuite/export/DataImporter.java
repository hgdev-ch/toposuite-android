package ch.hgdev.toposuite.export;

import ch.hgdev.toposuite.points.Point;

/**
 * Interface for importing data from a file.
 * 
 * @author HGdev
 * 
 */
public interface DataImporter {
    /**
     * Unserialize object from CSV.
     * 
     * @param csvLine
     *            A CSV line.
     * @return A point
     */
    Point createPointFromCSV(String csvLine);
}