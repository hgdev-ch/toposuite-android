package ch.hgdev.toposuite.calculation.activities.polarimplantation;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;

/**
 * Adapter to correctly format a list of points, with associated station height
 * (S), to display.
 * 
 * @author HGdev
 * 
 */
public class ArrayListOfPointsWithSAdapter extends
        ArrayAdapter<PolarImplantationActivity.PointWithS> {
    private final ArrayList<PolarImplantationActivity.PointWithS> points;
    private final Context                                         context;

    public ArrayListOfPointsWithSAdapter(Context context, int textViewResourceId,
            ArrayList<PolarImplantationActivity.PointWithS> points) {
        super(context, textViewResourceId, points);
        this.points = points;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.points_list_item, null);
        }

        Point point = this.points.get(position);
        if (point != null) {
            TextView numberTextView = (TextView) view.findViewById(R.id.point_number_item);
            TextView eastTextView = (TextView) view.findViewById(R.id.point_east_item);
            TextView northTextView = (TextView) view.findViewById(R.id.point_north_item);
            TextView altitudeTextView = (TextView) view.findViewById(R.id.point_altitude_item);
            TextView sTextView = (TextView) view.findViewById(R.id.s_item);

            if (numberTextView != null) {
                numberTextView.setText(DisplayUtils.toString(point.getNumber()));
            }
            if (eastTextView != null) {
                eastTextView.setText(DisplayUtils.toString(point.getEast()));
            }
            if (northTextView != null) {
                northTextView.setText(DisplayUtils.toString(point.getNorth()));
            }
            if (altitudeTextView != null) {
                altitudeTextView.setText(DisplayUtils.toString(point.getAltitude()));
            }
            if (sTextView != null) {
                sTextView.setText(point.getBasePointAsString(this.context));
            }
        }

        return view;
    }
}