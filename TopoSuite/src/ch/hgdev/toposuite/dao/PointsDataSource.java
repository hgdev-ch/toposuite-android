package ch.hgdev.toposuite.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.dao.interfaces.DAO;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.Logger;

/**
 * DAO for {@link Point}
 *
 * @author HGdev
 */
public class PointsDataSource implements DAO {
    private static final String     ERROR_CREATE   = "Unable to create a new point!";
    private static final String     ERROR_DELETE   = "Unable to delete a point!";
    private static final String     ERROR_UPDATE   = "Unable to update a point!";

    private static final String     SUCCESS_CREATE = "Point successfully created!";
    private static final String     SUCCESS_DELETE = "Point successfully deleted!";
    private static final String     SUCCESS_UPDATE = "Point successfully updated!";

    private static PointsDataSource pointsDataSource;

    public static PointsDataSource getInstance() {
        if (PointsDataSource.pointsDataSource == null) {
            PointsDataSource.pointsDataSource = new PointsDataSource();
        }
        return PointsDataSource.pointsDataSource;
    }

    /**
     * Find all points.
     *
     * @return the list of all points.
     */
    public ArrayList<Point> findAll() {
        SQLiteDatabase db = App.dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + PointsTable.TABLE_NAME_POINTS + " ORDER BY number ASC", null);
        ArrayList<Point> points = new ArrayList<Point>();

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                String number = cursor.getString(
                        cursor.getColumnIndex(PointsTable.COLUMN_NAME_NUMBER));
                double east = cursor.getDouble(
                        cursor.getColumnIndex(PointsTable.COLUMN_NAME_EAST));
                double north = cursor.getDouble(
                        cursor.getColumnIndex(PointsTable.COLUMN_NAME_NORTH));
                double altitude = cursor.getDouble(cursor
                        .getColumnIndex(PointsTable.COLUMN_NAME_ALTITUDE));
                boolean isBasePoint = cursor.getInt(
                        cursor.getColumnIndex(PointsTable.COLUMN_NAME_BASE_POINT)) == 1;

                points.add(new Point(number, east, north, altitude, isBasePoint));
                cursor.moveToNext();
            }
        }

        return points;
    }

    /**
     * Create a new Point in the database.
     *
     * @param obj
     *            a point
     * @throws SQLiteTopoSuiteException
     */
    @Override
    public void create(Object obj) throws SQLiteTopoSuiteException {
        Point point = (Point) obj;
        SQLiteDatabase db = App.dbHelper.getReadableDatabase();

        ContentValues pointValues = new ContentValues();
        pointValues.put(PointsTable.COLUMN_NAME_NUMBER, point.getNumber());
        pointValues.put(PointsTable.COLUMN_NAME_EAST, point.getEast());
        pointValues.put(PointsTable.COLUMN_NAME_NORTH, point.getNorth());
        pointValues.put(PointsTable.COLUMN_NAME_ALTITUDE, point.getAltitude());
        pointValues.put(PointsTable.COLUMN_NAME_BASE_POINT, point.isBasePoint() ? 1 : 0);

        long rowID = db.insert(PointsTable.TABLE_NAME_POINTS, null, pointValues);
        if (rowID == -1) {
            Logger.log(Logger.ErrLabel.SQL_ERROR, PointsDataSource.ERROR_CREATE + " => " +
                    Logger.formatPoint(point));
            throw new SQLiteTopoSuiteException(PointsDataSource.ERROR_CREATE);
        }

        Logger.log(Logger.InfoLabel.SQL_SUCCESS, PointsDataSource.SUCCESS_CREATE + " => " +
                Logger.formatPoint(point));
    }

    @Override
    public void update(Object obj) {
        Point point = (Point) obj;
        SQLiteDatabase db = App.dbHelper.getWritableDatabase();

        ContentValues pointValues = new ContentValues();
        pointValues.put(PointsTable.COLUMN_NAME_EAST, point.getEast());
        pointValues.put(PointsTable.COLUMN_NAME_NORTH, point.getNorth());
        pointValues.put(PointsTable.COLUMN_NAME_ALTITUDE, point.getAltitude());

        long rowID = db.update(
                PointsTable.TABLE_NAME_POINTS,
                pointValues,
                PointsTable.COLUMN_NAME_NUMBER + " = ?",
                new String[] { point.getNumber() });
        if (rowID == -1) {
            Logger.log(Logger.ErrLabel.SQL_ERROR, PointsDataSource.ERROR_UPDATE + " => " +
                    Logger.formatPoint(point));
            throw new SQLiteTopoSuiteException(PointsDataSource.ERROR_UPDATE);
        }
        Logger.log(Logger.InfoLabel.SQL_SUCCESS,
                PointsDataSource.SUCCESS_UPDATE + " => " + Logger.formatPoint(point));
    }

    /**
     * Delete a Point.
     *
     * @param obj
     *            a point
     * @throws SQLiteTopoSuiteException
     */
    @Override
    public void delete(Object obj) throws SQLiteTopoSuiteException {
        Point point = (Point) obj;
        SQLiteDatabase db = App.dbHelper.getWritableDatabase();

        long rowID = db.delete(PointsTable.TABLE_NAME_POINTS,
                PointsTable.COLUMN_NAME_NUMBER + " = '" + point.getNumber() + "'", null);
        if (rowID == -1) {
            Logger.log(Logger.ErrLabel.SQL_ERROR, PointsDataSource.ERROR_DELETE + " => " +
                    Logger.formatPoint(point));
            throw new SQLiteTopoSuiteException(PointsDataSource.ERROR_DELETE);
        }

        Logger.log(Logger.InfoLabel.SQL_SUCCESS, PointsDataSource.SUCCESS_DELETE + " => " +
                Logger.formatPoint(point));
    }

    /**
     * Delete all Points.
     */
    @Override
    public void deleteAll() {
        SQLiteDatabase db = App.dbHelper.getWritableDatabase();
        db.delete(PointsTable.TABLE_NAME_POINTS, null, null);
    }

    /**
     * Truncate table.
     */
    public void truncate() {
        this.deleteAll();

        SQLiteDatabase db = App.dbHelper.getWritableDatabase();
        db.execSQL(
                String.format(
                        "DELETE FROM sqlite_sequence WHERE name = '%s'",
                        PointsTable.TABLE_NAME_POINTS));
    }
}