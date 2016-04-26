package ch.hgdev.toposuite.export;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.common.io.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.dao.CalculationsDataSource;
import ch.hgdev.toposuite.dao.PointsDataSource;
import ch.hgdev.toposuite.jobs.Job;
import ch.hgdev.toposuite.points.PointsImporter;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.ViewUtils;

/**
 * This class is used to display an import dialog which allows the user to
 * choose the file to import.
 *
 * @author HGdev
 */
public class ImportDialog extends DialogFragment {
    private ImportDialogListener listener;

    private ArrayAdapter<String> adapter;

    private Spinner filesListSpinner;
    private TextView fileLastModificationTextView;
    private TextView fileNumberOfPointsTextView;

    /**
     * Listener for handling dialog events.
     *
     * @author HGdev
     */
    public interface ImportDialogListener {
        /**
         * This callback is triggered when the action performed by the dialog
         * succeed.
         */
        void onImportDialogSuccess();

        /**
         * This callback is triggered when the action performed by the dialog
         * fail.
         */
        void onImportDialogError();
    }

    @Override
    public
    @NonNull
    Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
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

        Button importButton = (Button) view.findViewById(R.id.import_button);
        importButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ImportDialog.this.performImportAction();
            }
        });

        this.filesListSpinner = (Spinner) view.findViewById(R.id.files_list_spinner);

        List<String> files = new ArrayList<>();

        String[] filesList = new File(App.publicDataDirectory).list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return SupportedFileTypes.isSupported(
                        Files.getFileExtension(filename));
            }

        });
        Arrays.sort(filesList);

        if (filesList.length == 0) {
            files.add(this.getActivity().getString(R.string.no_files));
        } else {
            files.add(this.getActivity().getString(R.string.select_files_3dots));
        }
        Collections.addAll(files, filesList);

        this.adapter = new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_spinner_dropdown_item, files);
        this.filesListSpinner.setAdapter(this.adapter);
        this.filesListSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String filename = ImportDialog.this.adapter.getItem(pos);

                // skip when the selected item is the default item of the
                // spinner
                if (filename.equals(ImportDialog.this.getActivity().getString(
                        R.string.select_files_3dots))) {
                    return;
                }

                File f = new File(App.publicDataDirectory, filename);

                // display the last modification date of the selected file
                ImportDialog.this.fileLastModificationTextView.setText(
                        String.format(ImportDialog.this.getActivity().getString(
                                R.string.last_modification_label), DisplayUtils.formatDate(f.lastModified())));

                try {
                    // display the number of points contained in the file
                    LineNumberReader lnr = new LineNumberReader(new FileReader(f));
                    lnr.skip(Long.MAX_VALUE);
                    ImportDialog.this.fileNumberOfPointsTextView.setText(
                            String.format(ImportDialog.this.getActivity().getString(
                                    R.string.number_of_points_label),
                                    lnr.getLineNumber()));
                    lnr.close();
                } catch (IOException e) {
                    Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
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
                    + " must implement ImportDialogListener");
        }
    }

    private void closeOnSuccess() {
        this.dismiss();
        this.listener.onImportDialogSuccess();
    }

    private void closeOnError() {
        this.dismiss();
        this.listener.onImportDialogError();
    }

    /**
     * Import the selected file.
     */
    private void performImportAction() {
        // check use input
        final int fileNamePosition = this.filesListSpinner.getSelectedItemPosition();
        if (fileNamePosition == 0) {
            ViewUtils.showToast(this.getActivity(),
                    this.getActivity().getString(R.string.error_choose_file));
            return;
        }

        final Activity act = this.getActivity();
        this.dismiss();

        final ProgressDialog progress = new ProgressDialog(this.getActivity());
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progress.show();
        progress.setContentView(new ProgressBar(this.getActivity()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                // remove previous points and calculations from the SQLite DB
                PointsDataSource.getInstance().truncate();
                CalculationsDataSource.getInstance().truncate();

                // clean in-memory residues
                SharedResources.getSetOfPoints().clear();
                SharedResources.getCalculationsHistory().clear();

                // erase current job name
                Job.setCurrentJobName(null);

                try {
                    String filename = ImportDialog.this.adapter.getItem(fileNamePosition);
                    String ext = Files.getFileExtension(filename);

                    if (SupportedFileTypes.isSupported(ext)) {
                        InputStream inputStream = new FileInputStream(new File(App.publicDataDirectory, filename));

                        List<Pair<Integer, String>> errors = PointsImporter.importFromFile(inputStream, ext);
                        if (errors.isEmpty()) {
                            Job.setCurrentJobName(Files.getNameWithoutExtension(filename));
                        }
                    } else {
                        Logger.log(Logger.ErrLabel.INPUT_ERROR, "unsupported file format: " + ext);
                    }
                } catch (IOException e) {
                    Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
                }

                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        if (Job.getCurrentJobName() == null) {
                            ImportDialog.this.closeOnError();
                        } else {
                            ImportDialog.this.closeOnSuccess();
                        }
                    }
                });
            }
        }).start();
    }
}
