package ch.hgdev.toposuite.calculation.activities.orthoimpl;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.OrthogonalImplantation;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.ViewUtils;

public class OrthoImplantationResultsActivity extends TopoSuiteActivity {
    private TextView baseTextView;
    private ListView resultsListView;

    private ArrayListOfResultsAdapter adapter;

    private OrthogonalImplantation orthImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_ortho_implantation_results);

        this.baseTextView = (TextView) this.findViewById(R.id.orthogonal_implantation);
        this.resultsListView = (ListView) this.findViewById(R.id.results_list);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            this.orthImpl = (OrthogonalImplantation) bundle.getSerializable(OrthogonalImplantationActivity.ORTHO_IMPLANTATION);
            try {
                this.orthImpl.compute();
                StringBuilder builder = new StringBuilder();
                builder.append(this.orthImpl.getOrthogonalBase().getOrigin());
                builder.append("-");
                builder.append(this.orthImpl.getOrthogonalBase().getExtremity());

                this.baseTextView.setText(builder.toString());
                this.drawList();
            } catch (CalculationException e) {
                Logger.log(Logger.ErrLabel.CALCULATION_COMPUTATION_ERROR, e.getMessage());
                ViewUtils.showToast(this, this.getString(R.string.error_computation_exception));
            }
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_ortho_implantation_results);
    }

    private void drawList() {
        this.adapter = new ArrayListOfResultsAdapter(this, R.layout.orth_impl_results_list_item, this.orthImpl.getResults());
        this.resultsListView.setAdapter(this.adapter);
    }
}
