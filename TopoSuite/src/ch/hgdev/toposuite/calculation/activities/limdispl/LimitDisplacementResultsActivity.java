package ch.hgdev.toposuite.calculation.activities.limdispl;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.LimitDisplacement;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class LimitDisplacementResultsActivity extends TopoSuiteActivity {
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
            this.limDispl.compute();

            this.limitDisplacementLabelTextView.setText(
                    String.format(this.getString(
                            R.string.limit_displ_results_label),
                            DisplayUtils.toStringForTextView(
                                    this.limDispl.getSurface())));
            this.pointWestTextView.setText(
                    DisplayUtils.formatPoint(this, this.limDispl.getNewPointX()));
            this.pointEastTextView.setText(
                    DisplayUtils.formatPoint(this, this.limDispl.getNewPointY()));
            this.distParaSouthTextView.setText(DisplayUtils.toStringForTextView(
                    this.limDispl.getDistanceToSouthLimitAD()));
            this.distLonWestTextView.setText(DisplayUtils.toStringForTextView(
                    this.limDispl.getDistanceToWestLimitAX()));
            this.distLonEastTextView.setText(DisplayUtils.toStringForTextView(
                    this.limDispl.getDistanceToEastLimitDY()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.limit_displacement_results, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(
                R.string.title_activity_limit_displacement_results);
    }
}
