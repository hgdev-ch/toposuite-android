package ch.hgdev.toposuite.export;

/**
 * Interface for exporting data to a file.
 * 
 * @author HGdev
 * 
 */
public interface DataExporter {
    /**
     * Serialize object into CSV.
     * 
     * @return CSV representation of the object.
     */
    String toCSV();
}
