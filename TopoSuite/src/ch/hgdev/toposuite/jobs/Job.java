package ch.hgdev.toposuite.jobs;

import java.text.ParseException;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.Calculation;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.AppUtils;

class Job {
    public static final String  EXTENSION            = "tpst";

    private static final String GENERATED_BY_KEY     = "generated_by";
    private static final String VERSION_KEY          = "version";
    private static final String CREATED_AT_KEY       = "created_at";
    private static final String APP_VERSION_NAME_KEY = "toposuite_name_version";
    private static final String APP_VERSION_CODE_KEY = "toposuite_code_version";
    private static final String POINTS_KEY           = "points";
    private static final String CALCULATIONS_KEY     = "calculations";

    private static final String VERSION              = "1";
    private static final String GENERATOR            = "TopoSuite Android";

    public static String getCurrentJobAsString() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put(Job.GENERATED_BY_KEY, Job.GENERATOR);
        jo.put(Job.VERSION_KEY, Job.VERSION);
        jo.put(Job.CREATED_AT_KEY, Calendar.getInstance().getTime());
        jo.put(Job.APP_VERSION_NAME_KEY, AppUtils.getVersionName());
        jo.put(Job.APP_VERSION_CODE_KEY, AppUtils.getVersionCode());

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