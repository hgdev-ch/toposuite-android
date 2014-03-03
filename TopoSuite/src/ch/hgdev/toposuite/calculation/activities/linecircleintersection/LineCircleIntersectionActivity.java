package ch.hgdev.toposuite.calculation.activities.linecircleintersection;

import android.os.Bundle;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.points.Point;

public class LineCircleIntersectionActivity extends TopoSuiteActivity {

    private Spinner                             point1Spinner;
    private Spinner                             point2Spinner;
    private TextView                            point1TextView;
    private TextView                            point2TextView;
    private TextView                            distP1TexView;
    private TextView                            intersectionPointTextView;
    private EditText                            gisementEditText;
    private EditText                            displacementEditText;
    private EditText                            pointNumberEditText;
    private EditText                            distP1EditText;
    private LinearLayout                        point2SpinnerLayout;
    private LinearLayout                        point2Layout;
    private LinearLayout                        gisementLayout;
    private LinearLayout                        resultLayout;
    private RadioButton                         modeGisementRadio;
    private CheckBox                            perpendicularCheckBox;
    private int                                 point1SelectedPosition;
    private int                                 point2SelectedPosition;
    private boolean                             isLinePerpendicular;
    private LineCircleIntersectionActivity.Mode mode;

    private Spinner                             centerCSpinner;
    private int                                 centerCSelectedPosition;
    private Point                               centerCPoint;
    private TextView                            centerCTextView;
    private double                              radiusC;
    private EditText                            radiusCEditText;
    private Spinner                             byPointSpinner;
    private int                                 byPointSelectedPosition;
    private Point                               byPoint;
    private TextView                            byPointTextView;

    private TextView                            intersectionOneTextView;
    private EditText                            intersectionOneEditText;
    private Point                               intersectionOne;

    private TextView                            intersectionTwoTextView;
    private EditText                            intersectionTwoEditText;
    private Point                               intersectionTwo;

    private enum Mode {
        LINE,
        GISEMENT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_line_circle_intersection);

        this.mapViews();
        this.initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.line_circle_intersection, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_line_circle_intersection);
    }

    /**
     * Map views to their respective attributes.
     */
    private void mapViews() {
        this.point1Spinner = (Spinner) this.findViewById(R.id.point_1_spinner);
        this.point2Spinner = (Spinner) this.findViewById(R.id.point_2_spinner);
        this.point1TextView = (TextView) this.findViewById(R.id.point_1);
        this.point2TextView = (TextView) this.findViewById(R.id.point_2);
        this.distP1TexView = (TextView) this.findViewById(R.id.dist_p1_label);
        this.gisementEditText = (EditText) this.findViewById(R.id.gisement);
        this.displacementEditText = (EditText) this.findViewById(R.id.displacement);

        this.centerCSpinner = (Spinner) this.findViewById(R.id.center_spinner);
        this.centerCTextView = (TextView) this.findViewById(R.id.center_textview);
        this.radiusCEditText = (EditText) this.findViewById(R.id.radius);
        this.byPointSpinner = (Spinner) this.findViewById(R.id.by_point_spinner);
        this.byPointTextView = (TextView) this.findViewById(R.id.by_point_textview);

        this.intersectionOneTextView = (TextView) this.findViewById(R.id.intersection_one);
        this.intersectionTwoTextView = (TextView) this.findViewById(R.id.intersection_two);

        this.intersectionOneEditText = (EditText) this.findViewById(R.id.intersection_one_number);
        this.intersectionTwoEditText = (EditText) this.findViewById(R.id.intersection_two_number);
    }

    /**
     * Init views.
     */
    private void initViews() {
    }

}
