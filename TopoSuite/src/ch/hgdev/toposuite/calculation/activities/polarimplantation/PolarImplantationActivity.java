package ch.hgdev.toposuite.calculation.activities.polarimplantation;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
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
import ch.hgdev.toposuite.calculation.Abriss;
import ch.hgdev.toposuite.calculation.Abriss.Result;
import ch.hgdev.toposuite.calculation.Calculation;
import ch.hgdev.toposuite.calculation.CalculationType;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.calculation.PolarImplantation;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.MathUtils;

public class PolarImplantationActivity extends TopoSuiteActivity implements
        AddPointWithSDialogFragment.AddPointWithSDialogListener {

    public static final String                               STATION_NUMBER_LABEL      = "station_number";
    public static final String                               POINTS_WITH_S_LABEL       = "points_with_s";

    private static final String                              STATION_SELECTED_POSITION = "station_selected_position";
    private Spinner                                          stationSpinner;
    private int                                              stationSelectedPosition;
    private TextView                                         stationPointTextView;
    private EditText                                         iEditText;
    private Spinner                                          unknownOrientSpinner;
    private int                                              unknownOrientSelectedPosition;
    private TextView                                         unknownOrientTextView;
    private EditText                                         unknownOrientEditText;
    private ListView                                         pointsListView;
    private ArrayAdapter<Measure>                            adapter;

    private Point                                            station;
    private PolarImplantation                                polarImplantation;
    private PolarImplantationActivity.UnknownOrientationItem unknownOrientation;

    /**
     * Position of the calculation in the calculations list. Only used when open
     * from the history.
     */
    private int                                              position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_polar_implantation);

        this.position = -1;

        this.stationSpinner = (Spinner) this.findViewById(R.id.station_spinner);
        this.stationPointTextView = (TextView) this.findViewById(R.id.station_point);
        this.unknownOrientEditText = (EditText) this.findViewById(R.id.unknown_orientation);
        this.unknownOrientSpinner = (Spinner) this.findViewById(R.id.unknown_orientation_spinner);
        this.unknownOrientTextView = (TextView) this
                .findViewById(R.id.unknown_orientation_spinner_view);
        this.iEditText = (EditText) this.findViewById(R.id.i);
        this.pointsListView = (ListView) this.findViewById(R.id.list_of_points);

        this.iEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);
        this.unknownOrientEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

        this.stationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                PolarImplantationActivity.this.stationSelectedPosition = pos;

                PolarImplantationActivity.this.station = (Point) PolarImplantationActivity.this.stationSpinner
                        .getItemAtPosition(pos);
                if (PolarImplantationActivity.this.station.getNumber() > 0) {
                    PolarImplantationActivity.this.stationPointTextView.setText(DisplayUtils
                            .formatPoint(PolarImplantationActivity.this,
                                    PolarImplantationActivity.this.station));
                } else {
                    PolarImplantationActivity.this.stationPointTextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.unknownOrientSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                PolarImplantationActivity.this.unknownOrientSelectedPosition = pos;

                PolarImplantationActivity.this.unknownOrientation =
                        (PolarImplantationActivity.UnknownOrientationItem) PolarImplantationActivity.this.unknownOrientSpinner
                                .getItemAtPosition(pos);
                if (PolarImplantationActivity.this.unknownOrientation.getStation().getNumber() > 0) {
                    PolarImplantationActivity.this.unknownOrientTextView
                            .setText(DisplayUtils
                                    .toString(PolarImplantationActivity.this.unknownOrientation
                                            .getZ0()));
                    PolarImplantationActivity.this.unknownOrientEditText.setText("");
                } else {
                    PolarImplantationActivity.this.unknownOrientTextView
                            .setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });
        this.unknownOrientEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (PolarImplantationActivity.this.unknownOrientEditText.length() > 0) {
                    PolarImplantationActivity.this.unknownOrientSpinner.setSelection(0);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // nothing
            }
        });

        ArrayList<Measure> list = new ArrayList<Measure>();

        // check if we create a new polar implantation calculation or if we
        // modify an
        // existing one.
        Bundle bundle = this.getIntent().getExtras();
        if ((bundle != null)) {
            this.position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.polarImplantation = (PolarImplantation) SharedResources.getCalculationsHistory()
                    .get(
                            this.position);
            list = this.polarImplantation.getMeasures();
        }

        this.adapter = new ArrayListOfPointsWithSAdapter(this,
                R.layout.points_with_s_list_item, list);

        this.drawList();

        this.registerForContextMenu(this.pointsListView);
    }

    @Override
    public void onResume() {
        super.onResume();

        List<Point> points = new ArrayList<Point>();
        points.add(new Point(0, 0.0, 0.0, 0.0, false));
        points.addAll(SharedResources.getSetOfPoints());

        ArrayAdapter<Point> ap = new ArrayAdapter<Point>(
                this, R.layout.spinner_list_item, points);
        this.stationSpinner.setAdapter(ap);

        List<PolarImplantationActivity.UnknownOrientationItem> unknownOrientationList =
                new ArrayList<PolarImplantationActivity.UnknownOrientationItem>();
        unknownOrientationList
                .add(new UnknownOrientationItem(new Point(0, 0.0, 0.0, 0.0, false),
                        0, Double.MIN_VALUE));
        for (Calculation c : SharedResources.getCalculationsHistory()) {
            if ((c != null) && (c.getType() != CalculationType.ABRISS)) {
                continue;
            }
            Abriss a = (Abriss) c;
            a.compute();
            for (Result m : a.getResults()) {
                unknownOrientationList.add(new UnknownOrientationItem(a.getStation(), m
                        .getOrientation().getNumber(), m.getUnknownOrientation()));
            }
        }

        ArrayAdapter<UnknownOrientationItem> aauoi = new ArrayAdapter<PolarImplantationActivity.UnknownOrientationItem>(
                this, R.layout.spinner_list_item, unknownOrientationList);
        this.unknownOrientSpinner.setAdapter(aauoi);

        if (this.polarImplantation != null) {
            this.stationSpinner.setSelection(
                    ap.getPosition(this.polarImplantation.getStation()));
            Measure m = this.polarImplantation.getMeasures().get(0);

            this.iEditText.setText(DisplayUtils.toString(m.getI()));
            this.unknownOrientEditText.setText(DisplayUtils.toString(m.getUnknownOrientation()));
        } else {
            if (this.stationSelectedPosition > 0) {
                this.stationSpinner.setSelection(
                        this.stationSelectedPosition);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.polar_implantation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(PolarImplantationActivity.STATION_SELECTED_POSITION,
                this.stationSelectedPosition);

        JSONArray json = new JSONArray();
        for (int i = 0; i < this.adapter.getCount(); i++) {
            json.put(this.adapter.getItem(i).toJSONObject());
        }

        outState.putString(PolarImplantationActivity.POINTS_WITH_S_LABEL, json.toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            this.adapter.clear();
            this.stationSelectedPosition = savedInstanceState.getInt(
                    PolarImplantationActivity.STATION_SELECTED_POSITION);
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(
                        savedInstanceState
                                .getString(PolarImplantationActivity.POINTS_WITH_S_LABEL));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = (JSONObject) jsonArray.get(i);
                    Measure m = Measure.getMeasureFromJSON(json.toString());
                    this.adapter.add(m);
                }
            } catch (JSONException e) {
                // TODO
            }
            this.drawList();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
        case R.id.add_point_button:
            if (this.checkInputs()) {
                this.showAddPointDialog();
            } else {
                Toast errorToast = Toast.makeText(this, this.getText(R.string.error_fill_data),
                        Toast.LENGTH_SHORT);
                errorToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                errorToast.show();
            }
            return true;
        case R.id.run_calculation_button:
            if (this.checkInputs()) {
                this.showPolarImplantationResultActivity();
            } else {
                Toast errorToast = Toast.makeText(this, this.getText(R.string.error_fill_data),
                        Toast.LENGTH_SHORT);
                errorToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                errorToast.show();
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Show a dialog to add a new point, with optional S.
     */
    private void showAddPointDialog() {
        AddPointWithSDialogFragment dialog = new AddPointWithSDialogFragment();
        dialog.show(this.getFragmentManager(), "AddPointWithSDialogFragment");
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
        if ((this.station == null) || (this.station.getNumber() < 1)) {
            return false;
        }
        if ((this.unknownOrientEditText.length() == 0) && (this.unknownOrientSelectedPosition < 1)) {
            return false;
        }
        return true;
    }

    /**
     * Start the activity that shows the results of the calculation.
     */
    private void showPolarImplantationResultActivity() {
        Bundle bundle = new Bundle();
        bundle.putInt(PolarImplantationActivity.STATION_NUMBER_LABEL, this.station.getNumber());

        JSONArray json = new JSONArray();
        for (int i = 0; i < this.adapter.getCount(); i++) {
            json.put(this.adapter.getItem(i).toJSONObject());
        }

        bundle.putString(PolarImplantationActivity.POINTS_WITH_S_LABEL, json.toString());

        Intent resultsActivityIntent = new Intent(this, PolarImplantationResultsActivity.class);
        resultsActivityIntent.putExtras(bundle);
        this.startActivity(resultsActivityIntent);
    }

    @Override
    public void onDialogAdd(AddPointWithSDialogFragment dialog) {
        double i = 0.0;
        double unknownOrient;

        if (this.iEditText.length() > 0) {
            i = Double.parseDouble(this.iEditText.getText().toString());
        }
        if (this.unknownOrientEditText.length() > 0) {
            unknownOrient = Double.parseDouble(this.unknownOrientEditText.getText().toString());
        } else if (this.unknownOrientSelectedPosition > 0) {
            unknownOrient = ((PolarImplantationActivity.UnknownOrientationItem) this.unknownOrientSpinner
                    .getItemAtPosition(this.unknownOrientSelectedPosition)).getZ0();
        } else {
            Toast errorToast = Toast.makeText(this,
                    this.getText(R.string.error_choose_unknown_orientation),
                    Toast.LENGTH_SHORT);
            errorToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            errorToast.show();
            return;
        }

        double s = !MathUtils.isZero(i) ? dialog.getS() : 0.0;
        Measure m = new Measure(
                dialog.getPoint(),
                0.0,
                0.0,
                0.0,
                s,
                0.0,
                0.0,
                i,
                unknownOrient);

        this.adapter.add(m);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogCancel(AddPointWithSDialogFragment dialog) {
        // do nothing actually
    }

    private class UnknownOrientationItem {
        private final Point  station;
        private final int    orientationNumber;
        private final double z0;

        private UnknownOrientationItem(Point _station, int _orientationNumber, double _z0) {
            this.station = _station;
            this.orientationNumber = _orientationNumber;
            this.z0 = _z0;
        }

        public Point getStation() {
            return this.station;
        }

        private double getZ0() {
            return this.z0;
        }

        @Override
        public String toString() {
            if (this.station.getNumber() < 1) {
                return "";
            } else {
                String item = PolarImplantationActivity.this.getString(R.string.station_label);
                item += ": " + DisplayUtils.toString(this.station.getNumber()) + "; ";
                item += PolarImplantationActivity.this.getString(R.string.orientation_label);
                item += ": " + DisplayUtils.toString(this.orientationNumber) + "; ";
                return item;
            }
        }
    }
}
