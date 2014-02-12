package ch.hgdev.toposuite.calculation.activities.levepolaire;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.calculation.LevePolaire;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class ArrayListOfResultsAdapter extends ArrayAdapter<LevePolaire.Result> {
    private final ArrayList<LevePolaire.Result> results;

    public ArrayListOfResultsAdapter(Context context, int textViewResourceId,
            ArrayList<LevePolaire.Result> _results) {
        super(context, textViewResourceId, _results);
        this.results = _results;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.leve_polaire_results_list_item, null);
        }
        LevePolaire.Result result = this.results.get(position);

        if (result != null) {
            TextView numberTextView = (TextView) view.findViewById(R.id.number_item);
            TextView eastTextView = (TextView) view.findViewById(R.id.east_item);
            TextView northTextView = (TextView) view.findViewById(R.id.north_item);
            TextView altitudeTextView = (TextView) view.findViewById(R.id.altitude_item);

            if (numberTextView != null) {
                numberTextView
                        .setText(DisplayUtils.toString(result.getDetermination().getNumber()));
            }
            if (eastTextView != null) {
                eastTextView.setText(DisplayUtils.toString(result.getEast()));
            }
            if (northTextView != null) {
                northTextView.setText(DisplayUtils.toString(result.getNorth()));
            }
            if (altitudeTextView != null) {
                altitudeTextView.setText(DisplayUtils.toString(result.getAltitude()));
            }
        }

        return view;
    }
}
