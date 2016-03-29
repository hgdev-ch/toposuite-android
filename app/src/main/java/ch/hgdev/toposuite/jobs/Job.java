package ch.hgdev.toposuite.jobs;

import java.text.ParseException;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.Calculation;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.settings.SettingsActivity;
import ch.hgdev.toposuite.utils.AppUtils;

class Job {
    public static final String  EXTENSION                = "tpst";

    private static final String GENERATED_BY_KEY         = "generated_by";
    private static final String VERSION_KEY              = "version";
    private static final String CREATED_AT_KEY           = "created_at";
    private static final String APP_VERSION_NAME_KEY     = "toposuite_name_version";
    private static final String APP_VERSION_CODE_KEY     = "toposuite_code_version";
    private static final String SETTINGS_KEY             = "settings";
    private static final String COORDINATE_PRECISION_KEY = "coordinate_precision";
    private static final String ANGLE_PRECISION_KEY      = "angle_precision";
    private static final String DISTANCE_PRECISION_KEY   = "distance_precision";
    private static final String AVERAGE_PRECISION_KEY    = "average_precision";
    private static final String GAP_PRECISION_KEY        = "gap_precision";
    private static final String SURFACE_PRECISION_KEY    = "surface_precision";
    private static final String COORDINATE_ROUNDING      = "coordinate_rounding";
    private static final String POINTS_KEY               = "points";
    private static final String CALCULATIONS_KEY         = "calculations";

    private static final String VERSION                  = "1";
    private static final String GENERATOR                = "TopoSuite Android";

    public static String getCurrentJobAsString() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put(Job.GENERATED_BY_KEY, Job.GENERATOR);
        jo.put(Job.VERSION_KEY, Job.VERSION);
        jo.put(Job.CREATED_AT_KEY, Calendar.getInstance().getTime());
        jo.put(Job.APP_VERSION_NAME_KEY, AppUtils.getVersionName());
        jo.put(Job.APP_VERSION_CODE_KEY, AppUtils.getVersionCode());

        JSONObject settingsObject = new JSONObject();
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

    public static void loadJobFromJSON(String json) throws JSONException, ParseException {
        JSONObject jo = new JSONObject(json);

        JSONObject settingsObject = jo.getJSONObject(Job.SETTINGS_KEY);
        App.setDecimalPrecisionForCoordinate(
                settingsObject.getInt(Job.COORDINATE_PRECISION_KEY));
        App.setDecimalPrecisionForAngle(
                settingsObject.getInt(Job.ANGLE_PRECISION_KEY));
        App.setDecimalPrecisionForDistance(
                settingsObject.getInt(Job.DISTANCE_PRECISION_KEY));
        App.setDecimalPrecisionForAverage(
                settingsObject.getInt(Job.AVERAGE_PRECISION_KEY));
        App.setDecimalPrecisionForGap(
                settingsObject.getInt(Job.GAP_PRECISION_KEY));
        App.setDecimalPrecisionForSurface(
                settingsObject.getInt(Job.SURFACE_PRECISION_KEY));
        App.setCoordinateDecimalRounding(
                settingsObject.getInt(Job.COORDINATE_ROUNDING));

        // update the shared preferences with the new settings values
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                App.getContext()).edit();
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

        for (int i = 0; i < jo.getJSONArray(Job.POINTS_KEY).length(); i++) {
            JSONObject pointObject = (JSONObject) jo.getJSONArray(
                    Job.POINTS_KEY).get(i);
            Point.createPointFromJSON(pointObject.toString());
        }

        for (int i = 0; i < jo.getJSONArray(Job.CALCULATIONS_KEY).length(); i++) {
            JSONObject calculationObject = (JSONObject) jo.getJSONArray(
                    Job.CALCULATIONS_KEY).get(i);
            Calculation.createCalculationFromJSON(calculationObject.toString());
        }
    }

    /**
     * Check if a given extension is valid for a job.
     * 
     * @param ext
     *            A file extension
     * @return True if the extension is valid, false otherwise.
     */
    public static boolean isExtensionValid(String ext) {
        return Job.EXTENSION.equalsIgnoreCase(ext);
    }
}