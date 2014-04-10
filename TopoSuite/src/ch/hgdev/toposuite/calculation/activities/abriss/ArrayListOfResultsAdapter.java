package ch.hgdev.toposuite.calculation.activities.abriss;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.calculation.Abriss;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class ArrayListOfResultsAdapter extends ArrayAdapter<Abriss.Result> {
    private final ArrayList<Abriss.Result> results;

    public ArrayListOfResultsAdapter(Context context, int textViewResourceId,
            ArrayList<Abriss.Result> results) {
        super(context, textViewResourceId, results);
        this.results = results;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.abriss_results_list_item, null);
        }

        Abriss.Result result = this.results.get(position);

        if (result != null) {
            if (result.isDeactivated()) {
                view.setEnabled(false);
            }

            TextView targetPointTextView = (TextView) view.findViewById(R.id.target_point);
            TextView calcDistTextView = (TextView) view.findViewById(R.id.calculated_distance);
            TextView unknOrientTextView = (TextView) view.findViewById(R.id.unknown_orientation);
            TextView errAngTextView = (TextView) view.findViewById(R.id.err_ang_item);
            TextView errTransTextView = (TextView) view.findViewById(R.id.err_trans_item);
            TextView errLonTextView = (TextView) view.findViewById(R.id.err_lon_item);

            if (targetPointTextView != null) {
                targetPointTextView.setText(result.getOrientation().getNumber());
                this.colorizeTextView(targetPointTextView, result);
            }

            if (calcDistTextView != null) {
                calcDistTextView.setText(
                        DisplayUtils.formatDistance(result.getCalculatedDistance()));
                this.colorizeTextView(calcDistTextView, result);
            }

            if (unknOrientTextView != null) {
                unknOrientTextView.setText(
                        DisplayUtils.formatAngle(result.getUnknownOrientation()));
                this.colorizeTextView(unknOrientTextView, result);
            }

            if (errAngTextView != null) {
                errAngTextView.setText(DisplayUtils.formatCC(result.getErrAngle()));
                this.colorizeTextView(errAngTextView, result);
            }

            if (errTransTextView != null) {
                errTransTextView.setText(
                        DisplayUtils.formatGap(result.getErrTrans()));
                this.colorizeTextView(errTransTextView, result);
            }

            if ((errLonTextView != null) && !MathUtils.isZero(result.getDistance())) {
                errLonTextView.setText(
                        DisplayUtils.formatGap(result.getErrLong()));
                this.colorizeTextView(errLonTextView, result);
            }
        }

        return view;
    }

    /**
     * Colorize the TextView according to the Abriss.Result state.
     * 
     * @param tv
     *            A TextView
     * @param r
     *            An Abriss.Result
     */
    private void colorizeTextView(TextView tv, Abriss.Result r) {
        if (r.isDeactivated()) {
            tv.setTextColor(ViewUtils.DEACTIVATED_COLOR);
        } else {
            tv.setTextColor(Color.BLACK);
        }
    }
}