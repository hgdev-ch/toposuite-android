package ch.hgdev.toposuite.dao;

import android.database.sqlite.SQLiteDatabase;

/**
 * Handle migration for the calculations table.
 * 
 * @author HGdev
 */
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
        db.execSQL("CREATE TABLE " + CalculationsTable.TABLE_NAME_CALCULATIONS + "(" +
                CalculationsTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CalculationsTable.COLUMN_NAME_TYPE + " TEXT," +
                CalculationsTable.COLUMN_NAME_DESCRIPTION + " TEXT," +
                CalculationsTable.COLUMN_NAME_LAST_MODIFICATION + " TEXT," +
                CalculationsTable.COLUMN_NAME_SERIALIZED_INPUT_DATA + " TEXT)");
    }

    /**
     * See {@link SQLiteDatabase#onUpgrade(SQLiteDatabase}
     */
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CalculationsTable.TABLE_NAME_CALCULATIONS);
        CalculationsTable.onCreate(db);
    }
}
