package ch.hgdev.toposuite.calculation.activities.surface;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.calculation.Surface;
import ch.hgdev.toposuite.calculation.Surface.PointWithRadius;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class ArrayListOfPointsWithRadiusAdapter extends ArrayAdapter<Surface.PointWithRadius> {
    private final ArrayList<Surface.PointWithRadius> points;

    public ArrayListOfPointsWithRadiusAdapter(Context context, int textViewResourceId,
            ArrayList<Surface.PointWithRadius> points) {
        super(context, textViewResourceId, points);
        this.points = points;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.points_with_radius_list_item, null);
        }

        PointWithRadius p = this.points.get(position);
        if (p != null) {
            TextView vertexTextView = (TextView) view.findViewById(R.id.vertex_number_item);
            TextView radiusTextView = (TextView) view.findViewById(R.id.radius_item);
            TextView numberTextView = (TextView) view.findViewById(R.id.point_number_item);
            TextView eastTextView = (TextView) view.findViewById(R.id.point_east_item);
            TextView northTextView = (TextView) view.findViewById(R.id.point_north_item);

            if (vertexTextView != null) {
                vertexTextView.setText(String.valueOf(p.getVertexNumber()));
            }
            if (radiusTextView != null) {
                radiusTextView.setText(DisplayUtils.formatDistance(p.getRadius()));
            }
            if (numberTextView != null) {
                numberTextView.setText(p.getNumber());
            }
            if (eastTextView != null) {
                eastTextView.setText(DisplayUtils.formatCoordinate(p.getEast()));
            }
            if (northTextView != null) {
                northTextView.setText(DisplayUtils.formatCoordinate(p.getNorth()));
            }
        }
        return view;
    }
}
