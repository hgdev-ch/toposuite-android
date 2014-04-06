package ch.hgdev.toposuite.calculation.activities.polarsurvey;

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

public class ArrayListOfDeterminationsAdapter extends ArrayAdapter<Measure> {
    private final ArrayList<Measure> determinations;

    public ArrayListOfDeterminationsAdapter(Context context, int textViewResourceId,
            ArrayList<Measure> orientations) {
        super(context, textViewResourceId, orientations);
        this.determinations = orientations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.determinations_list_item, null);
        }

        Measure determination = this.determinations.get(position);
        if (determination != null) {
            TextView numberTextView = (TextView) view.findViewById(R.id.measure_number_item);
            TextView horizOrientTextView = (TextView) view.findViewById(R.id.horiz_orient_item);
            TextView horizDistTextView = (TextView) view.findViewById(R.id.horiz_dist_item);
            TextView altitudeTextView = (TextView) view.findViewById(R.id.altitude_item);
            TextView sTextView = (TextView) view.findViewById(R.id.s_item);
            TextView latDeplTextView = (TextView) view.findViewById(R.id.lat_depl_item);
            TextView lonDeplTextView = (TextView) view.findViewById(R.id.lon_depl_item);

            if (numberTextView != null) {
                numberTextView.setText(determination.getMeasureNumber());
            }

            if (horizOrientTextView != null) {
                horizOrientTextView.setText(DisplayUtils.formatAngle(determination.getHorizDir()));
            }

            if (horizDistTextView != null) {
                horizDistTextView.setText(DisplayUtils.formatDistance(determination.getDistance()));
            }

            if (altitudeTextView != null) {
                altitudeTextView.setText(DisplayUtils.formatAngle(determination.getZenAngle()));
            }

            if (sTextView != null) {
                sTextView.setText(DisplayUtils.formatDistance(determination.getS()));
            }

            if (latDeplTextView != null) {
                latDeplTextView.setText(
						DisplayUtils.formatDistance(
							determination.getLatDepl()));
            }

            if (lonDeplTextView != null) {
                lonDeplTextView.setText(
						DisplayUtils.formatDistance(
							determination.getLonDepl()));
            }
        }

        return view;
    }
}
