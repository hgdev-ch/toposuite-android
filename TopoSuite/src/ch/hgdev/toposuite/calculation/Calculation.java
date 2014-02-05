package ch.hgdev.toposuite.calculation;

import java.util.Calendar;
import java.util.Date;

/**
 * Calculation is the base class for all calculations. Each subclass of Calculation <b>must</b>
 * call {@link Calculation#updateLastModification()} when they update their attributes in order
 * to keep the last modification up to date.
 *  
 * @author HGdev
 */
public abstract class Calculation {
    /**
     * Type of calculation.
     */
    private String type;
    
    /**
     * Calculations description.
     */
    private String description;
    
    /**
     * Date of the last modification.
     */
    private Calendar lastModification;
    
    /**
     * Constructs a new Calculation.
     * @param _type type of calculation
     * @param _description description of the calculation
     */
    public Calculation(String _type, String _description) {
        this.type = _type;
        this.description = _description;
        
        // set the updateAt to the current time
        this.lastModification = new Date();
    }

    /**
     * Getter for the description.
     * @return the calculation description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the description.
     * @param _description the new description
     */
    public void setDescription(String _description) {
        this.description = _description;
    }

    /**
     * Getter for the calculation type.
     * @return the calculation type
     */
    public String getType() {
        return type;
    }

    /**
     * Getter for the last modification date.
     * @return the last modification date
     */
    public Calendar getLastModification() {
        return lastModification;
    }
    
    /**
     * Update the last modification date with the current date.
     */
    public void updateLastModification() {
        this.lastModification = Calendar.getInstance();
    }
}