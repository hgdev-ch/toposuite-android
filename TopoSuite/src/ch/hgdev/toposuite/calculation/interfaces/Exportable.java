package ch.hgdev.toposuite.calculation.interfaces;

import org.json.JSONException;

public interface Exportable {
    public String exportToJSON() throws JSONException;
}