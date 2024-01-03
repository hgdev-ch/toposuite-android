package ch.hgdev.toposuite.points;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
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
import ch.hgdev.toposuite.dao.SQLiteTopoSuiteException;
import ch.hgdev.toposuite.jobs.Job;
import ch.hgdev.toposuite.transfer.ImportDialogListener;
import ch.hgdev.toposuite.transfer.SupportedPointsFileTypes;
import ch.hgdev.toposuite.utils.AppUtils;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.ViewUtils;

/**
 * This class is used to display an import dialog which allows the user to
 * choose the file to import.
 *
 * @author HGdev
 */
public class PointsImporterDialog extends DialogFragment {
    private ImportDialogListener listener;

    private ArrayAdapter<String> adapter;

    private Spinner filesListSpinner;
    private TextView fileLastModificationTextView;
    private TextView fileNumberOfPointsTextView;
    private String errMsg;
    private String successMsg;

    @Override
    public
    @NonNull
    Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
        d.setTitle(this.getString(R.string.import_label));
        this.errMsg = "";
        this.successMsg = this.getString(R.string.success_import_dialog);
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
                PointsImporterDialog.this.dismiss();
            }
        });

        Button importButton = (Button) view.findViewById(R.id.import_button);
        importButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PointsImporterDialog.this.doImportPoints();
            }
        });

        this.filesListSpinner = (Spinner) view.findViewById(R.id.files_list_spinner);

        List<String> files = new ArrayList<>();

        String pubDir = AppUtils.publicDataDirectory(PointsImporterDialog.this.getActivity());
        if (pubDir != null) {
            String[] filesList = new File(pubDir).list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return SupportedPointsFileTypes.isSupported(
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
        } else {
            Logger.log(Logger.WarnLabel.RESOURCE_NOT_FOUND, "public data directory");
        }

        this.adapter = new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_spinner_dropdown_item, files);
        this.filesListSpinner.setAdapter(this.adapter);
        this.filesListSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String filename = PointsImporterDialog.this.adapter.getItem(pos);

                // skip when the selected item is the default item of the
                // spinner
                if (filename.equals(PointsImporterDialog.this.getActivity().getString(
                        R.string.select_files_3dots))) {
                    return;
                }

                String pubDir = AppUtils.publicDataDirectory(PointsImporterDialog.this.getActivity());
                if (pubDir != null) {
                    File f = new File(pubDir, filename);

                    // display the last modification date of the selected file
                    PointsImporterDialog.this.fileLastModificationTextView.setText(
                            String.format(PointsImporterDialog.this.getActivity().getString(
                                    R.string.last_modification_label), DisplayUtils.formatDate(f.lastModified())));

                    try {
                        // display the number of points contained in the file
                        LineNumberReader lnr = new LineNumberReader(new FileReader(f));
                        lnr.skip(Long.MAX_VALUE);
                        PointsImporterDialog.this.fileNumberOfPointsTextView.setText(
                                String.format(PointsImporterDialog.this.getActivity().getString(
                                        R.string.number_of_points_label),
                                        lnr.getLineNumber()));
                        lnr.close();
                    } catch (IOException e) {
                        Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
                    }
                } else {
                    Logger.log(Logger.ErrLabel.RESOURCE_NOT_FOUND, "public data directory");
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

    /**
     * Import the selected file.
     */
    private void doImportPoints() {
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
        progress.setCancelable(false);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progress.show();
        progress.setContentView(new ProgressBar(this.getActivity()));

        final Activity callingActivity = this.getActivity();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String filename = PointsImporterDialog.this.adapter.getItem(fileNamePosition);
                String ext = Files.getFileExtension(filename);

                if (SupportedPointsFileTypes.isSupported(ext)) {
                    try {
                        Job.deleteCurrentJob();
                        InputStream inputStream = new FileInputStream(new File(AppUtils.publicDataDirectory(callingActivity), filename));
                        List<Pair<Integer, String>> errors = PointsImporter.importFromFile(inputStream, ext);
                        if (!errors.isEmpty()) {
                            PointsImporterDialog.this.errMsg = PointsImporter.formatErrors(filename, errors);
                        }
                    } catch (IOException e) {
                        Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
                        PointsImporterDialog.this.errMsg = App.getContext().getString(R.string.error_points_import);
                    } catch (SQLiteTopoSuiteException e) {
                        Logger.log(Logger.ErrLabel.SQL_ERROR, e.getMessage());
                        PointsImporterDialog.this.errMsg = App.getContext().getString(R.string.error_points_import);
                    }
                } else {
                    Logger.log(Logger.ErrLabel.INPUT_ERROR, "unsupported file format: " + ext);
                    PointsImporterDialog.this.errMsg = App.getContext().getString(R.string.error_unsupported_format);
                }

                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        if (PointsImporterDialog.this.errMsg.isEmpty()) {
                            PointsImporterDialog.this.listener.onImportDialogSuccess(PointsImporterDialog.this.successMsg);
                        } else {
                            PointsImporterDialog.this.listener.onImportDialogError(PointsImporterDialog.this.errMsg);
                        }
                    }
                });
            }
        }).start();
    }
}