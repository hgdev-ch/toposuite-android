package ch.hgdev.toposuite.calculation.activities.polarimplantation;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
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
import ch.hgdev.toposuite.calculation.Abriss;
import ch.hgdev.toposuite.calculation.Abriss.Result;
import ch.hgdev.toposuite.calculation.Calculation;
import ch.hgdev.toposuite.calculation.CalculationType;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.calculation.PolarImplantation;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class PolarImplantationActivity extends TopoSuiteActivity {

    private static final String                                STATION_SELECTED_POSITION = "station_selected_position";
    private Spinner                                            stationSpinner;
    private int                                                stationSelectedPosition;
    private TextView                                           stationPointTextView;
    private EditText                                           iEditText;
    private Spinner                                            unknownOrientSpinner;
    private int                                                unknownOrientSelectedPosition;
    private TextView                                           unknownOrientTextView;
    private EditText                                           unknownOrientEditText;
    private ListView                                           pointsListView;
    private ArrayAdapter<PolarImplantationActivity.PointWithS> adapter;

    private Point                                              station;
    private PolarImplantation                                  polarImplantation;
    private PolarImplantationActivity.UnknownOrientationItem   unknownOrientation;

    /**
     * Position of the calculation in the calculations list. Only used when open
     * from the history.
     */
    private int                                                position;

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
            if (c.getType() != CalculationType.ABRISS) {
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
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.polar_implantation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void drawList() {
        // TODO implement
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

    /**
     * We need to display a list of points and their associated prism height
     * that the user might enter.
     * 
     * @author HGdev
     * 
     */
    protected class PointWithS extends Point {
        private final double s;

        public PointWithS(int number, double east, double north, double altitude,
                boolean basePoint, double _s) {
            super(number, east, north, altitude, basePoint, false);
            this.s = _s;
        }

        public double getS() {
            return this.s;
        }
    }
}
