package ch.hgdev.toposuite.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import ch.hgdev.toposuite.App;

/**
 * Class for managing the persistent storage of Point objects in DB.
 * 
 * @author HGdev
 */
public class DBHelper extends SQLiteOpenHelper {

    /**
     * Constructs a new DBPointEntity.
     * 
     * @param context
     *            the context
     */
    public DBHelper(Context context) {
        super(context, App.DATABASE, null, App.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        PointsTable.onCreate(db);
        CalculationsTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        PointsTable.onUpgrade(db, oldVersion, newVersion);
        CalculationsTable.onUpgrade(db, oldVersion, newVersion);
    }
}