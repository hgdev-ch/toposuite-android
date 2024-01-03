package ch.hgdev.toposuite.calculation.activities.polarsurvey;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.PolarSurvey;
import ch.hgdev.toposuite.calculation.PolarSurvey.Result;
import ch.hgdev.toposuite.calculation.activities.MergePointsDialog;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.ViewUtils;

public class PolarSurveyResultsActivity extends TopoSuiteActivity implements
        MergePointsDialog.MergePointsDialogListener {
    private static final String POLAR_SURVEY_RESULTS_ACTIVITY = "PolarSurveyResultsActivity: ";

    private ListView resultsListView;

    private PolarSurvey polarSurvey;
    private ArrayListOfResultsAdapter adapter;

    private int saveCounter;
    private int mergeDialogCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_polar_survey_results);

        this.resultsListView = (ListView) this.findViewById(R.id.results_list);
        this.registerForContextMenu(this.resultsListView);

        Bundle bundle = this.getIntent().getExtras();
        this.polarSurvey = (PolarSurvey) bundle.getSerializable(PolarSurveyActivity.POLAR_SURVEY_CALCULATION);

        try {
            this.polarSurvey.compute();
            this.saveCounter = this.polarSurvey.getResults().size();
            this.displayResults();
        } catch (CalculationException e) {
            Logger.log(Logger.ErrLabel.CALCULATION_COMPUTATION_ERROR, e.getMessage());
            ViewUtils.showToast(this, this.getString(R.string.error_computation_exception));
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_polar_survey_results);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.action_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.save_button:
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
        inflater.inflate(R.menu.context_list_row_delete_save, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.save_button:
                if (this.savePoint(info.position)) {
                    ViewUtils.showToast(this,
                            this.getString(R.string.point_add_success));
                }
                this.adapter.notifyDataSetChanged();
                return true;
            case R.id.delete_button:
                this.adapter.remove(this.adapter.getItem(info.position));
                this.adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Display the list of resulting points.
     */
    private void displayResults() {
        this.adapter = new ArrayListOfResultsAdapter(this, R.layout.polar_survey_results_list_item,
                this.polarSurvey.getResults());
        this.resultsListView.setAdapter(this.adapter);
    }

    /**
     * Save all points from the list to the database of points. If a point
     * already exists in the database, it is simply skipped.
     */
    private void savePoints() {
        // rester the merge dialog counter
        this.mergeDialogCounter = 0;

        for (int position = this.adapter.getCount() - 1; position >= 0; position--) {
            this.savePoint(position);
        }

        // If the mergeDialogCounter is still 0, it means that no merge dialog
        // has been popped-up so far. And since the merge dialog callback
        // handles
        // the redirection to the points manager itself, it is needed to do it
        // here.
        if (this.mergeDialogCounter == 0) {
            ViewUtils.redirectToPointsManagerActivity(this);
        }
    }

    /**
     * Save a point to the database of points.
     *
     * @param position Position of the point in the list of points.
     * @return True if it was a success, false otherwise.
     */
    private boolean savePoint(int position) {
        Result r = this.adapter.getItem(position);
        if (SharedResources.getSetOfPoints().find(r.getDeterminationNumber()) == null) {
            Point point = new Point(
                    r.getDeterminationNumber(),
                    r.getEast(),
                    r.getNorth(),
                    r.getAltitude(),
                    false);
            SharedResources.getSetOfPoints().add(point);
        } else {
            // this point already exists
            ++this.mergeDialogCounter;

            MergePointsDialog dialog = new MergePointsDialog();

            Bundle args = new Bundle();
            args.putString(
                    MergePointsDialog.POINT_NUMBER,
                    r.getDeterminationNumber());

            args.putDouble(MergePointsDialog.NEW_EAST,
                    r.getEast());
            args.putDouble(MergePointsDialog.NEW_NORTH,
                    r.getNorth());
            args.putDouble(MergePointsDialog.NEW_ALTITUDE,
                    r.getAltitude());

            dialog.setArguments(args);
            dialog.show(this.getSupportFragmentManager(), "MergePointsDialogFragment");
        }

        return true;
    }

    /**
     * Pop up a confirmation dialog and save all points on approval.
     */
    private void saveAllPoints() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.save_points)
                .setMessage(R.string.save_all_points)
                .setIcon(R.drawable.ic_dialog_warning)
                .setPositiveButton(R.string.save_all,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PolarSurveyResultsActivity.this.savePoints();
                                PolarSurveyResultsActivity.this.adapter.notifyDataSetChanged();
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