package ch.hgdev.toposuite.calculation.activities.pointproj;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.PointProjectionOnALine;
import ch.hgdev.toposuite.dao.PointsDataSource;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class PointProjectionResultActivity extends TopoSuiteActivity {

    private PointProjectionOnALine ppoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_point_projection_result);

        Bundle bundle = this.getIntent().getExtras();
        int position = bundle.getInt(PointProjectionActivity.POINT_PROJ_POSITION);
        this.ppoal = (PointProjectionOnALine)
                SharedResources.getCalculationsHistory().get(position);
        this.ppoal.compute();

        TextView pointNumberTextView = (TextView) this.findViewById(R.id.point_number);
        TextView eastTextView = (TextView) this.findViewById(R.id.east);
        TextView northTextView = (TextView) this.findViewById(R.id.north);

        TextView projPointLineDistTextView = (TextView) this.findViewById(
                R.id.distance_projpoint_line);
        TextView projPointP1DistTextView = (TextView) this.findViewById(
                R.id.distance_projpoint_point_1);
        TextView projPointP2DistTextView = (TextView) this.findViewById(
                R.id.distance_projpoint_point_2);

        pointNumberTextView.setText(DisplayUtils.toString(
                this.ppoal.getNumber()));
        eastTextView.setText(DisplayUtils.toString(
                this.ppoal.getProjPt().getEast()));
        northTextView.setText(DisplayUtils.toString(
                this.ppoal.getProjPt().getNorth()));

        projPointLineDistTextView.setText(DisplayUtils.toString(
                this.ppoal.getDistPtToLine()));
        projPointP1DistTextView.setText(DisplayUtils.toString(
                this.ppoal.getDistPtToP1()));
        projPointP2DistTextView.setText(DisplayUtils.toString(
                this.ppoal.getDistPtToP2()));
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
            if (SharedResources.getSetOfPoints().find(
                    this.ppoal.getProjPt().getNumber()) == null) {
                SharedResources.getSetOfPoints().add(this.ppoal.getProjPt());
                this.ppoal.getProjPt().registerDAO(PointsDataSource.getInstance());

                Toast.makeText(this, R.string.point_add_success, Toast.LENGTH_LONG)
                        .show();
            }

            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
