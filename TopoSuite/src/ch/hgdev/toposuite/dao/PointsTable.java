package ch.hgdev.toposuite.dao;

import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @author HGdev
 */
public class PointsTable {
    public static final String TABLE_NAME_POINTS      = "points";
    public static final String COLUMN_NAME_ID         = "id";
    public static final String COLUMN_NAME_NUMBER     = "number";
    public static final String COLUMN_NAME_EAST       = "east";
    public static final String COLUMN_NAME_NORTH      = "north";
    public static final String COLUMN_NAME_ALTITUDE   = "altitude";
    public static final String COLUMN_NAME_BASE_POINT = "base_point";

    /**
     * See {@link SQLiteDatabase#onCreate(SQLiteDatabase}
     */
    public static void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_POINTS + "(" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME_NUMBER + " INTEGER," +
                    COLUMN_NAME_EAST + " REAL," +
                    COLUMN_NAME_NORTH + " REAL," +
                    COLUMN_NAME_ALTITUDE + " REAL," +
                    COLUMN_NAME_BASE_POINT + " INTEGER)");
    }

    /**
     * See {@link SQLiteDatabase#onUpgrade(SQLiteDatabase}
     */
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_POINTS);
        onCreate(db);
    }
}
