package ch.hgdev.toposuite;

import java.io.File;
import java.util.Locale;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.InputType;
import ch.hgdev.toposuite.calculation.Calculation;
import ch.hgdev.toposuite.dao.CalculationsDataSource;
import ch.hgdev.toposuite.dao.DBHelper;
import ch.hgdev.toposuite.dao.PointsDataSource;
import ch.hgdev.toposuite.dao.collections.DAOMapperArrayList;
import ch.hgdev.toposuite.dao.collections.DAOMapperTreeSet;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.settings.SettingsActivity;
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
    public static final String PUBLIC_DIR                    = "Toposuite";

    /**
     * Database file name.
     */
    public static final String DATABASE                      = "topo_suite.db";

    /**
     * The file name used by the points sharing function.
     */
    public static final String FILENAME_FOR_POINTS_SHARING   = "toposuite-points.csv";

    /**
     * Database version. This number must be increased whenever the database
     * schema is upgraded in order to trigger the
     * {@link SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)}
     * method.
     */
    public static final int    DATABASE_VERSION              = 6;

    /**
     * CSV separator.
     */
    public static final String CSV_SEPARATOR                 = ";";

    /**
     * Number of decimal to display with dealing with numbers.
     */
    public static String       numberOfDecimals              = "%.4f";

    /**
     * A smaller number of decimals than {@link App}. It is used to format
     * numbers that are not meant to be very precise.
     */
    public static String       smallNumberOfDecimals         = "%.2f";

    /**
     * Date format.
     */
    public static final String dateFormat                    = "MM-dd-yyyy HH:mm";

    /**
     * Default locale (language).
     */
    public static final Locale locale                        = Locale.getDefault();

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
    public static boolean      arePointsExported             = false;

    /**
     * Database helper.
     */
    public static DBHelper     dbHelper;

    /**
     * Application context.
     */
    private static Context     context;

    /**
     * Set number of decimal to which a coordinate value should be rounded to
     * (not only display).
     */
    private static int         coordinateDecimalRounding     = 3;

    /**
     * Number of decimal used for value of type coordinate.
     */
    private static int         decimalPrecisionForCoordinate = 3;

    /**
     * Number of decimal used for value of type angle.
     */
    private static int         decimalPrecisionForAngle      = 4;

    /**
     * Number of decimal used for value of type distance.
     */
    private static int         decimalPrecisionForDistance   = 3;

    /**
     * Number of decimal used for value of type average.
     */
    private static int         decimalPrecisionForAverage    = 3;

    /**
     * Number of decimal used for value of type gap.
     */
    private static int         decimalPrecisionForGap        = 1;

    /**
     * Number of decimal used for value of type surface.
     */
    private static int         decimalPrecisionForSurface    = 4;

    /**
     * Number of decimal used for value of type CC.
     */
    private static int         decimalPrecisionForCC         = 0;

    /**
     * Determine an input type of type coordinate, that is a decimal signed
     * number.
     */
    private static int         inputTypeCoordinate;

    /**
     * Standard type for coordinates.
     */
    private static final int   coordinatesTypeStandard       = InputType.TYPE_CLASS_NUMBER
                                                                     | InputType.TYPE_NUMBER_FLAG_DECIMAL;
    /**
     * Type of coordinate that allows values to be negative.
     */
    private static final int   coordinatesTypeAllowNegative  = App.coordinatesTypeStandard
                                                                     | InputType.TYPE_NUMBER_FLAG_SIGNED;

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
                Logger.log(Logger.ErrLabel.IO_ERROR,
                        "Failed to create the temporary directoy!");
            }
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean allowNegativeCoordinate = prefs.getBoolean(
                SettingsActivity.SettingsFragment.KEY_PREF_NEGATIVE_COORDINATES, true);
        if (allowNegativeCoordinate) {
            App.inputTypeCoordinate = App.coordinatesTypeAllowNegative;
        } else {
            App.inputTypeCoordinate = App.coordinatesTypeStandard;
        }

        App.decimalPrecisionForCoordinate = prefs.getInt(
                SettingsActivity.SettingsFragment.KEY_PREF_COORDINATES_DISPLAY_PRECISION, 3);
        App.decimalPrecisionForAngle = prefs.getInt(
                SettingsActivity.SettingsFragment.KEY_PREF_ANGLES_DISPLAY_PRECISION, 4);
        App.decimalPrecisionForDistance = prefs.getInt(
                SettingsActivity.SettingsFragment.KEY_PREF_DISTANCES_DISPLAY_PRECISION, 3);
        App.decimalPrecisionForAverage = prefs.getInt(
                SettingsActivity.SettingsFragment.KEY_PREF_AVERAGES_DISPLAY_PRECISION, 3);
        App.decimalPrecisionForGap = prefs.getInt(
                SettingsActivity.SettingsFragment.KEY_PREF_GAPS_DISPLAY_PRECISION, 1);
        App.decimalPrecisionForSurface = prefs.getInt(
                SettingsActivity.SettingsFragment.KEY_PREF_SURFACES_DISPLAY_PRECISION, 4);
    }

    @Override
    public void onTerminate() {
        File tmpDir = new File(App.tmpDirectoryPath);
        if (!tmpDir.delete()) {
            Logger.log(Logger.ErrLabel.IO_ERROR, "Cannot delete temportary directory!");
        }
        super.onTerminate();
    }

    public static Context getContext() {
        return App.context;
    }

    /*
     * Toggle the allowed input coordinates.
     */
    public static void toggleNegativeCoordinates() {
        switch (App.inputTypeCoordinate) {
        case App.coordinatesTypeStandard:
            App.inputTypeCoordinate = App.coordinatesTypeAllowNegative;
            break;
        case App.coordinatesTypeAllowNegative:
            App.inputTypeCoordinate = App.coordinatesTypeStandard;
            break;
        default:
            Logger.log(Logger.ErrLabel.SETTINGS_ERROR,
                    "The type of allowed input coordinate is non valid");
        }
    }

    public static int getInputTypeCoordinate() {
        return App.inputTypeCoordinate;
    }

    public static int getDecimalPrecisionForCoordinate() {
        return App.decimalPrecisionForCoordinate;
    }

    public static void setDecimalPrecisionForCoordinate(int decimalPrecisionForCoordinate) {
        App.decimalPrecisionForCoordinate = decimalPrecisionForCoordinate;
    }

    public static int getDecimalPrecisionForAngle() {
        return App.decimalPrecisionForAngle;
    }

    public static void setDecimalPrecisionForAngle(int decimalPrecisionForAngle) {
        App.decimalPrecisionForAngle = decimalPrecisionForAngle;
    }

    public static int getDecimalPrecisionForDistance() {
        return App.decimalPrecisionForDistance;
    }

    public static void setDecimalPrecisionForDistance(int decimalPrecisionForDistance) {
        App.decimalPrecisionForDistance = decimalPrecisionForDistance;
    }

    public static int getDecimalPrecisionForAverage() {
        return App.decimalPrecisionForAverage;
    }

    public static void setDecimalPrecisionForAverage(int decimalPrecisionForAverage) {
        App.decimalPrecisionForAverage = decimalPrecisionForAverage;
    }

    public static int getDecimalPrecisionForGap() {
        return App.decimalPrecisionForGap;
    }

    public static void setDecimalPrecisionForGap(int decimalPrecisionForGap) {
        App.decimalPrecisionForGap = decimalPrecisionForGap;
    }

    public static int getDecimalPrecisionForSurface() {
        return App.decimalPrecisionForSurface;
    }

    public static void setDecimalPrecisionForSurface(int decimalPrecisionForSurface) {
        App.decimalPrecisionForSurface = decimalPrecisionForSurface;
    }

    public static int getDecimalPrecisionForCC() {
        return App.decimalPrecisionForCC;
    }

    public static int getCoordinateDecimalRounding() {
        return App.coordinateDecimalRounding;
    }

    public static void setCoordinateDecimalRounding(int coordinateDecimalRounding) {
        App.coordinateDecimalRounding = coordinateDecimalRounding;
    }

    public static double getCoordinateTolerance() {
        return 1.0 / Math.pow(10, App.getDecimalPrecisionForCoordinate());
    }

    public static double getAngleTolerance() {
        return 1.0 / Math.pow(10, App.getDecimalPrecisionForAngle());
    }
}