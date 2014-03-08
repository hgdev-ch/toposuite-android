package ch.hgdev.toposuite;

import java.util.Comparator;

import ch.hgdev.toposuite.calculation.Calculation;
import ch.hgdev.toposuite.dao.CalculationsDataSource;
import ch.hgdev.toposuite.dao.PointsDataSource;
import ch.hgdev.toposuite.dao.collections.DAOMapperArrayList;
import ch.hgdev.toposuite.dao.collections.DAOMapperTreeSet;
import ch.hgdev.toposuite.dao.collections.Searcher;
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
    private static DAOMapperArrayList<Calculation> calculationsHistory;
    /**
     * Set of points.
     */
    private static DAOMapperTreeSet<Point>         setOfPoints;

    /**
     * Static getter for the calculations history.
     * 
     * @return the calculations history
     */
    public static DAOMapperArrayList<Calculation> getCalculationsHistory() {
        if (calculationsHistory == null) {
            calculationsHistory = new DAOMapperArrayList<Calculation>(
                    new Searcher<Calculation>() {
                        @Override
                        public boolean isFound(Calculation currentElement, Object expectedElement) {
                            return ((Long) expectedElement).equals(currentElement.getId());
                        }
                    });
            calculationsHistory.registerDAO(CalculationsDataSource.getInstance());
        }

        return calculationsHistory;
    }

    /**
     * Static getter for the set of points.
     * 
     * @return The set of points.
     */
    public static DAOMapperTreeSet<Point> getSetOfPoints() {
        if (setOfPoints == null) {
            setOfPoints = new DAOMapperTreeSet<Point>(new Comparator<Point>() {
                @Override
                public int compare(Point left, Point right) {
                    int l = left.getNumber();
                    int r = right.getNumber();
                    return (r > l ? -1 : (r == l ? 0 : 1));
                }
            }, new Searcher<Point>() {
                @Override
                public boolean isFound(Point currentElement, Object expectedElement) {
                    Integer expectedNumber = (Integer) expectedElement;
                    return currentElement.getNumber() == expectedNumber;
                }
            });
            setOfPoints.registerDAO(PointsDataSource.getInstance());
        }
        return setOfPoints;
    }
}
