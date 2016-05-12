package ch.hgdev.toposuite.calculation.activities.cheminortho;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.calculation.CheminementOrthogonal;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class ArrayListOfMeasuresAdapter extends ArrayAdapter<CheminementOrthogonal.Measure> {

    public ArrayListOfMeasuresAdapter(Context context, int textViewResourceId, ArrayList<CheminementOrthogonal.Measure> measures) {
        super(context, textViewResourceId, measures);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.cheminement_ortho_measures_list_item, parent, false);
        }

        CheminementOrthogonal.Measure measure = this.getItem(position);
        if (measure != null) {
            TextView numberTextView = (TextView) view.findViewById(R.id.number_item);
            TextView distanceTextView = (TextView) view.findViewById(R.id.distance_item);

            if (numberTextView != null) {
                numberTextView.setText(measure.getNumber());
            }

            if (distanceTextView != null) {
                distanceTextView.setText(DisplayUtils.formatDistance(measure.getDistance()));
            }
        }

        return view;
    }

    public ArrayList<CheminementOrthogonal.Measure> getMeasures() {
        ArrayList<CheminementOrthogonal.Measure> measures = new ArrayList<>();
        for (int i = 0; i < this.getCount(); i++) {
            measures.add(this.getItem(i));
        }
        return measures;
    }
}