package ch.hgdev.toposuite.transfer;

/**
 * Interface for importing data from a file.
 * 
 * @author HGdev
 * 
 */
public interface DataImporter {
    /**
     * Initialize Point attributes from CSV.
     * 
     * @param csvLine
     *            A CSV line that contains the values of the Point attributes.
     * @throws InvalidFormatException
     */
    void createPointFromCSV(String csvLine) throws InvalidFormatException;

    /**
     * Initialize Point attributes from LTOP.
     * 
     * @param ltopLine
     *            A LTOP line that contains the values of the Point attributes.
     * @throws InvalidFormatException
     */
    void createPointFromLTOP(String ltopLine) throws InvalidFormatException;

    /**
     * Initialize Point attributes from PTP.
     * 
     * @param ptpLine
     *            A PTP line that contains the values of the Point attributes.
     * @throws InvalidFormatException
     */
    void createPointFromPTP(String ptpLine) throws InvalidFormatException;
}