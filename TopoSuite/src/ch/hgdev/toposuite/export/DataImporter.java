package ch.hgdev.toposuite.export;

/**
 * Interface for importing data from a file.
 * 
 * @author HGdev
 * 
 */
public interface DataImporter {
    /**
     * Initialize Point's attributes from CSV.
     * 
     * @param csvLine
     *            A CSV line that contains the values of the Point's attributes.
     */
    void createPointFromCSV(String csvLine);
}