package ch.hgdev.toposuite.calculation.activities.axisimpl;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.AxisImplantation;
import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.ViewUtils;

public class AxisImplantationResultsActivity extends TopoSuiteActivity {

    private TextView axisImplantationPointsTextView;
    private TextView axisImplantationStationTextView;
    private ListView resultsListView;

    private AxisImplantation axisImpl;

    private ArrayListOfResultsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_axis_implantation_results);

        this.axisImplantationPointsTextView = (TextView) this.findViewById(R.id.axis_implantation_points);
        this.axisImplantationStationTextView = (TextView) this.findViewById(R.id.axis_implantation_station);
        this.resultsListView = (ListView) this.findViewById(R.id.results_list);

        Bundle bundle = this.getIntent().getExtras();
        this.axisImpl = (AxisImplantation) bundle.getSerializable(AxisImplantationActivity.AXIS_IMPLANTATION);
        try {
            this.axisImpl.compute();
            StringBuilder builder = new StringBuilder();
            builder.append(this.axisImpl.getOrthogonalBase().getOrigin());
            builder.append("-");
            builder.append(this.axisImpl.getOrthogonalBase().getExtremity());
            this.axisImplantationPointsTextView.setText(builder.toString());
            this.axisImplantationStationTextView.setText(this.axisImpl.getStation().toString());
        } catch (CalculationException e) {
            Logger.log(Logger.ErrLabel.CALCULATION_COMPUTATION_ERROR, e.getMessage());
            ViewUtils.showToast(this, this.getString(R.string.error_computation_exception));
        }
        this.drawList();
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_axis_implantation_results);
    }

    private void drawList() {
        this.adapter = new ArrayListOfResultsAdapter(this,
                R.layout.orth_impl_results_list_item,
                this.axisImpl.getResults());
        this.resultsListView.setAdapter(this.adapter);
    }
}