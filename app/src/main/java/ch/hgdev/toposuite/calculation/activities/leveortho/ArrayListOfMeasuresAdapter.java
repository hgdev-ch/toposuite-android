package ch.hgdev.toposuite.calculation.activities.leveortho;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.calculation.LeveOrthogonal;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class ArrayListOfMeasuresAdapter extends ArrayAdapter<LeveOrthogonal.Measure> {
    private final ArrayList<LeveOrthogonal.Measure> measures;

    public ArrayListOfMeasuresAdapter(Context context, int textViewResourceId,
            ArrayList<LeveOrthogonal.Measure> measures) {
        super(context, textViewResourceId, measures);
        this.measures = measures;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.leve_ortho_measures_list_item, null);
        }

        LeveOrthogonal.Measure measure = this.measures.get(position);
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
}