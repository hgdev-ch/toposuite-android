package ch.hgdev.toposuite.export;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
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

        // XXX since we only support export to CSV for now, we must keep only the
        // first element of the supported file types list.
        // TODO make a new SupportedFileTypes for export only.
        //List<String> list = SupportedFileTypes.toList();
        List<String> list = new ArrayList<String>();
        list.add(SupportedFileTypes.toList().get(0));
        list.add(0, this.getActivity().getString(R.string.format_3dots));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this.getActivity(), android.R.layout.simple_spinner_item, list);
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
        if ((this.formatSpinner.getSelectedItemPosition() == 0)
                || this.filenameEditText.getText().toString().isEmpty()) {
            ViewUtils.showToast(this.getActivity(),
                    this.getActivity().getString(R.string.error_fill_data));
            return;
        }

        String format = (String) this.formatSpinner.getSelectedItem();
        String filename = this.filenameEditText.getEditableText().toString();

        // at this point we are sure that the user has selected an format
        String ext = Files.getFileExtension(filename);
        if (ext.isEmpty() || !ext.equals(format.toLowerCase(App.locale))) {
            filename += "." + format.toLowerCase(App.locale);
            this.filenameEditText.setText(filename);
        }

        File f = new File(this.getActivity().getFilesDir(), filename);
        if (f.isFile()) {
            ViewUtils.showToast(this.getActivity(),
                    this.getActivity().getString(R.string.error_file_already_exists));
            return;
        }

        int lines = 0;

        try {
            SupportedFileTypes type = SupportedFileTypes.valueOf(format);

            switch (type) {
            case CSV:
                lines = SharedResources.getSetOfPoints().saveAsCSV(
                        this.getActivity(), App.publicDataDirectory, filename);
                break;
            default:
                ViewUtils.showToast(this.getActivity(),
                        this.getActivity().getString(R.string.error_unsupported_format));
                return;
            }
        } catch (IOException e) {
            this.closeOnError(e.getMessage());
        }

        this.closeOnSuccess(String.format(
                this.getActivity().getString(R.string.success_export_dialog),
                lines));
        App.arePointsExported = true;
    }
}
