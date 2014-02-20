package ch.hgdev.toposuite;

import java.io.File;
import java.util.Locale;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import ch.hgdev.toposuite.calculation.Calculation;
import ch.hgdev.toposuite.dao.CalculationsDataSource;
import ch.hgdev.toposuite.dao.DBHelper;
import ch.hgdev.toposuite.dao.PointsDataSource;
import ch.hgdev.toposuite.dao.collections.DAOMapperArrayList;
import ch.hgdev.toposuite.dao.collections.DAOMapperTreeSet;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.Logger;

/**
 * Handle every settings that need to be global to the application.
 * 
 * @author HGdev
 * 
 */
public class App extends Application {
    /**
     * App (public) directory.
     */
    public static final String PUBLIC_DIR                       = "Toposuite";

    /**
     * Database file name.
     */
    public static final String DATABASE                         = "topo_suite.db";

    /**
     * The file name used by the points sharing function.
     */
    public static final String FILENAME_FOR_POINTS_SHARING      = "toposuite-points.csv";

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
     * This variable contains the path to the publicly accessible data directory
     * of the app. It is initialized in the {@link App#onCreate()} method.
     */
    public static String       publicDataDirectory;

    /**
     * Path to the temporary directory. It is initialized in the
     * {@link App#onCreate()} method.
     */
    public static String       tmpDirectoryPath;

    /**
     * Flag for verifying if the points have been exported or not.
     */
    public static boolean      arePointsExported                = false;

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

        // init the public data directory path
        App.publicDataDirectory = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/" + App.PUBLIC_DIR;

        // init the temporary directory path
        App.tmpDirectoryPath = App.publicDataDirectory + "/tmp";

        // setup temporary directory
        File tmpDir = new File(App.tmpDirectoryPath);
        if (!tmpDir.exists()) {
            if (!tmpDir.mkdirs()) {
                Log.e(Logger.TOPOSUITE_IO_ERROR,
                        "Failed to create the temporary directoy!");
            }
        }
    }

    @Override
    public void onTerminate() {
        File tmpDir = new File(App.tmpDirectoryPath);
        if (!tmpDir.delete()) {
            Log.e(Logger.TOPOSUITE_IO_ERROR, "Cannot delete temportary directory!");
        }
        super.onTerminate();
    }

    public static Context getContext() {
        return App.context;
    }
}