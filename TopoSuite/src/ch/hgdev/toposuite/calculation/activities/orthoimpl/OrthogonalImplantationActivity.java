package ch.hgdev.toposuite.calculation.activities.orthoimpl;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.OrthogonalImplantation;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class OrthogonalImplantationActivity extends TopoSuiteActivity {
    private Spinner                originSpinner;
    private Spinner                extremitySpinner;

    private TextView               originPointTextView;
    private TextView               extremityPointTextView;
    private TextView               calcDistTextView;
    private TextView               scaleTextView;

    private EditText               measuredDistEditText;

    private ListView               measuresListView;

    private int                    originSelectedPosition;
    private int                    extremitySelectedPosition;

    private OrthogonalImplantation orthoImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_orthogonal_implantation);

        this.originSpinner = (Spinner) this.findViewById(R.id.origin_spinner);
        this.extremitySpinner = (Spinner) this.findViewById(R.id.extremity_spinner);

        this.originPointTextView = (TextView) this.findViewById(R.id.origin_point);
        this.extremityPointTextView = (TextView) this.findViewById(R.id.extremity_point);
        this.calcDistTextView = (TextView) this.findViewById(R.id.calculated_distance);

        this.measuresListView = (ListView) this.findViewById(R.id.points_list);

        this.originSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                OrthogonalImplantationActivity.this.originSelectedPosition = pos;

                Point pt = (Point)
                        OrthogonalImplantationActivity.this.originSpinner.getItemAtPosition(pos);
                if (pt.getNumber() > 0) {
                    OrthogonalImplantationActivity.this.originPointTextView.setText
                            (DisplayUtils.formatPoint(OrthogonalImplantationActivity.this, pt));
                }
                else {
                    OrthogonalImplantationActivity.this.originPointTextView.setText("");
                }
                OrthogonalImplantationActivity.this.itemSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.extremitySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                OrthogonalImplantationActivity.this.extremitySelectedPosition = pos;

                Point pt = (Point)
                        OrthogonalImplantationActivity.this.extremitySpinner.getItemAtPosition(pos);
                if (pt.getNumber() > 0) {
                    OrthogonalImplantationActivity.this.extremityPointTextView.setText
                            (DisplayUtils.formatPoint(OrthogonalImplantationActivity.this, pt));
                }
                else {
                    OrthogonalImplantationActivity.this.extremityPointTextView.setText("");
                }
                OrthogonalImplantationActivity.this.itemSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.leve_ortho, menu);
        return super.onCreateOptionsMenu(menu);
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

        if (this.orthoImpl != null) {
            this.originSpinner.setSelection(
                    a.getPosition(this.orthoImpl.getOrthogonalBase().getOrigin()));
            this.extremitySpinner.setSelection(
                    a.getPosition(this.orthoImpl.getOrthogonalBase().getExtemity()));
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
    }

    private void itemSelected() {
        Point p1 = (Point) this.originSpinner.getSelectedItem();
        Point p2 = (Point) this.extremitySpinner.getSelectedItem();

        if ((p1.getNumber() == 0) || (p2.getNumber() == 0)) {
            this.resetResults();
        } else if (p1.getNumber() == p2.getNumber()) {
            this.resetResults();
            Toast.makeText(this, R.string.error_same_points, Toast.LENGTH_LONG).show();
        }
    }

    private void resetResults() {
        this.calcDistTextView.setText("");
    }
}
