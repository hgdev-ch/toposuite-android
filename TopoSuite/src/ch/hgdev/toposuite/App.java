package ch.hgdev.toposuite;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import ch.hgdev.toposuite.patterns.ObservableTreeSet;
import ch.hgdev.toposuite.persistence.DBPointEntity;
import ch.hgdev.toposuite.points.Point;

/**
 * Handle every settings that need to be global to the application.
 * 
 * @author HGdev
 * 
 */
public class App extends Application {
    /**
     * Database file name.
     */
    public static final String DATABASE = "topo_suite.db";
    
    /**
     * Database version. This number must be increased whenever the database
     * schema is upgraded in order to trigger the
     * {@link SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)}
     * method.
     */
    public static final int DATABASE_VERSION = 1;
    
    /**
     * Number of decimal to display with dealing with numbers.
     */
    public static String numberOfDecimals = "%.4f";
    
    /**
     * Date format.
     */
    public static String dateFormat = "MM-dd-yyyy HH:mm";
    
    public static DBPointEntity dbPointEntity;

    
    @Override
    public void onCreate() {
        super.onCreate();
        
        Log.d("FOOBAR", "PLOP");
        Log.d("TOPOSUITE", "HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
        App.dbPointEntity = new DBPointEntity(this.getApplicationContext());
        
        ObservableTreeSet<Point> points = SharedResources.getSetOfPoints();
        points.setNotifyOnChange(false);
        
        points.addAll(App.dbPointEntity.findAll());
        points.setNotifyOnChange(true);
    }
}