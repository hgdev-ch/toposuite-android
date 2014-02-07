package ch.hgdev.toposuite.calculation;

import java.util.Calendar;
import java.util.Date;

import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.interfaces.Exportable;
import ch.hgdev.toposuite.calculation.interfaces.Importable;
import ch.hgdev.toposuite.utils.DisplayUtils;

/**
 * Calculation is the base class for all calculations. Each subclass of
 * Calculation <b>must</b> call {@link Calculation#updateLastModification()}
 * when they update their attributes in order to keep the last modification up
 * to date.
 * 
 * @author HGdev
 */
public abstract class Calculation implements Exportable, Importable {
    /**
     * The ID used by the database.
     */
    private long            id;

    /**
     * Type of calculation.
     */
    private CalculationType type;

    /**
     * Calculations description.
     */
    private String          description;

    /**
     * Date of the last modification.
     */
    private Date            lastModification;

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
    public Calculation(long _id, CalculationType _type, String _description, Date _lastModification) {
        this.id = _id;
        this.type = _type;
        this.description = _description;
        this.lastModification = _lastModification;

        if (this.lastModification == null) {
            this.lastModification = Calendar.getInstance().getTime();
        }
    }

    /**
     * 
     * @param _type
     * @param _description
     */
    public Calculation(CalculationType _type, String _description) {
        this(0, _type, _description, null);

        // since no ID is provided, this a new calculation and then, we have to
        // add it
        // into the calculation history.
        SharedResources.getCalculationsHistory().add(0, this);
    }

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

    @Override
    public String toString() {
        return String.format("%s    %s",
                DisplayUtils.formatDate(this.lastModification),
                this.type);
    }
}