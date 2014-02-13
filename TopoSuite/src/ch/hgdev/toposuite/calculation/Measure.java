package ch.hgdev.toposuite.calculation;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.Logger;

public class Measure {
    public static final String  ORIENTATION_NUMBER     = "orientation_number";
    public static final String  HORIZ_DIR              = "horiz_dir";
    public static final String  ZEN_ANGLE              = "zen_angle";
    public static final String  DISTANCE               = "distance";
    public static final String  S                      = "s";
    public static final String  I                      = "i";
    public static final String  LAT_DEPL               = "lat_depl";
    public static final String  LON_DEPL               = "lon_depl";
    public static final String  UNKNOWN_ORIENTATION    = "unknown_orientation";
    public static final String  MEASURE_NUMBER         = "measure_number";

    private static final String JSON_SERIALIZE_ERROR   = "Unable to serialize Measure!";
    private static final String JSON_UNSERIALIZE_ERROR = "Unable to unserialize Measure!";

    /**
     * Orientation point.
     */
    private Point               orientation;
    /**
     * Horizontal direction (Hz).
     */
    private double              horizDir;
    /**
     * Zenithal angle (Vz).
     */
    private double              zenAngle;
    /**
     * Dist. Incl. (Ds).
     */
    private double              distance;
    /**
     * Height of the prism (S).
     */
    private double              s;
    /**
     * Height of the instrument (I).
     */
    private double              i;
    /**
     * Lateral displacement (Dlat or sometimes Dm1).
     */
    private double              latDepl;
    /**
     * Longitudinal displacement (Dlong or sometimes Dm1).
     */
    private double              lonDepl;

    /**
     * Unknown orientation (Z0, result of abriss calculation).
     */
    private double              unknownOrientation;

    /**
     * Determine a number for the measure.
     */
    private int                 measureNumber;

    public Measure(Point _orientation, double _horizDir, double _zenAngle, double _distance,
            double _s, double _latDepl, double _lonDepl, double _i, double _unknownOrientation,
            int _measureNumber) {
        this.orientation = _orientation;
        this.horizDir = _horizDir;
        this.zenAngle = _zenAngle;
        this.distance = _distance;
        this.s = _s;
        this.latDepl = _latDepl;
        this.lonDepl = _lonDepl;
        this.i = _i;
        this.unknownOrientation = _unknownOrientation;
        this.measureNumber = _measureNumber;
    }

    public Measure(Point _orientation, double _horizDir, double _zenAngle, double _distance,
            double _s, double _latDepl, double _lonDepl, double _i, double _unknownOrientation) {
        this(_orientation, _horizDir, _zenAngle, _distance, _s, _latDepl, _lonDepl, _i,
                _unknownOrientation, 0);
    }

    public Measure(Point _orientation, double _horizDir, double _zenAngle, double _distance,
            double _s, double _latDepl, double _lonDepl, double _i) {
        this(_orientation, _horizDir, _zenAngle, _distance, _s, _latDepl, _lonDepl, _i, 0.0);
    }

    public Measure(Point _orientation, double _horizDir, double _zenAngle, double _distance,
            double _s, double _latDepl, double _lonDepl) {
        this(_orientation, _horizDir, _zenAngle, _distance, _s, _latDepl, _lonDepl, 0.0);
    }

    public Measure(Point _orientation, double _horizDir, double _zenAngle, double _distance,
            double _s, double _latDepl) {
        this(_orientation, _horizDir, _zenAngle, _distance, _s, _latDepl, 0.0);
    }

    public Measure(Point _orientation, double _horizDir, double _zenAngle, double _distance,
            double _s) {
        this(_orientation, _horizDir, _zenAngle, _distance, _s, 0.0);
    }

    public Measure(Point _orientation, double _horizDir, double _zenAngle, double _distance) {
        this(_orientation, _horizDir, _zenAngle, _distance, 0.0);
    }

    public Measure(Point _orientation, double _horizDir, double _zenAngle) {
        this(_orientation, _horizDir, _zenAngle, 0.0);
    }

    public Measure(Point _orientation, double _horizDir) {
        this(_orientation, _horizDir, 100.0);
    }

    public Measure(Point _orientation) {
        this(_orientation, 0.0);
    }

    public Point getOrientation() {
        return this.orientation;
    }

    public void setOrientation(Point orientation) {
        this.orientation = orientation;
    }

    public double getHorizDir() {
        return this.horizDir;
    }

    public void setHorizDir(double horizDir) {
        this.horizDir = horizDir;
    }

    public double getZenAngle() {
        return this.zenAngle;
    }

    public void setZenAngle(double zenAngle) {
        this.zenAngle = zenAngle;
    }

    public double getDistance() {
        return this.distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getS() {
        return this.s;
    }

    public void setS(double s) {
        this.s = s;
    }

    public double getLatDepl() {
        return this.latDepl;
    }

    public void setLatDepl(double latDepl) {
        this.latDepl = latDepl;
    }

    public double getLonDepl() {
        return this.lonDepl;
    }

    public void setLonDepl(double lonDepl) {
        this.lonDepl = lonDepl;
    }

    public double getI() {
        return this.i;
    }

    public void setI(double _i) {
        this.i = _i;
    }

    public double getUnknownOrientation() {
        return this.unknownOrientation;
    }

    public void setUnknownOrientation(double _unknownOrientation) {
        this.unknownOrientation = _unknownOrientation;
    }

    public int getMeasureNumber() {
        return this.measureNumber;
    }

    public void setMeasureNumber(int measureNumber) {
        this.measureNumber = measureNumber;
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();

        try {
            if (this.orientation != null) {
                json.put(Measure.ORIENTATION_NUMBER, this.orientation.getNumber());
            }
            json.put(Measure.HORIZ_DIR, this.horizDir);
            json.put(Measure.ZEN_ANGLE, this.zenAngle);
            json.put(Measure.DISTANCE, this.distance);
            json.put(Measure.S, this.s);
            json.put(Measure.LAT_DEPL, this.latDepl);
            json.put(Measure.LON_DEPL, this.lonDepl);
            json.put(Measure.I, this.i);
            json.put(Measure.UNKNOWN_ORIENTATION, this.unknownOrientation);
            json.put(Measure.MEASURE_NUMBER, this.measureNumber);

        } catch (JSONException e) {
            Log.e(Logger.TOPOSUITE_PARSE_ERROR, Measure.JSON_SERIALIZE_ERROR);
        }

        return json;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        if (this.orientation != null) {
            bundle.putInt(Measure.ORIENTATION_NUMBER, this.orientation.getNumber());
        }
        bundle.putDouble(Measure.HORIZ_DIR, this.horizDir);
        bundle.putDouble(Measure.ZEN_ANGLE, this.zenAngle);
        bundle.putDouble(Measure.DISTANCE, this.distance);
        bundle.putDouble(Measure.S, this.s);
        bundle.putDouble(Measure.LAT_DEPL, this.latDepl);
        bundle.putDouble(Measure.LON_DEPL, this.lonDepl);
        bundle.putDouble(Measure.I, this.i);
        bundle.putDouble(Measure.UNKNOWN_ORIENTATION, this.unknownOrientation);
        bundle.putInt(Measure.MEASURE_NUMBER, this.measureNumber);
        return bundle;
    }

    public static Measure getMeasureFromJSON(String jsonString) {
        Measure m = null;
        JSONObject json;
        try {
            json = new JSONObject(jsonString);
            Point orient = json.has(Measure.ORIENTATION_NUMBER) ?
                    SharedResources.getSetOfPoints().find(json.getInt(Measure.ORIENTATION_NUMBER))
                    : null;
            m = new Measure(
                    orient,
                    json.getDouble(Measure.HORIZ_DIR),
                    json.getDouble(Measure.ZEN_ANGLE),
                    json.getDouble(Measure.DISTANCE),
                    json.getDouble(Measure.S),
                    json.getDouble(Measure.LAT_DEPL),
                    json.getDouble(Measure.LON_DEPL),
                    json.getDouble(Measure.I),
                    json.getDouble(Measure.UNKNOWN_ORIENTATION),
                    json.getInt(Measure.MEASURE_NUMBER));
        } catch (JSONException e) {
            Log.e(Logger.TOPOSUITE_PARSE_ERROR, Measure.JSON_UNSERIALIZE_ERROR);
        }

        return m;
    }
}