package ch.hgdev.toposuite;

import ch.hgdev.toposuite.calculation.Calculation;
import ch.hgdev.toposuite.dao.CalculationsDataSource;
import ch.hgdev.toposuite.dao.PointsDataSource;
import ch.hgdev.toposuite.dao.collections.DAOMapperArrayList;
import ch.hgdev.toposuite.dao.collections.DAOMapperTreeSet;
import ch.hgdev.toposuite.dao.collections.Searcher;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.AlphanumComparator;

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
        if (SharedResources.calculationsHistory == null) {
            SharedResources.calculationsHistory = new DAOMapperArrayList<Calculation>(
                    new Searcher<Calculation>() {
                        @Override
                        public boolean isFound(Calculation currentElement, Object expectedElement) {
                            return ((Long) expectedElement).equals(currentElement.getId());
                        }
                    });
            SharedResources.calculationsHistory.registerDAO(CalculationsDataSource.getInstance());
        }

        return SharedResources.calculationsHistory;
    }

    /**
     * Static getter for the set of points.
     * 
     * @return The set of points.
     */
    public static DAOMapperTreeSet<Point> getSetOfPoints() {
        if (SharedResources.setOfPoints == null) {
            SharedResources.setOfPoints = new DAOMapperTreeSet<Point>(new AlphanumComparator(),
                    new Searcher<Point>() {
                        @Override
                        public boolean isFound(Point currentElement, Object expectedElement) {
                            String expectedNumber = (String) expectedElement;
                            return currentElement.getNumber().equals(expectedNumber);
                        }
                    });
            SharedResources.setOfPoints.registerDAO(PointsDataSource.getInstance());
        }
        return SharedResources.setOfPoints;
    }
}
