package ch.hgdev.toposuite.calculation;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.MathUtils;

public class OrthogonalBase implements Serializable {
    private static final String ORIGIN = "origin";
    private static final String EXTREMITY = "extremity";
    private static final String MEASURED_DISTANCE = "measured_distance";
    private static final String SCALE_FACTOR_LABEL = "default_scale_factor";

    private Point origin;
    private Point extremity;
    private double calculatedDistance;
    private double measuredDistance;
    private double scaleFactor;

    private final double DEFAULT_SCALE_FACTOR;

    public OrthogonalBase(@NonNull Point _origin, @NonNull Point _extremity, double _measuredDistance,
                          double defaultScaleFactor) {
        this.DEFAULT_SCALE_FACTOR = defaultScaleFactor;
        this.origin = _origin;
        this.extremity = _extremity;
        this.measuredDistance = _measuredDistance;

        this.updateCalcDistAndScaleFactor();
    }

    public OrthogonalBase(@NonNull Point _origin, @NonNull Point _extremity, double _measuredDistance) {
        this(_origin, _extremity, _measuredDistance, MathUtils.IGNORE_DOUBLE);
    }

    public OrthogonalBase(@NonNull Point _origin, @NonNull Point _extremity) {
        this(_origin, _extremity, MathUtils.IGNORE_DOUBLE);
    }

    public OrthogonalBase() {
        this.DEFAULT_SCALE_FACTOR = MathUtils.IGNORE_DOUBLE;
    }

    public OrthogonalBase(double defaultScaleFactor) {
        this.DEFAULT_SCALE_FACTOR = defaultScaleFactor;
        this.measuredDistance = MathUtils.IGNORE_DOUBLE;
    }

    private void updateCalcDistAndScaleFactor() {
        if ((this.origin == null) || (this.extremity == null)) {
            return;
        }

        this.calculatedDistance = MathUtils.euclideanDistance(this.origin, this.extremity);

        if (!MathUtils.isZero(this.measuredDistance) || !MathUtils.isIgnorable(this.measuredDistance)) {
            this.scaleFactor = this.calculatedDistance / this.measuredDistance;
        } else {
            this.scaleFactor = this.DEFAULT_SCALE_FACTOR;
        }
    }

    public JSONObject toJSONObject() {
        JSONObject jo = new JSONObject();

        try {
            if (this.origin != null) {
                jo.put(OrthogonalBase.ORIGIN, this.origin.getNumber());
            }

            if (this.extremity != null) {
                jo.put(OrthogonalBase.EXTREMITY, this.extremity.getNumber());
            }

            jo.put(OrthogonalBase.MEASURED_DISTANCE, this.measuredDistance);
            jo.put(OrthogonalBase.SCALE_FACTOR_LABEL, this.DEFAULT_SCALE_FACTOR);
        } catch (JSONException e) {
            Logger.log(Logger.ErrLabel.PARSE_ERROR, e.getMessage());
        }

        return jo;
    }

    public static OrthogonalBase getOrthogonalBaseFromJSON(@NonNull String json) {
        OrthogonalBase ob = null;

        try {
            JSONObject jo = new JSONObject(json);

            Point origin = SharedResources.getSetOfPoints().find(
                    jo.getString(OrthogonalBase.ORIGIN));
            Point extremity = SharedResources.getSetOfPoints().find(
                    jo.getString(OrthogonalBase.EXTREMITY));
            double measureDist = jo.getDouble(OrthogonalBase.MEASURED_DISTANCE);
            double defaultScaleFactor = jo.getDouble(OrthogonalBase.SCALE_FACTOR_LABEL);

            ob = new OrthogonalBase(origin, extremity, measureDist, defaultScaleFactor);
        } catch (JSONException e) {
            Logger.log(Logger.ErrLabel.PARSE_ERROR, e.getMessage());
        }

        return ob;
    }

    public Point getOrigin() {
        return this.origin;
    }

    public void setOrigin(@NonNull Point _origin) {
        this.origin = _origin;
        this.updateCalcDistAndScaleFactor();
    }

    public Point getExtremity() {
        return this.extremity;
    }

    public void setExtremity(@NonNull Point _extremity) {
        this.extremity = _extremity;
        this.updateCalcDistAndScaleFactor();
    }

    public double getCalculatedDistance() {
        return this.calculatedDistance;
    }

    public double getMeasuredDistance() {
        return this.measuredDistance;
    }

    public void setMeasuredDistance(double measuredDistance) {
        this.measuredDistance = measuredDistance;
        this.updateCalcDistAndScaleFactor();
    }

    public double getScaleFactor() {
        return this.scaleFactor;
    }

    public int getScaleFactorPPM() {
        return MathUtils.scaleToPPM(this.scaleFactor);
    }
}