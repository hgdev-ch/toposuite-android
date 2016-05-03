package ch.hgdev.toposuite.calculation.activities.axisimpl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.utils.DisplayUtils;

class ArrayListOfMeasuresAdapter extends ArrayAdapter<Measure> {
    private final int           textViewResourceId;

    public ArrayListOfMeasuresAdapter(Context context, int textViewResourceId,
            List<Measure> measures) {
        super(context, textViewResourceId, measures);
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

        Measure measure = this.getItem(position);
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

    public ArrayList<Measure> getMeasures() {
        ArrayList<Measure> measures = new ArrayList<>();
        for (int i = 0; i < this.getCount(); i++) {
            measures.add(this.getItem(i));
        }
        return measures;
    }
}