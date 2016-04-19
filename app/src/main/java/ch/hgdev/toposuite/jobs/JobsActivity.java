package ch.hgdev.toposuite.jobs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.View;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.dao.CalculationsDataSource;
import ch.hgdev.toposuite.dao.PointsDataSource;
import ch.hgdev.toposuite.utils.AppUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class JobsActivity extends TopoSuiteActivity implements ExportDialog.ExportDialogListener,
        ImportDialog.ImportDialogListener, ActivityCompat.OnRequestPermissionsResultCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_jobs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_jobs);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (AppUtils.Permission.valueOf(requestCode)) {
            case READ_EXTERNAL_STORAGE:
                if (AppUtils.isPermissionGranted(this, AppUtils.Permission.READ_EXTERNAL_STORAGE)) {
                    this.importJob();
                } else {
                    ViewUtils.showToast(this, this.getString(R.string.error_impossible_to_import));
                }
                break;
            case WRITE_EXTERNAL_STORAGE:
                if (AppUtils.isPermissionGranted(this, AppUtils.Permission.WRITE_EXTERNAL_STORAGE)) {
                    this.exportJob();
                } else {
                    ViewUtils.showToast(this, this.getString(R.string.error_impossible_to_export));
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void onExportButtonClicked(View view) {
        if (AppUtils.isPermissionGranted(this, AppUtils.Permission.WRITE_EXTERNAL_STORAGE)) {
            this.exportJob();
        } else {
            AppUtils.requestPermission(this, AppUtils.Permission.WRITE_EXTERNAL_STORAGE,
                    String.format(this.getString(R.string.need_storage_access), AppUtils.getAppName()));
        }
    }

    public void onImportButtonClicked(View view) {
        if (AppUtils.isPermissionGranted(this, AppUtils.Permission.READ_EXTERNAL_STORAGE)) {
            this.importJob();
        } else {
            AppUtils.requestPermission(this, AppUtils.Permission.READ_EXTERNAL_STORAGE,
                    String.format(this.getString(R.string.need_storage_access), AppUtils.getAppName()));
        }
    }

    public void onClearButtonClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_job)
                .setMessage(R.string.loose_job)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.delete,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // remove previous points and calculations from the SQLite DB
                                PointsDataSource.getInstance().truncate();
                                CalculationsDataSource.getInstance().truncate();

                                // clean in-memory residues 
                                SharedResources.getSetOfPoints().clear();
                                SharedResources.getCalculationsHistory().clear();
                            }
                        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        builder.create().show();
    }

    @Override
    public void onExportDialogSuccess(String message) {
        ViewUtils.showToast(this, message);
    }

    @Override
    public void onExportDialogError(String message) {
        ViewUtils.showToast(this, message);
    }

    @Override
    public void onImportDialogSuccess(String message) {
        ViewUtils.showToast(this, message);
    }

    @Override
    public void onImportDialogError(String message) {
        ViewUtils.showToast(this, message);
    }

    private void exportJob() {
        ExportDialog dialog = new ExportDialog();
        dialog.show(this.getFragmentManager(), "ExportDialogFragment");
    }

    private void importJob() {
        ImportDialog dialog = new ImportDialog();
        dialog.show(this.getFragmentManager(), "ImportDialogFragment");
    }
}
