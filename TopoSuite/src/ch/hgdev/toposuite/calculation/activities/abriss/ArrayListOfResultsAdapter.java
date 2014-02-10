package ch.hgdev.toposuite.calculation.activities.abriss;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.calculation.Abriss;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class ArrayListOfResultsAdapter extends ArrayAdapter<Abriss.Result> {
    private final ArrayList<Abriss.Result> results;

    public ArrayListOfResultsAdapter(Context context, int textViewResourceId,
            ArrayList<Abriss.Result> results) {
        super(context, textViewResourceId, results);
        this.results = results;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.abriss_results_list_item, null);
        }

        Abriss.Result result = this.results.get(position);
        if (result != null) {
            TextView targetPointTextView = (TextView) view.findViewById(R.id.target_point);
            TextView calcDistTextView = (TextView) view.findViewById(R.id.calculated_distance);
            TextView unknOrientTextView = (TextView) view.findViewById(R.id.unknown_orientation);
            TextView orientDirTextView = (TextView) view.findViewById(R.id.oriented_direction);

            if (targetPointTextView != null) {
                targetPointTextView.setText(DisplayUtils.toString(result.getOrientation()
                        .getNumber()));
            }

            if (calcDistTextView != null) {
                calcDistTextView.setText(DisplayUtils.toString(result.getCalculatedDistance()));
            }

            if (unknOrientTextView != null) {
                unknOrientTextView.setText(DisplayUtils.toString(result.getUnknownOrientation()));
            }

            if (orientDirTextView != null) {
                orientDirTextView.setText(DisplayUtils.toString(result.getOrientedDirection()));
            }
        }

        return view;
    }
}