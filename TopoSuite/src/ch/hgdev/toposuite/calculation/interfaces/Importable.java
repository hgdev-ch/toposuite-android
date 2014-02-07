package ch.hgdev.toposuite.calculation.interfaces;

import org.json.JSONException;

public interface Importable {
    public void importFromJSON(String jsonInputArgs)  throws JSONException ;
}