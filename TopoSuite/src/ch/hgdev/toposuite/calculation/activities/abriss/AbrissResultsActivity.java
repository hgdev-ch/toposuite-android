package ch.hgdev.toposuite.calculation.activities.abriss;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.Abriss;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class AbrissResultsActivity extends TopoSuiteActivity {
    private ListView                    resultsListView;

    private TextView                    stationNumberTextView;
    private TextView                    unknOrientTextView;
    private TextView                    meanErrorTextView;

    private Abriss                      abriss;
    private ArrayAdapter<Abriss.Result> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_abriss_results);

        this.resultsListView = (ListView) this.findViewById(R.id.results_list);
        this.stationNumberTextView = (TextView) this.findViewById(R.id.station_number);
        this.unknOrientTextView = (TextView) this.findViewById(R.id.unknown_orientation);
        this.meanErrorTextView = (TextView) this.findViewById(R.id.mean_error);

        Bundle bundle = this.getIntent().getExtras();
        Point station = SharedResources.getSetOfPoints().find(
                bundle.getInt(AbrissActivity.STATION_NUMBER_LABEL));
        int position = bundle.getInt(AbrissActivity.CALCULATION_POSITION_LABEL);

        ArrayList<Measure> orientationsList = new ArrayList<Measure>();
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(bundle.getString(AbrissActivity.ORIENTATIONS_LABEL));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                Measure m = Measure.getMeasureFromJSON(json.toString());
                orientationsList.add(m);
            }
        } catch (JSONException e) {
            // TODO
        }

        if (position != -1) {
            this.abriss = (Abriss) SharedResources.getCalculationsHistory().get(position);
            this.abriss.setStation(station);
        } else {
            this.abriss = new Abriss(station, true);
        }

        this.abriss.getMeasures().clear();
        this.abriss.getResults().clear();
        this.abriss.getMeasures().addAll(orientationsList);
        this.abriss.compute();

        this.displayResults();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.abriss_results, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void displayResults() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.abriss.getStation().getNumber());
        builder.append(" (");
        builder.append(DisplayUtils.formatPoint(this, this.abriss.getStation()));
        builder.append(")");

        this.stationNumberTextView.setText(builder.toString());

        this.adapter = new ArrayListOfResultsAdapter(this, R.layout.abriss_results_list_item,
                this.abriss.getResults());
        this.resultsListView.setAdapter(this.adapter);

        this.unknOrientTextView.setText(DisplayUtils.toString(this.abriss.getMean()));
        this.meanErrorTextView.setText(DisplayUtils.toString(this.abriss.getMSE()));
    }
}
