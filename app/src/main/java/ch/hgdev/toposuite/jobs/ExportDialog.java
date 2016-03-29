package ch.hgdev.toposuite.jobs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.utils.ViewUtils;

import com.google.common.io.Files;

/**
 * This class is used to display an export dialog which allows the user to
 * choose the export format and the path where the file will be stored.
 * 
 * @author HGdev
 */
public class ExportDialog extends DialogFragment {
    private ExportDialogListener listener;
    private EditText             filenameEditText;

    /**
     * Listener for handling dialog events.
     * 
     * @author HGdev
     */
    public interface ExportDialogListener {
        /**
         * This callback is triggered when the action performed by the dialog
         * succeed.
         * 
         * @param message
         *            Success message.
         */
        void onExportDialogSuccess(String message);

        /**
         * This callback is triggered when the action performed by the dialog
         * fail.
         * 
         * @param error
         *            Error message.
         */
        void onExportDialogError(String message);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
        d.setTitle(this.getActivity().getString(R.string.export));

        return d;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_export_job, container, false);

        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ExportDialog.this.dismiss();
            }
        });

        Button exportButton = (Button) view.findViewById(R.id.export_button);
        exportButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ExportDialog.this.performExportAction();
            }
        });

        this.filenameEditText = (EditText) view.findViewById(R.id.filename_edit_text);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (ExportDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ExportDialogListener");
        }
    }

    private final void closeOnError(String message) {
        this.listener.onExportDialogError(message);
        this.dismiss();
    }

    private final void closeOnSuccess(String message) {
        this.listener.onExportDialogSuccess(message);
        this.dismiss();
    }

    /**
     * Perform export.
     */
    private final void performExportAction() {
        // make sure that the input is correct
        if (this.filenameEditText.getText().toString().isEmpty()) {
            ViewUtils.showToast(this.getActivity(),
                    this.getActivity().getString(R.string.error_fill_data));
            return;
        }

        String filename = this.filenameEditText.getEditableText().toString();

        // make sure the extension is there
        String ext = Files.getFileExtension(filename);
        if (ext.isEmpty() || !ext.equalsIgnoreCase(Job.EXTENSION)) {
            filename = filename.concat("." + Job.EXTENSION);
        }

        File f = new File(this.getActivity().getFilesDir(), filename);
        if (f.isFile()) {
            ViewUtils.showToast(this.getActivity(),
                    this.getActivity().getString(R.string.error_file_already_exists));
            return;
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(
                    new File(App.publicDataDirectory, filename));
            outputStream.write(Job.getCurrentJobAsString().getBytes());
            outputStream.close();
        } catch (IOException e) {
            this.closeOnError(e.getMessage());
        } catch (JSONException e) {
            this.closeOnError(e.getMessage());
        }

        this.closeOnSuccess(this.getActivity().getString(
                R.string.success_export_job_dialog));
    }
}
