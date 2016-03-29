package ch.hgdev.toposuite.calculation.activities.polarsurvey;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.calculation.PolarSurvey;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class ArrayListOfResultsAdapter extends ArrayAdapter<PolarSurvey.Result> {
    private final ArrayList<PolarSurvey.Result> results;

    public ArrayListOfResultsAdapter(Context context, int textViewResourceId,
            ArrayList<PolarSurvey.Result> _results) {
        super(context, textViewResourceId, _results);
        this.results = _results;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.polar_survey_results_list_item, null);
        }
        PolarSurvey.Result result = this.results.get(position);

        if (result != null) {
            TextView numberTextView = (TextView) view.findViewById(R.id.determination_number_item);
            TextView eastTextView = (TextView) view.findViewById(R.id.east_item);
            TextView northTextView = (TextView) view.findViewById(R.id.north_item);
            TextView altitudeTextView = (TextView) view.findViewById(R.id.altitude_item);

            if (numberTextView != null) {
                numberTextView.setText(result.getDeterminationNumber());
            }

            if (eastTextView != null) {
                eastTextView.setText(DisplayUtils.formatCoordinate(result.getEast()));
            }

            if (northTextView != null) {
                northTextView.setText(DisplayUtils.formatCoordinate(result.getNorth()));
            }

            if (altitudeTextView != null) {
                altitudeTextView.setText(DisplayUtils.formatCoordinate(result.getAltitude()));
            }
        }

        return view;
    }
}
