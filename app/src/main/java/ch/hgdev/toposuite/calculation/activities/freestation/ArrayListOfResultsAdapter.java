package ch.hgdev.toposuite.calculation.activities.freestation;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.calculation.FreeStation;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class ArrayListOfResultsAdapter extends ArrayAdapter<FreeStation.Result> {
    private final ArrayList<FreeStation.Result> results;
    private final boolean                       hasAltimetry;

    public ArrayListOfResultsAdapter(Context context, int textViewResourceId,
            ArrayList<FreeStation.Result> results, boolean _hasAltimetry) {
        super(context, textViewResourceId, results);
        this.results = results;
        this.hasAltimetry = _hasAltimetry;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.free_station_results_list_item, null);
        }

        FreeStation.Result result = this.results.get(position);
        if (result != null) {
            TextView numberTextView = (TextView) view.findViewById(R.id.number_item);
            TextView vETextView = (TextView) view.findViewById(R.id.ve_item);
            TextView vNTextView = (TextView) view.findViewById(R.id.vn_item);
            TextView fSTextView = (TextView) view.findViewById(R.id.fs_item);
            TextView vATextView = (TextView) view.findViewById(R.id.va_item);

            if (numberTextView != null) {
                numberTextView.setText(
                        result.getPoint().getNumber());
                this.colorizeTextView(numberTextView, result);
            }

            if (vETextView != null) {
                vETextView.setText(DisplayUtils.formatGap(result.getvE()));
                this.colorizeTextView(vETextView, result);
            }

            if (vNTextView != null) {
                vNTextView.setText(DisplayUtils.formatGap(result.getvN()));
                this.colorizeTextView(vNTextView, result);
            }

            if (fSTextView != null) {
                fSTextView.setText(DisplayUtils.formatGap(result.getfS()));
                this.colorizeTextView(fSTextView, result);
            }

            if (vATextView != null) {
                if (this.hasAltimetry) {
                	if (MathUtils.isIgnorable(result.getPoint().getAltitude())) {
                		vATextView.setText(
                                App.getContext().getString(R.string.no_value));
                	} else {
                		vATextView.setText(DisplayUtils.formatGap(result.getvA()));
                	}
                } else {
                    vATextView.setText(
                            App.getContext().getString(R.string.no_value));
                }
                this.colorizeTextView(vATextView, result);
            }
        }

        return view;
    }

    /**
     * Colorize the TextView according to the FreeStation.Result state.
     * 
     * @param tv
     *            A TextView
     * @param r
     *            A FreeStation.Result
     */
    private void colorizeTextView(TextView tv, FreeStation.Result r) {
        if (r.isDeactivated()) {
            tv.setTextColor(ViewUtils.DEACTIVATED_COLOR);
        } else {
            tv.setTextColor(Color.BLACK);
        }
    }
}
