package ch.hgdev.toposuite;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ch.hgdev.toposuite.calculation.Calculation;
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
    private static List<Calculation> calculationsHistory;
    /**
     * Set of points.
     */
    private static Set<Point>        setOfPoints;

    /**
     * Static getter for the calculations history.
     * 
     * @return the calculations history
     */
    public static List<Calculation> getCalculationsHistory() {
        if (calculationsHistory == null) {
            calculationsHistory = new ArrayList<Calculation>();
        }
        return calculationsHistory;
    }

    /**
     * Static getter for the set of points.
     * 
     * @return The set of points.
     */
    public static Set<Point> getSetOfPoints() {
        if (setOfPoints == null) {
            setOfPoints = new TreeSet<Point>(new Comparator<Point>() {
                @Override
                public int compare(Point left, Point right) {
                    int l = left.getNumber();
                    int r = right.getNumber();
                    return (r > l ? -1 : (r == l ? 0 : 1));
                }
            });
        }
        return setOfPoints;
    }
}
