package ch.hgdev.toposuite.points;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Pair;
import android.widget.ProgressBar;
import android.content.Intent;

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
        
        // Also check for SEND action which might use EXTRA_STREAM instead of getData()
        if (this.dataUri == null && Intent.ACTION_SEND.equals(getIntent().getAction())) {
            this.dataUri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
        }
        
        if (this.dataUri != null) {
            ContentResolver resolver = getContentResolver();
            this.mime = resolver.getType(this.dataUri);
            if (this.mime == null) {
                this.mime = getIntent().getType();
            }
            
            // Get the filename - differs between file:// and content:// URIs
            if ("file".equals(this.dataUri.getScheme())) {
                this.filename = this.dataUri.getLastPathSegment();
            } else {
                // For content:// URIs, try to get the display name
                try (Cursor cursor = resolver.query(this.dataUri, null, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        if (displayNameIndex != -1) {
                            this.filename = cursor.getString(displayNameIndex);
                        }
                    }
                } catch (Exception e) {
                    Logger.log(Logger.ErrLabel.IO_ERROR, "Failed to query content URI: " + e.getMessage());
                }
                
                // Fallback if we couldn't get the name
                if (this.filename == null) {
                    this.filename = this.dataUri.getLastPathSegment();
                }
            }
            
            this.importPoints();
        } else {
            // No data provided, inform the user and close
            //ViewUtils.showToast(this, this.getString(R.string.error_no_file_selected));
            ViewUtils.showToast(this, "no file selected");
            this.finish();
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
            
            // Get extension from filename or content type
            String ext = this.filename != null ? 
                Files.getFileExtension(this.filename).toLowerCase() : "";
                
            if (ext.isEmpty() && this.mime != null && !this.mime.isEmpty()) {
                // Try to get extension from MIME type
                if (this.mime.equals("text/csv") || this.mime.contains("comma-separated-values")) {
                    ext = "csv";
                } else if (this.mime.equals("text/ltop")) {
                    ext = "ltop";
                } else if (this.mime.equals("text/ptp")) {
                    ext = "ptp";
                } else if (this.mime.contains("coo")) {
                    ext = "coo";
                } else if (this.mime.contains("koo")) {
                    ext = "koo";
                }
            }

            // Make sure the file format is supported
            if (SupportedPointsFileTypes.isSupported(ext)) {
                try {
                    Job.deleteCurrentJob();
                    InputStream inputStream = cr.openInputStream(this.dataUri);
                    if (inputStream == null) {
                        throw new IOException("Could not open input stream from URI");
                    }
                    
                    List<Pair<Integer, String>> errors = PointsImporter.importFromFile(inputStream, ext);
                    if (!errors.isEmpty()) {
                        this.errMsg = PointsImporter.formatErrors(this.filename != null ? this.filename : ext, errors);
                    }
                } catch (IOException e) {
                    Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
                    this.errMsg = this.getString(R.string.error_points_import);
                } catch (SQLiteTopoSuiteException e) {
                    Logger.log(Logger.ErrLabel.SQL_ERROR, e.getMessage());
                    this.errMsg = this.getString(R.string.error_points_import);
                }
            } else {
                Logger.log(Logger.ErrLabel.INPUT_ERROR, "Unsupported file format: " + ext);
                this.errMsg = this.getString(R.string.error_unsupported_format);
            }

            this.runOnUiThread(() -> {
                this.progress.dismiss();
                if (this.errMsg.isEmpty()) {
                    this.onImportDialogSuccess(this.successMsg);
                } else {
                    this.onImportDialogError(this.errMsg);
                }
            });
        }).start();
    }
}
