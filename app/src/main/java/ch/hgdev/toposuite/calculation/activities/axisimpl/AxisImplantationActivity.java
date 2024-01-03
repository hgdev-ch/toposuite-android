package ch.hgdev.toposuite.calculation.activities.axisimpl;

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
import ch.hgdev.toposuite.calculation.AxisImplantation;
import ch.hgdev.toposuite.calculation.Calculation;
import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.CalculationType;
import ch.hgdev.toposuite.calculation.FreeStation;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.calculation.activities.axisimpl.MeasureDialogFragment.MeasureDialogListener;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class AxisImplantationActivity extends TopoSuiteActivity implements MeasureDialogListener {
    public static final String AXIS_IMPLANTATION = "axis_implantation";

    private static final String AXIS_IMPL_ACTIVITY = "AxisImplantationActivity: ";
    private static final String STATION_SELECTED_POSITION = "station_selected_position";
    private static final String ORIGIN_SELECTED_POSITION = "origin_selected_position";
    private static final String EXTREMITY_SELECTED_POSITION = "extremity_selected_position";
    private static final String MEASURES_LIST_LABEL = "measures_list";

    private CheckBox checkboxZ0;

    private Spinner stationSpinner;
    private Spinner originSpinner;
    private Spinner extremitySpinner;

    private TextView calculatedDistanceTextView;
    private TextView extremityTextView;
    private TextView originTextView;
    private TextView stationTextView;

    private EditText unknownOrientationEditText;

    private ListView measuresListView;

    private FloatingActionButton addButton;

    private AxisImplantation axisImpl;

    private ArrayListOfMeasuresAdapter adapter;
    private ArrayAdapter<Point> pointsAdapter;

    private int stationSelectedPosition;
    private int originSelectedPosition;
    private int extremitySelectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_axis_implantation);

        this.checkboxZ0 = (CheckBox) this.findViewById(R.id.checkbox_z0);
        this.stationSpinner = (Spinner) this.findViewById(R.id.station_spinner);
        this.originSpinner = (Spinner) this.findViewById(R.id.origin_spinner);
        this.extremitySpinner = (Spinner) this.findViewById(R.id.extremity_spinner);
        this.calculatedDistanceTextView = (TextView) this.findViewById(R.id.calculated_distance);
        this.extremityTextView = (TextView) this.findViewById(R.id.extremity_point);
        this.originTextView = (TextView) this.findViewById(R.id.origin_point);
        this.stationTextView = (TextView) this.findViewById(R.id.station_point);
        this.measuresListView = (ListView) this.findViewById(R.id.measures_list);
        this.unknownOrientationEditText = (EditText) this.findViewById(R.id.unknown_orientation);
        this.addButton = (FloatingActionButton) this.findViewById(R.id.add_measure_button);

        this.unknownOrientationEditText.setInputType(App.getInputTypeCoordinate());

        this.stationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                AxisImplantationActivity.this.stationSelectedPosition = pos;

                Point pt = (Point) AxisImplantationActivity.this.stationSpinner
                        .getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    AxisImplantationActivity.this.stationTextView.setText
                            (DisplayUtils.formatPoint(AxisImplantationActivity.this, pt));
                } else {
                    AxisImplantationActivity.this.stationTextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // nothing
            }
        });

        this.originSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                AxisImplantationActivity.this.originSelectedPosition = pos;

                Point pt = (Point) AxisImplantationActivity.this.originSpinner.getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    AxisImplantationActivity.this.originTextView.setText
                            (DisplayUtils.formatPoint(AxisImplantationActivity.this, pt));
                } else {
                    AxisImplantationActivity.this.originTextView.setText("");
                }

                AxisImplantationActivity.this.orthogonalBasePointsSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // nothing
            }
        });

        this.extremitySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                AxisImplantationActivity.this.extremitySelectedPosition = pos;

                Point pt = (Point) AxisImplantationActivity.this.extremitySpinner
                        .getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    AxisImplantationActivity.this.extremityTextView.setText
                            (DisplayUtils.formatPoint(AxisImplantationActivity.this, pt));
                } else {
                    AxisImplantationActivity.this.extremityTextView.setText("");
                }

                AxisImplantationActivity.this.orthogonalBasePointsSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // nothing
            }
        });

        this.measuresListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AxisImplantationActivity.this.showEditMeasureDialog(position);
            }
        });

        this.addButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  AxisImplantationActivity.this.showAddMeasureDialog();
                                              }
                                          }
        );

        List<Point> points = new ArrayList<>();
        points.add(new Point(false));
        points.addAll(SharedResources.getSetOfPoints());

        this.pointsAdapter = new ArrayAdapter<>(this, R.layout.spinner_list_item, points);

        Bundle bundle = this.getIntent().getExtras();
        if ((bundle != null)) {
            int position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.axisImpl = (AxisImplantation) SharedResources.getCalculationsHistory().get(position);

            this.stationSelectedPosition = this.pointsAdapter.getPosition(this.axisImpl.getStation());
            this.originSelectedPosition = this.pointsAdapter.getPosition(this.axisImpl.getOrthogonalBase().getOrigin());
            this.extremitySelectedPosition = this.pointsAdapter.getPosition(this.axisImpl.getOrthogonalBase().getExtremity());

            // the user has retrieved his z0 from last calculation previously
            if (this.axisImpl.getZ0CalculationId() > 0) {
                Calculation c = SharedResources.getCalculationsHistory().find(this.axisImpl.getZ0CalculationId());

                if ((c != null) && (c.getType() == CalculationType.ABRISS)) {
                    Abriss a = (Abriss) c;
                    try {
                        a.compute();
                        this.axisImpl.setUnknownOrientation(a.getMean());
                        this.axisImpl.setStation(a.getStation());
                    } catch (CalculationException e) {
                        Logger.log(Logger.ErrLabel.CALCULATION_COMPUTATION_ERROR, e.getMessage());
                        ViewUtils.showToast(this, this.getString(R.string.error_computation_exception));
                    }
                } else if ((c != null) && (c.getType() == CalculationType.FREESTATION)) {
                    FreeStation fs = (FreeStation) c;
                    try {
                        fs.compute();
                        this.axisImpl.setUnknownOrientation(fs.getUnknownOrientation());
                        this.axisImpl.setStation(fs.getStationResult());
                    } catch (CalculationException e) {
                        Logger.log(Logger.ErrLabel.CALCULATION_COMPUTATION_ERROR, e.getMessage());
                        ViewUtils.showToast(this, this.getString(R.string.error_computation_exception));
                    }
                } else {
                    Logger.log(
                            Logger.ErrLabel.CALCULATION_INVALID_TYPE,
                            AxisImplantationActivity.AXIS_IMPL_ACTIVITY
                                    + "trying to get Z0 from a calculation that does not compute one");
                }
                this.checkboxZ0.setChecked(true);
                this.unknownOrientationEditText.setEnabled(false);
            }

            this.unknownOrientationEditText.setText(DisplayUtils.toStringForEditText(this.axisImpl.getUnknownOrientation()));
        } else {
            this.axisImpl = new AxisImplantation(true);
        }

        this.adapter = new ArrayListOfMeasuresAdapter(
                this, R.layout.axis_implantation_list_item,
                new ArrayList<>(this.axisImpl.getMeasures()));
        this.registerForContextMenu(this.measuresListView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.stationSpinner.setAdapter(this.pointsAdapter);
        this.originSpinner.setAdapter(this.pointsAdapter);
        this.extremitySpinner.setAdapter(this.pointsAdapter);

        if (this.stationSelectedPosition > 0) {
            this.stationSpinner.setSelection(
                    this.stationSelectedPosition);
        }

        if (this.originSelectedPosition > 0) {
            this.originSpinner.setSelection(
                    this.originSelectedPosition);
        }

        if (this.extremitySelectedPosition > 0) {
            this.extremitySpinner.setSelection(
                    this.extremitySelectedPosition);
        }

        this.drawList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.action_run_calculation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.context_list_row_delete, menu);
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_axis_implantation);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(AxisImplantationActivity.STATION_SELECTED_POSITION,
                this.stationSelectedPosition);
        outState.putInt(AxisImplantationActivity.ORIGIN_SELECTED_POSITION,
                this.originSelectedPosition);
        outState.putInt(AxisImplantationActivity.EXTREMITY_SELECTED_POSITION,
                this.extremitySelectedPosition);

        outState.putSerializable(AxisImplantationActivity.MEASURES_LIST_LABEL, this.adapter.getMeasures());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            this.stationSelectedPosition = savedInstanceState
                    .getInt(AxisImplantationActivity.STATION_SELECTED_POSITION);
            this.originSelectedPosition = savedInstanceState
                    .getInt(AxisImplantationActivity.ORIGIN_SELECTED_POSITION);
            this.extremitySelectedPosition = savedInstanceState
                    .getInt(AxisImplantationActivity.EXTREMITY_SELECTED_POSITION);

            ArrayList<Measure> measures = (ArrayList<Measure>) savedInstanceState.getSerializable(AxisImplantationActivity.MEASURES_LIST_LABEL);
            this.adapter.clear();
            this.adapter.addAll(measures);
            this.drawList();
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.run_calculation_button:
                if (this.checkInputs()) {
                    // update I and station number
                    this.axisImpl.setStation(this.pointsAdapter.getItem(this.stationSelectedPosition));
                    this.axisImpl.getOrthogonalBase().setOrigin(
                            this.pointsAdapter.getItem(this.originSelectedPosition));
                    this.axisImpl.getOrthogonalBase().setExtremity(
                            this.pointsAdapter.getItem(this.extremitySelectedPosition));

                    this.axisImpl.getMeasures().clear();
                    this.axisImpl.getMeasures().addAll(this.adapter.getMeasures());

                    this.axisImpl.setUnknownOrientation(ViewUtils.readDouble(this.unknownOrientationEditText));

                    this.startAxisImplantationResultsActivity();
                } else {
                    ViewUtils.showToast(
                            this, this.getString(R.string.error_fill_data));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Display a dialog to allow the user to insert a new measure.
     */
    private void showAddMeasureDialog() {
        ViewUtils.lockScreenOrientation(this);

        MeasureDialogFragment dialog = MeasureDialogFragment.newInstance();
        dialog.show(this.getSupportFragmentManager(), "MeasureDialogFragment");
    }

    /**
     * Display a dialog to allow the user to edit an existing measure.
     */
    private void showEditMeasureDialog(int position) {
        ViewUtils.lockScreenOrientation(this);

        Measure m = this.adapter.getItem(position);
        MeasureDialogFragment dialog = MeasureDialogFragment.newInstance(m);
        dialog.show(this.getSupportFragmentManager(), "MeasureDialogFragment");
    }

    @Override
    public void onDialogAdd(MeasureDialogFragment dialog) {
        Measure m = new Measure(
                null,
                dialog.getHorizDir(),
                MathUtils.IGNORE_DOUBLE,
                dialog.getDistance());
        m.setMeasureNumber(dialog.getMeasureNumber());
        this.adapter.add(m);
        this.adapter.notifyDataSetChanged();
        this.showAddMeasureDialog();
    }

    @Override
    public void onDialogEdit(MeasureDialogFragment dialog) {
        int position = this.adapter.getMeasures().indexOf(dialog.getMeasure());

        Measure m = this.adapter.getMeasures().get(position);
        m.setMeasureNumber(dialog.getMeasureNumber());
        m.setHorizDir(dialog.getHorizDir());
        m.setDistance(dialog.getDistance());

        this.adapter.notifyDataSetChanged();

        ViewUtils.unlockScreenOrientation(this);
    }

    @Override
    public void onDialogCancel() {
        ViewUtils.unlockScreenOrientation(this);
    }

    /**
     * Callback for R.id.checkbox_z0
     */
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.checkbox_z0:
                if (checked) {
                    this.fetchLastFreeStationOrAbriss();
                    if (MathUtils.isIgnorable(this.axisImpl.getUnknownOrientation())) {
                        ViewUtils.showToast(this,
                                this.getString(R.string.error_no_suitable_calculation_found));
                    } else {
                        this.unknownOrientationEditText.setText(DisplayUtils.toStringForEditText(this.axisImpl.getUnknownOrientation()));
                        this.unknownOrientationEditText.setEnabled(false);
                        this.stationSpinner.setSelection(this.pointsAdapter.getPosition(this.axisImpl.getStation()));
                        this.stationSpinner.setEnabled(false);
                    }
                } else {
                    this.unknownOrientationEditText.setText("");
                    this.unknownOrientationEditText.setEnabled(true);
                    this.stationSpinner.setSelection(0);
                    this.stationSpinner.setEnabled(true);
                }
                break;
        }
    }

    private void drawList() {
        this.measuresListView.setAdapter(this.adapter);
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
                    this.axisImpl.setUnknownOrientation(a.getMean());
                    this.axisImpl.setStation(a.getStation());
                    this.axisImpl.setZ0CalculationId(c.getId());
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
                    this.axisImpl.setUnknownOrientation(fs.getUnknownOrientation());
                    this.axisImpl.setStation(fs.getStationResult());
                    this.axisImpl.setZ0CalculationId(c.getId());
                } catch (CalculationException e) {
                    Logger.log(Logger.ErrLabel.CALCULATION_COMPUTATION_ERROR, e.getMessage());
                    ViewUtils.showToast(this, this.getString(R.string.error_computation_exception));
                }
                break;
            }
        }
    }

    /**
     * Start the free station results activity. This action in only performed
     * when the user run the calculation.
     */
    private void startAxisImplantationResultsActivity() {
        Bundle bundle = new Bundle();

        // At this point we are sure that the axis implantation calculation
        // has been instantiated.
        bundle.putSerializable(AxisImplantationActivity.AXIS_IMPLANTATION, this.axisImpl);

        Intent resultsActivityIntent = new Intent(this, AxisImplantationResultsActivity.class);
        resultsActivityIntent.putExtras(bundle);
        this.startActivity(resultsActivityIntent);
    }

    private boolean checkInputs() {
        return ((this.stationSelectedPosition > 0)
                && (this.originSelectedPosition > 0)
                && (this.extremitySelectedPosition > 0)
                && (this.unknownOrientationEditText.length() > 0)
                && (this.adapter.getMeasures().size() >= 1));
    }

    private void orthogonalBasePointsSelected() {
        if ((this.originSelectedPosition > 0) && (this.extremitySelectedPosition > 0)) {
            this.calculatedDistanceTextView.setText(
                    DisplayUtils.formatDistance(
                            MathUtils.euclideanDistance(
                                    this.pointsAdapter.getItem(this.originSelectedPosition),
                                    this.pointsAdapter.getItem(this.extremitySelectedPosition))));
        }
    }
}
