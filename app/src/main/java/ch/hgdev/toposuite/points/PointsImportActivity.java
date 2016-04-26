package ch.hgdev.toposuite.points;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Pair;

import com.google.common.io.Files;
import com.google.common.io.LineReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.export.SupportedFileTypes;
import ch.hgdev.toposuite.jobs.ImportDialog;
import ch.hgdev.toposuite.jobs.Job;
import ch.hgdev.toposuite.utils.AppUtils;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.ViewUtils;

public class PointsImportActivity extends TopoSuiteActivity implements ImportDialog.ImportDialogListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private Uri dataUri;
    String mime;
    String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // detect if another app is sending data to this activity
        this.dataUri = this.getIntent().getData();
        this.mime = this.getIntent().getType();
        this.filename = this.dataUri.getLastPathSegment();
        if (this.dataUri != null) {
            if (AppUtils.isPermissionGranted(this, AppUtils.Permission.READ_EXTERNAL_STORAGE)) {
                this.importPoints();
            } else {
                AppUtils.requestPermission(this, AppUtils.Permission.READ_EXTERNAL_STORAGE,
                        String.format(this.getString(R.string.need_storage_access), AppUtils.getAppName()));
            }
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
        ViewUtils.showToast(this, message);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (AppUtils.Permission.valueOf(requestCode)) {
            case READ_EXTERNAL_STORAGE:
                if (AppUtils.isPermissionGranted(this, AppUtils.Permission.READ_EXTERNAL_STORAGE)) {
                    this.importPoints();
                } else {
                    ViewUtils.showToast(this, this.getString(R.string.error_impossible_to_import));
                    this.finish();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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
                .setMessage(R.string.warning_import_file_without_warning_label)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.import_label,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContentResolver cr = PointsImportActivity.this.getContentResolver();
                                String ext = Files.getFileExtension(PointsImportActivity.this.filename);
                                if (ext.isEmpty()) {
                                    // attempt to detect type via mime then
                                    ext = PointsImportActivity.this.mime.substring(PointsImportActivity.this.mime.lastIndexOf("/") + 1);

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
                                if (!SupportedFileTypes.isSupported(ext)) {
                                    ViewUtils.showToast(PointsImportActivity.this, PointsImportActivity.this.getString(
                                            R.string.error_unsupported_format));
                                    PointsImportActivity.this.finish();
                                }

                                try {
                                    Job.deleteCurrentJob();

                                    InputStream inputStream = cr.openInputStream(PointsImportActivity.this.dataUri);
                                    List<Pair<Integer, String>> errors = PointsImporter.importFromFile(inputStream, ext);

                                    if (!errors.isEmpty()) {
                                        PointsImportActivity.this.onImportDialogError(PointsImporter.formatErrors(ext, errors));
                                    } else {
                                        PointsImportActivity.this.onImportDialogSuccess(PointsImportActivity.this.getString(R.string.success_import_dialog));
                                    }
                                } catch (IOException e) {
                                    Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
                                    ViewUtils.showToast(PointsImportActivity.this, e.getMessage());
                                } finally {
                                    PointsImportActivity.this.finish();
                                }
                            }
                        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ViewUtils.showToast(PointsImportActivity.this,
                                PointsImportActivity.this.getString(R.string.error_impossible_to_import));
                        PointsImportActivity.this.finish();
                    }
                });
        builder.create().show();
    }
}
