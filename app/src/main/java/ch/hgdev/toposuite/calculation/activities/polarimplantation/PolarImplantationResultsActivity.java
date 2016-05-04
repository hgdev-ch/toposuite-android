package ch.hgdev.toposuite.calculation.activities.polarimplantation;

import android.os.Bundle;
import android.widget.ListView;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.PolarImplantation;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.ViewUtils;

public class PolarImplantationResultsActivity extends TopoSuiteActivity {
    private static final String POLAR_IMPLANTATION_RESULTS_ACTIVITY = "PolarImplantationResultsActivity: ";

    private ListView resultsListView;

    private PolarImplantation polarImplantation;
    private ArrayListOfResultsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_polar_implantation_results);

        this.resultsListView = (ListView) this.findViewById(R.id.results_list);

        Bundle bundle = this.getIntent().getExtras();
        this.polarImplantation = (PolarImplantation) bundle.getSerializable(PolarImplantationActivity.POLAR_IMPLANT_CALCULATION);

        try {
            this.polarImplantation.compute();
            this.displayResults();
        } catch (CalculationException e) {
            Logger.log(Logger.ErrLabel.CALCULATION_COMPUTATION_ERROR, e.getMessage());
            ViewUtils.showToast(this, this.getString(R.string.error_computation_exception));
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_polar_implantation_results);
    }

    private void displayResults() {
        this.adapter = new ArrayListOfResultsAdapter(this,
                R.layout.polar_implantation_results_list_item,
                this.polarImplantation.getResults());
        this.resultsListView.setAdapter(this.adapter);
    }
}
