package ch.hgdev.toposuite;

import java.util.ArrayList;
import java.util.List;

import ch.hgdev.toposuite.calculation.Calculation;

/**
 * SharedResources provides an easy way to share resources between the activities.
 * 
 * @author HGdev
 */
public class SharedResources {
    /**
     * Calculations history.
     */
    private static List<Calculation> calculationsHistory;
    
    /**
     * Static getter for the calculations history.
     * @return the calculations history
     */
    public static List<Calculation> getCalculationsHistory() {
        if (calculationsHistory == null) {
            calculationsHistory = new ArrayList<Calculation>();
        }
        return calculationsHistory;
    }
}
