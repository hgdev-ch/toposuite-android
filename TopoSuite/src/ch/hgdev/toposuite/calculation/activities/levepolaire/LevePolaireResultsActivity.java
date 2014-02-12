package ch.hgdev.toposuite.calculation.activities.levepolaire;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.ListView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.LevePolaire;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.points.Point;

public class LevePolaireResultsActivity extends TopoSuiteActivity {

    private ListView                  resultsListView;

    private LevePolaire               levePolaire;
    private ArrayListOfResultsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_leve_polaire_results);

        this.resultsListView = (ListView) this.findViewById(R.id.results_list);

        Bundle bundle = this.getIntent().getExtras();
        Point station = SharedResources.getSetOfPoints().find(
                bundle.getInt(LevePolaireActivity.STATION_NUMBER_LABEL));

        if (this.levePolaire == null) {
            this.levePolaire = new LevePolaire(station, true);
        }

        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(bundle.getString(LevePolaireActivity.DETERMINATIONS_LABEL));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                Measure m = Measure.getMeasureFromJSON(json.toString());
                this.levePolaire.getDeterminations().add(m);
            }
        } catch (JSONException e) {
            // TODO
        }

        this.levePolaire.compute();

        this.displayResults();
    }

    private void displayResults() {
        this.adapter = new ArrayListOfResultsAdapter(this, R.layout.leve_polaire_results_list_item,
                this.levePolaire.getResults());
        this.resultsListView.setAdapter(this.adapter);
    }
}