package ch.hgdev.toposuite.points;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.jobs.Job;
import ch.hgdev.toposuite.transfer.ExportDialogListener;
import ch.hgdev.toposuite.transfer.SupportedPointsFileTypes;
import ch.hgdev.toposuite.utils.AppUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

/**
 * This class is used to display an export dialog which allows the user to
 * choose the export format and the path where the file will be stored.
 *
 * @author HGdev
 */
public class PointsExporterDialog extends DialogFragment {
    private ExportDialogListener listener;

    private Spinner formatSpinner;
    private EditText filenameEditText;

    @NonNull
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
                PointsExporterDialog.this.dismiss();
            }
        });

        Button exportButton = (Button) view.findViewById(R.id.export_button);
        exportButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PointsExporterDialog.this.performExportAction();
            }
        });

        this.formatSpinner = (Spinner) view.findViewById(R.id.format_spinner);

        // XXX since we only support export to CSV for now, we must keep only the
        // first element of the supported file types list.
        // TODO make a new SupportedPointsFileTypes for export only.
        List<String> list = new ArrayList<>();
        list.add(SupportedPointsFileTypes.toList().get(0));
        list.add(0, this.getActivity().getString(R.string.format_3dots));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this.getActivity(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.formatSpinner.setAdapter(adapter);

        this.filenameEditText = (EditText) view.findViewById(R.id.filename_edit_text);
        String name = Job.getCurrentJobName();
        if ((name != null) && (!name.isEmpty())) {
            this.filenameEditText.setText(name);
        }

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

    private void closeOnError(String message) {
        this.listener.onExportDialogError(message);
        this.dismiss();
    }

    private void closeOnSuccess(String message) {
        this.listener.onExportDialogSuccess(message);
        this.dismiss();
    }

    /**
     * Perform export.
     */
    private void performExportAction() {
        // make sure that the input is correct
        if ((this.formatSpinner.getSelectedItemPosition() == 0) || ViewUtils.readString(this.filenameEditText).isEmpty()) {
            ViewUtils.showToast(this.getActivity(), this.getActivity().getString(R.string.error_fill_data));
            return;
        }

        String format = (String) this.formatSpinner.getSelectedItem();
        String filename = this.filenameEditText.getEditableText().toString();

        // at this point we are sure that the user has selected an format
        String ext = Files.getFileExtension(filename);
        if (ext.isEmpty() || !ext.equals(format.toLowerCase(App.getLocale()))) {
            filename += "." + format.toLowerCase(App.getLocale());
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
            SupportedPointsFileTypes type = SupportedPointsFileTypes.valueOf(format);

            switch (type) {
                case CSV:
                    lines = SharedResources.getSetOfPoints().saveAsCSV(
                            this.getActivity(), AppUtils.publicDataDirectory(this.getActivity()), filename);
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
