package ch.hgdev.toposuite.calculation.activities.surface;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
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
    public static final String                    POINT_WITH_RADIUS_NUMBER_LABEL = "point_with_radius_number";
    public static final String                    RADIUS_LABEL                   = "radius";
    private static final String                   POINT_WITH_RADIUS_LABEL        = "points_with_radius";
    private static final String                   SURFACE_NAME_LABEL             = "surface_name";
    private static final String                   SURFACE_DESCRIPTION_LABEL      = "surface_description";
    private static final String                   PERIMETER_LABEL                = "perimeter_label";
    private static final String                   SURFACE_LABEL                  = "surface_label";
    private ListView                              pointsListView;
    private EditText                              nameEditText;
    private EditText                              descriptionEditText;
    private TextView                              surfaceTextView;
    private TextView                              perimeterTextView;

    private String                                name;
    private String                                description;
    private double                                surface;
    private double                                perimeter;
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

        // check if we create a new surface calculation or if we modify an
        // existing one.
        Bundle bundle = this.getIntent().getExtras();
        if ((bundle != null)) {
            this.position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.surfaceCalculation = (Surface) SharedResources.getCalculationsHistory()
                    .get(this.position);
            if (this.surfaceCalculation != null) {
                this.name = this.surfaceCalculation.getSurfaceName();
                this.description = this.surfaceCalculation.getSurfaceDescription();
                this.surface = this.surfaceCalculation.getSurface();
                this.perimeter = this.surfaceCalculation.getPerimeter();
            }
        } else {
            this.surfaceCalculation = new Surface(this.name, this.description, true);
        }

        this.adapter = new ArrayListOfPointsWithRadiusAdapter(this,
                R.layout.points_with_radius_list_item,
                (ArrayList<Surface.PointWithRadius>) this.surfaceCalculation.getPoints());

        this.drawList();

        this.registerForContextMenu(this.pointsListView);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!this.name.isEmpty()) {
            this.nameEditText.setText(this.name);
        }
        if (!this.description.isEmpty()) {
            this.descriptionEditText.setText(this.description);
        }
        if (!MathUtils.isZero(this.surface) && !MathUtils.isZero(this.perimeter)) {
            this.updateResults();
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_surface);
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
        inflater.inflate(R.menu.surface_points_list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
        case R.id.edit_point:
            this.showEditPointDialog(info.position);
            return true;
        case R.id.delete_point:
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

        JSONArray json = new JSONArray();
        for (int i = 0; i < this.adapter.getCount(); i++) {
            json.put(this.adapter.getItem(i).toJSONObject());
        }
        outState.putString(SurfaceActivity.POINT_WITH_RADIUS_LABEL, json.toString());
        outState.putString(SurfaceActivity.SURFACE_DESCRIPTION_LABEL, this.description);
        outState.putString(SurfaceActivity.SURFACE_NAME_LABEL, this.name);
        outState.putDouble(SurfaceActivity.PERIMETER_LABEL, this.perimeter);
        outState.putDouble(SurfaceActivity.SURFACE_LABEL, this.surface);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            this.adapter.clear();
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(
                        savedInstanceState.getString(SurfaceActivity.POINT_WITH_RADIUS_LABEL));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = (JSONObject) jsonArray.get(i);
                    PointWithRadius p = PointWithRadius.getPointFromJSON(json.toString());
                    this.adapter.add(p);
                }
            } catch (JSONException e) {
                Logger.log(Logger.ErrLabel.PARSE_ERROR,
                        "SurfaceActivity: cannot restore saved instance.");
            }
            this.name = savedInstanceState.getString(SurfaceActivity.SURFACE_NAME_LABEL);
            this.description = savedInstanceState
                    .getString(SurfaceActivity.SURFACE_DESCRIPTION_LABEL);
            this.perimeter = savedInstanceState.getDouble(SurfaceActivity.PERIMETER_LABEL);
            this.surface = savedInstanceState.getDouble(SurfaceActivity.SURFACE_LABEL);
            this.drawList();
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
            this.description = this.descriptionEditText.getText().toString();
        } else {
            this.description = "";
        }

        this.surfaceCalculation.setSurfaceName(this.name);
        this.surfaceCalculation.setSurfaceDescription(this.description);

        this.surfaceCalculation.compute();
        this.surface = this.surfaceCalculation.getSurface();
        this.perimeter = this.surfaceCalculation.getPerimeter();
    }

    /**
     * Update the surface and perimeter text view.
     */
    private void updateResults() {
        this.surfaceTextView.setText(DisplayUtils.formatSurface(this.surface));
        this.perimeterTextView.setText(DisplayUtils.formatDistance(this.perimeter));
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

    /**
     * Show a dialog to edit a point.
     *
     * @param position
     *            Position of the point in the list of points.
     */
    private void showEditPointDialog(int position) {
        EditPointWithRadiusDialogFragment dialog = new EditPointWithRadiusDialogFragment(
                this.surfaceCalculation);

        this.position = position;
        Surface.PointWithRadius p = this.adapter.getItem(position);
        Bundle args = new Bundle();
        args.putString(SurfaceActivity.POINT_WITH_RADIUS_NUMBER_LABEL, p.getNumber());
        args.putDouble(SurfaceActivity.RADIUS_LABEL, p.getRadius());

        dialog.setArguments(args);
        dialog.show(this.getFragmentManager(), "EditPointWithRadiusDialogFragment");
    }

    @Override
    public void onDialogAdd(AddPointWithRadiusDialogFragment dialog) {
        Surface.PointWithRadius p = new PointWithRadius(
                dialog.getPoint().getNumber(),
                dialog.getPoint().getEast(),
                dialog.getPoint().getNorth(),
                dialog.getRadius(),
                this.surfaceCalculation.getPoints().size() + 1);
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
        Surface.PointWithRadius p = this.adapter.getItem(this.position);
        p.setNumber(dialog.getPoint().getNumber());
        p.setEast(dialog.getPoint().getEast());
        p.setNorth(dialog.getPoint().getNorth());
        p.setRadius(dialog.getRadius());

        if (!dialog.getPositionAfter().isEmpty()) {
            ArrayList<Surface.PointWithRadius> newPoints =
                    new ArrayList<Surface.PointWithRadius>();

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

            this.surfaceCalculation.setPoints(newPoints);
        }

        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogCancel(EditPointWithRadiusDialogFragment dialog) {
        // do nothing actually
    }

}
