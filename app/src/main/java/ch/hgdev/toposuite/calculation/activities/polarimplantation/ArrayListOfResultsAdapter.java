package ch.hgdev.toposuite.calculation.activities.polarimplantation;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.calculation.PolarImplantation;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class ArrayListOfResultsAdapter extends ArrayAdapter<PolarImplantation.Result> {
    private final ArrayList<PolarImplantation.Result> results;

    public ArrayListOfResultsAdapter(Context context, int textViewResourceId,
            ArrayList<PolarImplantation.Result> _results) {
        super(context, textViewResourceId, _results);
        this.results = _results;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.polar_implantation_results_list_item, null);
        }
        PolarImplantation.Result result = this.results.get(position);

        if (result != null) {
            TextView pointNumberTextView = (TextView) view.findViewById(R.id.point_number_item);
            TextView horizDirTextView = (TextView) view.findViewById(R.id.hz_item);
            TextView horizDistTextView = (TextView) view.findViewById(R.id.hdist_item);
            TextView distanceTextView = (TextView) view.findViewById(R.id.distance_item);
            TextView zenAngleTextView = (TextView) view.findViewById(R.id.zenithal_item);
            TextView sTextView = (TextView) view.findViewById(R.id.s_item);
            TextView gisementTextView = (TextView) view.findViewById(R.id.gisement_item);

            if (pointNumberTextView != null) {
                pointNumberTextView.setText(result.getPointNumber());
            }

            if (horizDirTextView != null) {
                horizDirTextView.setText(DisplayUtils.formatAngle(result.getHorizDir()));
            }

            if (horizDistTextView != null) {
                horizDistTextView.setText(DisplayUtils.formatDistance(result.getHorizDist()));
            }

            if (distanceTextView != null) {
                distanceTextView.setText(DisplayUtils.formatDistance(result.getDistance()));
            }

            if (zenAngleTextView != null) {
                zenAngleTextView.setText(DisplayUtils.formatAngle(result.getZenAngle()));
            }

            if (sTextView != null) {
                sTextView.setText(DisplayUtils.formatDistance(result.getS()));
            }

            if (gisementTextView != null) {
                gisementTextView.setText(DisplayUtils.formatAngle(result.getGisement()));
            }
        }

        return view;
    }
}
