package ch.hgdev.toposuite.points;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;

import com.google.common.io.LineReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.export.SupportedFileTypes;
import ch.hgdev.toposuite.jobs.ImportDialog;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.ViewUtils;

public class PointsImportActivity extends TopoSuiteActivity implements ImportDialog.ImportDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // detect if another app is sending data to this activity
        Uri dataUri = this.getIntent().getData();
        if (dataUri != null) {
            String mime = this.getIntent().getType();

            // minor hack to handle LTOP format
            if (mime.equals("application/octet-stream") || mime.equals("text/plain")
                    || mime.isEmpty()) {
                // We need to check if the file is a LTOP file or not.
                // This verification can only be achieved by reading the
                // first line of the file.
                try {
                    ContentResolver cr = this.getContentResolver();
                    InputStreamReader in = new InputStreamReader(
                            cr.openInputStream(dataUri));
                    LineReader lr = new LineReader(in);
                    String firstLine = lr.readLine();

                    if (firstLine == null) {
                        ViewUtils.showToast(this, this.getString(
                                R.string.error_unsupported_format));
                        return;
                    } else if ((firstLine.length() >= 4)
                            && firstLine.substring(0, 4).equals("$$PK")) {
                        // fix the MIME type
                        mime = "text/ltop";
                    } else {
                        // small hack for handling PTP files because there is no
                        // proper way to detect them
                        mime = "text/ptp";
                    }
                } catch (IOException e) {
                    Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
                    ViewUtils.showToast(this, e.getMessage());
                }
            }
            this.importFromExternalFile(dataUri, mime);
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_points_manager);
    }

    @Override
    public void onImportDialogSuccess(String message) {
        ViewUtils.showToast(this, message);
    }

    @Override
    public void onImportDialogError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error_import_label)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(message)
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        });
        builder.create().show();
    }

    private void importFromExternalFile(final Uri dataUri, final String mime) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.import_label)
                .setMessage(R.string.warning_import_file_without_warning_label)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.import_label,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContentResolver cr = PointsImportActivity.this
                                        .getContentResolver();
                                String ext = mime.substring(mime.lastIndexOf("/") + 1);

                                // ugly hack to support ES File Explorer and
                                // Samsung's file explorer that set the MIME
                                // type of a CSV file to
                                // "text/comma-separated-values" instead of
                                // "text/csv"
                                if (ext.equalsIgnoreCase("comma-separated-values")) {
                                    ext = "csv";
                                }

                                // make sure the file format is supported
                                if (!SupportedFileTypes.isSupported(ext)) {
                                    ViewUtils.showToast(PointsImportActivity.this,
                                            PointsImportActivity.this.getString(
                                                    R.string.error_unsupported_format));
                                    return;
                                }

                                try {
                                    // clear existing points and calculations
                                    SharedResources.getCalculationsHistory().clear();
                                    SharedResources.getSetOfPoints().clear();

                                    InputStream inputStream = cr.openInputStream(dataUri);
                                    List<Pair<Integer, String>> errors = PointsImporter.importFromFile(inputStream, ext);

                                    if (!errors.isEmpty()) {
                                        dialog.dismiss();
                                        PointsImportActivity.this.onImportDialogError(PointsImporter.formatErrors(ext, errors));
                                    }
                                } catch (IOException e) {
                                    Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
                                    ViewUtils.showToast(PointsImportActivity.this, e.getMessage());
                                }
                                PointsImportActivity.this.finish();
                            }
                        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PointsImportActivity.this.finish();
                    }
                });
        builder.create().show();
    }
}
