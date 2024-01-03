package ch.hgdev.toposuite.calculation.activities.polarsurvey;

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
import ch.hgdev.toposuite.calculation.PolarSurvey;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

/**
 * Activity related to the polar survey calculation.
 *
 * @author HGdev
 */
public class PolarSurveyActivity extends TopoSuiteActivity implements
        AddDeterminationDialogFragment.AddDeterminationDialogListener,
        EditDeterminationDialogFragment.EditDeterminationDialogListener {

    public static final String POLAR_SURVEY_CALCULATION = "polar_survey_calculation";

    public static final String DETERMINATION_NUMBER = "determination_number";
    public static final String DETERMINATION_POSITION = "determination_position";
    public static final String HORIZ_DIR = "horizontal_direction";
    public static final String DISTANCE = "distance";
    public static final String ZEN_ANGLE = "zenithal_angle";
    public static final String S = "s";
    public static final String LAT_DEPL = "lateral_displacement";
    public static final String LON_DEPL = "longitudinal displacement";

    private static final String STATION_SELECTED_POSITION = "station_selected_position";
    private static final String DETERMINATIONS_LABEL = "determinations";

    private Spinner stationSpinner;
    private int stationSelectedPosition;
    private ArrayAdapter<Point> stationAdapter;
    private TextView stationPointTextView;
    private EditText iEditText;
    private EditText unknownOrientEditText;
    private ListView determinationsListView;
    private FloatingActionButton addButton;
    private ArrayListOfDeterminationsAdapter adapter;
    private CheckBox z0CheckBox;

    private Point station;
    private double z0;
    private double instrumentHeight;

    private PolarSurvey polarSurvey;

    private Point z0Station;
    private long z0Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_polar_survey);

        this.z0 = MathUtils.IGNORE_DOUBLE;
        this.instrumentHeight = MathUtils.IGNORE_DOUBLE;
        this.z0Id = -1;

        this.stationSpinner = (Spinner) this.findViewById(R.id.station_spinner);
        this.stationPointTextView = (TextView) this.findViewById(R.id.station_point);
        this.unknownOrientEditText = (EditText) this.findViewById(R.id.unknown_orientation);
        this.iEditText = (EditText) this.findViewById(R.id.i);
        this.determinationsListView = (ListView) this.findViewById(R.id.determinations_list);
        this.addButton = (FloatingActionButton) this.findViewById(R.id.add_orientation_button);
        this.z0CheckBox = (CheckBox) this.findViewById(R.id.checkbox_z0);

        this.iEditText.setInputType(App.getInputTypeCoordinate());
        this.unknownOrientEditText.setInputType(App.getInputTypeCoordinate());

        this.stationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                PolarSurveyActivity.this.stationSelectedPosition = pos;

                PolarSurveyActivity.this.station = (Point) PolarSurveyActivity.this.stationSpinner
                        .getItemAtPosition(pos);
                if (!PolarSurveyActivity.this.station.getNumber().isEmpty()) {
                    PolarSurveyActivity.this.stationPointTextView.setText(DisplayUtils
                            .formatPoint(PolarSurveyActivity.this, PolarSurveyActivity.this.station));
                } else {
                    PolarSurveyActivity.this.stationPointTextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.determinationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PolarSurveyActivity.this.showEditDeterminationDialog(position);
            }
        });

        this.addButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  PolarSurveyActivity.this.showAddDeterminationDialog();
                                              }
                                          }
        );


        // check if we create a new polar survey calculation or if we modify an
        // existing one.
        Bundle bundle = this.getIntent().getExtras();
        if ((bundle != null)) {
            int position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.polarSurvey = (PolarSurvey) SharedResources.getCalculationsHistory().get(position);

            this.z0Id = this.polarSurvey.getZ0CalculationId();
            // the user has retrieved his z0 from last calculation previously
            if (this.z0Id > 0) {
                Calculation c = SharedResources.getCalculationsHistory().find(this.z0Id);
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
                } else if ((c != null) && (c.getType() == CalculationType.FREESTATION)) {
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
                } else {
                    Logger.log(
                            Logger.ErrLabel.CALCULATION_INVALID_TYPE,
                            "trying to get Z0 from a calculation that does not compute one");
                }
                this.z0CheckBox.setChecked(true);
                this.unknownOrientEditText.setEnabled(false);
            }
        } else {
            this.polarSurvey = new PolarSurvey(true);
        }

        if (MathUtils.isIgnorable(this.instrumentHeight)) {
            this.instrumentHeight = this.polarSurvey.getInstrumentHeight();
        }
        if (MathUtils.isIgnorable(this.z0)) {
            this.z0 = this.polarSurvey.getUnknownOrientation();
        }

        this.iEditText.setText(DisplayUtils.toStringForEditText(this.instrumentHeight));
        this.unknownOrientEditText.setText(DisplayUtils.toStringForEditText(this.z0));

        this.adapter = new ArrayListOfDeterminationsAdapter(
                this, R.layout.determinations_list_item,
                new ArrayList<>(this.polarSurvey.getDeterminations()));
        this.registerForContextMenu(this.determinationsListView);
    }

    @Override
    public void onResume() {
        super.onResume();

        List<Point> points = new ArrayList<>();
        points.add(new Point(false));
        points.addAll(SharedResources.getSetOfPoints());

        this.stationAdapter = new ArrayAdapter<>(this, R.layout.spinner_list_item, points);
        this.stationSpinner.setAdapter(this.stationAdapter);

        Point station = this.polarSurvey.getStation();
        if (station != null) {
            this.stationSpinner.setSelection(this.stationAdapter.getPosition(station));
        } else {
            if (this.stationSelectedPosition > 0) {
                this.stationSpinner.setSelection(this.stationSelectedPosition);
            }
        }

        this.drawList();
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_polar_survey);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.action_run_calculation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(PolarSurveyActivity.STATION_SELECTED_POSITION,
                this.stationSelectedPosition);

        outState.putSerializable(PolarSurveyActivity.DETERMINATIONS_LABEL,
                this.adapter.getMeasures());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            this.stationSelectedPosition = savedInstanceState.getInt(
                    PolarSurveyActivity.STATION_SELECTED_POSITION);


            ArrayList<Measure> measures = (ArrayList<Measure>) savedInstanceState.getSerializable(
                    PolarSurveyActivity.DETERMINATIONS_LABEL);
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
                    this.showPolarSurveyResultActivity();
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
                    this.fetchLastFreeStationOrAbriss();
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
     * Display a dialog to allow the user to insert a new determination.
     */
    private void showAddDeterminationDialog() {
        ViewUtils.lockScreenOrientation(this);

        AddDeterminationDialogFragment dialog = new AddDeterminationDialogFragment();
        dialog.show(this.getSupportFragmentManager(), "AddDeterminationDialogFragment");
    }

    /**
     * @param position Position of the determination measure to edit.
     */
    private void showEditDeterminationDialog(int position) {
        ViewUtils.lockScreenOrientation(this);

        EditDeterminationDialogFragment dialog = new EditDeterminationDialogFragment();

        Measure d = this.adapter.getItem(position);
        Bundle args = new Bundle();
        args.putInt(PolarSurveyActivity.DETERMINATION_POSITION, position);
        args.putString(PolarSurveyActivity.DETERMINATION_NUMBER, d.getMeasureNumber());
        args.putDouble(PolarSurveyActivity.HORIZ_DIR, d.getHorizDir());
        args.putDouble(PolarSurveyActivity.DISTANCE, d.getDistance());
        args.putDouble(PolarSurveyActivity.ZEN_ANGLE, d.getZenAngle());
        args.putDouble(PolarSurveyActivity.S, d.getS());
        args.putDouble(PolarSurveyActivity.LAT_DEPL, d.getLatDepl());
        args.putDouble(PolarSurveyActivity.LON_DEPL, d.getLonDepl());

        dialog.setArguments(args);
        dialog.show(this.getSupportFragmentManager(), "EditDeterminationDialogFragment");
    }

    /**
     * Draw the main table containing all the determinations.
     */
    private void drawList() {
        this.determinationsListView.setAdapter(this.adapter);
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
     * Perform actions required when the calculation button is clicked.
     */
    private void showPolarSurveyResultActivity() {
        this.instrumentHeight = ViewUtils.readDouble(this.iEditText);
        if (!ViewUtils.isEmpty(this.unknownOrientEditText)) {
            this.z0 = ViewUtils.readDouble(this.unknownOrientEditText);
        } else {
            ViewUtils.showToast(this, this.getText(R.string.error_choose_unknown_orientation));
            return;
        }

        this.polarSurvey.setStation(this.station);
        this.polarSurvey.setUnknownOrientation(this.z0);
        this.polarSurvey.setInstrumentHeight(this.instrumentHeight);
        this.polarSurvey.setZ0CalculationId(this.z0Id);
        this.polarSurvey.getDeterminations().clear();
        this.polarSurvey.getDeterminations().addAll(this.adapter.getMeasures());

        Bundle bundle = new Bundle();
        bundle.putSerializable(
                PolarSurveyActivity.POLAR_SURVEY_CALCULATION,
                this.polarSurvey);

        Intent resultsActivityIntent = new Intent(this, PolarSurveyResultsActivity.class);
        resultsActivityIntent.putExtras(bundle);
        this.startActivity(resultsActivityIntent);
    }

    @Override
    public void onDialogAdd(AddDeterminationDialogFragment dialog) {

        Measure m = new Measure(
                null,
                dialog.getHorizDir(),
                dialog.getZenAngle(),
                dialog.getDistance(),
                dialog.getS(),
                dialog.getLatDepl(),
                dialog.getLonDepl(),
                MathUtils.IGNORE_DOUBLE,
                MathUtils.IGNORE_DOUBLE,
                dialog.getDeterminationNo());

        this.adapter.add(m);
        this.adapter.notifyDataSetChanged();
        this.showAddDeterminationDialog();
    }

    @Override
    public void onDialogCancel(AddDeterminationDialogFragment dialog) {
        ViewUtils.unlockScreenOrientation(this);
    }

    @Override
    public void onDialogEdit(EditDeterminationDialogFragment dialog) {
        Measure m = this.adapter.getItem(dialog.getPosition());
        m.setHorizDir(dialog.getHorizDir());
        m.setZenAngle(dialog.getZenAngle());
        m.setDistance(dialog.getDistance());
        m.setS(dialog.getS());
        m.setLatDepl(dialog.getLatDepl());
        m.setLonDepl(dialog.getLonDepl());
        m.setMeasureNumber(dialog.getDeterminationNo());
        this.adapter.notifyDataSetChanged();

        ViewUtils.unlockScreenOrientation(this);
    }

    @Override
    public void onDialogCancel(EditDeterminationDialogFragment dialog) {
        ViewUtils.unlockScreenOrientation(this);
    }

    /**
     * FIXME put this method in a util class of in the super class!
     */
    private void fetchLastFreeStationOrAbriss() {
        for (Calculation c : SharedResources.getCalculationsHistory()) {
            if ((c != null) && (c.getType() == CalculationType.ABRISS)) {
                Abriss a = (Abriss) c;
                try {
                    a.compute();
                    this.z0 = a.getMean();
                    this.z0Station = a.getStation();
                    this.z0Id = c.getId();
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
                    this.z0Id = c.getId();
                    this.instrumentHeight = fs.getI();
                } catch (CalculationException e) {
                    Logger.log(Logger.ErrLabel.CALCULATION_COMPUTATION_ERROR, e.getMessage());
                    ViewUtils.showToast(this, this.getString(R.string.error_computation_exception));
                }
                break;
            }
        }
    }
}