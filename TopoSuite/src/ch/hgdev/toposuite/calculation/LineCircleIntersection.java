package ch.hgdev.toposuite.calculation;

import java.util.Date;

import org.json.JSONException;

public class LineCircleIntersection extends Calculation {

    public LineCircleIntersection(long id, Date lastModification) {
        super(id,
                CalculationType.LINECIRCINTERSEC,
                "line circle intersection",
                lastModification,
                true);
    }

    @Override
    public void compute() {
        // TODO implement
    }

    @Override
    public String exportToJSON() throws JSONException {
        // TODO implement
        return null;
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        // TODO implement
    }

    @Override
    public Class<?> getActivityClass() {
        // TODO implement
        return null;
    }
}
