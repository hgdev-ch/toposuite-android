package ch.hgdev.toposuite.calculation.activities.surface;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.Surface;
import ch.hgdev.toposuite.calculation.Surface.PointWithRadius;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class SurfaceActivity extends TopoSuiteActivity implements
        AddPointWithRadiusDialogFragment.AddPointWithRadiusDialogListener {
    private ListView                              pointsListView;
    private EditText                              nameEditText;
    private EditText                              descriptionEditText;
    private TextView                              surfaceTextView;
    private TextView                              perimeterTextView;

    private String                                name;
    private String                                description;
    private double                                surface;
    private double                                perimeter;
    private int                                   vertexNumber;
    private ArrayAdapter<Surface.PointWithRadius> adapter;
    private Surface                               surfaceCalculation;

    /**
     * Position of the calculation in the calculations list. Only used when open
     * from the history.
     */
    private int                                   position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_surface);

        this.position = -1;
        this.vertexNumber = 0;
        this.surface = 0.0;
        this.perimeter = 0.0;

        this.pointsListView = (ListView) this.findViewById(R.id.list_of_points);
        this.nameEditText = (EditText) this.findViewById(R.id.name);
        this.descriptionEditText = (EditText) this.findViewById(R.id.description);
        this.surfaceTextView = (TextView) this.findViewById(R.id.surface);
        this.perimeterTextView = (TextView) this.findViewById(R.id.perimeter);

        this.nameEditText.setHint(
                this.getString(R.string.name) + this.getString(R.string.optional_prths));
        this.descriptionEditText.setHint(
                this.getString(R.string.description) + this.getString(R.string.optional_prths));

        ArrayList<Surface.PointWithRadius> list = new ArrayList<Surface.PointWithRadius>();
        // check if we create a new surface calculation or if we modify an
        // existing one.
        Bundle bundle = this.getIntent().getExtras();
        if ((bundle != null)) {
            this.position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.surfaceCalculation = (Surface) SharedResources.getCalculationsHistory()
                    .get(this.position);
            list = (ArrayList<PointWithRadius>) this.surfaceCalculation.getPoints();
        }

        this.adapter = new ArrayListOfPointsWithRadiusAdapter(this,
                R.layout.points_with_radius_list_item, list);

        this.drawList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.surface, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
        case R.id.add_point_button:
            this.showAddPointDialog();
            return true;
        case R.id.run_calculation_button:
            if (this.checkInputs()) {
                this.runCalculation();
                this.updateResults();
            } else {
                Toast errorToast = Toast.makeText(this,
                        this.getText(R.string.error_three_points_required),
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
     * Do the actual computation.
     */
    private void runCalculation() {
        if (this.nameEditText.length() > 0) {
            this.name = this.nameEditText.getText().toString();
        } else {
            this.name = "";
        }
        if (this.descriptionEditText.length() > 0) {
            this.name = this.descriptionEditText.getText().toString();
        } else {
            this.name = "";
        }
        Surface s = new Surface(this.name, this.description, true);
        for (int i = 0; i < this.adapter.getCount(); i++) {
            s.getPoints().add(this.adapter.getItem(i));
        }

        s.compute();
        this.surface = s.getSurface();
        this.perimeter = s.getPerimeter();
    }

    /**
     * Update the surface and perimeter text view.
     */
    private void updateResults() {
        this.surfaceTextView.setText(DisplayUtils.toString(this.surface));
        this.perimeterTextView.setText(DisplayUtils.toString(this.perimeter));
    }

    /**
     * Check that at least three points have been added.
     * 
     * @return True if input is OK, false otherwise.
     */
    private boolean checkInputs() {
        if (this.adapter.getCount() < 3) {
            return false;
        }
        return true;
    }

    /**
     * Draw the list of points.
     */
    private void drawList() {
        this.pointsListView.setAdapter(this.adapter);
    }

    /**
     * Show a dialog to add a new point, with optional radius.
     */
    private void showAddPointDialog() {
        AddPointWithRadiusDialogFragment dialog = new AddPointWithRadiusDialogFragment();
        dialog.show(this.getFragmentManager(), "AddPointWithRadiusDialogFragment");
    }

    @Override
    public void onDialogAdd(AddPointWithRadiusDialogFragment dialog) {
        this.vertexNumber++;
        Surface.PointWithRadius p = new PointWithRadius(
                dialog.getPoint().getNumber(),
                dialog.getPoint().getEast(),
                dialog.getPoint().getNorth(),
                dialog.getRadius(),
                this.vertexNumber);
        this.adapter.add(p);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogCancel(AddPointWithRadiusDialogFragment dialog) {
        // do nothing actually
    }

}
