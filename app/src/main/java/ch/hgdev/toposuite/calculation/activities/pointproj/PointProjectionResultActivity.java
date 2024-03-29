package ch.hgdev.toposuite.calculation.activities.pointproj;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.PointProjectionOnALine;
import ch.hgdev.toposuite.calculation.activities.MergePointsDialog;
import ch.hgdev.toposuite.dao.PointsDataSource;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.ViewUtils;

public class PointProjectionResultActivity extends TopoSuiteActivity implements
        MergePointsDialog.MergePointsDialogListener {

    private PointProjectionOnALine ppoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_point_projection_result);

        TextView pointNumberTextView = (TextView) this.findViewById(R.id.point_number);
        TextView eastTextView = (TextView) this.findViewById(R.id.east);
        TextView northTextView = (TextView) this.findViewById(R.id.north);

        TextView projPointLineDistTextView = (TextView) this.findViewById(
                R.id.distance_projpoint_line);
        TextView projPointP1DistTextView = (TextView) this.findViewById(
                R.id.distance_projpoint_point_1);
        TextView projPointP2DistTextView = (TextView) this.findViewById(
                R.id.distance_projpoint_point_2);

        Bundle bundle = this.getIntent().getExtras();
        this.ppoal = (PointProjectionOnALine) bundle.getSerializable(PointProjectionActivity.POINT_PROJ_CALCULATION);
        try {
            this.ppoal.compute();
        } catch (CalculationException e) {
            Logger.log(Logger.ErrLabel.CALCULATION_COMPUTATION_ERROR, e.getMessage());
            ViewUtils.showToast(this, this.getString(R.string.error_computation_exception));
        }

        pointNumberTextView.setText(this.ppoal.getNumber());
        eastTextView.setText(DisplayUtils.formatCoordinate(
                this.ppoal.getProjPt().getEast()));
        northTextView.setText(DisplayUtils.formatCoordinate(
                this.ppoal.getProjPt().getNorth()));

        projPointLineDistTextView.setText(DisplayUtils.formatDistance(
                this.ppoal.getDistPtToLine()));
        projPointP1DistTextView.setText(DisplayUtils.formatDistance(
                this.ppoal.getDistPtToP1()));
        if (!this.ppoal.getP2().getNumber().isEmpty()) {
            projPointP2DistTextView.setText(DisplayUtils.formatDistance(
                    this.ppoal.getDistPtToP2()));
        } else {
            projPointP2DistTextView.setText(R.string.no_value);
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_point_projection_result);
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
            if (SharedResources.getSetOfPoints().find(
                    this.ppoal.getProjPt().getNumber()) == null) {
                SharedResources.getSetOfPoints().add(this.ppoal.getProjPt());
                this.ppoal.getProjPt().registerDAO(PointsDataSource.getInstance());

                ViewUtils.showToast(this, this.getString(R.string.point_add_success));
            } else {
                // this point already exists
                MergePointsDialog dialog = new MergePointsDialog();

                Bundle args = new Bundle();
                args.putString(
                        MergePointsDialog.POINT_NUMBER,
                        this.ppoal.getNumber());

                args.putDouble(MergePointsDialog.NEW_EAST,
                        this.ppoal.getProjPt().getEast());
                args.putDouble(MergePointsDialog.NEW_NORTH,
                        this.ppoal.getProjPt().getNorth());
                args.putDouble(MergePointsDialog.NEW_ALTITUDE,
                        this.ppoal.getProjPt().getAltitude());

                dialog.setArguments(args);
                dialog.show(this.getSupportFragmentManager(), "MergePointsDialogFragment");
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
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
