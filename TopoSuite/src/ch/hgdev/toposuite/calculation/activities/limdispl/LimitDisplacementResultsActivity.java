package ch.hgdev.toposuite.calculation.activities.limdispl;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.LimitDisplacement;
import ch.hgdev.toposuite.calculation.activities.MergePointsDialog;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class LimitDisplacementResultsActivity extends TopoSuiteActivity implements
        MergePointsDialog.MergePointsDialogListener {
    private TextView          limitDisplacementLabelTextView;
    private TextView          pointWestTextView;
    private TextView          pointEastTextView;
    private TextView          distParaSouthTextView;
    private TextView          distLonWestTextView;
    private TextView          distLonEastTextView;

    private LimitDisplacement limDispl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_limit_displacement_results);

        this.limitDisplacementLabelTextView = (TextView) this.findViewById(
                R.id.limit_displ_results_label);
        this.pointWestTextView = (TextView) this.findViewById(R.id.point_west);
        this.pointEastTextView = (TextView) this.findViewById(R.id.point_east);
        this.distParaSouthTextView = (TextView) this.findViewById(
                R.id.distance_para_south);
        this.distLonWestTextView = (TextView) this.findViewById(
                R.id.distance_lon_west);
        this.distLonEastTextView = (TextView) this.findViewById(
                R.id.distance_lon_east);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            int position = bundle.getInt(
                    LimitDisplacementActivity.LIMIT_DISPLACEMENT_POSITION);
            this.limDispl = (LimitDisplacement) SharedResources.getCalculationsHistory()
                    .get(position);
            try {
                this.limDispl.compute();
            } catch (CalculationException e) {
                ViewUtils.showToast(this, e.getMessage());
                return;
            }

            this.limitDisplacementLabelTextView.setText(
                    String.format(this.getString(
                            R.string.limit_displ_results_label),
                            DisplayUtils.formatSurface(
                                    this.limDispl.getSurface())));
            this.pointWestTextView.setText(
                    DisplayUtils.formatPoint(this, this.limDispl.getNewPointX()));
            this.pointEastTextView.setText(
                    DisplayUtils.formatPoint(this, this.limDispl.getNewPointY()));
            this.distParaSouthTextView.setText(DisplayUtils.formatDistance(
                    this.limDispl.getDistanceToSouthLimitAD()));
            this.distLonWestTextView.setText(DisplayUtils.formatDistance(
                    this.limDispl.getDistanceToWestLimitAX()));
            this.distLonEastTextView.setText(DisplayUtils.formatDistance(
                    this.limDispl.getDistanceToEastLimitDY()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.limit_displacement_results, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
        case R.id.save_points:
            this.savePoint(this.limDispl.getNewPointX());
            this.savePoint(this.limDispl.getNewPointY());
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(
                R.string.title_activity_limit_displacement_results);
    }

    private boolean savePoint(Point pt) {
        if (SharedResources.getSetOfPoints().find(pt.getNumber()) == null) {
            SharedResources.getSetOfPoints().add(pt);
            ViewUtils.showToast(this, this.getString(R.string.point_add_success));
            return true;
        } else {
            // this point already exists
            MergePointsDialog dialog = new MergePointsDialog();

            Bundle args = new Bundle();
            args.putString(
                    MergePointsDialog.POINT_NUMBER,
                    pt.getNumber());
            args.putDouble(MergePointsDialog.NEW_EAST,
                    pt.getEast());
            args.putDouble(MergePointsDialog.NEW_NORTH,
                    pt.getNorth());
            args.putDouble(MergePointsDialog.NEW_ALTITUDE,
                    pt.getAltitude());

            dialog.setArguments(args);
            dialog.show(this.getFragmentManager(), "MergePointsDialogFragment");

            return false;
        }
    }

    @Override
    public void onMergePointsDialogSuccess(String message) {
        ViewUtils.showToast(this, message);
    }

    @Override
    public void onMergePointsDialogError(String message) {
        ViewUtils.showToast(this, message);
    }
}
