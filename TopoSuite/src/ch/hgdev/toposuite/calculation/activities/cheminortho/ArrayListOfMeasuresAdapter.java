package ch.hgdev.toposuite.calculation.activities.cheminortho;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.calculation.CheminementOrthogonal;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class ArrayListOfMeasuresAdapter extends ArrayAdapter<CheminementOrthogonal.Measure> {
    private final ArrayList<CheminementOrthogonal.Measure> measures;

    public ArrayListOfMeasuresAdapter(Context context, int textViewResourceId,
            ArrayList<CheminementOrthogonal.Measure> measures) {
        super(context, textViewResourceId, measures);
        this.measures = measures;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.cheminement_ortho_measures_list_item, null);
        }

        CheminementOrthogonal.Measure measure = this.measures.get(position);
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
}