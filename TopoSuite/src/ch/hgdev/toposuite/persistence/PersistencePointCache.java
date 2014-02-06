package ch.hgdev.toposuite.persistence;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.patterns.Observable;
import ch.hgdev.toposuite.patterns.ObservableTreeSet;
import ch.hgdev.toposuite.patterns.Observer;
import ch.hgdev.toposuite.points.Point;

/**
 * 
 * @author HGdev
 */
public class PersistencePointCache implements Observer {
    
    @Override
    public void update(Observable o, Object arg) {
        if (arg != null) {
            App.dbPointEntity.delete((Point) arg);
        } else {
            ObservableTreeSet<Point> points = (ObservableTreeSet<Point>) o;
            App.dbPointEntity.create(points.last());
        }
    }
}
