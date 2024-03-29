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
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.ViewUtils;

public class LimitDisplacementResultsActivity extends TopoSuiteActivity implements MergePointsDialog.MergePointsDialogListener {

    private LimitDisplacement limDispl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_limit_displacement_results);

        TextView limitDisplacementLabelTextView = (TextView) this.findViewById(R.id.limit_displ_results_label);
        TextView pointWestTextView = (TextView) this.findViewById(R.id.point_west);
        TextView pointEastTextView = (TextView) this.findViewById(R.id.point_east);
        TextView distParaSouthTextView = (TextView) this.findViewById(R.id.distance_para_south);
        TextView distLonWestTextView = (TextView) this.findViewById(R.id.distance_lon_west);
        TextView distLonEastTextView = (TextView) this.findViewById(R.id.distance_lon_east);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            this.limDispl = (LimitDisplacement) bundle.getSerializable(LimitDisplacementActivity.LIMIT_DISPLACEMENT);
            try {
                this.limDispl.compute();
            } catch (CalculationException e) {
                Logger.log(Logger.ErrLabel.CALCULATION_COMPUTATION_ERROR, e.getMessage());
                ViewUtils.showToast(this, this.getString(R.string.error_computation_exception));
                return;
            }

            limitDisplacementLabelTextView.setText(String.format(this.getString(
                    R.string.limit_displ_results_label),
                    DisplayUtils.formatSurface(this.limDispl.getSurface())));
            pointWestTextView.setText(
                    DisplayUtils.formatPoint(this, this.limDispl.getNewPointX()));
            pointEastTextView.setText(DisplayUtils.formatPoint(this, this.limDispl.getNewPointY()));
            distParaSouthTextView.setText(DisplayUtils.formatDistance(this.limDispl.getDistanceToSouthLimitAD()));
            distLonWestTextView.setText(DisplayUtils.formatDistance(this.limDispl.getDistanceToWestLimitAX()));
            distLonEastTextView.setText(DisplayUtils.formatDistance(this.limDispl.getDistanceToEastLimitDY()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.action_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_button) {
            this.savePoint(this.limDispl.getNewPointX());
            this.savePoint(this.limDispl.getNewPointY());
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            args.putString(MergePointsDialog.POINT_NUMBER, pt.getNumber());
            args.putDouble(MergePointsDialog.NEW_EAST, pt.getEast());
            args.putDouble(MergePointsDialog.NEW_NORTH, pt.getNorth());
            args.putDouble(MergePointsDialog.NEW_ALTITUDE, pt.getAltitude());

            dialog.setArguments(args);
            dialog.show(this.getSupportFragmentManager(), "MergePointsDialogFragment");

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
