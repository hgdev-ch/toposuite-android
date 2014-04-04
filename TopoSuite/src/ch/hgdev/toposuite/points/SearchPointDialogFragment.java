package ch.hgdev.toposuite.points;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.utils.ViewUtils;

/**
 * @author HGdev
 * 
 */
public class SearchPointDialogFragment extends DialogFragment {
    /**
     * The activity that creates an instance of SearchPointDialogFragment must
     * implement this interface in order to receive event callbacks. Each method
     * passes the DialogFragment in case the host needs to query it.
     * 
     * @author HGdev
     * 
     */
    public interface SearchPointDialogListener {
        /**
         * Define what to do when the "Cancel" button is clicked
         * 
         * @param dialog
         *            Dialog with NO useful information to fetch from.
         */
        void onDialogCancel(SearchPointDialogFragment dialog);

        /**
         * Define what to do when the "Search" button is clicked.
         * 
         * @param dialog
         *            Dialog to fetch information from.
         */
        void onDialogSearch(SearchPointDialogFragment dialog);
    }

    SearchPointDialogListener listener;
    private EditText          pointNumberEditText;
    private String            pointNumber;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.initAttributes();
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(this.getActivity().getString(R.string.point_search))
                .setView(this.pointNumberEditText)
                .setPositiveButton(this.getActivity().getString(R.string.search),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (SearchPointDialogFragment.this.checkDialogInputs()) {
                                    SearchPointDialogFragment.this.pointNumber =
                                            SearchPointDialogFragment.this.pointNumberEditText
                                                    .getText().toString();
                                    SearchPointDialogFragment.this.listener
                                            .onDialogSearch(SearchPointDialogFragment.this);
                                } else {
                                    ViewUtils.showToast(
                                            SearchPointDialogFragment.this.getActivity(),
                                            SearchPointDialogFragment.this.getActivity().getString(
                                                    R.string.error_fill_data));
                                }
                            }
                        })
                .setNegativeButton(this.getActivity().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SearchPointDialogFragment.this.listener
                                        .onDialogCancel(SearchPointDialogFragment.this);
                            }
                        });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (SearchPointDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SearchPointDialogListener");
        }
    }

    /**
     * Verify that the user has entered required input.
     * 
     * @return True if the EditText has been filled, false otherwise.
     */
    private boolean checkDialogInputs() {
        if (this.pointNumberEditText.length() == 0) {
            return false;
        }
        return true;
    }

    /**
     * Initializes class attributes.
     */
    private void initAttributes() {
        this.pointNumberEditText = new EditText(this.getActivity());
        this.pointNumberEditText.setHint(this.getActivity().getString(R.string.point_number_3dots));
        this.pointNumber = "";
    }

    public String getPointNumber() {
        return this.pointNumber;
    }
}
