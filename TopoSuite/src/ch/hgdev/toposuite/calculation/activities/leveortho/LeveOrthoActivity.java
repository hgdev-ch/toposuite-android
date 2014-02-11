package ch.hgdev.toposuite.calculation.activities.leveortho;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.LeveOrthogonal;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class LeveOrthoActivity extends TopoSuiteActivity {
    public static final String ORIGIN_SELECTED_POSITION    = "origine_selected_position";
    public static final String EXTREMITY_SELECTED_POSITION = "extremity_selected_position";
    public static final String MEASURED_DISTANCE           = "measured_distance";
    public static final String LEVE_ORTHO_POSITION         = "leve_ortho_position";

    private Spinner            originSpinner;
    private Spinner            extremitySpinner;

    private TextView           originPointTextView;
    private TextView           extremityPointTextView;
    private TextView           calcDistTextView;
    private TextView           scaleTextView;

    private EditText           measuredDistEditText;

    private ListView           pointsListView;

    private int                originSelectedPosition;
    private int                extremitySelectedPosition;

    private double             measuredDist;

    private LeveOrthogonal     leveOrtho;

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
        this.measuredDistEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

        this.pointsListView = (ListView) this.findViewById(R.id.points_list);

        this.originSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                LeveOrthoActivity.this.originSelectedPosition = pos;

                Point pt = (Point)
                        LeveOrthoActivity.this.originSpinner.getItemAtPosition(pos);
                if (pt.getNumber() > 0) {
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
                if (pt.getNumber() > 0) {
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
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Point> points = new ArrayList<Point>();
        points.add(new Point(0, 0.0, 0.0, 0.0, true));
        points.addAll(SharedResources.getSetOfPoints());

        ArrayAdapter<Point> a = new ArrayAdapter<Point>(
                this, R.layout.spinner_list_item, points);
        this.originSpinner.setAdapter(a);
        this.extremitySpinner.setAdapter(a);

        if (this.leveOrtho != null) {
            this.originSpinner.setSelection(
                    a.getPosition(this.leveOrtho.getOrthogonalBase().getOrigin()));
            this.extremitySpinner.setSelection(
                    a.getPosition(this.leveOrtho.getOrthogonalBase().getExtemity()));
        } else {
            if (this.originSelectedPosition > 0) {
                this.originSpinner.setSelection(
                        this.originSelectedPosition);
            }

            if (this.extremitySelectedPosition > 0) {
                this.extremitySpinner.setSelection(
                        this.extremitySelectedPosition);
            }
        }

        if (this.measuredDist != Double.MIN_VALUE) {
            this.measuredDistEditText.setText(DisplayUtils.toString(this.measuredDist));
        }
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
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            int index = savedInstanceState.getInt(LeveOrthoActivity.LEVE_ORTHO_POSITION);
            if (index != 0) {
                this.leveOrtho = (LeveOrthogonal) SharedResources.getCalculationsHistory()
                        .get(index);
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
        case R.id.add_orientation_button:
            // this.showAddPointDialog();
            return true;
        case R.id.run_calculation_button:

            // Bundle bundle = new Bundle();
            // bundle.putInt(AbrissActivity.CALCULATION_POSITION_LABEL,
            // this.position);
            //
            // bundle.putInt(AbrissActivity.STATION_NUMBER_LABEL,
            // station.getNumber());
            //
            // JSONArray json = new JSONArray();
            // for (int i = 0; i < this.adapter.getCount(); i++) {
            // json.put(this.adapter.getItem(i).toJSONObject());
            // }
            //
            // bundle.putString(AbrissActivity.ORIENTATIONS_LABEL,
            // json.toString());
            //
            // Intent resultsActivityIntent = new Intent(this,
            // AbrissResultsActivity.class);
            // resultsActivityIntent.putExtras(bundle);
            // this.startActivity(resultsActivityIntent);

            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void itemSelected() {
        Point p1 = (Point) this.originSpinner.getSelectedItem();
        Point p2 = (Point) this.extremitySpinner.getSelectedItem();

        if ((p1.getNumber() == 0) || (p2.getNumber() == 0)) {
            this.resetResults();
        } else if (p1.getNumber() == p2.getNumber()) {
            this.resetResults();
            Toast.makeText(this, R.string.error_same_points, Toast.LENGTH_LONG).show();
        } else {
            if (this.leveOrtho == null) {
                this.leveOrtho = new LeveOrthogonal(p1, p2, true);
            } else {
                this.leveOrtho.getOrthogonalBase().setOrigin(p1);
                this.leveOrtho.getOrthogonalBase().setExtemity(p2);
            }

            this.calcDistTextView.setText(DisplayUtils.toString(
                    this.leveOrtho.getOrthogonalBase().getCalculatedDistance()));

            this.updateScaleFactor();
        }
    }

    private void resetResults() {
        this.calcDistTextView.setText("");
        this.scaleTextView.setText("");
    }

    private void updateScaleFactor() {
        Point p1 = (Point) this.originSpinner.getSelectedItem();
        Point p2 = (Point) this.extremitySpinner.getSelectedItem();

        if ((p1 == null) || (p2 == null)) {
            return;
        }

        if ((p1.getNumber() != 0) && (p2.getNumber() != 0)
                && (this.leveOrtho != null)) {

            String inputText = this.measuredDistEditText.getText().toString();

            if (!inputText.isEmpty()) {
                this.measuredDist = Double.parseDouble(inputText);
                this.leveOrtho.getOrthogonalBase().setMeasuredDistance(this.measuredDist);

                double scaleFactor = this.leveOrtho.getOrthogonalBase().getScaleFactor();
                this.scaleTextView.setText(DisplayUtils.toString(scaleFactor));
            }
        }
    }
}
