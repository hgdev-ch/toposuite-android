package ch.hgdev.toposuite.calculation.activities.circularsegmentation;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.CircularSegmentation;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.ViewUtils;

public class CircularSegmentationResultsActivity extends TopoSuiteActivity {
    private double                   circleRadius;
    private ArrayList<Point>         points;

    private ListView                 resultsListView;
    private ArrayListOfPointsAdapter adapter;

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

        Log.e("ARC", String.format("%f", arcLength));
        Log.e("SEGMENT", String.format("%d", numberOfSegments));

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

        this.displayResults();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.circular_segmentation_results, menu);
        return super.onCreateOptionsMenu(menu);
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
}