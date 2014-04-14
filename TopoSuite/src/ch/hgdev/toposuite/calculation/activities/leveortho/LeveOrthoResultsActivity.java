package ch.hgdev.toposuite.calculation.activities.leveortho;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.LeveOrthogonal;
import ch.hgdev.toposuite.calculation.LeveOrthogonal.Measure;
import ch.hgdev.toposuite.calculation.activities.MergePointsDialog;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class LeveOrthoResultsActivity extends TopoSuiteActivity implements
        MergePointsDialog.MergePointsDialogListener {

    private TextView                  baseTextView;
    private ListView                  resultsListView;

    private ArrayListOfResultsAdapter adapter;

    private LeveOrthogonal            leveOrtho;

    private int                       saveCounter;
    private int                       mergeDialogCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_leve_ortho_results);

        this.baseTextView = (TextView) this.findViewById(R.id.base);
        this.resultsListView = (ListView) this.findViewById(R.id.results_list);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            int position = bundle.getInt(LeveOrthoActivity.LEVE_ORTHO_POSITION);
            this.leveOrtho = (LeveOrthogonal) SharedResources.getCalculationsHistory().get(
                    position);
            this.leveOrtho.getResults().clear();
            this.leveOrtho.compute();

            StringBuilder builder = new StringBuilder();
            builder.append(this.leveOrtho.getOrthogonalBase().getOrigin());
            builder.append("-");
            builder.append(this.leveOrtho.getOrthogonalBase().getExtremity());

            this.baseTextView.setText(builder.toString());
            this.registerForContextMenu(this.resultsListView);
            this.drawList();

            this.saveCounter = this.leveOrtho.getResults().size();
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_leve_ortho_results);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.calculation_results_points_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
        case R.id.save_points:
            this.saveAllPoints();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.calculations_points_list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
        case R.id.save_point:
            if (this.savePoint(info.position)) {
                ViewUtils.showToast(this,
                        this.getString(R.string.point_add_success));
            }
            this.adapter.notifyDataSetChanged();
            return true;
        case R.id.delete_point:
            this.adapter.remove(this.adapter.getItem(info.position));
            this.adapter.notifyDataSetChanged();
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    /**
     * Draw the list of results.
     */
    private void drawList() {
        this.adapter = new ArrayListOfResultsAdapter(this, R.layout.leve_ortho_results_list_item,
                this.leveOrtho.getResults());
        this.resultsListView.setAdapter(this.adapter);
    }

    /**
     * Save all points from the list to the database of points. If a point
     * already exists in the database, it is simply skipped.
     */
    private void savePoints() {
        // reset the merge dialog counter
        this.mergeDialogCounter = 0;

        for (int position = 0; position < this.adapter.getCount(); position++) {
            this.savePoint(position);
        }

        // If the mergeDialogCounter is still 0, it means that no merge dialog
        // has been popped-up so far. And since the merge dialog callback
        // handles the redirection to the points manager itself, it is needed to
        // do it here.
        if (this.mergeDialogCounter == 0) {
            ViewUtils.redirectToPointsManagerActivity(this);
        }
    }

    /**
     * Save a point to the database of points.
     * 
     * @param position
     *            Position of the point in the list of points.
     * @return True if it was a success, false otherwise.
     */
    private boolean savePoint(int position) {
        Measure m = this.adapter.getItem(position);
        if (SharedResources.getSetOfPoints().find(m.getNumber()) == null) {
            Point point = new Point(
                    m.getNumber(),
                    m.getAbscissa(),
                    m.getOrdinate(),
                    MathUtils.IGNORE_DOUBLE,
                    false);
            SharedResources.getSetOfPoints().add(point);
            return true;
        } else {
            // this point already exists
            ++this.mergeDialogCounter;

            MergePointsDialog dialog = new MergePointsDialog();

            Bundle args = new Bundle();
            args.putString(
                    MergePointsDialog.POINT_NUMBER,
                    m.getNumber());

            args.putDouble(MergePointsDialog.NEW_EAST,
                    m.getAbscissa());
            args.putDouble(MergePointsDialog.NEW_NORTH,
                    m.getOrdinate());
            args.putDouble(MergePointsDialog.NEW_ALTITUDE,
                    MathUtils.IGNORE_DOUBLE);

            dialog.setArguments(args);
            dialog.show(this.getFragmentManager(), "MergePointsDialogFragment");

            return false;
        }
    }

    /**
     * Pop up a confirmation dialog and save all points on approval.
     */
    private void saveAllPoints() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.save_points)
                .setMessage(R.string.save_all_points)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.save_all,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LeveOrthoResultsActivity.this.savePoints();
                                LeveOrthoResultsActivity.this.adapter.notifyDataSetChanged();
                            }
                        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        builder.create().show();
    }

    @Override
    public void onMergePointsDialogSuccess(String message) {
        ViewUtils.showToast(this, message);
        --this.saveCounter;
        if (this.saveCounter == 0) {
            ViewUtils.redirectToPointsManagerActivity(this);
        }
    }

    @Override
    public void onMergePointsDialogError(String message) {
        ViewUtils.showToast(this, message);
    }
}