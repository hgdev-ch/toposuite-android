package ch.hgdev.toposuite.jobs;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.google.common.base.Joiner;
import com.google.common.io.Files;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.dao.SQLiteTopoSuiteException;
import ch.hgdev.toposuite.transfer.ImportDialogListener;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.ViewUtils;

/**
 * This class is used to display an import dialog which allows the user to
 * choose the file to import.
 *
 * @author HGdev
 */
public class JobImporterActivity extends TopoSuiteActivity implements ImportDialogListener,
        ActivityCompat.OnRequestPermissionsResultCallback {
    private String path;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.progress = new ProgressDialog(this);
        this.progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progress.setIndeterminate(true);
        this.progress.setCancelable(false);
        this.progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // detect if another app is sending data to this activity
        Uri dataUri = this.getIntent().getData();
        if (dataUri != null) {
            this.path = dataUri.getPath();
            this.importJob();
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_jobs);
    }

    @Override
    public void onImportDialogSuccess(String message) {
        ViewUtils.showToast(this, message);
    }

    @Override
    public void onImportDialogError(String message) {
        ViewUtils.showToast(this, message);
    }

    private void importJob() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.job_import)
                .setMessage(R.string.warning_import_without_warning)
                .setIcon(R.drawable.ic_dialog_warning)
                .setPositiveButton(R.string.import_label,
                        (dialog, which) -> {
                            dialog.dismiss();
                            JobImporterActivity.this.doImportJob();
                        })
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> {
                            dialog.dismiss();
                            JobImporterActivity.this.finish();
                        });
        builder.create().show();
    }

    private void doImportJob() {
        this.progress.show();
        this.progress.setContentView(new ProgressBar(this));

        new Thread(() -> {
            try {
                Job.deleteCurrentJob();
                File jsonFile = new File(JobImporterActivity.this.path);
                List<String> lines;
                lines = Files.readLines(jsonFile, Charset.defaultCharset());
                String json = Joiner.on('\n').join(lines);
                Job.loadJobFromJSON(json);
                Job.renameCurrentJob(Files.getNameWithoutExtension(jsonFile.getName()));
            } catch (IOException e) {
                Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
            } catch (JSONException e) {
                Logger.log(Logger.ErrLabel.PARSE_ERROR, e.getMessage());
            } catch (SQLiteTopoSuiteException e) {
                Logger.log(Logger.ErrLabel.SQL_ERROR, e.getMessage());
            }

            JobImporterActivity.this.runOnUiThread(() -> {
                JobImporterActivity.this.progress.dismiss();
                if (Job.getCurrentJobName() == null) {
                    ViewUtils.showToast(JobImporterActivity.this,
                            JobImporterActivity.this.getString(R.string.error_impossible_to_import));
                } else {
                    ViewUtils.showToast(JobImporterActivity.
                            this, JobImporterActivity.this.getString(R.string.success_import_job_dialog));
                }
                JobImporterActivity.this.finish();
            });
        }).start();
    }
}
