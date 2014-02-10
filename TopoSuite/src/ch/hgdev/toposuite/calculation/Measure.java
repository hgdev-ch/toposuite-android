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
    public static final String  LAT_DEPL               = "lat_depl";
    public static final String  LON_DEPL               = "lon_depl";

    private static final String JSON_SERIALIZE_ERROR   = "Unable to serialize Measure!";
    private static final String JSON_UNSERIALIZE_ERROR = "Unable to unserialize Measure!";

    private Point               orientation;
    private double              horizDir;
    private double              zenAngle;
    private double              distance;
    private double              s;
    private double              latDepl;
    private double              lonDepl;

    public Measure(Point _orientation, double _horizDir, double _zenAngle,
            double _distance, double _s, double _latDepl, double _lonDepl) {
        this.orientation = _orientation;
        this.horizDir = _horizDir;
        this.zenAngle = _zenAngle;
        this.distance = _distance;
        this.s = _s;
        this.latDepl = _latDepl;
        this.lonDepl = _lonDepl;
    }

    public Measure(Point _orientation, double _horizDir, double _zenAngle,
            double _distance, double _s) {
        this(_orientation, _horizDir, _zenAngle, _distance, _s, 0.0, 0.0);
    }

    public Measure(Point _orientation, double _horizDir) {
        this(_orientation, _horizDir, 100.0, 0.0, 0.0);
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

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();

        try {
            json.put(Measure.ORIENTATION_NUMBER, this.orientation.getNumber());
            json.put(Measure.HORIZ_DIR, this.horizDir);
            json.put(Measure.ZEN_ANGLE, this.zenAngle);
            json.put(Measure.DISTANCE, this.distance);
            json.put(Measure.S, this.s);
            json.put(Measure.LAT_DEPL, this.latDepl);
            json.put(Measure.LON_DEPL, this.lonDepl);

        } catch (JSONException e) {
            Log.e(Logger.TOPOSUITE_PARSE_ERROR, Measure.JSON_SERIALIZE_ERROR);
        }

        return json;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt(Measure.ORIENTATION_NUMBER, this.orientation.getNumber());
        bundle.putDouble(Measure.HORIZ_DIR, this.horizDir);
        bundle.putDouble(Measure.ZEN_ANGLE, this.zenAngle);
        bundle.putDouble(Measure.DISTANCE, this.distance);
        bundle.putDouble(Measure.S, this.s);
        bundle.putDouble(Measure.LAT_DEPL, this.latDepl);
        bundle.putDouble(Measure.LON_DEPL, this.lonDepl);
        return bundle;
    }

    public static Measure getMeasureFromJSON(String jsonString) {
        Measure m = null;
        JSONObject json;
        try {
            json = new JSONObject(jsonString);
            m = new Measure(
                    SharedResources.getSetOfPoints().find(json.getInt(Measure.ORIENTATION_NUMBER)),
                    json.getDouble(Measure.HORIZ_DIR),
                    json.getDouble(Measure.ZEN_ANGLE),
                    json.getDouble(Measure.DISTANCE),
                    json.getDouble(Measure.S),
                    json.getDouble(Measure.LAT_DEPL),
                    json.getDouble(Measure.LON_DEPL));
        } catch (JSONException e) {
            Log.e(Logger.TOPOSUITE_PARSE_ERROR, Measure.JSON_UNSERIALIZE_ERROR);
        }

        return m;
    }
}