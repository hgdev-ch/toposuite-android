package ch.hgdev.toposuite.calculation.activities.freestation;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.calculation.FreeStation;
import ch.hgdev.toposuite.utils.DisplayUtils;

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
                        DisplayUtils.toStringForTextView(result.getPoint().getNumber()));
            }

            if (vETextView != null) {
                vETextView.setText(DisplayUtils.toStringForTextView(result.getvE()));
            }

            if (vNTextView != null) {
                vNTextView.setText(DisplayUtils.toStringForTextView(result.getvN()));
            }

            if (fSTextView != null) {
                fSTextView.setText(DisplayUtils.toStringForTextView(result.getfS()));
            }

            if (vATextView != null) {
                if (this.hasAltimetry) {
                    vATextView.setText(DisplayUtils.toStringForTextView(result.getvA()));
                } else {
                    vATextView.setText(
                            App.getContext().getString(R.string.no_value));
                }
            }
        }

        return view;
    }
}
