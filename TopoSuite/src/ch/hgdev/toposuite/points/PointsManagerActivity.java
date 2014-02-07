package ch.hgdev.toposuite.points;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
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
public class PointsManagerActivity extends TopoSuiteActivity implements
        AddPointDialogFragment.AddPointDialogListener,
        EditPointDialogFragment.EditPointDialogListener {

    private int                      selectedPointId;
    private ListView                 pointsListView;
    private ArrayListOfPointsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_points_manager);

        this.pointsListView = (ListView) this.findViewById(R.id.apm_list_of_points);
        this.registerForContextMenu(this.pointsListView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.selectedPointId = 0;
        this.drawList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
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
    public void onDialogAdd(AddPointDialogFragment dialog) {
        this.addPoint(dialog.getNumber(), dialog.getEast(),
                dialog.getNorth(), dialog.getAltitude());
        this.drawList();
    }

    @Override
    public void onDialogCancel(AddPointDialogFragment dialog) {
        // do nothing
    }

    @Override
    public void onDialogEdit(EditPointDialogFragment dialog) {
        Point point = SharedResources.getSetOfPoints().get(this.selectedPointId);
        point.setEast(dialog.getEast());
        point.setNorth(dialog.getNorth());
        point.setAltitude(dialog.getAltitude());
        this.drawList();
    }

    @Override
    public void onDialogCancel(EditPointDialogFragment dialog) {
        // do nothing
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.points_list_row_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Point point;
        switch (item.getItemId()) {
        case R.id.delete_point:
            point = SharedResources.getSetOfPoints().get((int) info.id);
            this.adapter.remove(point);
            this.adapter.notifyDataSetChanged();
            SharedResources.getSetOfPoints().remove(point);
            return true;
        case R.id.edit_point:
            this.showEditPointDialog((int) info.id);
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    /**
     * Display a dialog to allow the user to insert a new point.
     */
    private void showAddPointDialog() {
        AddPointDialogFragment dialog = new AddPointDialogFragment();
        dialog.show(this.getFragmentManager(), "AddPointDialogFragment");
    }

    /**
     * Display a dialog to allow the user to edit a point.
     * 
     * @param id
     *            Id of the point to be edited.
     */
    private void showEditPointDialog(int id) {
        EditPointDialogFragment dialog = new EditPointDialogFragment();
        Bundle args = new Bundle();
        this.selectedPointId = id;
        args.putInt(EditPointDialogFragment.POINT_POSITION, id);
        dialog.setArguments(args);

        dialog.show(this.getFragmentManager(), "EditPointDialogFragment");
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