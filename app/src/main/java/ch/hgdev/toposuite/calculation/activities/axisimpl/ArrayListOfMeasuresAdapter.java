package ch.hgdev.toposuite.calculation.activities.axisimpl;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.utils.DisplayUtils;

class ArrayListOfMeasuresAdapter extends ArrayAdapter<Measure> {
    private final List<Measure> measures;
    private final int           textViewResourceId;

    public ArrayListOfMeasuresAdapter(Context context, int textViewResourceId,
            List<Measure> measures) {
        super(context, textViewResourceId, measures);
        this.measures = measures;
        this.textViewResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(this.textViewResourceId, null);
        }

        Measure measure = this.measures.get(position);
        if (measure != null) {
            TextView numberTextView = (TextView) view.findViewById(R.id.measure_number_item);
            TextView horizOrientTextView = (TextView) view.findViewById(R.id.horiz_orient_item);
            TextView horizDistTextView = (TextView) view.findViewById(R.id.horiz_dist_item);

            if (numberTextView != null) {
                numberTextView.setText(measure.getMeasureNumber());
            }

            if (horizOrientTextView != null) {
                horizOrientTextView.setText(DisplayUtils.formatAngle(measure.getHorizDir()));
            }

            if (horizDistTextView != null) {
                horizDistTextView.setText(DisplayUtils.formatDistance(measure.getDistance()));
            }
        }

        return view;
    }
}