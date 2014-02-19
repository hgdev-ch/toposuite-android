package ch.hgdev.toposuite.export;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.Logger;

import com.google.common.io.Files;

/**
 * This class is used to display an import dialog which allows the user to
 * choose the file to import.
 * 
 * @author HGdev
 */
public class ImportDialog extends DialogFragment {
    private ImportDialogListener listener;

    private ArrayAdapter<String> adapter;

    private Spinner              filesListSpinner;
    private TextView             fileLastModificationTextView;
    private TextView             fileNumberOfPointsTextView;

    private boolean              isConfirmationAsked = false;

    /**
     * Listener for handling dialog events.
     * 
     * @author HGdev
     */
    public interface ImportDialogListener {
        /**
         * This callback is triggered when the action performed by the dialog
         * succeed.
         * 
         * @param message
         *            Success message.
         */
        void onImportDialogSuccess(String message);

        /**
         * This callback is triggered when the action performed by the dialog
         * fail.
         * 
         * @param error
         *            Error message.
         */
        void onImportDialogError(String message);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
        d.setTitle(this.getString(R.string.import_label));

        return d;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_import_points, container, false);

        this.fileLastModificationTextView = (TextView) view.findViewById(
                R.id.file_last_modification);
        this.fileNumberOfPointsTextView = (TextView) view.findViewById(
                R.id.file_number_of_points);

        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ImportDialog.this.dismiss();
            }
        });

        Button exportButton = (Button) view.findViewById(R.id.export_button);
        exportButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ImportDialog.this.performImportAction();
            }
        });

        this.filesListSpinner = (Spinner) view.findViewById(R.id.files_list_spinner);

        List<String> files = new ArrayList<String>();

        String[] filesList = new File(
                this.getActivity().getFilesDir().getAbsolutePath()).list();
        Arrays.sort(filesList);

        if (filesList.length == 0) {
            files.add(this.getActivity().getString(R.string.no_files));
        } else {
            files.add(this.getActivity().getString(R.string.select_files_3dots));
        }

        for (String s : filesList) {
            files.add(s);
        }

        this.adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_dropdown_item, files);
        this.filesListSpinner.setAdapter(this.adapter);
        this.filesListSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String filename = ImportDialog.this.adapter.getItem(pos);

                // skip when the selected item is the default item of the spinner
                if (filename.equals(ImportDialog.this.getActivity().getString(
                        R.string.select_files_3dots))) {
                    return;
                }

                // reset the confirmation flag
                ImportDialog.this.isConfirmationAsked = false;

                File f = new File(ImportDialog.this.getActivity().getFilesDir(), filename);
                SimpleDateFormat sdf = new SimpleDateFormat(App.dateFormat);

                // display the last modification date of the selected file
                ImportDialog.this.fileLastModificationTextView.setText(
                        String.format(ImportDialog.this.getActivity().getString(
                                R.string.last_modification_label),
                                sdf.format(f.lastModified())));

                try {
                    // display the number of points contained in the file
                    LineNumberReader lnr = new LineNumberReader(new FileReader(f));
                    lnr.skip(Long.MAX_VALUE);
                    ImportDialog.this.fileNumberOfPointsTextView.setText(
                            String.format(ImportDialog.this.getActivity().getString(
                                    R.string.number_of_points_label),
                                    lnr.getLineNumber()));
                    lnr.close();
                } catch (FileNotFoundException e) {
                    Log.e(Logger.TOPOSUITE_IO_ERROR, e.getMessage());
                } catch (IOException e) {
                    Log.e(Logger.TOPOSUITE_IO_ERROR, e.getMessage());
                }
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
            this.listener = (ImportDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ExportDialogListener");
        }
    }

    private final void closeOnError(String message) {
        this.listener.onImportDialogError(message);
        this.dismiss();
    }

    private final void closeOnSuccess(String message) {
        this.listener.onImportDialogSuccess(message);
        this.dismiss();
    }

    /**
     * Import the selected file.
     */
    private void performImportAction() {
        // check use input
        int fileNamePosition = this.filesListSpinner.getSelectedItemPosition();
        if (fileNamePosition == 0) {
            Toast.makeText(this.getActivity(),
                    this.getActivity().getString(R.string.error_choose_file),
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (!App.arePointsExported && !this.isConfirmationAsked) {
            Toast.makeText(
                    this.getActivity(),
                    this.getActivity().getString(R.string.import_confirmation),
                    Toast.LENGTH_LONG).show();
            this.isConfirmationAsked = true;
            return;
        }

        String filename = this.adapter.getItem(fileNamePosition);
        String ext = Files.getFileExtension(filename);

        try {
            InputStream inputStream = this.getActivity().openFileInput(
                    filename);

            if (inputStream != null) {
                // remove previous points and calculations
                SharedResources.getSetOfPoints().clear();
                SharedResources.getCalculationsHistory().clear();

                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(inputStream));
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    Point newPt = new Point();

                    if (ext.equalsIgnoreCase("CSV")) {
                        newPt.createPointFromCSV(line);
                    }

                    SharedResources.getSetOfPoints().add(newPt);
                }
                inputStream.close();
            } else {
                this.closeOnError(this.getActivity().getString(
                        R.string.error_impossible_to_import));
            }
        } catch (FileNotFoundException e) {
            Log.e(Logger.TOPOSUITE_IO_ERROR, e.getMessage());
            Toast.makeText(this.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        } catch (IOException e) {
            Log.e(Logger.TOPOSUITE_IO_ERROR, e.getMessage());
            Toast.makeText(this.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        this.closeOnSuccess(this.getActivity().getString(R.string.success_import_dialog));
    }
}
