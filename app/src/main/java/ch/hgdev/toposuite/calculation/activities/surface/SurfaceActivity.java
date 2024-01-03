package ch.hgdev.toposuite.calculation.activities.surface;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.Surface;
import ch.hgdev.toposuite.calculation.Surface.PointWithRadius;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class SurfaceActivity extends TopoSuiteActivity implements
        AddPointWithRadiusDialogFragment.AddPointWithRadiusDialogListener,
        EditPointWithRadiusDialogFragment.EditPointWithRadiusDialogListener {
    public static final String POINT_POSITION_LABEL = "point_position";
    public static final String POINT_WITH_RADIUS_NUMBER_LABEL = "point_with_radius_number";
    public static final String RADIUS_LABEL = "radius";
    public static final String SURFACE_CALCULATION = "surface_calculation";
    public static final String POINT_WITH_RADIUS_LABEL = "points_with_radius";

    private ListView pointsListView;
    private FloatingActionButton addButton;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private TextView surfaceTextView;
    private TextView perimeterTextView;

    private String name;
    private String description;
    private double surface;
    private double perimeter;
    private ArrayListOfPointsWithRadiusAdapter adapter;
    private Surface surfaceCalculation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_surface);

        this.surface = MathUtils.IGNORE_DOUBLE;
        this.perimeter = MathUtils.IGNORE_DOUBLE;
        this.name = "";
        this.description = "";

        this.pointsListView = (ListView) this.findViewById(R.id.list_of_points);
        this.nameEditText = (EditText) this.findViewById(R.id.name);
        this.descriptionEditText = (EditText) this.findViewById(R.id.description);
        this.surfaceTextView = (TextView) this.findViewById(R.id.surface);
        this.perimeterTextView = (TextView) this.findViewById(R.id.perimeter);

        this.nameEditText.setHint(
                this.getString(R.string.name) + this.getString(R.string.optional_prths));
        this.descriptionEditText.setHint(
                this.getString(R.string.description) + this.getString(R.string.optional_prths));

        this.addButton = (FloatingActionButton) this.findViewById(R.id.add_point_button);

        this.pointsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SurfaceActivity.this.showEditPointDialog(position);
            }
        });

        this.addButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  SurfaceActivity.this.showAddPointDialog();
                                              }
                                          }
        );

        // check if we create a new surface calculation or if we modify an
        // existing one.
        Bundle bundle = this.getIntent().getExtras();
        if ((bundle != null)) {
            int position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.surfaceCalculation = (Surface) SharedResources.getCalculationsHistory().get(position);
            this.name = this.surfaceCalculation.getSurfaceName();
            this.description = this.surfaceCalculation.getSurfaceDescription();
            this.surface = this.surfaceCalculation.getSurface();
            this.perimeter = this.surfaceCalculation.getPerimeter();
        } else {
            this.surfaceCalculation = new Surface(this.name, this.description, true);
        }

        this.adapter = new ArrayListOfPointsWithRadiusAdapter(this,
                R.layout.points_with_radius_list_item,
                new ArrayList<>(this.surfaceCalculation.getPoints()));

        this.registerForContextMenu(this.pointsListView);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.name == null) {
            this.name = "";
        }
        if (this.description == null) {
            this.description = "";
        }
        if (!this.name.isEmpty()) {
            this.nameEditText.setText(this.name);
        }
        if (!this.description.isEmpty()) {
            this.descriptionEditText.setText(this.description);
        }
        if (!MathUtils.isZero(this.surface) && !MathUtils.isZero(this.perimeter)) {
            this.updateResults();
        }

        this.drawList();
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_surface);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.action_run_calculation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.run_calculation_button:
                if (this.checkInputs()) {
                    this.runCalculation();
                    this.updateResults();
                } else {
                    ViewUtils.showToast(
                            this, this.getText(R.string.error_three_points_required));
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

                // update the vertices number
                for (int i = info.position; i < this.adapter.getCount(); i++) {
                    this.adapter.getItem(i).setVertexNumber(
                            this.adapter.getItem(i).getVertexNumber() - 1);
                }
                this.adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(
                SurfaceActivity.POINT_WITH_RADIUS_LABEL,
                this.adapter.getPoints());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            ArrayList<Surface.PointWithRadius> points = (ArrayList<PointWithRadius>) savedInstanceState.getSerializable(
                    SurfaceActivity.POINT_WITH_RADIUS_LABEL);
            this.adapter.clear();
            this.adapter.addAll(points);
            this.drawList();
        }
    }

    /**
     * Do the actual computation.
     */
    private void runCalculation() {
        this.name = ViewUtils.readString(this.nameEditText);
        this.description = ViewUtils.readString(this.descriptionEditText);

        this.surfaceCalculation.setSurfaceName(this.name);
        this.surfaceCalculation.setSurfaceDescription(this.description);
        this.surfaceCalculation.getPoints().clear();
        this.surfaceCalculation.getPoints().addAll(this.adapter.getPoints());

        try {
            this.surfaceCalculation.compute();
            this.surface = this.surfaceCalculation.getSurface();
            this.perimeter = this.surfaceCalculation.getPerimeter();
        } catch (CalculationException e) {
            Logger.log(Logger.ErrLabel.CALCULATION_COMPUTATION_ERROR, e.getMessage());
            ViewUtils.showToast(this, this.getString(R.string.error_computation_exception));
        }
    }

    /**
     * Update the surface and perimeter text view.
     */
    private void updateResults() {
        if (MathUtils.isIgnorable(this.surface) || MathUtils.isIgnorable(this.perimeter)) {
            ViewUtils.showToast(this, this.getString(R.string.error_impossible_calculation));
        }
        this.surfaceTextView.setText(DisplayUtils.formatSurface(this.surface));
        this.perimeterTextView.setText(DisplayUtils.formatDistance(this.perimeter));
    }

    /**
     * Check that at least three points have been added.
     *
     * @return True if input is OK, false otherwise.
     */
    private boolean checkInputs() {
        return this.adapter.getCount() > 2;
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
        dialog.show(this.getSupportFragmentManager(), "AddPointWithRadiusDialogFragment");
    }

    /**
     * Show a dialog to edit a point.
     *
     * @param position Position of the point in the list of points.
     */
    private void showEditPointDialog(int position) {
        EditPointWithRadiusDialogFragment dialog = new EditPointWithRadiusDialogFragment();

        Surface.PointWithRadius p = this.adapter.getItem(position);
        Bundle args = new Bundle();
        args.putInt(SurfaceActivity.POINT_POSITION_LABEL, position);
        args.putString(SurfaceActivity.POINT_WITH_RADIUS_NUMBER_LABEL, p.getNumber());
        args.putDouble(SurfaceActivity.RADIUS_LABEL, p.getRadius());
        args.putSerializable(SurfaceActivity.POINT_WITH_RADIUS_LABEL, this.adapter.getPoints());

        dialog.setArguments(args);
        dialog.show(this.getSupportFragmentManager(), "EditPointWithRadiusDialogFragment");
    }

    @Override
    public void onDialogAdd(AddPointWithRadiusDialogFragment dialog) {
        Surface.PointWithRadius p = new PointWithRadius(
                dialog.getPoint().getNumber(),
                dialog.getPoint().getEast(),
                dialog.getPoint().getNorth(),
                dialog.getRadius(),
                this.adapter.getPoints().size() + 1);
        this.adapter.add(p);
        this.adapter.notifyDataSetChanged();
        this.showAddPointDialog();
    }

    @Override
    public void onDialogCancel(AddPointWithRadiusDialogFragment dialog) {
        // do nothing actually
    }

    @Override
    public void onDialogEdit(EditPointWithRadiusDialogFragment dialog) {
        Surface.PointWithRadius p = this.adapter.getItem(dialog.getPosition());
        p.setNumber(dialog.getPoint().getNumber());
        p.setEast(dialog.getPoint().getEast());
        p.setNorth(dialog.getPoint().getNorth());
        p.setRadius(dialog.getRadius());

        if (!dialog.getPositionAfter().isEmpty()) {
            ArrayList<Surface.PointWithRadius> newPoints = new ArrayList<>();

            int vertexNumber = 1;
            for (int i = 0; i < this.adapter.getCount(); i++) {
                Surface.PointWithRadius currPt = this.adapter.getItem(i);
                if (currPt == p) {
                    continue;
                }

                newPoints.add(currPt);
                currPt.setVertexNumber(vertexNumber);

                if (currPt.getNumber().equals(dialog.getPositionAfter())) {
                    newPoints.add(p);
                    vertexNumber++;
                    p.setVertexNumber(vertexNumber);
                }

                vertexNumber++;
            }

            this.adapter.clear();
            this.adapter.addAll(newPoints);
        }

        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogCancel(EditPointWithRadiusDialogFragment dialog) {
        // do nothing actually
    }

}
