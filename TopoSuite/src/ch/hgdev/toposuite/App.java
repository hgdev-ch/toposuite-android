package ch.hgdev.toposuite;

import java.util.Locale;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.InputType;
import ch.hgdev.toposuite.calculation.Calculation;
import ch.hgdev.toposuite.dao.CalculationsDataSource;
import ch.hgdev.toposuite.dao.DBHelper;
import ch.hgdev.toposuite.dao.PointsDataSource;
import ch.hgdev.toposuite.dao.collections.DAOMapperArrayList;
import ch.hgdev.toposuite.dao.collections.DAOMapperTreeSet;
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
    public static final String DATABASE                         = "topo_suite.db";

    /**
     * Database version. This number must be increased whenever the database
     * schema is upgraded in order to trigger the
     * {@link SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)}
     * method.
     */
    public static final int    DATABASE_VERSION                 = 5;

    /**
     * Determine an input type of type coordinate, that is a decimal signed
     * number.
     */
    public static final int    INPUTTYPE_TYPE_NUMBER_COORDINATE = InputType.TYPE_CLASS_NUMBER
                                                                        | InputType.TYPE_NUMBER_FLAG_DECIMAL
                                                                        | InputType.TYPE_NUMBER_FLAG_SIGNED;

    /**
     * CSV separator.
     */
    public static final String CSV_SEPARATOR                    = ";";

    /**
     * Number of decimal to display with dealing with numbers.
     */
    public static String       numberOfDecimals                 = "%.4f";

    /**
     * A smaller number of decimals than {@link App}. It is used to format
     * numbers that are not meant to be very precise.
     */
    public static String       smallNumberOfDecimals            = "%.2f";

    /**
     * Date format.
     */
    public static final String dateFormat                       = "MM-dd-yyyy HH:mm";

    /**
     * Default locale (language).
     */
    public static final Locale locale                           = Locale.getDefault();

    /**
     * Database helper.
     */
    public static DBHelper     dbHelper;

    /**
     * Application context.
     */
    private static Context     context;

    @Override
    public void onCreate() {
        super.onCreate();
        App.context = this.getApplicationContext();

        App.dbHelper = new DBHelper(App.context);

        DAOMapperTreeSet<Point> points = SharedResources.getSetOfPoints();
        points.setNotifyOnChange(false);
        points.addAll(PointsDataSource.getInstance().findAll());
        points.setNotifyOnChange(true);

        DAOMapperArrayList<Calculation> calculations =
                SharedResources.getCalculationsHistory();
        calculations.setNotifyOnChange(false);
        calculations.addAll(CalculationsDataSource.getInstance().findAll());
        calculations.setNotifyOnChange(true);
    }

    public static Context getContext() {
        return App.context;
    }
}