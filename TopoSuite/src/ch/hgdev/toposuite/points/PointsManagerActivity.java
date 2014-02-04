package ch.hgdev.toposuite.points;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.utils.DisplayUtils;

/**
 * Activity to manage points, such as adding, removing or modifying them.
 * 
 * @author HGdev
 * 
 */
public class PointsManagerActivity extends TopoSuiteActivity
        implements AddPointDialogFragment.AddPointDialogListener {

    private TableLayout mainTable;
    private Set<Point>  setOfPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_points_manager);

        this.mainTable = (TableLayout) this.findViewById(R.id.apm_header_table_main);
        this.setOfPoints = new TreeSet<Point>(new Comparator<Point>() {
            @Override
            public int compare(Point lhs, Point rhs) {
                return lhs.getNumber() < rhs.getNumber() ? 1 : -1;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
        case R.id.add_point_button:
            this.showAddPointDialog();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.points_manager, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDialogAdd(DialogFragment dialog) {
        this.addPoint(((AddPointDialogFragment) dialog).getNumber(),
                ((AddPointDialogFragment) dialog).getEast(),
                ((AddPointDialogFragment) dialog).getNorth(),
                ((AddPointDialogFragment) dialog).getAltitude());
    }

    @Override
    public void onDialogCancel(DialogFragment dialog) {
        // do nothing
    }

    /**
     * Display a dialog to allow the user to insert a new point.
     */
    private void showAddPointDialog() {
        DialogFragment dialog = new AddPointDialogFragment();
        dialog.show(this.getFragmentManager(), "AddPointDialogFragment");
    }

    /**
     * Create a point based on the input and add it to the table of points and
     * the set of points.
     * 
     * @param number
     *            Point's number attribute.
     * @param east
     *            Point's east attribute.
     * @param north
     *            Point's north attribute.
     * @param altitude
     *            Point's altitude attribute.
     */
    private void addPoint(int number, double east, double north, double altitude) {
        // when created by a user and not computed, a point IS a basepoint
        boolean basePoint = true;
        Point point = new Point(number, east, north, altitude, basePoint);
        this.setOfPoints.add(point);
        this.addPointToMainTable(point);
    }

    /**
     * Add a point to the main table of points.
     * 
     * @param point
     *            Point to be added.
     */
    private void addPointToMainTable(Point point) {
        TableRow row = new TableRow(this);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams();

        // wrap the content of a row
        rowParams.height = LayoutParams.WRAP_CONTENT;
        rowParams.width = LayoutParams.WRAP_CONTENT;

        // wrap the content of a cell
        TableRow.LayoutParams cellParams = new TableRow.LayoutParams();
        cellParams.height = LayoutParams.WRAP_CONTENT;
        cellParams.width = LayoutParams.WRAP_CONTENT;
        cellParams.gravity = Gravity.CENTER;

        TextView cell = new TextView(this);

        // point number cell
        cell.setText(point.getNumber());
        row.addView(cell, cellParams);

        // east cell
        cell.setText(DisplayUtils.toString(point.getEast()));
        row.addView(cell, cellParams);

        // north cell
        cell.setText(DisplayUtils.toString(point.getNorth()));
        row.addView(cell, cellParams);

        // altitude cell
        cell.setText(DisplayUtils.toString(point.getAltitude()));
        row.addView(cell, cellParams);

        // base point cell
        cell.setText(DisplayUtils.toString(this, point.getBasePoint()));
        row.addView(cell, cellParams);

        this.mainTable.addView(row, rowParams);
    }
}