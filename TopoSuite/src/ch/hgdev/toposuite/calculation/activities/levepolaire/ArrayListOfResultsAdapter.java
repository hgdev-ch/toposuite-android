package ch.hgdev.toposuite.calculation.activities.levepolaire;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.LevePolaire;
import ch.hgdev.toposuite.calculation.LevePolaire.Result;
import ch.hgdev.toposuite.points.Point;
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
            TextView numberTextView = (TextView) view.findViewById(R.id.determination_number_item);
            TextView eastTextView = (TextView) view.findViewById(R.id.east_item);
            TextView northTextView = (TextView) view.findViewById(R.id.north_item);
            TextView altitudeTextView = (TextView) view.findViewById(R.id.altitude_item);

            if (numberTextView != null) {
                String numberText = DisplayUtils.toString(result.getDeterminationNumber()) + " ";
                Result r = this.getItem(position);
                Point point = new Point(
                        r.getDeterminationNumber(),
                        r.getEast(),
                        r.getNorth(),
                        r.getAltitude(),
                        false);
                Point p = SharedResources.getSetOfPoints().find(r.getDeterminationNumber());
                numberText += point.equals(p) ?
                        App.getContext().getString(R.string.heavy_checkmark) :
                        App.getContext().getString(R.string.heavy_ballot);
                numberTextView.setText(numberText);
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
