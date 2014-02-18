package ch.hgdev.toposuite.export;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;

import com.google.common.io.Files;

/**
 * This class is used to display an export dialog which allows the user to
 * choose the export format and the path where the file will be stored.
 * 
 * @author HGdev
 */
public class ExportDialog extends DialogFragment {
    private ExportDialogListener listener;

    private Spinner              formatSpinner;
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
        void onDialogSuccess(String message);

        /**
         * This callback is triggered when the action performed by the dialog
         * fail.
         * 
         * @param error
         *            Error message.
         */
        void onDialogError(String message);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
        d.setTitle("Export");

        return d;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_export_points, container, false);

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

        this.formatSpinner = (Spinner) view.findViewById(R.id.format_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this.getActivity(), R.array.file_formats,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.formatSpinner.setAdapter(adapter);

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
        this.listener.onDialogError(message);
        this.dismiss();
    }

    private final void closeOnSuccess(String message) {
        this.listener.onDialogSuccess(message);
        this.dismiss();
    }

    /**
     * Perform export.
     */
    private final void performExportAction() {
        if ((this.formatSpinner.getSelectedItemPosition() == 0)
                || this.filenameEditText.getText().toString().isEmpty()) {
            Toast.makeText(this.getActivity(), R.string.error_fill_data,
                    Toast.LENGTH_LONG).show();
            return;
        }

        String format = (String) this.formatSpinner.getSelectedItem();
        String filename = this.filenameEditText.getEditableText().toString();

        // at this point we are sure that the user has selected an format
        String ext = Files.getFileExtension(filename);
        if (ext.isEmpty() || !ext.equals(format.toLowerCase())) {
            filename += "." + format.toLowerCase();
            this.filenameEditText.setText(filename);
        }

        File f = new File(this.getActivity().getFilesDir(), filename);
        if (f.isFile()) {
            Toast.makeText(this.getActivity(), R.string.error_file_already_exists,
                    Toast.LENGTH_LONG).show();
            return;
        }

        int lines = 0;

        try {
            if (format.equalsIgnoreCase("CSV")) {
                lines = SharedResources.getSetOfPoints().saveAsCSV(
                        this.getActivity(), filename);
            } else {
                Toast.makeText(this.getActivity(), R.string.error_unsupported_format,
                        Toast.LENGTH_LONG).show();
                return;
            }
        } catch (IOException e) {
            this.closeOnError(e.getMessage());
        }

        this.closeOnSuccess(String.format(
                this.getActivity().getString(R.string.success_export_dialog),
                lines));
    }
}
