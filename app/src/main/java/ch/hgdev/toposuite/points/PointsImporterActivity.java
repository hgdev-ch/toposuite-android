package ch.hgdev.toposuite.points;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.google.common.io.Files;
import com.google.common.io.LineReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.dao.SQLiteTopoSuiteException;
import ch.hgdev.toposuite.jobs.Job;
import ch.hgdev.toposuite.transfer.ImportDialogListener;
import ch.hgdev.toposuite.transfer.SupportedPointsFileTypes;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.ViewUtils;

public class PointsImporterActivity extends TopoSuiteActivity implements ImportDialogListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private ProgressDialog progress;
    private Uri dataUri;
    private String errMsg;
    private String successMsg;
    private String mime;
    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.errMsg = "";
        this.successMsg = this.getString(R.string.success_import_dialog);

        this.progress = new ProgressDialog(this);
        this.progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progress.setIndeterminate(true);
        this.progress.setCancelable(false);
        this.progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        // detect if another app is sending data to this activity
        this.dataUri = this.getIntent().getData();
        this.mime = this.getIntent().getType();
        this.filename = this.dataUri.getLastPathSegment();
        if (this.dataUri != null) {
            this.importPoints();
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_points_manager);
    }

    @Override
    public void onImportDialogSuccess(String message) {
        ViewUtils.showToast(this, message);
        this.finish();
    }

    @Override
    public void onImportDialogError(String message) {
        ViewUtils.showToast(this, message);
        this.finish();
    }

    private void importPoints() {
        // minor hack to handle LTOP format
        if (this.mime.equals("application/octet-stream") || this.mime.equals("text/plain") || this.mime.isEmpty()) {
            // We need to check if the file is a LTOP file or not.
            // This verification can only be achieved by reading the
            // first line of the file.
            try {
                ContentResolver cr = this.getContentResolver();
                InputStreamReader in = new InputStreamReader(cr.openInputStream(this.dataUri));
                LineReader lr = new LineReader(in);
                String firstLine = lr.readLine();

                if (firstLine == null) {
                    ViewUtils.showToast(this, this.getString(
                            R.string.error_unsupported_format));
                    return;
                } else if ((firstLine.length() >= 4)
                        && firstLine.substring(0, 4).equals("$$PK")) {
                    // fix the MIME type
                    this.mime = "text/ltop";
                } else {
                    // small hack for handling PTP files because there is no
                    // proper way to detect them
                    this.mime = "text/ptp";
                }
            } catch (IOException e) {
                Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
                ViewUtils.showToast(this, e.getMessage());
            }
        }
        this.importFromExternalFile();
    }

    private void importFromExternalFile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.import_label)
                .setMessage(R.string.warning_import_without_warning)
                .setIcon(R.drawable.ic_dialog_warning)
                .setPositiveButton(R.string.import_label,
                        (dialog, which) -> {
                            dialog.dismiss();
                            PointsImporterActivity.this.doImportPoints();
                        })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                    PointsImporterActivity.this.finish();
                });
        builder.create().show();
    }

    private void doImportPoints() {
        this.progress.show();
        this.progress.setContentView(new ProgressBar(this));

        new Thread(() -> {
            ContentResolver cr = PointsImporterActivity.this.getContentResolver();
            String ext = Files.getFileExtension(PointsImporterActivity.this.filename);
            if (ext.isEmpty()) {
                // attempt to detect type via mime then
                ext = PointsImporterActivity.this.mime.substring(PointsImporterActivity.this.mime.lastIndexOf("/") + 1);

                // ugly hack to support ES File Explorer and
                // Samsung's file explorer that set the MIME
                // type of a CSV file to
                // "text/comma-separated-values" instead of
                // "text/csv"
                if (ext.equalsIgnoreCase("comma-separated-values")) {
                    ext = "csv";
                }
            }

            // make sure the file format is supported
            if (SupportedPointsFileTypes.isSupported(ext)) {
                try {
                    Job.deleteCurrentJob();
                    InputStream inputStream = cr.openInputStream(PointsImporterActivity.this.dataUri);
                    List<Pair<Integer, String>> errors = PointsImporter.importFromFile(inputStream, ext);
                    if (!errors.isEmpty()) {
                        PointsImporterActivity.this.errMsg = PointsImporter.formatErrors(ext, errors);
                    }
                } catch (IOException e) {
                    Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
                    PointsImporterActivity.this.errMsg = PointsImporterActivity.this.getString(R.string.error_points_import);
                } catch (SQLiteTopoSuiteException e) {
                    Logger.log(Logger.ErrLabel.SQL_ERROR, e.getMessage());
                    PointsImporterActivity.this.errMsg = PointsImporterActivity.this.getString(R.string.error_points_import);
                }
            } else {
                Logger.log(Logger.ErrLabel.INPUT_ERROR, "unsupported file format: " + ext);
                PointsImporterActivity.this.errMsg = PointsImporterActivity.this.getString(
                        R.string.error_unsupported_format);
            }

            PointsImporterActivity.this.runOnUiThread(() -> {
                PointsImporterActivity.this.progress.dismiss();
                if (PointsImporterActivity.this.errMsg.isEmpty()) {
                    PointsImporterActivity.this.onImportDialogSuccess(PointsImporterActivity.this.successMsg);
                } else {
                    PointsImporterActivity.this.onImportDialogError(PointsImporterActivity.this.errMsg);
                }
            });
        }).start();
    }
}
