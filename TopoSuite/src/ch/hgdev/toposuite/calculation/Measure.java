package ch.hgdev.toposuite.calculation;

import android.os.Parcel;
import android.os.Parcelable;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.points.Point;

public class Measure implements Parcelable {
    private Point                                   orientation;
    private double                                  horizDir;
    private double                                  zenAngle;
    private double                                  distance;
    private double                                  s;
    private double                                  latDepl;
    private double                                  lonDepl;

    /**
     * Attribute used by the Parcelable interface.
     */
    public static final Parcelable.Creator<Measure> CREATOR =
            new Parcelable.Creator<Measure>() {
        @Override
        public Measure createFromParcel(Parcel in) {
            return new Measure(in);
        }

        @Override
        public Measure[] newArray(
                int size) {
            return new Measure[size];
        }
    };

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

    /**
     * Private constructor for deserializing a parcelabled Measure.
     * @param in
     *            Parcel input
     */
    private Measure(Parcel in) {
        this.orientation = SharedResources.getSetOfPoints().find(in.readInt());
        this.horizDir = in.readDouble();
        this.zenAngle = in.readDouble();
        this.distance = in.readDouble();
        this.s = in.readDouble();
        this.latDepl = in.readDouble();
        this.lonDepl = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        // the point number that will be used for retrieving the point
        // in the SharedResources.getSetOfPoints()
        out.writeInt(this.orientation.getNumber());

        out.writeDouble(this.horizDir);
        out.writeDouble(this.zenAngle);
        out.writeDouble(this.distance);
        out.writeDouble(this.s);
        out.writeDouble(this.latDepl);
        out.writeDouble(this.lonDepl);
    }

    @Override
    public int describeContents() {
        return 0;
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
}