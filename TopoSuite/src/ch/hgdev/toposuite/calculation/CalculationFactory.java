package ch.hgdev.toposuite.calculation;

import java.util.Date;

import org.json.JSONException;

import android.util.Log;
import ch.hgdev.toposuite.utils.Logger;

import com.google.common.base.Strings;

/**
 * Factory for easily creating new Calculation of a given type. Its main purpose
 * is to create empty Calculation and to eventually import some input arguments.
 * This is actually only useful for database operations. Indeed, it simplifies
 * and makes cleaner the loading of the calculations history from the DB.
 * 
 * @author HGdev
 */
public class CalculationFactory {
    public static final String CALCULATION_NOT_FOUND = "Calculation not found. Probably none existent or not implemented.";

    /**
     * Create a new calculation object and fill the input arguments with a
     * serialized set of input arguments.
     * 
     * @param type
     *            The type of calculations as defined in CalculationType enum.
     * @param id
     *            The calculation ID
     * @param description
     *            Calculation description
     * @param lastModification
     *            The last modification date
     * @param jsonInputArgs
     *            The serialized input arguments.
     * @return a calculation object with default values
     */
    public static Calculation createCalculation(CalculationType type, long id, String description,
            Date lastModification, String jsonInputArgs) {
        Calculation calculation = null;

        switch (type) {
        case GISEMENT:
            calculation = new Gisement(id, lastModification);
            break;
        case ABRISS:
            calculation = new Abriss(id, lastModification);
        case LEVEPOLAIRE:
            // TODO uncomment once the calculation has been implemented
            // calculation = new LevePolaire(id, lastModification);
            break;
        case LEVEORTHO:
            calculation = new LeveOrthogonal(id, lastModification);
            break;
        case CHEMINORTHO:
            calculation = new CheminementOrthogonal(id, lastModification);
        default:
            Log.e(Logger.TOPOSUITE_CALCULATION_IMPORT_ERROR,
                    CalculationFactory.CALCULATION_NOT_FOUND);
        }

        if ((calculation != null) && !Strings.isNullOrEmpty(jsonInputArgs)) {
            try {
                calculation.importFromJSON(jsonInputArgs);
            } catch (JSONException e) {
                Log.e(Logger.TOPOSUITE_CALCULATION_IMPORT_ERROR, e.getMessage());
            }
        }

        return calculation;
    }

    /**
     * See {@link CalculationFactory#createCalculation(CalculationType, String)}
     * 
     * @param type
     *            The type of calculations as defined in CalculationType enum.
     * @return calculation object with empty fields
     */
    public static Calculation createCalculation(CalculationType type) {
        return CalculationFactory.createCalculation(type, 0, "", null, null);
    }
}
