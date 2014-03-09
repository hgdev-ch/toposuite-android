package ch.hgdev.toposuite.calculation.activities.abriss;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.calculation.Abriss;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.MathUtils;

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
            TextView targetPointTextView = (TextView) view.findViewById(R.id.target_point);
            TextView calcDistTextView = (TextView) view.findViewById(R.id.calculated_distance);
            TextView unknOrientTextView = (TextView) view.findViewById(R.id.unknown_orientation);
            TextView errAngTextView = (TextView) view.findViewById(R.id.err_ang_item);
            TextView errTransTextView = (TextView) view.findViewById(R.id.err_trans_item);
            TextView errLonTextView = (TextView) view.findViewById(R.id.err_lon_item);

            if (targetPointTextView != null) {
                targetPointTextView.setText(DisplayUtils.toStringForTextView(result.getOrientation()
                        .getNumber()));
            }

            if (calcDistTextView != null) {
                calcDistTextView.setText(DisplayUtils.toStringForTextView(result.getCalculatedDistance()));
            }

            if (unknOrientTextView != null) {
                unknOrientTextView.setText(DisplayUtils.toStringForTextView(result.getUnknownOrientation()));
            }

            if (errAngTextView != null) {
                errAngTextView.setText(DisplayUtils.formatCC(result.getErrAngle()));
            }

            if (errTransTextView != null) {
                errTransTextView.setText(DisplayUtils.toStringForTextView(
                        result.getErrTrans(), App.smallNumberOfDecimals));
            }

            if ((errLonTextView != null) && !MathUtils.isZero(result.getDistance())) {
                errLonTextView.setText(DisplayUtils.toStringForTextView(
                        result.getErrLong(), App.smallNumberOfDecimals));
            }
        }

        return view;
    }
}