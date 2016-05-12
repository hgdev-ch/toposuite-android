package ch.hgdev.toposuite.calculation.activities.polarimplantation;

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

/**
 * Adapter to correctly format a list of points, with associated station height
 * (S), to display.
 *
 * @author HGdev
 */
public class ArrayListOfPointsWithSAdapter extends ArrayAdapter<Measure> {

    public ArrayListOfPointsWithSAdapter(Context context, int textViewResourceId, ArrayList<Measure> points) {
        super(context, textViewResourceId, points);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.points_with_s_list_item, parent, false);
        }

        Measure m = this.getItem(position);
        if (m != null) {
            TextView numberTextView = (TextView) view.findViewById(R.id.point_number_item);
            TextView eastTextView = (TextView) view.findViewById(R.id.point_east_item);
            TextView northTextView = (TextView) view.findViewById(R.id.point_north_item);
            TextView altitudeTextView = (TextView) view.findViewById(R.id.point_altitude_item);
            TextView sTextView = (TextView) view.findViewById(R.id.s_item);

            if (numberTextView != null) {
                Point p = m.getPoint();
                if (p != null) {
                    numberTextView.setText(p.getNumber());
                }
            }
            if (eastTextView != null) {
                eastTextView.setText(DisplayUtils.formatCoordinate(m.getPoint().getEast()));
            }
            if (northTextView != null) {
                northTextView.setText(DisplayUtils.formatCoordinate(m.getPoint().getNorth()));
            }
            if (altitudeTextView != null) {
                altitudeTextView.setText(DisplayUtils.formatCoordinate(m.getPoint()
                        .getAltitude()));
            }
            if (sTextView != null) {
                sTextView.setText(DisplayUtils.formatDistance(m.getS()));
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
