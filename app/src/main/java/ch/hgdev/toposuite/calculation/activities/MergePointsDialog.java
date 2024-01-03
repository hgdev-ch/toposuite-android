package ch.hgdev.toposuite.calculation.activities;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.MathUtils;

/**
 * This class is used to display a dialog for merging two points.
 * 
 * @author HGdev
 */
public class MergePointsDialog extends DialogFragment {
    public static final String         POINT_NUMBER                = "point_number";
    public static final String         NEW_EAST                    = "new_east";
    public static final String         NEW_NORTH                   = "new_north";
    public static final String         NEW_ALTITUDE                = "new_altitude";

    /**
     * Merge mode: without altitude. The value depends on the position in the
     * R.array.merge_modes
     */
    private static final int           MERGE_MODE_WITHOUT_ALTITUDE = 1;

    /**
     * Merge mode: altitude only. The value depends on the position in the
     * R.array.merge_modes
     */
    private static final int           MERGE_MODE_ALTITUDE_ONLY    = 2;

    private TextView                   pointNumberTextView;
    private TextView                   actualPointTextView;
    private TextView                   newPointTextView;
    private TextView                   pointDifferencesTextView;
    private Spinner                    mergeTypeSpinner;

    private MergePointsDialogListener  listener;
    private ArrayAdapter<CharSequence> adapter;

    private int                        selectedMode                = 0;
    private Point                      oldPt;
    private Point                      newPt;

    /**
     * Listener for handling dialog events.
     * 
     * @author HGdev
     */
    public interface MergePointsDialogListener {
        /**
         * This callback is triggered when the action performed by the dialog
         * succeed.
         * 
         * @param message
         *            Success message.
         */
        void onMergePointsDialogSuccess(String message);

        /**
         * This callback is triggered when the action performed by the dialog
         * fail.
         * 
         * @param message
         *            Error message.
         */
        void onMergePointsDialogError(String message);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
        d.setTitle(this.getString(R.string.merge_points_label));

        return d;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_merge_points, container, false);

        String pointNumber = this.getArguments().getString(MergePointsDialog.POINT_NUMBER);
        this.oldPt = SharedResources.getSetOfPoints().find(pointNumber);
        this.newPt = new Point(
                "",
                this.getArguments().getDouble(MergePointsDialog.NEW_EAST),
                this.getArguments().getDouble(MergePointsDialog.NEW_NORTH),
                this.getArguments().getDouble(MergePointsDialog.NEW_ALTITUDE),
                false,
                false);

        this.pointNumberTextView = (TextView) view.findViewById(
                R.id.point_number);
        this.pointNumberTextView.setText(
                String.format(
                        this.getActivity().getString(
                                R.string.existing_point_number), pointNumber));

        this.actualPointTextView = (TextView) view.findViewById(
                R.id.actual_point);
        this.actualPointTextView.setText(
                DisplayUtils.formatPoint(this.getActivity(), this.oldPt));

        this.newPointTextView = (TextView) view.findViewById(
                R.id.old_point);
        this.newPointTextView.setText(DisplayUtils.formatPoint(
                this.getActivity(), this.newPt));

        this.pointDifferencesTextView = (TextView) view.findViewById(
                R.id.point_differences);
        this.pointDifferencesTextView.setText(this.calculateDifferences());

        Button meanButton = (Button) view.findViewById(R.id.mean_button);
        meanButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MergePointsDialog.this.performMergeByMeanAction();
            }
        });

        Button replaceButton = (Button) view.findViewById(R.id.replace_button);
        replaceButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MergePointsDialog.this.performMergeByReplaceAction();
            }
        });

        Button keepButton = (Button) view.findViewById(R.id.keep_button);
        keepButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MergePointsDialog.this.performMergeByKeepAction();
            }
        });

        this.mergeTypeSpinner = (Spinner) view.findViewById(R.id.merge_type_spinner);

        this.adapter = ArrayAdapter.createFromResource(
                this.getActivity(),
                R.array.merge_modes,
                android.R.layout.simple_spinner_dropdown_item);
        this.adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        this.mergeTypeSpinner.setAdapter(this.adapter);

        this.mergeTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                MergePointsDialog.this.selectedMode = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nothing
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (MergePointsDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MergePointsDialogListener");
        }
    }

    private String calculateDifferences() {
        double deltaEast = (this.newPt.getEast() - this.oldPt.getEast()) * 100;
        double deltaNorth = (this.newPt.getNorth() - this.oldPt.getNorth()) * 100;
        double fs = Math.sqrt(Math.pow(deltaEast, 2) + Math.pow(deltaNorth, 2));
        double deltaAlt = (this.newPt.getAltitude() - this.oldPt.getAltitude()) * 100;
        return String.format(
                "%s: %s\n%s: %s\n%s: %s\n%s: %s",
                this.getActivity().getString(R.string.east),
                DisplayUtils.formatDifferences(deltaEast),
                this.getActivity().getString(R.string.north),
                DisplayUtils.formatDifferences(deltaNorth),
                this.getActivity().getString(R.string.fs_without_unit_label),
                DisplayUtils.formatDifferences(fs),
                this.getActivity().getString(R.string.fh_label),
                DisplayUtils.formatDifferences(deltaAlt));
    }

    private void closeOnSuccess(String message) {
        this.listener.onMergePointsDialogSuccess(message);
        this.dismiss();
    }

    private void performMergeByMeanAction() {
        if (this.selectedMode == MergePointsDialog.MERGE_MODE_ALTITUDE_ONLY) {
            this.oldPt.setAltitude(
                    (this.newPt.getAltitude() + this.oldPt.getAltitude()) / 2);
        } else {
            this.oldPt.setEast((this.newPt.getEast() + this.oldPt.getEast()) / 2);
            this.oldPt.setNorth((this.newPt.getNorth() + this.oldPt.getNorth()) / 2);

            if (this.selectedMode != MergePointsDialog.MERGE_MODE_WITHOUT_ALTITUDE) {
                if (MathUtils.isZero(this.oldPt.getAltitude())) {
                    this.oldPt.setAltitude(this.newPt.getAltitude());
                } else if (!MathUtils.isZero(this.newPt.getAltitude())) {
                    this.oldPt.setAltitude(
                            (this.newPt.getAltitude() + this.oldPt.getAltitude()) / 2);
                }
            }
        }

        this.closeOnSuccess(this.getActivity().getString(R.string.success_merge));
    }

    private void performMergeByReplaceAction() {
        if (this.selectedMode == MergePointsDialog.MERGE_MODE_ALTITUDE_ONLY) {
            this.oldPt.setAltitude(this.newPt.getAltitude());
        } else {
            this.oldPt.setEast(this.newPt.getEast());
            this.oldPt.setNorth(this.newPt.getNorth());

            if (this.selectedMode != MergePointsDialog.MERGE_MODE_WITHOUT_ALTITUDE) {
                this.oldPt.setAltitude(this.newPt.getAltitude());
            }
        }

        this.closeOnSuccess(this.getActivity().getString(R.string.success_replace));
    }

    private void performMergeByKeepAction() {
        this.closeOnSuccess(this.getActivity().getString(R.string.success_point_kept));
    }
}
