package ch.hgdev.toposuite.calculation.activities.leveortho;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
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
    public static final String         ORIGIN_SELECTED_POSITION    = "origine_selected_position";
    public static final String         EXTREMITY_SELECTED_POSITION = "extremity_selected_position";
    public static final String         MEASURED_DISTANCE           = "measured_distance";
    public static final String         LEVE_ORTHO_POSITION         = "leve_ortho_position";
    public static final String         MEASURE_POSITION            = "measure_position";

    private Spinner                    originSpinner;
    private Spinner                    extremitySpinner;

    private TextView                   originPointTextView;
    private TextView                   extremityPointTextView;
    private TextView                   calcDistTextView;
    private TextView                   scaleTextView;

    private EditText                   measuredDistEditText;

    private ListView                   measuresListView;

    private int                        originSelectedPosition;
    private int                        extremitySelectedPosition;

    private double                     measuredDist;

    private LeveOrthogonal             leveOrtho;

    private ArrayListOfMeasuresAdapter adapter;

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

        this.originSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                LeveOrthoActivity.this.originSelectedPosition = pos;

                Point pt = (Point)
                        LeveOrthoActivity.this.originSpinner.getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    LeveOrthoActivity.this.originPointTextView.setText
                            (DisplayUtils.formatPoint(LeveOrthoActivity.this, pt));
                }
                else {
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
                }
                else {
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

        Bundle bundle = this.getIntent().getExtras();
        if ((bundle != null)) {
            int position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.leveOrtho = (LeveOrthogonal) SharedResources.getCalculationsHistory().get(
                    position);
            this.measuredDist = this.leveOrtho.getOrthogonalBase().getMeasuredDistance();
        } else {
            this.leveOrtho = new LeveOrthogonal(true);
        }

        this.registerForContextMenu(this.measuresListView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.adapter = new ArrayListOfMeasuresAdapter(this,
                R.layout.leve_ortho_measures_list_item, this.leveOrtho.getMeasures());
        this.drawList();

        List<Point> points = new ArrayList<Point>();
        points.add(new Point("", 0.0, 0.0, 0.0, true));
        points.addAll(SharedResources.getSetOfPoints());

        ArrayAdapter<Point> a = new ArrayAdapter<Point>(
                this, R.layout.spinner_list_item, points);
        this.originSpinner.setAdapter(a);
        this.extremitySpinner.setAdapter(a);

        if (this.leveOrtho.getOrthogonalBase().getOrigin() != null) {
            this.originSelectedPosition = a.getPosition(
                    this.leveOrtho.getOrthogonalBase().getOrigin());
        }

        if (this.leveOrtho.getOrthogonalBase().getExtremity() != null) {
            this.extremitySelectedPosition = a.getPosition(
                    this.leveOrtho.getOrthogonalBase().getExtremity());
        }

        if (this.originSelectedPosition > 0) {
            this.originSpinner.setSelection(
                    this.originSelectedPosition);
        }

        if (this.extremitySelectedPosition > 0) {
            this.extremitySpinner.setSelection(
                    this.extremitySelectedPosition);
        }

        if (!MathUtils.isZero(this.measuredDist)) {
            this.measuredDistEditText.setText(
                    DisplayUtils.toStringForEditText(this.measuredDist));
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_leve_ortho);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.leve_ortho, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(LeveOrthoActivity.ORIGIN_SELECTED_POSITION,
                this.originSelectedPosition);
        outState.putInt(LeveOrthoActivity.EXTREMITY_SELECTED_POSITION,
                this.extremitySelectedPosition);
        outState.putDouble(LeveOrthoActivity.MEASURED_DISTANCE,
                this.measuredDist);

        if (this.leveOrtho != null) {
            int index = SharedResources.getCalculationsHistory().indexOf(this.leveOrtho);
            outState.putInt(LeveOrthoActivity.LEVE_ORTHO_POSITION,
                    index);
        } else {
            outState.putInt(LeveOrthoActivity.LEVE_ORTHO_POSITION,
                    -1);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            int index = savedInstanceState.getInt(LeveOrthoActivity.LEVE_ORTHO_POSITION);
            if (index != -1) {
                if (this.adapter != null) {
                    this.adapter.clear();
                }
                this.leveOrtho = (LeveOrthogonal) SharedResources.getCalculationsHistory()
                        .get(index);
                this.drawList();
            } else {
                this.originSelectedPosition = savedInstanceState
                        .getInt(LeveOrthoActivity.ORIGIN_SELECTED_POSITION);
                this.extremitySelectedPosition = savedInstanceState
                        .getInt(LeveOrthoActivity.EXTREMITY_SELECTED_POSITION);
                this.measuredDist = savedInstanceState
                        .getDouble(LeveOrthoActivity.MEASURED_DISTANCE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
        case R.id.add_point_button:
            this.showAddMeasureDialog();
            return true;
        case R.id.run_calculation_button:
            if ((this.originSelectedPosition == 0) || (this.extremitySelectedPosition == 0)
                    || (this.adapter.getCount() == 0)) {
                ViewUtils.showToast(this, this.getString(R.string.error_fill_data));
                return true;
            }

            int position = SharedResources.getCalculationsHistory()
                    .indexOf(this.leveOrtho);

            Bundle bundle = new Bundle();
            bundle.putInt(LeveOrthoActivity.LEVE_ORTHO_POSITION, position);

            Intent resultsActivityIntent = new Intent(this,
                    LeveOrthoResultsActivity.class);
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
        inflater.inflate(R.menu.leve_ortho_measures_list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
        case R.id.edit_measure:
            this.showEditMeasureDialog(info.position);
            return true;
        case R.id.delete_measure:
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
            if (this.leveOrtho.getOrthogonalBase() != null) {
                this.leveOrtho.getOrthogonalBase().setOrigin(p1);
                this.leveOrtho.getOrthogonalBase().setExtremity(p2);
            } else {
                this.leveOrtho.setOrthogonalBase(new OrthogonalBase(p1, p2,
                        ViewUtils.readDouble(this.measuredDistEditText), 1.0));
            }

            this.calcDistTextView.setText(DisplayUtils.formatDistance(
                    this.leveOrtho.getOrthogonalBase().getCalculatedDistance()));

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
                this.leveOrtho.getOrthogonalBase().setMeasuredDistance(this.measuredDist);

                double scaleFactor = this.leveOrtho.getOrthogonalBase().getScaleFactor();

                StringBuilder builder = new StringBuilder();
                builder.append(DisplayUtils.formatDistance(scaleFactor));
                builder.append(" (");
                builder.append(MathUtils.scaleToPPM(scaleFactor));
                builder.append("ppm)");
                this.scaleTextView.setText(builder.toString());
            }
        }
    }

    private void showAddMeasureDialog() {
        ViewUtils.lockScreenOrientation(this);

        AddMeasureDialogFragment dialog = new AddMeasureDialogFragment();
        dialog.show(this.getFragmentManager(), "AddPointDialogFragment");
    }

    private void showEditMeasureDialog(int pos) {
        ViewUtils.lockScreenOrientation(this);

        EditMeasureDialogFragment dialog = new EditMeasureDialogFragment();

        int leveOrthoPos = SharedResources.getCalculationsHistory().indexOf(this.leveOrtho);

        Bundle bundle = new Bundle();
        bundle.putInt(LeveOrthoActivity.LEVE_ORTHO_POSITION, leveOrthoPos);
        bundle.putInt(LeveOrthoActivity.MEASURE_POSITION, pos);

        dialog.setArguments(bundle);
        dialog.show(this.getFragmentManager(), "EditMeasureDialogFragment");
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
        LeveOrthogonal.Measure m = this.leveOrtho.getMeasures().get(
                dialog.getMeasurePosition());

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
