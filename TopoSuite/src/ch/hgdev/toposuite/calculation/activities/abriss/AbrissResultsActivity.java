package ch.hgdev.toposuite.calculation.activities.abriss;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
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
import ch.hgdev.toposuite.utils.Logger;

public class AbrissResultsActivity extends TopoSuiteActivity {
    private ListView                    resultsListView;

    private TextView                    stationNumberTextView;
    private TextView                    meanTextView;
    private TextView                    meanErrorDirectionTextView;
    private TextView                    meanErrorCompensatedTextView;

    private Abriss                      abriss;
    private ArrayAdapter<Abriss.Result> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_abriss_results);

        this.resultsListView = (ListView) this.findViewById(R.id.results_list);
        this.stationNumberTextView = (TextView) this.findViewById(R.id.station_number);
        this.meanTextView = (TextView) this.findViewById(R.id.mean);
        this.meanErrorDirectionTextView = (TextView) this.findViewById(
                R.id.mean_error_direction);
        this.meanErrorCompensatedTextView = (TextView) this.findViewById(
                R.id.mean_error_compensated);

        Bundle bundle = this.getIntent().getExtras();
        Point station = SharedResources.getSetOfPoints().find(
                bundle.getString(AbrissActivity.STATION_NUMBER_LABEL));
        int position = bundle.getInt(AbrissActivity.CALCULATION_POSITION_LABEL);

        if (position != -1) {
            this.abriss = (Abriss) SharedResources.getCalculationsHistory().get(position);
            this.abriss.setStation(station);
        } else {
            this.abriss = new Abriss(station, true);

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
                Logger.log(Logger.ErrLabel.PARSE_ERROR, e.getMessage());
            }

            this.abriss.getMeasures().addAll(orientationsList);
        }

        this.abriss.compute();

        this.displayResults();

        this.registerForContextMenu(this.resultsListView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // reset deactivated measures
        for (Measure m : this.abriss.getMeasures()) {
            m.reactivate();
        }

        this.abriss.getResults().clear();
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_abriss_results);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.abriss_results, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.abriss_results_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
        case R.id.toggle_measure:
            this.abriss.getResults().get(info.position).toggle();
            this.adapter.notifyDataSetChanged();
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
        case R.id.run_calculation_button:
            for (int i = 0; i < this.abriss.getResults().size(); i++) {
                if (this.abriss.getResults().get(i).isDeactivated()) {
                    this.abriss.getMeasures().get(i).deactivate();
                } else {
                    this.abriss.getMeasures().get(i).reactivate();
                }
            }
            this.abriss.compute();
            this.displayResults();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
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

        this.meanTextView.setText(DisplayUtils.formatAngle(this.abriss.getMean()));
        this.meanErrorDirectionTextView.setText("±" + DisplayUtils.formatCC(
                this.abriss.getMSE()));
        this.meanErrorCompensatedTextView.setText("±" + DisplayUtils.formatCC(
                this.abriss.getMeanErrComp()));
    }
}
