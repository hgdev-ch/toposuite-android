package ch.hgdev.toposuite.calculation.activities.abriss;

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

public class ArrayListOfOrientationsAdapter extends ArrayAdapter<Measure> {
    private final ArrayList<Measure> orientations;

    public ArrayListOfOrientationsAdapter(Context context, int textViewResourceId,
            ArrayList<Measure> orientations) {
        super(context, textViewResourceId, orientations);
        this.orientations = orientations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.orientations_list_item, null);
        }

        Measure orientation = this.orientations.get(position);
        if (orientation != null) {
            TextView numberTextView = (TextView) view.findViewById(R.id.point_number_item);
            TextView horizOrientTextView = (TextView) view.findViewById(R.id.horiz_orient_item);
            TextView horizDistTextView = (TextView) view.findViewById(R.id.horiz_dist_item);
            TextView zenAngleTextView = (TextView) view.findViewById(R.id.zen_angle_item);

            if (numberTextView != null) {
                numberTextView.setText(
                        orientation.getPoint().getNumber());
            }

            if (horizOrientTextView != null) {
                horizOrientTextView.setText(DisplayUtils.formatAngle(orientation.getHorizDir()));
            }

            if (horizDistTextView != null) {
                horizDistTextView.setText(DisplayUtils.formatDistance(orientation.getDistance()));
            }

            if (zenAngleTextView != null) {
                zenAngleTextView.setText(DisplayUtils.formatAngle(orientation.getZenAngle()));
            }
        }

        return view;
    }
}