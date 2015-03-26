package ch.hgdev.toposuite.calculation.activities.polarimplantation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.ListView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.calculation.PolarImplantation;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.Logger;

public class PolarImplantationResultsActivity extends TopoSuiteActivity {
    private static final String       POLAR_IMPLANTATION_RESULTS_ACTIVITY = "PolarImplantationResultsActivity: ";

    private ListView                  resultsListView;

    private PolarImplantation         polarImplantation;
    private ArrayListOfResultsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_polar_implantation_results);

        this.resultsListView = (ListView) this.findViewById(R.id.results_list);

        Bundle bundle = this.getIntent().getExtras();
        Point station = SharedResources.getSetOfPoints().find(
                bundle.getString(PolarImplantationActivity.STATION_NUMBER_LABEL));
        if (this.polarImplantation == null) {
            this.polarImplantation = new PolarImplantation(station, true);
        }
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(
                    bundle.getString(PolarImplantationActivity.POINTS_WITH_S_LABEL));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                Measure m = Measure.getMeasureFromJSON(json.toString());
                this.polarImplantation.getMeasures().add(m);
            }
        } catch (JSONException e) {
            Logger.log(Logger.ErrLabel.PARSE_ERROR,
                    PolarImplantationResultsActivity.POLAR_IMPLANTATION_RESULTS_ACTIVITY
                            + "error retrieving list of measures from JSON");
        }

        this.polarImplantation.compute();
        this.displayResults();
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
