package ch.hgdev.toposuite.calculation.activities.pointproj;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.R.id;
import ch.hgdev.toposuite.R.layout;
import ch.hgdev.toposuite.calculation.PointProjectionOnALine;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class PointProjectionResultActivity extends TopoSuiteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_point_projection_result);

        Bundle bundle = this.getIntent().getExtras();
        int position = bundle.getInt(PointProjectionActivity.POINT_PROJ_POSITION);
        PointProjectionOnALine ppoal = (PointProjectionOnALine)
                SharedResources.getCalculationsHistory().get(position);
        ppoal.compute();

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
                ppoal.getNumber()));
        eastTextView.setText(DisplayUtils.toString(
                ppoal.getProjPt().getEast()));
        northTextView.setText(DisplayUtils.toString(
                ppoal.getProjPt().getNorth()));

        projPointLineDistTextView.setText(DisplayUtils.toString(
                ppoal.getDistPtToLine()));
        projPointP1DistTextView.setText(DisplayUtils.toString(
                ppoal.getDistPtToP1()));
        projPointP2DistTextView.setText(DisplayUtils.toString(
                ppoal.getDistPtToP2()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //this.getMenuInflater().inflate(R.menu.point_projection_result, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
