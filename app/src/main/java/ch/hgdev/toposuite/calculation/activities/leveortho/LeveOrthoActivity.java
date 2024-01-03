package ch.hgdev.toposuite.calculation.activities.leveortho;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
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
import ch.hgdev.toposuite.calculation.LeveOrthogonal;
import ch.hgdev.toposuite.calculation.OrthogonalBase;
import ch.hgdev.toposuite.calculation.activities.leveortho.AddMeasureDialogFragment.AddMeasureDialogListener;
import ch.hgdev.toposuite.calculation.activities.leveortho.EditMeasureDialogFragment.EditMeasureDialogListener;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class LeveOrthoActivity extends TopoSuiteActivity implements AddMeasureDialogListener,
        EditMeasureDialogListener {
    public static final String ORTHOGONAL_SURVEY = "orthogonal_survey";
    public static final String MEASURE_LABEL = "measure";
    public static final String MEASURE_POSITION = "measure_position";

    private static final String ORIGIN_SELECTED_POSITION = "origin_selected_position";
    private static final String EXTREMITY_SELECTED_POSITION = "extremity_selected_position";
    private static final String MEASURED_DISTANCE = "measured_distance";
    private static final String MEASURES_LIST_LABEL = "measures_list";

    private Spinner originSpinner;
    private Spinner extremitySpinner;

    private TextView originPointTextView;
    private TextView extremityPointTextView;
    private TextView calcDistTextView;
    private TextView scaleTextView;

    private EditText measuredDistEditText;

    private ListView measuresListView;

    private FloatingActionButton addButton;

    private int originSelectedPosition;
    private int extremitySelectedPosition;

    private double measuredDist;

    private ArrayListOfMeasuresAdapter adapter;
    private LeveOrthogonal orthoSurvey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_leve_ortho);

        this.originSelectedPosition = 0;
        this.extremitySelectedPosition = 0;
        this.measuredDist = Double.MIN_VALUE;

        this.originSpinner = (Spinner) this.findViewById(R.id.origin_spinner);
        this.extremitySpinner = (Spinner) this.findViewById(R.id.extremity_spinner);

        this.originPointTextView = (TextView) this.findViewById(R.id.origin_point);
        this.extremityPointTextView = (TextView) this.findViewById(R.id.extremity_point);
        this.calcDistTextView = (TextView) this.findViewById(R.id.calculated_distance);
        this.scaleTextView = (TextView) this.findViewById(R.id.scale_factor);

        this.measuredDistEditText = (EditText) this.findViewById(R.id.measured_distance);
        this.measuredDistEditText.setInputType(App.getInputTypeCoordinate());

        this.measuresListView = (ListView) this.findViewById(R.id.points_list);
        this.addButton = (FloatingActionButton) this.findViewById(R.id.add_measure_button);

        this.originSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                LeveOrthoActivity.this.originSelectedPosition = pos;

                Point pt = (Point)
                        LeveOrthoActivity.this.originSpinner.getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    LeveOrthoActivity.this.originPointTextView.setText
                            (DisplayUtils.formatPoint(LeveOrthoActivity.this, pt));
                } else {
                    LeveOrthoActivity.this.originPointTextView.setText("");
                }
                LeveOrthoActivity.this.itemSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.extremitySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                LeveOrthoActivity.this.extremitySelectedPosition = pos;

                Point pt = (Point)
                        LeveOrthoActivity.this.extremitySpinner.getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    LeveOrthoActivity.this.extremityPointTextView.setText
                            (DisplayUtils.formatPoint(LeveOrthoActivity.this, pt));
                } else {
                    LeveOrthoActivity.this.extremityPointTextView.setText("");
                }
                LeveOrthoActivity.this.itemSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.measuredDistEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // NOTHING
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // NOTHING
            }

            @Override
            public void afterTextChanged(Editable s) {
                LeveOrthoActivity.this.updateScaleFactor();
            }
        });


        this.measuresListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LeveOrthoActivity.this.showEditMeasureDialog(position);
            }
        });

        this.addButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  LeveOrthoActivity.this.showAddMeasureDialog();
                                              }
                                          }
        );

        Bundle bundle = this.getIntent().getExtras();
        if ((bundle != null)) {
            int position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.orthoSurvey = (LeveOrthogonal) SharedResources.getCalculationsHistory().get(position);
            this.measuredDist = this.orthoSurvey.getOrthogonalBase().getMeasuredDistance();
        } else {
            this.orthoSurvey = new LeveOrthogonal(true);
        }

        this.adapter = new ArrayListOfMeasuresAdapter(
                this, R.layout.leve_ortho_measures_list_item,
                new ArrayList<>(this.orthoSurvey.getMeasures()));
        this.registerForContextMenu(this.measuresListView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Point> points = new ArrayList<>();
        points.add(new Point(false));
        points.addAll(SharedResources.getSetOfPoints());

        ArrayAdapter<Point> a = new ArrayAdapter<>(this, R.layout.spinner_list_item, points);
        this.originSpinner.setAdapter(a);
        this.extremitySpinner.setAdapter(a);

        if (this.orthoSurvey.getOrthogonalBase().getOrigin() != null) {
            this.originSelectedPosition = a.getPosition(
                    this.orthoSurvey.getOrthogonalBase().getOrigin());
        }

        if (this.orthoSurvey.getOrthogonalBase().getExtremity() != null) {
            this.extremitySelectedPosition = a.getPosition(
                    this.orthoSurvey.getOrthogonalBase().getExtremity());
        }

        if (this.originSelectedPosition > 0) {
            this.originSpinner.setSelection(this.originSelectedPosition);
        }

        if (this.extremitySelectedPosition > 0) {
            this.extremitySpinner.setSelection(this.extremitySelectedPosition);
        }

        if (!MathUtils.isIgnorable(this.measuredDist)) {
            this.measuredDistEditText.setText(
                    DisplayUtils.toStringForEditText(this.measuredDist));
        }

        this.drawList();
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_leve_ortho);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.action_run_calculation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(LeveOrthoActivity.ORIGIN_SELECTED_POSITION, this.originSelectedPosition);
        outState.putInt(LeveOrthoActivity.EXTREMITY_SELECTED_POSITION, this.extremitySelectedPosition);
        outState.putDouble(LeveOrthoActivity.MEASURED_DISTANCE, this.measuredDist);

        outState.putSerializable(LeveOrthoActivity.MEASURES_LIST_LABEL, this.adapter.getMeasures());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            this.originSelectedPosition = savedInstanceState.getInt(LeveOrthoActivity.ORIGIN_SELECTED_POSITION);
            this.extremitySelectedPosition = savedInstanceState.getInt(LeveOrthoActivity.EXTREMITY_SELECTED_POSITION);
            this.measuredDist = savedInstanceState.getDouble(LeveOrthoActivity.MEASURED_DISTANCE);

            ArrayList<LeveOrthogonal.Measure> measures = (ArrayList<LeveOrthogonal.Measure>) savedInstanceState.getSerializable(LeveOrthoActivity.MEASURES_LIST_LABEL);
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
                if ((this.originSelectedPosition == 0)
                        || (this.extremitySelectedPosition == 0)
                        || (this.adapter.getCount() == 0)) {
                    ViewUtils.showToast(this, this.getString(R.string.error_fill_data));
                    return true;
                }

                this.orthoSurvey.getMeasures().clear();
                this.orthoSurvey.getMeasures().addAll(this.adapter.getMeasures());

                Bundle bundle = new Bundle();
                bundle.putSerializable(LeveOrthoActivity.ORTHOGONAL_SURVEY, this.orthoSurvey);

                Intent resultsActivityIntent = new Intent(this, LeveOrthoResultsActivity.class);
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

    private void drawList() {
        this.measuresListView.setAdapter(this.adapter);
    }

    private void itemSelected() {
        Point p1 = (Point) this.originSpinner.getSelectedItem();
        Point p2 = (Point) this.extremitySpinner.getSelectedItem();

        if ((p1.getNumber().isEmpty()) || (p2.getNumber().isEmpty())) {
            this.resetResults();
        } else if (p1.getNumber().equals(p2.getNumber())) {
            this.resetResults();
            ViewUtils.showToast(this, this.getString(R.string.error_same_points));
        } else {
            if (this.orthoSurvey.getOrthogonalBase() != null) {
                this.orthoSurvey.getOrthogonalBase().setOrigin(p1);
                this.orthoSurvey.getOrthogonalBase().setExtremity(p2);
            } else {
                this.orthoSurvey.setOrthogonalBase(new OrthogonalBase(p1, p2,
                        ViewUtils.readDouble(this.measuredDistEditText), 1.0));
            }

            this.calcDistTextView.setText(DisplayUtils.formatDistance(
                    this.orthoSurvey.getOrthogonalBase().getCalculatedDistance()));

            this.updateScaleFactor();
        }
    }

    private void resetResults() {
        this.calcDistTextView.setText("");
        this.scaleTextView.setText(DisplayUtils.formatDistance(1.0000) + " (0ppm)");
    }

    private void updateScaleFactor() {
        Point p1 = (Point) this.originSpinner.getSelectedItem();
        Point p2 = (Point) this.extremitySpinner.getSelectedItem();

        if ((p1 == null) || (p2 == null)) {
            return;
        }

        if ((!p1.getNumber().isEmpty()) && (!p2.getNumber().isEmpty())) {
            if (this.measuredDistEditText.length() > 0) {
                this.measuredDist = ViewUtils.readDouble(this.measuredDistEditText);
                this.orthoSurvey.getOrthogonalBase().setMeasuredDistance(this.measuredDist);

                double scaleFactor = this.orthoSurvey.getOrthogonalBase().getScaleFactor();
                int scaleFactorPPM = this.orthoSurvey.getOrthogonalBase().getScaleFactorPPM();

                String scale = DisplayUtils.formatScaleFactor(scaleFactor)
                        + " (" + scaleFactorPPM + " ppm)";
                this.scaleTextView.setText(scale);
            }
        }
    }

    private void showAddMeasureDialog() {
        ViewUtils.lockScreenOrientation(this);

        AddMeasureDialogFragment dialog = new AddMeasureDialogFragment();
        dialog.show(this.getSupportFragmentManager(), "AddPointDialogFragment");
    }

    private void showEditMeasureDialog(int pos) {
        ViewUtils.lockScreenOrientation(this);

        EditMeasureDialogFragment dialog = new EditMeasureDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(LeveOrthoActivity.MEASURE_LABEL, this.adapter.getItem(pos));
        bundle.putInt(LeveOrthoActivity.MEASURE_POSITION, pos);

        dialog.setArguments(bundle);
        dialog.show(this.getSupportFragmentManager(), "EditMeasureDialogFragment");
    }

    @Override
    public void onDialogAdd(AddMeasureDialogFragment dialog) {
        String number = dialog.getNumber();
        double abscissa = dialog.getAbscissa();
        double ordinate = dialog.getOrdinate();

        LeveOrthogonal.Measure m = new LeveOrthogonal.Measure(number, abscissa, ordinate);
        this.adapter.add(m);
        this.adapter.notifyDataSetChanged();

        this.showAddMeasureDialog();
    }

    @Override
    public void onDialogCancel(AddMeasureDialogFragment dialog) {
        ViewUtils.unlockScreenOrientation(this);
    }

    @Override
    public void onDialogEdit(EditMeasureDialogFragment dialog) {
        LeveOrthogonal.Measure m = this.adapter.getItem(dialog.getMeasurePosition());
        m.setNumber(dialog.getNumber());
        m.setAbscissa(dialog.getAbscissa());
        m.setOrdinate(dialog.getOrdinate());
        this.adapter.notifyDataSetChanged();

        ViewUtils.unlockScreenOrientation(this);
    }

    @Override
    public void onDialogCancel(EditMeasureDialogFragment dialog) {
        ViewUtils.unlockScreenOrientation(this);
    }
}
