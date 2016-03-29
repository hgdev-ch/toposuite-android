package ch.hgdev.toposuite.calculation.activities.polarimplantation;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.utils.DisplayUtils;

/**
 * Adapter to correctly format a list of points, with associated station height
 * (S), to display.
 * 
 * @author HGdev
 * 
 */
public class ArrayListOfPointsWithSAdapter extends
        ArrayAdapter<Measure> {
    private final ArrayList<Measure> points;

    public ArrayListOfPointsWithSAdapter(Context context, int textViewResourceId,
            ArrayList<Measure> points) {
        super(context, textViewResourceId, points);
        this.points = points;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.points_with_s_list_item, null);
        }

        Measure m = this.points.get(position);
        if (m != null) {
            TextView numberTextView = (TextView) view.findViewById(R.id.point_number_item);
            TextView eastTextView = (TextView) view.findViewById(R.id.point_east_item);
            TextView northTextView = (TextView) view.findViewById(R.id.point_north_item);
            TextView altitudeTextView = (TextView) view.findViewById(R.id.point_altitude_item);
            TextView sTextView = (TextView) view.findViewById(R.id.s_item);

            if (numberTextView != null) {
                numberTextView.setText(m.getPoint().getNumber());
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
}
