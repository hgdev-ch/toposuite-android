package ch.hgdev.toposuite.calculation;

import com.google.common.base.Strings;

import org.json.JSONException;

import java.util.Date;

import ch.hgdev.toposuite.utils.Logger;

/**
 * Factory for easily creating new Calculation of a given type. Its main purpose
 * is to create empty Calculation and to eventually import some input arguments.
 * This is actually only useful for database operations. Indeed, it simplifies
 * and makes cleaner the loading of the calculations history from the DB.
 *
 * @author HGdev
 */
public class CalculationFactory {
    private static final String CALCULATION_NOT_FOUND = "Calculation not found. Probably none existent or not implemented.";

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
            case ABRISS -> calculation = new Abriss(id, lastModification);
            case AXISIMPLANTATION -> calculation = new AxisImplantation(id, lastModification);
            case CHEMINORTHO -> calculation = new CheminementOrthogonal(id, lastModification);
            case CIRCLE -> calculation = new Circle(id, lastModification);
            case CIRCLESINTERSEC -> calculation = new CirclesIntersection(id, lastModification);
            case CIRCCURVESOLVER -> calculation = new CircularCurvesSolver(id, lastModification);
            case CIRCULARSEGMENTATION ->
                    calculation = new CircularSegmentation(id, lastModification);
            case FREESTATION -> calculation = new FreeStation(id, lastModification);
            case GISEMENT -> calculation = new Gisement(id, lastModification);
            case LEVEORTHO -> calculation = new LeveOrthogonal(id, lastModification);
            case LIMITDISPL -> calculation = new LimitDisplacement(id, lastModification);
            case LINECIRCINTERSEC -> calculation = new LineCircleIntersection(id, lastModification);
            case LINEINTERSEC -> calculation = new LinesIntersection(id, lastModification);
            case ORTHOIMPL -> calculation = new OrthogonalImplantation(id, lastModification);
            case POLARIMPLANT -> calculation = new PolarImplantation(id, lastModification);
            case POLARSURVEY -> calculation = new PolarSurvey(id, lastModification);
            case PROJPT -> calculation = new PointProjectionOnALine(id, lastModification);
            case SURFACE -> calculation = new Surface(id, lastModification);
            case TRIANGLESOLVER -> calculation = new TriangleSolver(id, lastModification);
            default -> Logger.log(Logger.ErrLabel.CALCULATION_IMPORT_ERROR,
                    CalculationFactory.CALCULATION_NOT_FOUND);
        }

        if ((calculation != null) && !Strings.isNullOrEmpty(jsonInputArgs)) {
            calculation.setDescription(description);
            try {
                calculation.importFromJSON(jsonInputArgs);
            } catch (JSONException | CalculationSerializationException e) {
                Logger.log(Logger.ErrLabel.CALCULATION_IMPORT_ERROR, e.getMessage() + " (" + calculation.getType() + ")");
            }
        }

        return calculation;
    }

    /**
     * See {@link CalculationFactory#createCalculation(CalculationType type, long id, String description,
            Date lastModification, String jsonInputArgs)}
     *
     * @param type
     *            The type of calculations as defined in CalculationType enum.
     * @return calculation object with empty fields
     */
    public static Calculation createCalculation(CalculationType type) {
        return CalculationFactory.createCalculation(type, 0, "", null, null);
    }
}
