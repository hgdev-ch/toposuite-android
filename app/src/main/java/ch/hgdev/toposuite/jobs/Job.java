package ch.hgdev.toposuite.jobs;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;

import com.google.common.io.Files;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.Calculation;
import ch.hgdev.toposuite.dao.CalculationsDataSource;
import ch.hgdev.toposuite.dao.PointsDataSource;
import ch.hgdev.toposuite.dao.SQLiteTopoSuiteException;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.settings.SettingsActivity;
import ch.hgdev.toposuite.utils.AppUtils;
import ch.hgdev.toposuite.utils.Logger;

public class Job {
    public static final String EXTENSION = "tpst";

    private static final String GENERATED_BY_KEY = "generated_by";
    private static final String VERSION_KEY = "version";
    private static final String CREATED_AT_KEY = "created_at";
    private static final String APP_VERSION_NAME_KEY = "toposuite_name_version";
    private static final String APP_VERSION_CODE_KEY = "toposuite_code_version";
    private static final String SETTINGS_KEY = "settings";
    private static final String CSV_SEPARATOR = "csv_separator";
    private static final String COORDINATE_PRECISION_KEY = "coordinate_precision";
    private static final String ANGLE_PRECISION_KEY = "angle_precision";
    private static final String DISTANCE_PRECISION_KEY = "distance_precision";
    private static final String AVERAGE_PRECISION_KEY = "average_precision";
    private static final String GAP_PRECISION_KEY = "gap_precision";
    private static final String SURFACE_PRECISION_KEY = "surface_precision";
    private static final String COORDINATE_ROUNDING = "coordinate_rounding";
    private static final String POINTS_KEY = "points";
    private static final String CALCULATIONS_KEY = "calculations";

    private static final String VERSION = "2";
    private static final String GENERATOR = "TopoSuite Android";

    private final String name;
    private final long lastModified;
    private final File tpst;


    public Job(@NonNull File tpst) {
        this.name = Files.getNameWithoutExtension(tpst.getName());
        this.tpst = tpst;
        this.lastModified = tpst.lastModified();
    }

    public Job(@NonNull String name, @NonNull File tpst) {
        this.name = name;
        this.tpst = tpst;
        this.lastModified = tpst.lastModified();
    }

    public String getName() {
        return name;
    }

    public File getTpst() {
        return tpst;
    }

    public long getLastModified() {
        return lastModified;
    }

    public static String getCurrentJobName() {
        return App.getCurrentJobName();
    }

    private static void setCurrentJobName(String name) {
        App.setCurrentJobName(name);
    }

    public static String getCurrentJobAsJson() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put(Job.GENERATED_BY_KEY, Job.GENERATOR);
        jo.put(Job.VERSION_KEY, Job.VERSION);
        jo.put(Job.CREATED_AT_KEY, AppUtils.serializeDate(Calendar.getInstance().getTime()));
        jo.put(Job.APP_VERSION_NAME_KEY, AppUtils.getVersionName());
        jo.put(Job.APP_VERSION_CODE_KEY, AppUtils.getVersionCode());

        JSONObject settingsObject = new JSONObject();
        settingsObject.put(Job.CSV_SEPARATOR,
                App.getCSVSeparator());
        settingsObject.put(Job.COORDINATE_PRECISION_KEY,
                App.getDecimalPrecisionForCoordinate());
        settingsObject.put(Job.ANGLE_PRECISION_KEY,
                App.getDecimalPrecisionForAngle());
        settingsObject.put(Job.DISTANCE_PRECISION_KEY,
                App.getDecimalPrecisionForDistance());
        settingsObject.put(Job.AVERAGE_PRECISION_KEY,
                App.getDecimalPrecisionForAverage());
        settingsObject.put(Job.GAP_PRECISION_KEY,
                App.getDecimalPrecisionForGap());
        settingsObject.put(Job.SURFACE_PRECISION_KEY,
                App.getDecimalPrecisionForSurface());
        settingsObject.put(Job.COORDINATE_ROUNDING,
                App.getCoordinateDecimalRounding());

        jo.put(Job.SETTINGS_KEY, settingsObject);

        JSONArray pointsArray = new JSONArray();
        for (Point p : SharedResources.getSetOfPoints()) {
            pointsArray.put(p.toJSON());
        }

        jo.put(Job.POINTS_KEY, pointsArray);

        JSONArray calculationsArray = new JSONArray();
        for (Calculation c : SharedResources.getCalculationsHistory()) {
            calculationsArray.put(c.toJSON());

            jo.put(Job.CALCULATIONS_KEY, calculationsArray);
        }

        return jo.toString();
    }

    public static void loadJobFromJSON(String json) throws JSONException {
        JSONObject jo = new JSONObject(json);

        JSONObject settingsObject = jo.getJSONObject(Job.SETTINGS_KEY);

        try {
            String sep = settingsObject.getString(Job.CSV_SEPARATOR);
            if (sep.length() == 1) {
                for (String s : App.getContext().getResources().getStringArray(R.array.csv_separator)) {
                    if (sep.equals(s)) {
                        App.setCSVSeparator(sep);
                        break;
                    }
                }
            }
        } catch (JSONException e) {
            Logger.log(Logger.WarnLabel.RESOURCE_NOT_FOUND, e.getMessage());
        }

        try {
            App.setDecimalPrecisionForCoordinate(
                    settingsObject.getInt(Job.COORDINATE_PRECISION_KEY));
        } catch (JSONException e) {
            Logger.log(Logger.WarnLabel.RESOURCE_NOT_FOUND, e.getMessage());
        }

        try {
            App.setDecimalPrecisionForAngle
                    (settingsObject.getInt(Job.ANGLE_PRECISION_KEY));
        } catch (JSONException e) {
            Logger.log(Logger.WarnLabel.RESOURCE_NOT_FOUND, e.getMessage());
        }

        try {
            App.setDecimalPrecisionForDistance(
                    settingsObject.getInt(Job.DISTANCE_PRECISION_KEY));
        } catch (JSONException e) {
            Logger.log(Logger.WarnLabel.RESOURCE_NOT_FOUND, e.getMessage());
        }

        try {
            App.setDecimalPrecisionForAverage(
                    settingsObject.getInt(Job.AVERAGE_PRECISION_KEY));
        } catch (JSONException e) {
            Logger.log(Logger.WarnLabel.RESOURCE_NOT_FOUND, e.getMessage());
        }

        try {
            App.setDecimalPrecisionForGap(
                    settingsObject.getInt(Job.GAP_PRECISION_KEY));
        } catch (JSONException e) {
            Logger.log(Logger.WarnLabel.RESOURCE_NOT_FOUND, e.getMessage());
        }

        try {
            App.setDecimalPrecisionForSurface(
                    settingsObject.getInt(Job.SURFACE_PRECISION_KEY));
        } catch (JSONException e) {
            Logger.log(Logger.WarnLabel.RESOURCE_NOT_FOUND, e.getMessage());
        }

        // this setting is mandatory
        App.setCoordinateDecimalRounding(settingsObject.getInt(Job.COORDINATE_ROUNDING));

        // update the shared preferences with the new settings values
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                App.getContext()).edit();
        editor.putString(SettingsActivity.SettingsFragment.KEY_PREF_CSV_SEPARATOR,
                App.getCSVSeparator());
        editor.putInt(SettingsActivity.SettingsFragment.KEY_PREF_COORDINATES_DISPLAY_PRECISION,
                App.getDecimalPrecisionForCoordinate());
        editor.putInt(SettingsActivity.SettingsFragment.KEY_PREF_ANGLES_DISPLAY_PRECISION,
                App.getDecimalPrecisionForAngle());
        editor.putInt(SettingsActivity.SettingsFragment.KEY_PREF_DISTANCES_DISPLAY_PRECISION,
                App.getDecimalPrecisionForDistance());
        editor.putInt(SettingsActivity.SettingsFragment.KEY_PREF_AVERAGES_DISPLAY_PRECISION,
                App.getDecimalPrecisionForAverage());
        editor.putInt(SettingsActivity.SettingsFragment.KEY_PREF_GAPS_DISPLAY_PRECISION,
                App.getDecimalPrecisionForGap());
        editor.putInt(SettingsActivity.SettingsFragment.KEY_PREF_SURFACES_DISPLAY_PRECISION,
                App.getDecimalPrecisionForSurface());
        editor.putInt(SettingsActivity.SettingsFragment.KEY_PREF_COORDINATES_DECIMAL_PRECISION,
                App.getCoordinateDecimalRounding());
        editor.apply();

        if (!jo.isNull(Job.POINTS_KEY)) {
            for (int i = 0; i < jo.getJSONArray(Job.POINTS_KEY).length(); i++) {
                JSONObject pointObject = (JSONObject) jo.getJSONArray(
                        Job.POINTS_KEY).get(i);
                Point.createPointFromJSON(pointObject.toString());
            }
        }

        // load calculations, only if there are points as well
        if (!jo.isNull(Job.POINTS_KEY) && !jo.isNull(Job.CALCULATIONS_KEY)) {
            for (int i = 0; i < jo.getJSONArray(Job.CALCULATIONS_KEY).length(); i++) {
                JSONObject calculationObject = (JSONObject) jo.getJSONArray(
                        Job.CALCULATIONS_KEY).get(i);
                Calculation.createCalculationFromJSON(calculationObject.toString());
            }
        }
    }

    /**
     * Check if a given extension is valid for a job.
     *
     * @param ext A file extension
     * @return True if the extension is valid, false otherwise.
     */
    public static boolean isExtensionValid(String ext) {
        return Job.EXTENSION.equalsIgnoreCase(ext);
    }

    /**
     * Get a list of existing jobs.
     * Note: {@link ch.hgdev.toposuite.utils.AppUtils.Permission#READ_EXTERNAL_STORAGE} is required.
     *
     * @return A list of jobs.
     */
    public static ArrayList<Job> getJobsList() {
        ArrayList<Job> jobs = new ArrayList<>();
        String[] filenameList = new File(App.publicDataDirectory).list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return Files.getFileExtension(filename).equalsIgnoreCase(Job.EXTENSION);
            }
        });
        if ((filenameList != null) && (filenameList.length > 0)) {
            Arrays.sort(filenameList);

            for (String filename : filenameList) {
                jobs.add(new Job(new File(App.publicDataDirectory, filename)));
            }
        }
        return jobs;
    }

    public static boolean renameCurrentJob(@NonNull String name) {
        if (Job.getCurrentJobName() != null) {
            ArrayList<Job> jobsList = Job.getJobsList();
            for (Job j : jobsList) {
                if (name.equals(j.getName())) {
                    return false;
                }
            }
        }
        Job.setCurrentJobName(name);
        return true;
    }

    public static void deleteCurrentJob() throws SQLiteTopoSuiteException {
        // remove previous points and calculations from the SQLite DB
        PointsDataSource.getInstance().truncate();
        CalculationsDataSource.getInstance().truncate();

        // clean in-memory residues
        SharedResources.getSetOfPoints().clear();
        SharedResources.getCalculationsHistory().clear();

        // erase current job name
        Job.setCurrentJobName(null);
    }
}