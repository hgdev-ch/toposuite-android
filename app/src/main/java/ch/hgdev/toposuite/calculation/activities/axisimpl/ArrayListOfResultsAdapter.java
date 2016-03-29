package ch.hgdev.toposuite.calculation.activities.axisimpl;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.calculation.AxisImplantation;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class ArrayListOfResultsAdapter extends ArrayAdapter<AxisImplantation.Result> {
    private final List<AxisImplantation.Result> results;

    public ArrayListOfResultsAdapter(Context context, int textViewResourceId,
            List<AxisImplantation.Result> results) {
        super(context, textViewResourceId, results);
        this.results = results;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.orth_impl_results_list_item, null);
        }

        AxisImplantation.Result result = this.results.get(position);
        if (result != null) {
            TextView numberTextView = (TextView) view.findViewById(R.id.number_item);
            TextView eastTextView = (TextView) view.findViewById(R.id.east_item);
            TextView northTextView = (TextView) view.findViewById(R.id.north_item);
            TextView abscissaTextView = (TextView) view.findViewById(R.id.abscissa_item);
            TextView ordinateTextView = (TextView) view.findViewById(R.id.ordinate_item);

            if (numberTextView != null) {
                numberTextView.setText(result.getNumber());
            }

            if (eastTextView != null) {
                eastTextView.setText(DisplayUtils.formatCoordinate(result.getEast()));
            }

            if (northTextView != null) {
                northTextView.setText(DisplayUtils.formatCoordinate(result.getNorth()));
            }

            if (abscissaTextView != null) {
                abscissaTextView.setText(DisplayUtils.formatCoordinate(result.getAbscissa()));
            }

            if (ordinateTextView != null) {
                ordinateTextView.setText(DisplayUtils.formatCoordinate(result.getOrdinate()));
            }
        }

        return view;
    }
}