package ch.hgdev.toposuite.calculation;

import java.util.Date;

import org.json.JSONException;

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
        case ABRISS:
            calculation = new Abriss(id, lastModification);
            break;
        case AXISIMPLANTATION:
            calculation = new AxisImplantation(id, lastModification);
            break;
        case CHEMINORTHO:
            calculation = new CheminementOrthogonal(id, lastModification);
            break;
        case CIRCLE:
            calculation = new Circle(id, lastModification);
            break;
        case CIRCLESINTERSEC:
            calculation = new CirclesIntersection(id, lastModification);
            break;
        case CIRCCURVESOLVER:
            calculation = new CircularCurvesSolver(id, lastModification);
            break;
        case CIRCULARSEGMENTATION:
            calculation = new CircularSegmentation(id, lastModification);
            break;
        case FREESTATION:
            calculation = new FreeStation(id, lastModification);
            break;
        case GISEMENT:
            calculation = new Gisement(id, lastModification);
            break;
        case LEVEORTHO:
            calculation = new LeveOrthogonal(id, lastModification);
            break;
        case LIMITDISPL:
            calculation = new LimitDisplacement(id, lastModification);
            break;
        case LINECIRCINTERSEC:
            calculation = new LineCircleIntersection(id, lastModification);
            break;
        case LINEINTERSEC:
            calculation = new LinesIntersection(id, lastModification);
            break;
        case ORTHOIMPL:
            calculation = new OrthogonalImplantation(id, lastModification);
            break;
        case POLARIMPLANT:
            calculation = new PolarImplantation(id, lastModification);
            break;
        case POLARSURVEY:
            calculation = new PolarSurvey(id, lastModification);
            break;
        case PROJPT:
            calculation = new PointProjectionOnALine(id, lastModification);
            break;
        case SURFACE:
            calculation = new Surface(id, lastModification);
            break;
        case TRIANGLESOLVER:
            calculation = new TriangleSolver(id, lastModification);
            break;
        default:
            Logger.log(Logger.ErrLabel.CALCULATION_IMPORT_ERROR,
                    CalculationFactory.CALCULATION_NOT_FOUND);
        }

        if ((calculation != null) && !Strings.isNullOrEmpty(jsonInputArgs)) {
            calculation.setDescription(description);
            try {
                calculation.importFromJSON(jsonInputArgs);
            } catch (JSONException e) {
                Logger.log(Logger.ErrLabel.CALCULATION_IMPORT_ERROR, e.getMessage());
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
