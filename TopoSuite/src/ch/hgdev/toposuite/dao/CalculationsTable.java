package ch.hgdev.toposuite.dao;

import android.database.sqlite.SQLiteDatabase;

public class CalculationsTable {
    public static final String TABLE_NAME_CALCULATIONS           = "calculations";
    public static final String COLUMN_NAME_ID                    = "id";
    public static final String COLUMN_NAME_TYPE                  = "type";
    public static final String COLUMN_NAME_DESCRIPTION           = "description";
    public static final String COLUMN_NAME_LAST_MODIFICATION     = "last_modification";
    public static final String COLUMN_NAME_SERIALIZED_INPUT_DATA = "serialized_input_data";

    /**
     * See {@link SQLiteDatabase#onCreate(SQLiteDatabase}
     */
    public static void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_CALCULATIONS + "(" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME_TYPE + " TEXT," +
                    COLUMN_NAME_DESCRIPTION + " TEXT," +
                    COLUMN_NAME_LAST_MODIFICATION + " TEXT," +
                    COLUMN_NAME_SERIALIZED_INPUT_DATA + " TEXT)");
    }

    /**
     * See {@link SQLiteDatabase#onUpgrade(SQLiteDatabase}
     */
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CALCULATIONS);
        onCreate(db);
    }
}
