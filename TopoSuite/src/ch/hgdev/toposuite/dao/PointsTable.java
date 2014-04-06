package ch.hgdev.toposuite.dao;

import android.database.sqlite.SQLiteDatabase;

/**
 * Handle migration for the points table.
 * 
 * @author HGdev
 */
public class PointsTable {
    public static final String TABLE_NAME_POINTS      = "points";
    public static final String COLUMN_NAME_NUMBER     = "number";
    public static final String COLUMN_NAME_EAST       = "east";
    public static final String COLUMN_NAME_NORTH      = "north";
    public static final String COLUMN_NAME_ALTITUDE   = "altitude";
    public static final String COLUMN_NAME_BASE_POINT = "base_point";

    /**
     * See {@link SQLiteDatabase#onCreate(SQLiteDatabase}
     */
    public static void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + PointsTable.TABLE_NAME_POINTS + "(" +
                PointsTable.COLUMN_NAME_NUMBER + " TEXT PRIMARY KEY," +
                PointsTable.COLUMN_NAME_EAST + " REAL," +
                PointsTable.COLUMN_NAME_NORTH + " REAL," +
                PointsTable.COLUMN_NAME_ALTITUDE + " REAL," +
                PointsTable.COLUMN_NAME_BASE_POINT + " INTEGER)");
    }

    /**
     * See {@link SQLiteDatabase#onUpgrade(SQLiteDatabase}
     */
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PointsTable.TABLE_NAME_POINTS);
        PointsTable.onCreate(db);
    }
}
