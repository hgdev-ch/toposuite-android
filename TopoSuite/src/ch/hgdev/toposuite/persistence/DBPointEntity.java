package ch.hgdev.toposuite.persistence;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.Logger;

/**
 * Class for managing the persistent storage of Point objects in DB.
 * 
 * @author HGdev
 */
public class DBPointEntity extends SQLiteOpenHelper {

    private static final String TABLE_NAME_POINTS = "points";
    private static final String COLUMN_NAME_ID     = "id";
    private static final String COLUMN_NAME_NUMBER  = "number";
    private static final String COLUMN_NAME_EAST  = "east";
    private static final String COLUMN_NAME_NORTH  = "north";
    private static final String COLUMN_NAME_ALTITUDE  = "altitude";
    private static final String COLUMN_NAME_BASE_POINT  = "base_point";
    
    private static final String ERROR_CREATE = "Unable to create a new point!";
    private static final String ERROR_DELETE = "Unable to delete a point!";
    
    private static final String SUCCESS_CREATE = "Point successfully created!";
    private static final String SUCCESS_DELETE = "Point successfully deleted!";
    
    /**
     * Constructs a new DBPointEntity.
     * @param context
     *            the context
     */
    public DBPointEntity(Context context) {
        super(context, App.DATABASE, null, App.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_POINTS + "(" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME_NUMBER + " INTEGER," +
                    COLUMN_NAME_EAST + " REAL," +
                    COLUMN_NAME_NORTH + " REAL," +
                    COLUMN_NAME_ALTITUDE + " REAL," +
                    COLUMN_NAME_BASE_POINT + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_POINTS);
        this.onCreate(db);
    }
    
    public ArrayList<Point> findAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor  cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_POINTS +
                " ORDER BY number ASC", null);
        
        ArrayList<Point> points = new ArrayList<Point>();
        
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                int number = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_NUMBER));
                double east = cursor.getDouble(cursor.getColumnIndex(COLUMN_NAME_EAST));
                double north = cursor.getDouble(cursor.getColumnIndex(COLUMN_NAME_NORTH));
                double altitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_NAME_ALTITUDE));
                boolean isBasePoint = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_BASE_POINT)) == 1;
             
                points.add(new Point(number, east, north, altitude, isBasePoint));
                cursor.moveToNext();
            }
        }
        
        return points;
    }
    
    /**
     * Create a new Point in the database.
     * @param point
     *            a point
     * @throws SQLiteTopoSuiteException
     */
    public void create(Point point) throws SQLiteTopoSuiteException {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues pointValues = new ContentValues();
        pointValues.put(COLUMN_NAME_NUMBER, point.getNumber());
        pointValues.put(COLUMN_NAME_EAST, point.getEast());
        pointValues.put(COLUMN_NAME_NORTH, point.getNorth());
        pointValues.put(COLUMN_NAME_ALTITUDE, point.getAltitude());
        pointValues.put(COLUMN_NAME_BASE_POINT, point.isBasePoint() ? 1 : 0);
        
        long rowID = db.insert(TABLE_NAME_POINTS, null, pointValues);
        if  (rowID == -1) {
            Log.e(Logger.TOPOSUITE_SQL_ERROR, ERROR_CREATE + " => " +
                    Logger.formatPoint(point));
            throw new SQLiteTopoSuiteException(ERROR_CREATE);
        }
        
        Log.i(Logger.TOPOSUITE_SQL_SUCCESS, SUCCESS_CREATE + " => " +
                Logger.formatPoint(point));
    }
    
    /**
     * Delete a Point.
     * @param point
     *            a point
     * @throws SQLiteTopoSuiteException
     */
    public void delete(Point point) throws SQLiteTopoSuiteException {
        SQLiteDatabase db = this.getWritableDatabase();
        long rowID = db.delete(
                TABLE_NAME_POINTS, COLUMN_NAME_NUMBER + "=" + point.getNumber(), null);
        if (rowID == -1) {
            Log.e(Logger.TOPOSUITE_SQL_ERROR, ERROR_DELETE);
            throw new SQLiteTopoSuiteException(ERROR_DELETE);
        }
        
        Log.i(Logger.TOPOSUITE_SQL_SUCCESS, SUCCESS_DELETE + " => " +
                Logger.formatPoint(point));
    }
}