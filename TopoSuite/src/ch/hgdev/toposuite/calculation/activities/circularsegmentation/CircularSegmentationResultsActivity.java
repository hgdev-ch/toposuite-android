package ch.hgdev.toposuite.calculation.activities.circularsegmentation;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.CircularSegmentation;
import ch.hgdev.toposuite.calculation.activities.MergePointsDialog;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.ViewUtils;

public class CircularSegmentationResultsActivity extends TopoSuiteActivity implements
        MergePointsDialog.MergePointsDialogListener {
    private double                   circleRadius;
    private ArrayList<Point>         points;

    private ListView                 resultsListView;
    private ArrayListOfPointsAdapter adapter;

    private int                      saveCounter;
    private int                      mergeDialogCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_circular_segmentation_results);

        this.resultsListView = (ListView) this.findViewById(R.id.list_of_points);

        Bundle bundle = this.getIntent().getExtras();

        Point center = SharedResources.getSetOfPoints().find(
                bundle.getInt(CircularSegmentationActivity.CIRCLE_CENTER_POINT_NUMBER));
        Point start = SharedResources.getSetOfPoints().find(
                bundle.getInt(CircularSegmentationActivity.CIRCLE_START_POINT_NUMBER));
        Point end = SharedResources.getSetOfPoints().find(
                bundle.getInt(CircularSegmentationActivity.CIRCLE_END_POINT_NUMBER));
        int numberOfSegments = bundle.getInt(CircularSegmentationActivity.NUMBER_OF_SEGMENTS);
        double arcLength = bundle.getDouble(CircularSegmentationActivity.ARC_LENGTH);

        int resultPointNumber = bundle.getInt(
                CircularSegmentationActivity.FIRST_RESULT_POINT_NUMBER);

        CircularSegmentation circularSegmentation = new CircularSegmentation();
        try {
            circularSegmentation.initAttributes(center, start, end, numberOfSegments, arcLength);
            circularSegmentation.compute();
        } catch (IllegalArgumentException e) {
            ViewUtils.showToast(this, e.toString());
        } catch (CalculationException e) {
            ViewUtils.showToast(this, e.toString());
        }

        this.circleRadius = circularSegmentation.getCircleRadius();
        this.points = (ArrayList<Point>) circularSegmentation.getPoints();

        // set points numbers
        for (Point p : this.points) {
            p.setNumber(resultPointNumber);
            resultPointNumber++;
        }

        this.saveCounter = this.points.size();

        this.displayResults();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.circular_segmentation_results, menu);
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
     * @param position
     *            Position of the point in the list of points.
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
            args.putInt(
                    MergePointsDialog.POINT_NUMBER,
                    r.getNumber());

            args.putDouble(MergePointsDialog.NEW_EAST,
                    r.getEast());
            args.putDouble(MergePointsDialog.NEW_NORTH,
                    r.getNorth());
            args.putDouble(MergePointsDialog.NEW_ALTITUDE,
                    r.getAltitude());

            dialog.setArguments(args);
            dialog.show(this.getFragmentManager(), "MergePointsDialogFragment");
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
                .setIcon(android.R.drawable.ic_dialog_alert)
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