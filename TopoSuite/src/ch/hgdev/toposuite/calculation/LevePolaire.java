package ch.hgdev.toposuite.calculation;

import java.util.Date;

import org.json.JSONException;

import ch.hgdev.toposuite.points.Point;

/**
 * Implementation of the "leve polaire" calculation.
 * 
 * @author HGdev
 * 
 */
public class LevePolaire extends Calculation {
    private static final String CALCULATION_NAME = "LevePolaire";

    /**
     * The base station for the calculation.
     */
    private Point               station;
    private double              direction;

    public LevePolaire(long id, Date lastModification) {
        super(id, CalculationType.LEVEPOLAIRE, LevePolaire.CALCULATION_NAME, lastModification, true);
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
        // TODO uncomment when the activity is implemented
        return null;
        // return LevePolaireActivity.class;
    }
}