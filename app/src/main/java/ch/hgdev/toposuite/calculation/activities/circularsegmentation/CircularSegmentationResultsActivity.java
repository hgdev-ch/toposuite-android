package ch.hgdev.toposuite.calculation.activities.circularsegmentation;

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

import java.util.ArrayList;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.CircularSegmentation;
import ch.hgdev.toposuite.calculation.activities.MergePointsDialog;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.StringUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class CircularSegmentationResultsActivity extends TopoSuiteActivity implements MergePointsDialog.MergePointsDialogListener {
    private ArrayList<Point> points;

    private ListView resultsListView;
    private ArrayListOfPointsAdapter adapter;

    private int saveCounter;
    private int mergeDialogCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_circular_segmentation_results);

        TextView circleRadiusTextView = (TextView) this.findViewById(R.id.circle_radius);
        this.resultsListView = (ListView) this.findViewById(R.id.list_of_points);

        Bundle bundle = this.getIntent().getExtras();

        Point center = SharedResources.getSetOfPoints().find(bundle.getString(CircularSegmentationActivity.CIRCLE_CENTER_POINT_NUMBER));
        Point start = SharedResources.getSetOfPoints().find(bundle.getString(CircularSegmentationActivity.CIRCLE_START_POINT_NUMBER));
        Point end = SharedResources.getSetOfPoints().find(bundle.getString(CircularSegmentationActivity.CIRCLE_END_POINT_NUMBER));
        int numberOfSegments = bundle.getInt(CircularSegmentationActivity.NUMBER_OF_SEGMENTS);
        double arcLength = bundle.getDouble(CircularSegmentationActivity.ARC_LENGTH);

        String resultPointNumber = bundle.getString(CircularSegmentationActivity.FIRST_RESULT_POINT_NUMBER);

        CircularSegmentation circularSegmentation = new CircularSegmentation();
        try {
            circularSegmentation.initAttributes(center, start, end, numberOfSegments, arcLength);
            circularSegmentation.compute();
        } catch (Exception e) { // illegal argument or calculation exception
            Logger.log(Logger.WarnLabel.CALCULATION_IMPOSSIBLE, e.getMessage());
            ViewUtils.showToast(this, this.getString(R.string.error_impossible_calculation));
        }

        circleRadiusTextView.setText(DisplayUtils.formatDistance(circularSegmentation.getCircleRadius()));

        this.points = (ArrayList<Point>) circularSegmentation.getPoints();

        // set points numbers
        for (Point p : this.points) {
            p.setNumber(resultPointNumber);
            resultPointNumber = StringUtils.incrementAsNumber(resultPointNumber);
        }

        this.saveCounter = this.points.size();

        this.displayResults();

        this.registerForContextMenu(this.resultsListView);
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
        inflater.inflate(R.menu.context_list_row_save, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.save_button:
                this.savePoint(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_circular_segmentation_results);
    }

    /**
     * Display the list of resulting points.
     */
    private void displayResults() {
        this.adapter = new ArrayListOfPointsAdapter(this, R.layout.points_list_item, this.points);
        this.resultsListView.setAdapter(this.adapter);
    }

    /**
     * Save all points from the list to the database of points. If a point
     * already exists in the database, it is simply skipped.
     */
    private void savePoints() {
        // rester the merge dialog counter
        this.mergeDialogCounter = 0;

        for (int position = 0; position < this.adapter.getCount(); position++) {
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
        Point r = this.adapter.getItem(position);
        if (SharedResources.getSetOfPoints().find(r.getNumber()) == null) {
            Point point = new Point(
                    r.getNumber(),
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
            args.putString(MergePointsDialog.POINT_NUMBER, r.getNumber());

            args.putDouble(MergePointsDialog.NEW_EAST, r.getEast());
            args.putDouble(MergePointsDialog.NEW_NORTH, r.getNorth());
            args.putDouble(MergePointsDialog.NEW_ALTITUDE, r.getAltitude());

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
                                CircularSegmentationResultsActivity.this.savePoints();
                                CircularSegmentationResultsActivity.this.adapter
                                        .notifyDataSetChanged();
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