package ch.hgdev.toposuite.calculation;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.MathUtils;

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
     * Point (usually orientation point).
     */
    private Point               point;
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
    private String              measureNumber;

    /**
     * Abscissa.
     */
    private double              abscissa;

    /**
     * Ordinate.
     */
    private double              ordinate;

    /**
     * True if the measure is deactivated, false otherwise. This flag must not
     * be serialized because it must not be persistent.
     */
    private boolean             deactivated;

    public Measure(Point _point, double _horizDir, double _zenAngle, double _distance,
            double _s, double _latDepl, double _lonDepl, double _i, double _unknownOrientation,
            String _measureNumber, double _abscissa, double _ordinate) {
        this.point = _point;
        this.horizDir = _horizDir;
        this.zenAngle = _zenAngle;
        this.distance = _distance;
        this.s = _s;
        this.latDepl = _latDepl;
        this.lonDepl = _lonDepl;
        this.i = _i;
        this.unknownOrientation = _unknownOrientation;
        this.measureNumber = _measureNumber;
        this.abscissa = _abscissa;
        this.ordinate = _ordinate;
        this.deactivated = false;
    }

    public Measure(Point _point, double _horizDir, double _zenAngle, double _distance,
            double _s, double _latDepl, double _lonDepl, double _i, double _unknownOrientation,
            String _measureNumber, double _abscissa) {
        this(_point, _horizDir, _zenAngle, _distance, _s, _latDepl, _lonDepl, _i,
                _unknownOrientation, _measureNumber, _abscissa, MathUtils.IGNORE_DOUBLE);
    }

    public Measure(Point _point, double _horizDir, double _zenAngle, double _distance,
            double _s, double _latDepl, double _lonDepl, double _i, double _unknownOrientation,
            String _measureNumber) {
        this(_point, _horizDir, _zenAngle, _distance, _s, _latDepl, _lonDepl, _i,
                _unknownOrientation, _measureNumber, MathUtils.IGNORE_INT);
    }

    public Measure(Point _point, double _horizDir, double _zenAngle, double _distance,
            double _s, double _latDepl, double _lonDepl, double _i, double _unknownOrientation) {
        this(_point, _horizDir, _zenAngle, _distance, _s, _latDepl, _lonDepl, _i,
                _unknownOrientation, "");
    }

    public Measure(Point _point, double _horizDir, double _zenAngle, double _distance,
            double _s, double _latDepl, double _lonDepl, double _i) {
        this(_point, _horizDir, _zenAngle, _distance, _s, _latDepl, _lonDepl, _i,
                MathUtils.IGNORE_DOUBLE);
    }

    public Measure(Point _point, double _horizDir, double _zenAngle, double _distance,
            double _s, double _latDepl, double _lonDepl) {
        this(_point, _horizDir, _zenAngle, _distance, _s, _latDepl, _lonDepl,
                MathUtils.IGNORE_DOUBLE);
    }

    public Measure(Point _point, double _horizDir, double _zenAngle, double _distance,
            double _s, double _latDepl) {
        this(_point, _horizDir, _zenAngle, _distance, _s, _latDepl, MathUtils.IGNORE_DOUBLE);
    }

    public Measure(Point _point, double _horizDir, double _zenAngle, double _distance,
            double _s) {
        this(_point, _horizDir, _zenAngle, _distance, _s, MathUtils.IGNORE_DOUBLE);
    }

    public Measure(Point _point, double _horizDir, double _zenAngle, double _distance) {
        this(_point, _horizDir, _zenAngle, _distance, MathUtils.IGNORE_DOUBLE);
    }

    public Measure(Point _point, double _horizDir, double _zenAngle) {
        this(_point, _horizDir, _zenAngle, MathUtils.IGNORE_DOUBLE);
    }

    public Measure(Point _point, double _horizDir) {
        this(_point, _horizDir, 100.0);
    }

    public Measure(Point _point) {
        this(_point, MathUtils.IGNORE_DOUBLE);
    }

    public Point getPoint() {
        return this.point;
    }

    public void setPoint(Point orientation) {
        this.point = orientation;
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

    public String getMeasureNumber() {
        return this.measureNumber;
    }

    public void setMeasureNumber(String measureNumber) {
        this.measureNumber = measureNumber;
    }

    public double getAbscissa() {
        return this.abscissa;
    }

    public void setAbscissa(double abscissa) {
        this.abscissa = abscissa;
    }

    public double getOrdinate() {
        return this.ordinate;
    }

    public void setOrdinate(double ordinate) {
        this.ordinate = ordinate;
    }

    public final boolean isDeactivated() {
        return this.deactivated;
    }

    public final void deactivate() {
        this.deactivated = true;
    }

    public final void reactivate() {
        this.deactivated = false;
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();

        try {
            if (this.point != null) {
                json.put(Measure.ORIENTATION_NUMBER, this.point.getNumber());
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
            Logger.log(Logger.ErrLabel.PARSE_ERROR, Measure.JSON_SERIALIZE_ERROR);
        }

        return json;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        if (this.point != null) {
            bundle.putString(Measure.ORIENTATION_NUMBER, this.point.getNumber());
        }
        bundle.putDouble(Measure.HORIZ_DIR, this.horizDir);
        bundle.putDouble(Measure.ZEN_ANGLE, this.zenAngle);
        bundle.putDouble(Measure.DISTANCE, this.distance);
        bundle.putDouble(Measure.S, this.s);
        bundle.putDouble(Measure.LAT_DEPL, this.latDepl);
        bundle.putDouble(Measure.LON_DEPL, this.lonDepl);
        bundle.putDouble(Measure.I, this.i);
        bundle.putDouble(Measure.UNKNOWN_ORIENTATION, this.unknownOrientation);
        bundle.putString(Measure.MEASURE_NUMBER, this.measureNumber);
        return bundle;
    }

    public static Measure getMeasureFromJSON(String jsonString) {
        Measure m = null;
        JSONObject json;
        try {
            json = new JSONObject(jsonString);
            Point orient = json.has(Measure.ORIENTATION_NUMBER) ?
                    SharedResources.getSetOfPoints().find(
                            json.getString(Measure.ORIENTATION_NUMBER))
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
                                    json.getString(Measure.MEASURE_NUMBER));
        } catch (JSONException e) {
            Logger.log(Logger.ErrLabel.PARSE_ERROR, Measure.JSON_UNSERIALIZE_ERROR);
        }

        return m;
    }
}