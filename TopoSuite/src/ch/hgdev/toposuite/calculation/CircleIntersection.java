package ch.hgdev.toposuite.calculation;

import java.util.Date;

import org.json.JSONException;

import ch.hgdev.toposuite.SharedResources;

public class CircleIntersection extends Calculation {

    public CircleIntersection(long id, Date lastModification) {
        super(id,
                CalculationType.CIRCLEINTERSEC,
                "Circle intersection",
                lastModification,
                true);
    }

    public CircleIntersection(boolean hasDAO) {
        super(CalculationType.CIRCLEINTERSEC,
                "Circle intersection",
                hasDAO);

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
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
