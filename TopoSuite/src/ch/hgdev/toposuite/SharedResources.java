package ch.hgdev.toposuite;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import ch.hgdev.toposuite.calculation.Calculation;
import ch.hgdev.toposuite.patterns.ObservableArrayList;
import ch.hgdev.toposuite.patterns.ObservableTreeSet;
import ch.hgdev.toposuite.persistence.PersistencePointCache;
import ch.hgdev.toposuite.points.Point;

/**
 * SharedResources provides an easy way to share resources between the
 * activities.
 * 
 * @author HGdev
 */
public class SharedResources {
    /**
     * Calculations history.
     */
    private static ObservableArrayList<Calculation> calculationsHistory;
    /**
     * Set of points.
     */
    private static ObservableTreeSet<Point>        setOfPoints;

    /**
     * Static getter for the calculations history.
     * 
     * @return the calculations history
     */
    public static ObservableArrayList<Calculation> getCalculationsHistory() {
        if (calculationsHistory == null) {
            calculationsHistory = new ObservableArrayList<Calculation>();
        }
        
        return calculationsHistory;
    }

    /**
     * Static getter for the set of points.
     * 
     * @return The set of points.
     */
    public static ObservableTreeSet<Point> getSetOfPoints() {
        if (setOfPoints == null) {
            setOfPoints = new ObservableTreeSet<Point>(new Comparator<Point>() {
                @Override
                public int compare(Point left, Point right) {
                    int l = left.getNumber();
                    int r = right.getNumber();
                    return (r > l ? -1 : (r == l ? 0 : 1));
                }
            });
            setOfPoints.addObserver(new PersistencePointCache());
        }
        return setOfPoints;
    }
}
