package ch.hgdev.toposuite.calculation.activities.polarimplantation;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.Abriss;
import ch.hgdev.toposuite.calculation.Calculation;
import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.CalculationType;
import ch.hgdev.toposuite.calculation.FreeStation;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.calculation.PolarImplantation;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class PolarImplantationActivity extends TopoSuiteActivity implements
        AddPointWithSDialogFragment.AddPointWithSDialogListener,
        EditPointWithSDialogFragment.EditPointWithSDialogListener {

    public static final String POLAR_IMPLANT_CALCULATION = "polar_implantation_calculation";
    public static final String POINT_WITH_S_NUMBER_LABEL = "points_with_s_number";
    public static final String POINT_WITH_S_POSITION = "point_with_s_position";
    public static final String S = "s";

    private static final String STATION_SELECTED_POSITION = "station_selected_position";
    private static final String POINTS_WITH_S_LABEL = "points_with_s";
    private Spinner stationSpinner;
    private int stationSelectedPosition;
    private ArrayAdapter<Point> stationAdapter;
    private TextView stationPointTextView;
    private EditText iEditText;
    private EditText unknownOrientEditText;
    private ListView pointsListView;
    private FloatingActionButton addButton;
    private ArrayListOfPointsWithSAdapter adapter;

    private Point station;
    private PolarImplantation polarImplantation;

    private double z0;
    private Point z0Station;
    private double instrumentHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_polar_implantation);

        this.z0 = MathUtils.IGNORE_DOUBLE;
        this.instrumentHeight = MathUtils.IGNORE_DOUBLE;

        this.stationSpinner = (Spinner) this.findViewById(R.id.station_spinner);
        this.stationPointTextView = (TextView) this.findViewById(R.id.station_point);
        this.unknownOrientEditText = (EditText) this.findViewById(R.id.unknown_orientation);
        this.iEditText = (EditText) this.findViewById(R.id.i);
        this.pointsListView = (ListView) this.findViewById(R.id.list_of_points);
        this.addButton = (FloatingActionButton) this.findViewById(R.id.add_point_button);

        this.iEditText.setInputType(App.getInputTypeCoordinate());
        this.unknownOrientEditText.setInputType(App.getInputTypeCoordinate());

        this.stationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                PolarImplantationActivity.this.stationSelectedPosition = pos;

                PolarImplantationActivity.this.station = (Point) PolarImplantationActivity.this.stationSpinner.getItemAtPosition(pos);
                if (!PolarImplantationActivity.this.station.getNumber().isEmpty()) {
                    PolarImplantationActivity.this.stationPointTextView.setText(
                            DisplayUtils.formatPoint(PolarImplantationActivity.this, PolarImplantationActivity.this.station));
                } else {
                    PolarImplantationActivity.this.stationPointTextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.pointsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PolarImplantationActivity.this.showEditPointWithSDialog(position);
            }
        });

        this.addButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  PolarImplantationActivity.this.showAddPointDialog();
                                              }
                                          }
        );

        ArrayList<Measure> list = new ArrayList<>();

        // check if we create a new polar implantation calculation or if we modify an existing one.
        Bundle bundle = this.getIntent().getExtras();
        if ((bundle != null)) {
            int position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.polarImplantation = (PolarImplantation) SharedResources.getCalculationsHistory().get(position);
        } else {
            this.polarImplantation = new PolarImplantation(null, true);
        }

        this.adapter = new ArrayListOfPointsWithSAdapter(
                this, R.layout.points_with_s_list_item,
                new ArrayList<>(this.polarImplantation.getMeasures()));
        this.registerForContextMenu(this.pointsListView);
    }

    @Override
    public void onResume() {
        super.onResume();

        List<Point> points = new ArrayList<>();
        points.add(new Point(false));
        points.addAll(SharedResources.getSetOfPoints());

        this.stationAdapter = new ArrayAdapter<>(this, R.layout.spinner_list_item, points);
        this.stationSpinner.setAdapter(this.stationAdapter);

        for (Calculation c : SharedResources.getCalculationsHistory()) {
            if ((c != null) && (c.getType() == CalculationType.ABRISS)) {
                Abriss a = (Abriss) c;
                try {
                    a.compute();
                    this.z0 = a.getMean();
                    this.z0Station = a.getStation();
                } catch (CalculationException e) {
                    Logger.log(Logger.ErrLabel.CALCULATION_COMPUTATION_ERROR, e.getMessage());
                    ViewUtils.showToast(this, this.getString(R.string.error_computation_exception));
                }
                break;
            }
            if ((c != null) && (c.getType() == CalculationType.FREESTATION)) {
                FreeStation fs = (FreeStation) c;
                try {
                    fs.compute();
                    this.z0 = fs.getUnknownOrientation();
                    this.z0Station = fs.getStationResult();
                    this.instrumentHeight = fs.getI();
                } catch (CalculationException e) {
                    Logger.log(Logger.ErrLabel.CALCULATION_COMPUTATION_ERROR, e.getMessage());
                    ViewUtils.showToast(this, this.getString(R.string.error_computation_exception));
                }
                break;
            }
        }

        Point station = this.polarImplantation.getStation();
        if (station != null) {
            this.stationSpinner.setSelection(this.stationAdapter.getPosition(station));
        } else {
            if (this.stationSelectedPosition > 0) {
                this.stationSpinner.setSelection(this.stationSelectedPosition);
            }
        }

        if (!this.polarImplantation.getMeasures().isEmpty()) {
            Measure m = this.polarImplantation.getMeasures().get(0);
            this.iEditText.setText(DisplayUtils.toStringForEditText(m.getI()));
            this.unknownOrientEditText.setText(DisplayUtils.toStringForEditText(m.getUnknownOrientation()));
        }

        this.drawList();
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_polar_implantation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.action_run_calculation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(
                PolarImplantationActivity.STATION_SELECTED_POSITION,
                this.stationSelectedPosition);
        outState.putSerializable(
                PolarImplantationActivity.POINTS_WITH_S_LABEL,
                this.adapter.getMeasures());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            this.stationSelectedPosition = savedInstanceState.getInt(
                    PolarImplantationActivity.STATION_SELECTED_POSITION);

            ArrayList<Measure> measures = (ArrayList<Measure>) savedInstanceState.getSerializable(
                    PolarImplantationActivity.POINTS_WITH_S_LABEL);
            this.adapter.clear();
            this.adapter.addAll(measures);
            this.drawList();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.run_calculation_button:
                if (this.checkInputs()) {
                    this.showPolarImplantationResultActivity();
                } else {
                    ViewUtils.showToast(this, this.getString(R.string.error_fill_data));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.context_list_row_delete, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete_button:
                this.adapter.remove(this.adapter.getItem(info.position));
                this.adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.checkbox_z0:
                if (checked) {
                    if (MathUtils.isIgnorable(this.z0)) {
                        ViewUtils.showToast(this,
                                this.getString(R.string.error_no_suitable_calculation_found));
                    } else {
                        this.unknownOrientEditText.setText(DisplayUtils.toStringForEditText(this.z0));
                        this.unknownOrientEditText.setEnabled(false);
                        this.stationSpinner.setSelection(
                                this.stationAdapter.getPosition(this.z0Station));
                        this.stationSpinner.setEnabled(false);
                        if (!MathUtils.isIgnorable(this.instrumentHeight)) {
                            this.iEditText.setText(
                                    DisplayUtils.toStringForEditText(this.instrumentHeight));
                            this.iEditText.setEnabled(false);
                        }
                    }
                } else {
                    this.unknownOrientEditText.setText("");
                    this.unknownOrientEditText.setEnabled(true);
                    this.stationSpinner.setSelection(0);
                    this.stationSpinner.setEnabled(true);
                    this.iEditText.setText("");
                    this.iEditText.setEnabled(true);
                }
                break;
        }
    }

    /**
     * Show a dialog to add a new point, with optional S.
     */
    private void showAddPointDialog() {
        ViewUtils.lockScreenOrientation(this);

        AddPointWithSDialogFragment dialog = new AddPointWithSDialogFragment();
        dialog.show(this.getSupportFragmentManager(), "AddPointWithSDialogFragment");
    }

    /**
     * @param position Position of the point with S to edit.
     */
    private void showEditPointWithSDialog(int position) {
        ViewUtils.lockScreenOrientation(this);

        EditPointWithSDialogFragment dialog = new EditPointWithSDialogFragment();
        Measure m = this.adapter.getItem(position);
        Bundle args = new Bundle();
        args.putInt(PolarImplantationActivity.POINT_WITH_S_POSITION, position);
        args.putString(PolarImplantationActivity.POINT_WITH_S_NUMBER_LABEL, m.getPoint().getNumber());
        args.putDouble(PolarImplantationActivity.S, m.getS());

        dialog.setArguments(args);
        dialog.show(this.getSupportFragmentManager(), "EditPointWithSDialogFragment");
    }

    /**
     * Draw the list of points.
     */
    private void drawList() {
        this.pointsListView.setAdapter(this.adapter);
    }

    /**
     * Check that the I field, the unknown orientation field have been filled
     * and that the station has been chosen.
     *
     * @return True if inputs are OK, false otherwise.
     */
    private boolean checkInputs() {
        if ((this.station == null) || (this.station.getNumber().isEmpty())) {
            return false;
        }
        if (this.unknownOrientEditText.length() == 0) {
            return false;
        }
        if (this.adapter.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Start the activity that shows the results of the calculation.
     */
    private void showPolarImplantationResultActivity() {
        this.instrumentHeight = ViewUtils.readDouble(this.iEditText);
        if (!ViewUtils.isEmpty(this.unknownOrientEditText)) {
            this.z0 = ViewUtils.readDouble(this.unknownOrientEditText);
        } else {
            ViewUtils.showToast(this, this.getString(R.string.error_choose_unknown_orientation));
            return;
        }

        this.polarImplantation.setStation(this.station);
        this.polarImplantation.getMeasures().clear();
        for (Measure m : this.adapter.getMeasures()) {
            m.setI(this.instrumentHeight);
            m.setUnknownOrientation(this.z0);
            this.polarImplantation.getMeasures().add(m);
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(
                PolarImplantationActivity.POLAR_IMPLANT_CALCULATION,
                this.polarImplantation);

        Intent resultsActivityIntent = new Intent(this, PolarImplantationResultsActivity.class);
        resultsActivityIntent.putExtras(bundle);
        this.startActivity(resultsActivityIntent);
    }

    @Override
    public void onDialogAdd(AddPointWithSDialogFragment dialog) {
        Measure m = new Measure(
                dialog.getPoint(),
                MathUtils.IGNORE_DOUBLE,
                MathUtils.IGNORE_DOUBLE,
                MathUtils.IGNORE_DOUBLE,
                dialog.getS());

        this.adapter.add(m);
        this.adapter.notifyDataSetChanged();
        this.showAddPointDialog();
    }

    @Override
    public void onDialogCancel(AddPointWithSDialogFragment dialog) {
        ViewUtils.unlockScreenOrientation(this);
    }

    @Override
    public void onDialogEdit(EditPointWithSDialogFragment dialog) {
        Measure m = this.adapter.getItem(dialog.getPosition());
        m.setPoint(dialog.getPoint());
        m.setS(dialog.getS());
        this.adapter.notifyDataSetChanged();

        ViewUtils.unlockScreenOrientation(this);
    }

    @Override
    public void onDialogCancel(EditPointWithSDialogFragment dialog) {
        ViewUtils.unlockScreenOrientation(this);
    }
}
