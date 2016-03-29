package ch.hgdev.toposuite.calculation.activities.circularsegmentation;

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
 * Adapter to correctly format a list of points to display without their
 * altitude nor class.
 * 
 * @author HGdev
 * 
 */
public class ArrayListOfPointsAdapter extends ArrayAdapter<Point> {
    private final ArrayList<Point> points;

    public ArrayListOfPointsAdapter(Context context, int textViewResourceId, ArrayList<Point> points) {
        super(context, textViewResourceId, points);
        this.points = points;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.points_without_altitude_and_class_list_item, null);
        }

        Point point = this.points.get(position);
        if (point != null) {
            TextView numberTextView = (TextView) view.findViewById(R.id.point_number_item);
            TextView eastTextView = (TextView) view.findViewById(R.id.point_east_item);
            TextView northTextView = (TextView) view.findViewById(R.id.point_north_item);

            if (numberTextView != null) {
                numberTextView.setText(point.getNumber());
            }
            if (eastTextView != null) {
                eastTextView.setText(DisplayUtils.formatCoordinate(point.getEast()));
            }
            if (northTextView != null) {
                northTextView.setText(DisplayUtils.formatCoordinate(point.getNorth()));
            }
        }

        return view;
    }
}