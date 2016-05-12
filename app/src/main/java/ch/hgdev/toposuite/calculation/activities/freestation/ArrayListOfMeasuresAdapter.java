package ch.hgdev.toposuite.calculation.activities.freestation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class ArrayListOfMeasuresAdapter extends ArrayAdapter<Measure> {

    public ArrayListOfMeasuresAdapter(Context context, int textViewResourceId, ArrayList<Measure> measures) {
        super(context, textViewResourceId, measures);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.determinations_list_item, parent, false);
        }

        Measure determination = this.getItem(position);
        if (determination != null) {
            TextView numberTextView = (TextView) view.findViewById(R.id.measure_number_item);
            TextView horizOrientTextView = (TextView) view.findViewById(R.id.horiz_orient_item);
            TextView horizDistTextView = (TextView) view.findViewById(R.id.horiz_dist_item);

            // FIXME R.id.altitude MUST be called R.id.zen_angle, it must be
            // fixed in the R.layout.determinations_list_item
            TextView zenAngleTextView = (TextView) view.findViewById(R.id.altitude_item);

            TextView sTextView = (TextView) view.findViewById(R.id.s_item);
            TextView latDeplTextView = (TextView) view.findViewById(R.id.lat_depl_item);
            TextView lonDeplTextView = (TextView) view.findViewById(R.id.lon_depl_item);

            if (numberTextView != null) {
                Point p = determination.getPoint();
                if (p != null) {
                    numberTextView.setText(p.getNumber());
                }
            }

            if (horizOrientTextView != null) {
                horizOrientTextView.setText(DisplayUtils.formatAngle(determination.getHorizDir()));
            }

            if (horizDistTextView != null) {
                horizDistTextView.setText(DisplayUtils.formatDistance(determination.getDistance()));
            }

            if (zenAngleTextView != null) {
                zenAngleTextView.setText(DisplayUtils.formatAngle(determination.getZenAngle()));
            }

            if (sTextView != null) {
                sTextView.setText(DisplayUtils.formatDistance(determination.getS()));
            }

            if (latDeplTextView != null) {
                latDeplTextView.setText(DisplayUtils.formatDistance(determination.getLatDepl()));
            }

            if (lonDeplTextView != null) {
                lonDeplTextView.setText(DisplayUtils.formatDistance(determination.getLonDepl()));
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
