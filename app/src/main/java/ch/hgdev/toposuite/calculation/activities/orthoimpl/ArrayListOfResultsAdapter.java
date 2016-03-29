package ch.hgdev.toposuite.calculation.activities.orthoimpl;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.calculation.OrthogonalImplantation;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class ArrayListOfResultsAdapter extends ArrayAdapter<OrthogonalImplantation.Result> {
    private final ArrayList<OrthogonalImplantation.Result> results;

    public ArrayListOfResultsAdapter(Context context, int textViewResourceId,
            ArrayList<OrthogonalImplantation.Result> results) {
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

        OrthogonalImplantation.Result result = this.results.get(position);
        if (result != null) {
            TextView numberTextView = (TextView) view.findViewById(R.id.number_item);
            TextView eastTextView = (TextView) view.findViewById(R.id.east_item);
            TextView northTextView = (TextView) view.findViewById(R.id.north_item);
            TextView abscissaTextView = (TextView) view.findViewById(R.id.abscissa_item);
            TextView ordinateTextView = (TextView) view.findViewById(R.id.ordinate_item);

            if (numberTextView != null) {
                numberTextView.setText(result.getPoint().getNumber());
            }

            if (eastTextView != null) {
                eastTextView.setText(DisplayUtils.formatCoordinate(result.getPoint().getEast()));
            }

            if (northTextView != null) {
                northTextView.setText(DisplayUtils.formatCoordinate(result.getPoint().getNorth()));
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
