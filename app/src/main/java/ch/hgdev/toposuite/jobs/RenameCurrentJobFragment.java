package ch.hgdev.toposuite.jobs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.common.io.Files;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.utils.ViewUtils;

/**
 * This class is used to display an export dialog which allows the user to
 * choose the export format and the path where the file will be stored.
 *
 * @author HGdev
 */
public class RenameCurrentJobFragment extends DialogFragment {
    private RenameCurrentJobListener listener;
    private EditText nameEditText;

    /**
     * Listener for handling dialog events.
     *
     * @author HGdev
     */
    public interface RenameCurrentJobListener {
        /**
         * This callback is triggered when the action performed by the dialog
         * succeed.
         *
         * @param message Success message.
         */
        void onRenameCurrentJobSuccess(String message);

        /**
         * This callback is triggered when the action performed by the dialog
         * fail.
         *
         * @param message Error message.
         */
        void onRenameCurrentJobError(String message);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
        d.setTitle(this.getActivity().getString(R.string.job_name));

        return d;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_rename_job, container, false);

        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RenameCurrentJobFragment.this.dismiss();
            }
        });

        Button renameButton = (Button) view.findViewById(R.id.rename_button);
        renameButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RenameCurrentJobFragment.this.performRenameAction();
                RenameCurrentJobFragment.this.dismiss();
            }
        });

        this.nameEditText = (EditText) view.findViewById(R.id.filename_edit_text);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (RenameCurrentJobListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ImportDialogListener");
        }
    }

    /**
     * Perform export.
     */
    private void performRenameAction() {
        // make sure that the input is correct
        if (ViewUtils.readString(this.nameEditText).isEmpty()) {
            this.closeOnError(this.getActivity().getString(R.string.error_fill_data));
        }

        String name = ViewUtils.readString(this.nameEditText);
        // make sure that the user did not provide an extension
        name = Files.getNameWithoutExtension(name);

        if (!Job.renameCurrentJob(name)) {
            this.closeOnError(this.getString(R.string.error_name_exist));
        }
        this.closeOnSuccess(this.getString(R.string.success_rename));
    }

    private void closeOnSuccess(String message) {
        this.listener.onRenameCurrentJobSuccess(message);
        this.dismiss();
    }

    private void closeOnError(String message) {
        this.listener.onRenameCurrentJobError(message);
        this.dismiss();
    }
}
