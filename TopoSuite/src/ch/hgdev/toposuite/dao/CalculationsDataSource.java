package ch.hgdev.toposuite.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.calculation.Calculation;
import ch.hgdev.toposuite.calculation.CalculationFactory;
import ch.hgdev.toposuite.calculation.CalculationType;
import ch.hgdev.toposuite.dao.interfaces.DAO;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.Logger;

/**
 * DAO for {@link Calculation}.
 * 
 * @author HGdev
 */
public class CalculationsDataSource implements DAO {
    private static final String           ERROR_CREATE       = "Unable to create a new calculation!";
    private static final String           ERROR_DELETE       = "Unable to delete a calculation!";
    private static final String           ERROR_PARSING_DATE = "Error while parsing the last modification date!";

    private static final String           SUCCESS_CREATE     = "Calculation successfully created!";
    private static final String           SUCCESS_DELETE     = "Calculation successfully deleted!";

    private static CalculationsDataSource calculationsDataSource;

    public static CalculationsDataSource getInstance() {
        if (CalculationsDataSource.calculationsDataSource == null) {
            CalculationsDataSource.calculationsDataSource = new CalculationsDataSource();
        }
        return CalculationsDataSource.calculationsDataSource;
    }

    /**
     * Find all calculations.
     * 
     * @return the list of all calculations
     */
    public ArrayList<Calculation> findAll() {
        SQLiteDatabase db = App.dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + CalculationsTable.TABLE_NAME_CALCULATIONS + " ORDER BY id DESC",
                null);
        ArrayList<Calculation> calculations = new ArrayList<Calculation>();

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                long id = cursor.getLong(
                        cursor.getColumnIndex(CalculationsTable.COLUMN_NAME_ID));
                String type = cursor.getString(
                        cursor.getColumnIndex(CalculationsTable.COLUMN_NAME_TYPE));
                String description = cursor.getString(
                        cursor.getColumnIndex(CalculationsTable.COLUMN_NAME_DESCRIPTION));
                String lastModification = cursor.getString(
                        cursor.getColumnIndex(CalculationsTable.COLUMN_NAME_LAST_MODIFICATION));
                String serializedInputDate = cursor.getString(
                        cursor.getColumnIndex(CalculationsTable.COLUMN_NAME_SERIALIZED_INPUT_DATA));

                SimpleDateFormat sdf = new SimpleDateFormat(App.dateFormat);
                Date d;
                try {
                    d = sdf.parse(lastModification);
                    Calculation calculation = CalculationFactory.createCalculation(
                            CalculationType.valueOf(type), id, description, d, serializedInputDate);
                    calculations.add(calculation);
                } catch (ParseException e) {
                    Log.e(Logger.TOPOSUITE_PARSE_ERROR, CalculationsDataSource.ERROR_PARSING_DATE);
                }

                cursor.moveToNext();
            }
        }

        return calculations;
    }

    /**
     * Create a new Calculation in the database. TODO check for SQL Injection.
     * 
     * @param obj
     *            a calculation
     * @throws SQLiteTopoSuiteException
     */
    @Override
    public void create(Object obj) throws SQLiteTopoSuiteException {
        Calculation calculation = (Calculation) obj;
        SQLiteDatabase db = App.dbHelper.getReadableDatabase();

        ContentValues calculationValues = new ContentValues();
        calculationValues.put(CalculationsTable.COLUMN_NAME_TYPE,
                calculation.getType().toString());
        calculationValues.put(CalculationsTable.COLUMN_NAME_DESCRIPTION,
                calculation.getDescription());
        calculationValues.put(CalculationsTable.COLUMN_NAME_LAST_MODIFICATION,
                DisplayUtils.formatDate(calculation.getLastModification()));

        long rowID = db.insert(CalculationsTable.TABLE_NAME_CALCULATIONS, null, calculationValues);
        if (rowID == -1) {
            Log.e(Logger.TOPOSUITE_SQL_ERROR, CalculationsDataSource.ERROR_CREATE + " => " +
                    Logger.formatCalculation(calculation));
            throw new SQLiteTopoSuiteException(CalculationsDataSource.ERROR_CREATE);
        }

        // we update the object ID now we have one
        calculation.setId(rowID);

        Log.i(Logger.TOPOSUITE_SQL_SUCCESS, CalculationsDataSource.SUCCESS_CREATE + " => " +
                Logger.formatCalculation(calculation));
    }

    @Override
    public void update(Object obj) {
        // TODO
    }

    /**
     * Delete a Calculation.
     * 
     * @param obj
     *            a calculation
     * @throws SQLiteTopoSuiteException
     */
    @Override
    public void delete(Object obj) throws SQLiteTopoSuiteException {
        Calculation calculation = (Calculation) obj;
        SQLiteDatabase db = App.dbHelper.getWritableDatabase();

        long rowID = db.delete(CalculationsTable.TABLE_NAME_CALCULATIONS,
                CalculationsTable.COLUMN_NAME_ID + " = " + calculation.getId(), null);
        if (rowID == -1) {
            Log.e(Logger.TOPOSUITE_SQL_ERROR, CalculationsDataSource.ERROR_DELETE + " => " +
                    Logger.formatCalculation(calculation));
            throw new SQLiteTopoSuiteException(CalculationsDataSource.ERROR_DELETE);
        }

        Log.i(Logger.TOPOSUITE_SQL_SUCCESS, CalculationsDataSource.SUCCESS_DELETE + " => " +
                Logger.formatCalculation(calculation));
    }
}
