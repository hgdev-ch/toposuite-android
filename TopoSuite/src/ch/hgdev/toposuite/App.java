package ch.hgdev.toposuite;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;

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
     * Database version. This number must be increase whenever the database
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
}