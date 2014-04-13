package ch.hgdev.toposuite.jobs;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.List;

import org.json.JSONException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.dao.CalculationsDataSource;
import ch.hgdev.toposuite.dao.PointsDataSource;
import ch.hgdev.toposuite.utils.ViewUtils;

import com.google.common.base.Joiner;
import com.google.common.io.Files;

public class JobsActivity extends TopoSuiteActivity implements ExportDialog.ExportDialogListener,
        ImportDialog.ImportDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_jobs);

        // detect if another app is sending data to this activity
        Uri dataUri = this.getIntent().getData();
        if (dataUri != null) {
            this.importJob(dataUri.getPath());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_jobs);
    }

    public void onExportButtonClicked(View view) {
        ExportDialog dialog = new ExportDialog();
        dialog.show(this.getFragmentManager(), "ExportDialogFragment");
    }

    public void onImportButtonClicked(View view) {
        ImportDialog dialog = new ImportDialog();
        dialog.show(this.getFragmentManager(), "ImportDialogFragment");
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

    private void importJob(final String path) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.job_import)
                .setMessage(R.string.warning_import_job_without_warning_label)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.import_label,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                JobsActivity.this.doImportJob(path);
                            }
                        }).setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // nothing
                            }
                        });
        builder.create().show();
    }

    private void doImportJob(String path) {
        File jsonFile = new File(path);
        List<String> lines;
        try {
            lines = Files.readLines(jsonFile,
                    Charset.defaultCharset());

            // remove previous points and calculations from the SQLite DB
            PointsDataSource.getInstance().truncate();
            CalculationsDataSource.getInstance().truncate();

            // clean in-memory residues 
            SharedResources.getSetOfPoints().clear();
            SharedResources.getCalculationsHistory().clear();

            String json = Joiner.on('\n').join(lines);
            Job.loadJobFromJSON(json);
        } catch (IOException e) {
            ViewUtils.showToast(this, e.getMessage());
            return;
        } catch (JSONException e) {
            ViewUtils.showToast(this, e.getMessage());
            return;
        } catch (ParseException e) {
            ViewUtils.showToast(this, e.getMessage());
            return;
        }

        ViewUtils.showToast(this, this.getString(
                R.string.success_import_job_dialog));
    }
}
