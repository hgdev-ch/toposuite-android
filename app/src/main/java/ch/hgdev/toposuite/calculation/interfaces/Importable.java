package ch.hgdev.toposuite.calculation.interfaces;

import org.json.JSONException;

/**
 * This interface provides a simple method for importing a calculation input.
 * 
 * @author HGdev
 */
public interface Importable {
    /**
     * Import calculation inputs values from a JSON serialized string.
     * 
     * @param jsonInputArgs
     *            JSON string that contains the calculation input values
     * @throws JSONException
     */
    public void importFromJSON(String jsonInputArgs) throws JSONException;
}