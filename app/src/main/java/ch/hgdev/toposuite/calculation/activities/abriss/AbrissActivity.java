package ch.hgdev.toposuite.calculation.activities.abriss;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.Abriss;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class AbrissActivity extends TopoSuiteActivity implements
        AddOrientationDialogFragment.AddOrientationDialogListener,
        EditOrientationDialogFragment.EditOrientationDialogListener {

    public static final String    CALCULATION_POSITION_LABEL = "calculation_position";
    public static final String    STATION_NUMBER_LABEL       = "station_number";
    public static final String    ORIENTATIONS_LABEL         = "orientations";

    private static final String   STATION_SELECTED_POSITION  = "station_selected_position";
    private TextView              stationPointTextView;
    private Spinner               stationSpinner;
    private ListView              orientationsListView;
    private int                   stationSelectedPosition;
    private Abriss                abriss;
    private ArrayAdapter<Measure> adapter;

    /**
     * Position of the calculation in the calculations list. Only used when open
     * from the history.
     */
    private int                   position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_abriss);

        this.position = -1;

        this.stationSpinner = (Spinner) this.findViewById(R.id.station_spinner);
        this.orientationsListView = (ListView) this.findViewById(R.id.orientations_list);
        this.stationPointTextView = (TextView) this.findViewById(R.id.station_point);

        this.stationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                AbrissActivity.this.stationSelectedPosition = pos;

                Point pt = (Point) AbrissActivity.this.stationSpinner.getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    AbrissActivity.this.stationPointTextView.setText(DisplayUtils.formatPoint(
                            AbrissActivity.this, pt));
                } else {
                    AbrissActivity.this.stationPointTextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        ArrayList<Measure> list = new ArrayList<Measure>();

        // check if we create a new abriss calculation or if we modify an
        // existing one.
        Bundle bundle = this.getIntent().getExtras();
        if ((bundle != null)) {
            this.position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.abriss = (Abriss) SharedResources.getCalculationsHistory().get(this.position);
            list = this.abriss.getMeasures();
        }

        this.adapter = new ArrayListOfOrientationsAdapter(
                this, R.layout.orientations_list_item, list);
        this.drawList();

        this.registerForContextMenu(this.orientationsListView);
    }

    @Override
    public void onResume() {
        super.onResume();

        List<Point> points = new ArrayList<Point>();
        points.add(new Point("", 0.0, 0.0, 0.0, true));
        points.addAll(SharedResources.getSetOfPoints());

        ArrayAdapter<Point> a = new ArrayAdapter<Point>(
                this, R.layout.spinner_list_item, points);
        this.stationSpinner.setAdapter(a);

        if (this.abriss != null) {
            this.stationSpinner.setSelection(
                    a.getPosition(this.abriss.getStation()));
        } else {
            if (this.stationSelectedPosition > 0) {
                this.stationSpinner.setSelection(
                        this.stationSelectedPosition);
            }
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_abriss);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.abriss, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(AbrissActivity.STATION_SELECTED_POSITION,
                this.stationSelectedPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            this.stationSelectedPosition = savedInstanceState.getInt(
                    AbrissActivity.STATION_SELECTED_POSITION);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
        case R.id.add_orientation_button:
            this.showAddOrientationDialog();
            return true;
        case R.id.run_calculation_button:
            Point station = (Point) this.stationSpinner.getSelectedItem();

            if (station.getNumber().isEmpty()) {
                ViewUtils.showToast(this,
                        this.getString(R.string.error_no_station_selected));
                return true;
            }

            if (this.orientationsListView.getChildCount() == 0) {
                ViewUtils.showToast(this,
                        this.getString(R.string.error_at_least_one_orientation));
                return true;
            }

            Bundle bundle = new Bundle();
            bundle.putInt(AbrissActivity.CALCULATION_POSITION_LABEL,
                    this.position);

            bundle.putString(AbrissActivity.STATION_NUMBER_LABEL, station.getNumber());

            JSONArray json = new JSONArray();
            for (int i = 0; i < this.adapter.getCount(); i++) {
                json.put(this.adapter.getItem(i).toJSONObject());
            }

            bundle.putString(AbrissActivity.ORIENTATIONS_LABEL, json.toString());

            Intent resultsActivityIntent = new Intent(this, AbrissResultsActivity.class);
            resultsActivityIntent.putExtras(bundle);
            this.startActivity(resultsActivityIntent);

            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.orientations_list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
        case R.id.edit_orientation:
            this.showEditOrientationDialog(info.position);
            return true;
        case R.id.delete_calculation:
            this.adapter.remove(this.adapter.getItem(info.position));
            this.adapter.notifyDataSetChanged();
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    /**
     * Display a dialog to allow the user to insert a new orientation.
     */
    private void showAddOrientationDialog() {
        ViewUtils.lockScreenOrientation(this);

        AddOrientationDialogFragment dialog = new AddOrientationDialogFragment();
        dialog.show(this.getFragmentManager(), "AddOrientationDialogFragment");
    }

    /**
     * Display a dialog to allow the user to edit an orientation.
     */
    private void showEditOrientationDialog(int position) {
        ViewUtils.lockScreenOrientation(this);

        EditOrientationDialogFragment dialog = new EditOrientationDialogFragment();
        Bundle args = new Bundle();
        Measure measure = this.adapter.getItem(position);
        args.putString(EditOrientationDialogFragment.ORIENTATION_NUMBER, measure.getPoint()
                .getNumber());
        args.putDouble(EditOrientationDialogFragment.HORIZONTAL_DIRECTION, measure.getHorizDir());
        args.putDouble(EditOrientationDialogFragment.HORIZONTAL_DISTANCE, measure.getDistance());
        args.putDouble(EditOrientationDialogFragment.ZENITHAL_ANGLE, measure.getZenAngle());
        args.putInt(EditOrientationDialogFragment.ORIENTATION_POSITION, position);

        dialog.setArguments(args);
        dialog.show(this.getFragmentManager(), "EditOrientationDialogFragment");
    }

    /**
     * Draw the main table containing all the orientations.
     */
    private void drawList() {
        this.orientationsListView.setAdapter(this.adapter);
    }

    @Override
    public void onDialogAdd(AddOrientationDialogFragment dialog) {
        double zenithAngle = (MathUtils.isZero(dialog.getZenithalAngle()))
                ? 100.0 : dialog.getZenithalAngle();
        this.adapter.add(new Measure(
                dialog.getOrientation(),
                dialog.getHorizontalDirection(),
                zenithAngle,
                dialog.getHorizontalDistance()));
        this.adapter.notifyDataSetChanged();
        this.showAddOrientationDialog();
    }

    @Override
    public void onDialogCancel(AddOrientationDialogFragment dialog) {
        ViewUtils.unlockScreenOrientation(this);
    }

    @Override
    public void onDialogEdit(EditOrientationDialogFragment dialog) {
        double zenithAngle = (MathUtils.isZero(dialog.getZenithalAngle()))
                ? 100.0 : dialog.getZenithalAngle();
        Measure orientation = this.adapter.getItem(dialog.getOrientationPosition());
        orientation.setPoint(dialog.getOrientation());
        orientation.setHorizDir(dialog.getHorizontalDirection());
        orientation.setDistance(dialog.getHorizontalDistance());
        orientation.setZenAngle(zenithAngle);
        this.adapter.notifyDataSetChanged();

        ViewUtils.unlockScreenOrientation(this);
    }

    @Override
    public void onDialogCancel(EditOrientationDialogFragment dialog) {
        ViewUtils.unlockScreenOrientation(this);
    }
}
