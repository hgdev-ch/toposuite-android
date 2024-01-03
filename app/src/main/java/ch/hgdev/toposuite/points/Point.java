package ch.hgdev.toposuite.points;

import android.content.Context;
import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.dao.DAOException;
import ch.hgdev.toposuite.dao.PointsDataSource;
import ch.hgdev.toposuite.dao.interfaces.DAO;
import ch.hgdev.toposuite.dao.interfaces.DAOUpdater;
import ch.hgdev.toposuite.transfer.DataExporter;
import ch.hgdev.toposuite.transfer.DataImporter;
import ch.hgdev.toposuite.transfer.InvalidFormatException;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.MathUtils;

/**
 * A point is defined by a number, its distance to the east and the north and
 * its altitude.
 *
 * @author HGdev
 */
public class Point implements DAOUpdater, DataExporter, DataImporter, Serializable {
    private static final String NUMBER = "number";
    private static final String EAST = "east";
    private static final String NORTH = "north";
    private static final String ALTITUDE = "altitude";
    private static final String BASE_POINT = "base_point";

    private String number;
    private double east;
    private double north;
    private double altitude;
    private final boolean basePoint;

    /**
     * List of DAO linked.
     */
    private final ArrayList<DAO> daoList;

    /**
     * A point is characterized by its number, distance to the east and north
     * and its altitude.
     *
     * @param number    Point number.
     * @param east      Point distance to the east.
     * @param north     Point distance to the north.
     * @param altitude  Point altitude.
     * @param basePoint Determine if this point is a base point. A base point is a
     *                  point that has been added as is and NOT computed.
     */
    public Point(String number, double east, double north, double altitude, boolean basePoint, boolean hasDAO) {
        // FIXME adapt the check according to the new point number format
        // Preconditions.checkArgument(number >= 0,
        // "A point number must be a positive integer: %s", number);
        this.number = number;
        this.east = MathUtils.roundCoordinate(east);
        this.north = MathUtils.roundCoordinate(north);
        this.altitude = MathUtils.roundCoordinate(altitude);
        this.basePoint = basePoint;

        this.daoList = new ArrayList<>();

        if (hasDAO) {
            this.registerDAO(PointsDataSource.getInstance());
        }
    }

    public Point(String number, double east, double north, double altitude, boolean basePoint) {
        this(number, east, north, altitude, basePoint, true);
    }

    public Point(boolean hasDAO) {
        this.daoList = new ArrayList<>();
        this.basePoint = false;

        this.number = "";
        this.east = MathUtils.IGNORE_DOUBLE;
        this.north = MathUtils.IGNORE_DOUBLE;
        this.altitude = MathUtils.IGNORE_DOUBLE;

        if (hasDAO) {
            this.registerDAO(PointsDataSource.getInstance());
        }
    }

    public Point() {
        this.daoList = new ArrayList<>();
        this.basePoint = true;

        this.registerDAO(PointsDataSource.getInstance());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if ((o.getClass() != this.getClass())) {
            return false;
        }

        Point point = (Point) o;
        if (this.getNumber().equals(point.getNumber())) {
            if (!MathUtils.equals(this.getEast(), point.getEast())) {
                return false;
            }
            if (!MathUtils.equals(this.getNorth(), point.getNorth())) {
                return false;
            }
            if (!MathUtils.equals(this.getAltitude(), point.getAltitude())) {
                return false;
            }
            return true;
        }
        return false;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String _number) {
        this.number = _number;
    }

    public double getEast() {
        return this.east;
    }

    public void setEast(double _east) {
        this.east = MathUtils.roundCoordinate(_east);
        try {
            this.notifyUpdate(this);
        } catch (DAOException e) {
            Logger.log(Logger.ErrLabel.DAO_ERROR, e.getMessage());
        }
    }

    public double getNorth() {
        return this.north;
    }

    public void setNorth(double _north) {
        this.north = MathUtils.roundCoordinate(_north);
        try {
            this.notifyUpdate(this);
        } catch (DAOException e) {
            Logger.log(Logger.ErrLabel.DAO_ERROR, e.getMessage());
        }
    }

    public double getAltitude() {
        return this.altitude;
    }

    public void setAltitude(double _altitude) {
        this.altitude = MathUtils.roundCoordinate(_altitude);
        try {
            this.notifyUpdate(this);
        } catch (DAOException e) {
            Logger.log(Logger.ErrLabel.DAO_ERROR, e.getMessage());
        }
    }

    public boolean isBasePoint() {
        return this.basePoint;
    }

    public String getBasePointAsString(Context context) {
        return this.basePoint ? context.getString(R.string.point_provided) : context
                .getString(R.string.point_computed);
    }

    /**
     * Serialize Point to JSON.
     *
     * @return A JSON representation of the point.
     * @throws JSONException
     */
    public final JSONObject toJSON() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put(Point.NUMBER, this.number);
        jo.put(Point.EAST, this.east);
        jo.put(Point.NORTH, this.north);
        jo.put(Point.ALTITUDE, this.altitude);
        jo.put(Point.BASE_POINT, this.basePoint);

        return jo;
    }

    /**
     * Create a new Point object from a given JSON string.
     *
     * @param json JSON string that contains a serialized version of a Point.
     * @return A new Point object mapped in the DB using the DAO.
     * @throws JSONException
     */
    public static Point createPointFromJSON(String json) throws JSONException {
        JSONObject jo = new JSONObject(json);
        String number = jo.getString(Point.NUMBER);
        double east = jo.getDouble(Point.EAST);
        double north = jo.getDouble(Point.NORTH);
        double altitude = jo.getDouble(Point.ALTITUDE);
        boolean basePoint = jo.getBoolean(Point.BASE_POINT);

        Point p = new Point(number, east, north, altitude, basePoint, true);
        SharedResources.getSetOfPoints().add(p);

        return p;
    }

    @Override
    public String toCSV() {
        StringBuilder builder = new StringBuilder();
        builder.append("\"" + this.getNumber() + "\"");
        builder.append(App.getCSVSeparator());
        builder.append(this.getEast());
        builder.append(App.getCSVSeparator());
        builder.append(this.getNorth());

        if (!MathUtils.isZero(this.getAltitude())) {
            builder.append(App.getCSVSeparator());
            builder.append(this.getAltitude());
        }

        return builder.toString();
    }

    @Override
    public void createPointFromCSV(@NonNull String csvLine) throws InvalidFormatException {
        String[] tmp = csvLine.split(App.getCSVSeparator());

        // well, attempt parsing with other separators then
        if (tmp.length < 3) {
            String[] separators = App.getContext().getResources().getStringArray(R.array.csv_separator);
            for (String sep : separators) {
                tmp = csvLine.split(sep);
                if (tmp.length >= 3) {
                    break;
                }
            }
            if (tmp.length < 3) {
                throw new InvalidFormatException(App.getContext().getString(
                        R.string.exception_invalid_format_values_number));
            }
        }

        try {
            String number = tmp[0].replace("\"", "");
            double east = Double.parseDouble(tmp[1]);
            double north = Double.parseDouble(tmp[2]);
            double altitude = MathUtils.IGNORE_DOUBLE;

            if (tmp.length == 4) {
                altitude = Double.parseDouble(tmp[3]);
            }

            this.number = number;
            this.east = east;
            this.north = north;
            this.altitude = altitude;

            this.notifyUpdate(this);
        } catch (NumberFormatException e) {
            throw new InvalidFormatException(App.getContext().getString(
                    R.string.exception_invalid_format_values));
        } catch (DAOException e) {
            Logger.log(Logger.ErrLabel.DAO_ERROR, e.getMessage());
        }
    }

    @Override
    public void createPointFromLTOP(@NonNull String ltopLine) throws InvalidFormatException {
        if (ltopLine.length() < 56) {
            throw new InvalidFormatException(App.getContext().getString(
                    R.string.exception_invalid_format_values_number));
        }

        // 1-10 => PUNKT + 11-14 => TY
        String punkt = ltopLine.substring(0, 14);

        // 33-44 => Y
        String y = ltopLine.substring(32, 44);

        // 45-56 => X
        String x = (ltopLine.length() < 56) ?
                ltopLine.substring(44, ltopLine.length()) :
                ltopLine.substring(44, 56);

        // 61-70 => H (optional)
        int hPosLimit = ltopLine.length() < 70 ? ltopLine.length() : 70;
        String h = (ltopLine.length() >= 60) ? ltopLine.substring(60, hPosLimit) : null;

        try {
            this.number = punkt;
            this.east = Double.parseDouble(y);
            this.north = Double.parseDouble(x);
            this.altitude = (h != null) ? Double.parseDouble(h) : MathUtils.IGNORE_DOUBLE;
        } catch (NumberFormatException e) {
            throw new InvalidFormatException(App.getContext().getString(
                    R.string.exception_invalid_format_values));
        }
    }

    @Override
    public void createPointFromPTP(@NonNull String ptpLine) throws InvalidFormatException {
        if (ptpLine.length() < 55) {
            throw new InvalidFormatException(App.getContext().getString(
                    R.string.exception_invalid_format_values_number));
        }

        // 11-22 => POINT
        String point = ptpLine.substring(10, 22);

        // 33-43 => COORD Y
        String coordY = ptpLine.substring(32, 43);

        // 45-55 => COORD X
        String coordX = ptpLine.substring(44, 55);

        // 57-64 => altitude (optional)
        String alti = (ptpLine.length() >= 64) ? ptpLine.substring(56, 64) : null;

        try {
            this.number = point;
            this.east = Double.parseDouble(coordY);
            this.north = Double.parseDouble(coordX);
            this.altitude = ((alti != null) && !alti.trim().isEmpty()) ?
                    Double.parseDouble(alti) : MathUtils.IGNORE_DOUBLE;
        } catch (NumberFormatException e) {
            throw new InvalidFormatException(App.getContext().getString(
                    R.string.exception_invalid_format_values));
        }
    }

    @Override
    public String toString() {
        return (this.number == null) ? "" : this.number;
    }

    @Override
    public void registerDAO(DAO dao) {
        if (!this.daoList.contains(dao)) {
            this.daoList.add(dao);
        }
    }

    @Override
    public void removeDAO(DAO dao) {
        this.daoList.remove(dao);
    }

    @Override
    public void notifyUpdate(Object obj) throws DAOException {
        App.arePointsExported = false;
        for (DAO dao : this.daoList) {
            dao.update(obj);
        }
    }

    /**
     * Clone a point. Since a point must be unique, the point number will not be
     * cloned.
     * <p/>
     * The created point is <b>not</b> stored in the
     * {@link SharedResources#getSetOfPoints()}.
     *
     * @return A clone of the current point.
     */
    @Override
    public Point clone() {
        return new Point("", this.east, this.north, this.altitude,
                this.basePoint, false);
    }
}
