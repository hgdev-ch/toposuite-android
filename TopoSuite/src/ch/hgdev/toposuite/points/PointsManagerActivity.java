package ch.hgdev.toposuite.points;

import java.util.ArrayList;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;

/**
 * Activity to manage points, such as adding, removing or modifying them.
 * 
 * @author HGdev
 * 
 */
public class PointsManagerActivity extends TopoSuiteActivity implements AddPointDialogFragment.AddPointDialogListener {

    private ListView             pointsListView;
    private ArrayListOfPointsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_points_manager);

        this.pointsListView = (ListView) this.findViewById(R.id.apm_list_of_points);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.drawList();
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
        this.addPoint(((AddPointDialogFragment) dialog).getNumber(), ((AddPointDialogFragment) dialog).getEast(),
                ((AddPointDialogFragment) dialog).getNorth(), ((AddPointDialogFragment) dialog).getAltitude());
        this.drawList();
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
        SharedResources.getSetOfPoints().add(point);
    }

    /**
     * Draw the main table containing all the points.
     */
    private void drawList() {
        ArrayList<Point> points = new ArrayList<Point>(SharedResources.getSetOfPoints());
        this.adapter = new ArrayListOfPointsAdapter(this, R.layout.points_list_item, points);
        this.pointsListView.setAdapter(this.adapter);
    }
}