package ch.hgdev.toposuite.calculation.activities.cheminortho;

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
import android.widget.TextView;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.CheminementOrthogonal;
import ch.hgdev.toposuite.calculation.CheminementOrthogonal.Result;
import ch.hgdev.toposuite.calculation.activities.MergePointsDialog;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class CheminementOrthoResultsActivity extends TopoSuiteActivity implements
        MergePointsDialog.MergePointsDialogListener {
    private TextView baseTextView;
    private TextView scaleTextView;
    private TextView fsTextView;
    private TextView fLatTextView;
    private TextView fLonTextView;

    private ListView resultsListView;

    private ArrayListOfResultsAdapter adapter;

    private CheminementOrthogonal cheminOrtho;

    private int saveCounter;
    private int mergeDialogCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_cheminement_ortho_results);

        this.baseTextView = (TextView) this.findViewById(R.id.base);
        this.scaleTextView = (TextView) this.findViewById(R.id.scale_factor);
        this.fsTextView = (TextView) this.findViewById(R.id.fs);
        this.fLatTextView = (TextView) this.findViewById(R.id.flat);
        this.fLonTextView = (TextView) this.findViewById(R.id.flon);

        this.resultsListView = (ListView) this.findViewById(R.id.results_list);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            this.cheminOrtho = (CheminementOrthogonal) bundle.getSerializable(CheminementOrthoActivity.CHEMINEMENT_ORTHO);
            this.cheminOrtho.getResults().clear();
            try {
                this.cheminOrtho.compute();
                StringBuilder builder = new StringBuilder();
                builder.append(this.cheminOrtho.getOrthogonalBase().getOrigin());
                builder.append("-");
                builder.append(this.cheminOrtho.getOrthogonalBase().getExtremity());

                this.baseTextView.setText(builder.toString());
                this.scaleTextView.setText(DisplayUtils.formatDistance(this.cheminOrtho.getScale()));
                this.fsTextView.setText(DisplayUtils.formatDifferences(MathUtils.mToCm(this.cheminOrtho.getFs())));
                this.fLonTextView.setText(DisplayUtils.formatDifferences(MathUtils.mToCm(this.cheminOrtho.getfE())));
                this.fLatTextView.setText(DisplayUtils.formatDifferences(MathUtils.mToCm(this.cheminOrtho.getfN())));

                this.registerForContextMenu(this.resultsListView);
                this.drawList();

                this.saveCounter = this.cheminOrtho.getResults().size() - 1;
            } catch (CalculationException e) {
                Logger.log(Logger.ErrLabel.CALCULATION_COMPUTATION_ERROR, e.getMessage());
                ViewUtils.showToast(this, this.getString(R.string.error_computation_exception));
            }
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_cheminement_ortho_results);
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
                            this.getText(R.string.point_add_success));
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

    private void drawList() {
        this.adapter = new ArrayListOfResultsAdapter(this, R.layout.leve_ortho_results_list_item, this.cheminOrtho.getResults());
        this.resultsListView.setAdapter(this.adapter);
    }

    /**
     * Save all points from the list to the database of points. If a point
     * already exists in the database, it is simply skipped.
     */
    private void savePoints() {
        // reset merge dialog counter
        this.mergeDialogCounter = 0;

        // This loop stops at this.adapter.getCount()-1 because the last point
        // is the extremity and it must not be changed.
        for (int position = 0; position < (this.adapter.getCount() - 1); position++) {
            this.savePoint(position);
        }

        // If the mergeDialogCounter is still 0, it means that no merge dialog
        // has been popped-up so far. And since the merge dialog callback
        // handles the redirection to the points manager itself, it is needed
        // to do it here.
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
        if (SharedResources.getSetOfPoints().find(r.getNumber()) == null) {
            Point point = new Point(
                    r.getNumber(),
                    r.getEast(),
                    r.getNorth(),
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
                    r.getNumber());

            args.putDouble(MergePointsDialog.NEW_EAST,
                    r.getEast());
            args.putDouble(MergePointsDialog.NEW_NORTH,
                    r.getNorth());
            args.putDouble(MergePointsDialog.NEW_ALTITUDE,
                    MathUtils.IGNORE_DOUBLE);

            dialog.setArguments(args);
            dialog.show(this.getSupportFragmentManager(), "MergePointsDialogFragment");

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
                .setIcon(R.drawable.ic_dialog_warning)
                .setPositiveButton(R.string.save_all,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CheminementOrthoResultsActivity.this.savePoints();
                                CheminementOrthoResultsActivity.this.adapter.notifyDataSetChanged();
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
