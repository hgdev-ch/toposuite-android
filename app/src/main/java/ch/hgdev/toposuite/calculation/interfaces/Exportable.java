package ch.hgdev.toposuite.calculation.interfaces;

import org.json.JSONException;

/**
 * This interface provides a simple method for exporting the input values of a
 * calculation.
 * 
 * @author HGdev
 */
public interface Exportable {
    /**
     * Export a JSON string the input values of a calculation.
     * 
     * @return JSON string
     * @throws JSONException
     */
    public String exportToJSON() throws JSONException;
}