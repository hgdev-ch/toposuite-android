package ch.hgdev.toposuite.calculation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.interfaces.Exportable;
import ch.hgdev.toposuite.calculation.interfaces.Importable;
import ch.hgdev.toposuite.dao.CalculationsDataSource;
import ch.hgdev.toposuite.dao.interfaces.DAO;
import ch.hgdev.toposuite.dao.interfaces.DAOUpdater;
import ch.hgdev.toposuite.utils.DisplayUtils;

/**
 * Calculation is the base class for all calculations. Each subclass of
 * Calculation <b>must</b> call {@link Calculation#updateLastModification()}
 * when they update their attributes in order to keep the last modification up
 * to date.
 * 
 * @author HGdev
 */
public abstract class Calculation implements Exportable, Importable, DAOUpdater {
    public static final String    ID                = "id";
    public static final String    TYPE              = "type";
    public static final String    DESCRIPTION       = "description";
    public static final String    LAST_MODIFICATION = "last_modification";
    public static final String    INPUT_DATA        = "input_data";

    private static final String   DATE_FORMAT       = "dd-MM-yyyy H:m:s";

    /**
     * The ID used by the database.
     */
    private long                  id;

    /**
     * Type of calculation.
     */
    private final CalculationType type;

    /**
     * Calculations description.
     */
    private String                description;

    /**
     * Date of the last modification.
     */
    private Date                  lastModification;

    /**
     * List of DAO linked.
     */
    private final ArrayList<DAO>  daoList;

    /**
     * Constructs a new Calculation.
     * 
     * @param _id
     *            the calculation ID
     * @param _type
     *            type of calculation
     * @param _description
     *            description of the calculation
     * @param _lastModification
     *            the last modification date
     */
    public Calculation(long _id, CalculationType _type, String _description,
            Date _lastModification, boolean hasDAO) {
        this.id = _id;
        this.type = _type;
        this.description = _description;
        this.lastModification = _lastModification;

        if (this.lastModification == null) {
            this.lastModification = Calendar.getInstance().getTime();
        }

        this.daoList = new ArrayList<DAO>();

        if (hasDAO) {
            this.registerDAO(CalculationsDataSource.getInstance());
        }
    }

    /**
     * 
     * @param _type
     * @param _description
     */
    public Calculation(CalculationType _type, String _description, boolean hasDAO) {
        this(0, _type, _description, null, hasDAO);

        // since no ID is provided, this a new calculation and then, we have to
        // add it
        // into the calculation history.
        // SharedResources.getCalculationsHistory().add(0, this);
    }

    /**
     * Method that should return the activity class related to the calculation.
     * 
     * @return Activity class related to the calculation.
     */
    public abstract Class<?> getActivityClass();

    /**
     * Method that should return the calculation name.
     * 
     * @return Calculation name.
     */
    public abstract String getCalculationName();

    /**
     * Method that actually performs the calculation.
     */
    public abstract void compute() throws CalculationException;

    /**
     * Getter for the ID.
     * 
     * @return the ID
     */
    public long getId() {
        return this.id;
    }

    /**
     * Setter for the ID.
     * 
     * @param _id
     */
    public void setId(long _id) {
        this.id = _id;
    }

    /**
     * Getter for the description.
     * 
     * @return the calculation description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Setter for the description.
     * 
     * @param _description
     *            the new description
     */
    public void setDescription(String _description) {
        this.description = _description;
    }

    /**
     * Getter for the calculation type.
     * 
     * @return the calculation type
     */
    public CalculationType getType() {
        return this.type;
    }

    /**
     * Getter for the last modification date.
     * 
     * @return the last modification date
     */
    public Date getLastModification() {
        return this.lastModification;
    }

    /**
     * Update the last modification date with the current date.
     */
    public void updateLastModification() {
        this.lastModification = Calendar.getInstance().getTime();
    }

    /**
     * Serialize Calculation to JSON.
     * 
     * @return JSON representation of the calculation.
     * @throws JSONException
     */
    public final JSONObject toJSON() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put(Calculation.ID, this.id);
        jo.put(Calculation.DESCRIPTION, this.description);
        jo.put(Calculation.TYPE, this.type.toString());
        jo.put(Calculation.INPUT_DATA, this.exportToJSON());

        SimpleDateFormat sdf = new SimpleDateFormat(Calculation.DATE_FORMAT);
        jo.put(Calculation.LAST_MODIFICATION, sdf.format(this.lastModification));

        return jo;
    }

    /**
     * Create a Calculation from a JSON string.
     * 
     * @param json
     *            JSON string that represents a Calculation.
     * @return A new Calculation.
     */
    public static Calculation createCalculationFromJSON(String json) throws JSONException,
            ParseException {
        JSONObject jo = new JSONObject(json);
        long id = jo.getLong(Calculation.ID);
        String description = jo.getString(Calculation.DESCRIPTION);
        CalculationType type = CalculationType.valueOf(jo.getString(Calculation.TYPE));

        SimpleDateFormat sdf = new SimpleDateFormat(Calculation.DATE_FORMAT);
        Date lastModification = sdf.parse(jo.getString(Calculation.LAST_MODIFICATION));

        String jsonInputArgs = jo.getString(Calculation.INPUT_DATA);

        Calculation c = CalculationFactory.createCalculation(type, id, description,
                lastModification, jsonInputArgs);
        SharedResources.getCalculationsHistory().add(c);

        return c;
    }

    @Override
    public String toString() {
        return String.format("%s    %s",
                DisplayUtils.formatDate(this.lastModification),
                this.description);
    }

    @Override
    public void registerDAO(DAO dao) {
        this.daoList.add(dao);
    }

    @Override
    public void removeDAO(DAO dao) {
        this.daoList.remove(dao);
    }

    @Override
    public void notifyUpdate(Object obj) {
        for (DAO dao : this.daoList) {
            dao.update(obj);
        }
    }
}