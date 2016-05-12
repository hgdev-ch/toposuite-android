package ch.hgdev.toposuite.calculation.activities.leveortho;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.calculation.LeveOrthogonal;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class ArrayListOfMeasuresAdapter extends ArrayAdapter<LeveOrthogonal.Measure> {

    public ArrayListOfMeasuresAdapter(Context context, int textViewResourceId,
                                      ArrayList<LeveOrthogonal.Measure> measures) {
        super(context, textViewResourceId, measures);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.leve_ortho_measures_list_item, parent, false);
        }

        LeveOrthogonal.Measure measure = this.getItem(position);
        if (measure != null) {
            TextView numberTextView = (TextView) view.findViewById(R.id.number_item);
            TextView abscissaTextView = (TextView) view.findViewById(R.id.abscissa_item);
            TextView ordinateTextView = (TextView) view.findViewById(R.id.ordinate_item);

            if (numberTextView != null) {
                numberTextView.setText(measure.getNumber());
            }

            if (abscissaTextView != null) {
                abscissaTextView.setText(DisplayUtils.formatCoordinate(measure.getAbscissa()));
            }

            if (ordinateTextView != null) {
                ordinateTextView.setText(DisplayUtils.formatCoordinate(measure.getOrdinate()));
            }
        }

        return view;
    }

    public ArrayList<LeveOrthogonal.Measure> getMeasures() {
        ArrayList<LeveOrthogonal.Measure> measures = new ArrayList<>();
        for (int i = 0; i < this.getCount(); i++) {
            measures.add(this.getItem(i));
        }
        return measures;
    }
}