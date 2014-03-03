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
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.FreeStation;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class ArrayListOfResultsAdapter extends ArrayAdapter<FreeStation.Result> {
    private final ArrayList<FreeStation.Result> results;

    public ArrayListOfResultsAdapter(Context context, int textViewResourceId,
            ArrayList<FreeStation.Result> results) {
        super(context, textViewResourceId, results);
        this.results = results;
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
            TextView vATextView = (TextView) view.findViewById(R.id.va_item);
            TextView fSTextView = (TextView) view.findViewById(R.id.fs_item);

            if (numberTextView != null) {
                String numberText = DisplayUtils.toString(result.getPoint().getNumber()) + " ";
                FreeStation.Result r = this.getItem(position);
                Point point = new Point(
                        r.getPoint().getNumber(),
                        r.getPoint().getEast(),
                        r.getPoint().getNorth(),
                        0.0,
                        false);
                Point p = SharedResources.getSetOfPoints().find(r.getPoint().getNumber());
                numberText += point.equals(p) ?
                        App.getContext().getString(R.string.heavy_checkmark) :
                        App.getContext().getString(R.string.heavy_ballot);
                numberTextView.setText(numberText);
            }

            if (vETextView != null) {
                vETextView.setText(DisplayUtils.toString(result.getvE()));
            }

            if (vNTextView != null) {
                vNTextView.setText(DisplayUtils.toString(result.getvN()));
            }

            if (vATextView != null) {
                vATextView.setText(DisplayUtils.toString(result.getvA()));
            }

            if (fSTextView != null) {
                fSTextView.setText(DisplayUtils.toString(result.getfS()));
            }
        }

        return view;
    }
}
