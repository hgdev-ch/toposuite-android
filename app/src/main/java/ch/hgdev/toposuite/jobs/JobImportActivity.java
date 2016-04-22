package ch.hgdev.toposuite.jobs;

import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.common.base.Joiner;
import com.google.common.io.Files;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.List;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.dao.CalculationsDataSource;
import ch.hgdev.toposuite.dao.PointsDataSource;
import ch.hgdev.toposuite.utils.AppUtils;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.ViewUtils;

/**
 * This class is used to display an import dialog which allows the user to
 * choose the file to import.
 *
 * @author HGdev
 */
public class JobImportActivity extends TopoSuiteActivity implements ImportDialog.ImportDialogListener,
        ActivityCompat.OnRequestPermissionsResultCallback {
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // detect if another app is sending data to this activity
        Uri dataUri = this.getIntent().getData();
        if (dataUri != null) {
            this.path = dataUri.getPath();
            if (AppUtils.isPermissionGranted(this, AppUtils.Permission.READ_EXTERNAL_STORAGE)) {
                this.importJob();
            } else {
                AppUtils.requestPermission(this, AppUtils.Permission.READ_EXTERNAL_STORAGE,
                        String.format(this.getString(R.string.need_storage_access), AppUtils.getAppName()));
            }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (AppUtils.Permission.valueOf(requestCode)) {
            case READ_EXTERNAL_STORAGE:
                if (AppUtils.isPermissionGranted(this, AppUtils.Permission.READ_EXTERNAL_STORAGE)) {
                    this.importJob();
                } else {
                    ViewUtils.showToast(this, this.getString(R.string.error_impossible_to_import));
                    this.finish();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void importJob() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.job_import)
                .setMessage(R.string.warning_import_job_without_warning_label)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.import_label,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                JobImportActivity.this.doImportJob();
                                JobImportActivity.this.finish();
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                JobImportActivity.this.finish();
                            }
                        });
        builder.create().show();
    }

    private void doImportJob() {
        File jsonFile = new File(this.path);
        List<String> lines;
        try {
            lines = Files.readLines(jsonFile, Charset.defaultCharset());

            // remove previous points and calculations from the SQLite DB
            PointsDataSource.getInstance().truncate();
            CalculationsDataSource.getInstance().truncate();

            // clean in-memory residues
            SharedResources.getSetOfPoints().clear();
            SharedResources.getCalculationsHistory().clear();

            String json = Joiner.on('\n').join(lines);
            Job.loadJobFromJSON(json);
        } catch (IOException e) {
            Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
            ViewUtils.showToast(this, e.getMessage());
            return;
        } catch (JSONException | ParseException e) {
            Logger.log(Logger.ErrLabel.PARSE_ERROR, e.getMessage());
            ViewUtils.showToast(this, e.getMessage());
            return;
        }

        ViewUtils.showToast(this, this.getString(R.string.success_import_job_dialog));
    }
}
