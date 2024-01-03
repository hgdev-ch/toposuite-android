package ch.hgdev.toposuite.jobs;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.ShareActionProvider;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.common.base.Joiner;
import com.google.common.io.Files;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.dao.SQLiteTopoSuiteException;
import ch.hgdev.toposuite.utils.AppUtils;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.ViewUtils;

public class JobsActivity extends TopoSuiteActivity implements
        RenameCurrentJobFragment.RenameCurrentJobListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private ListView jobsListView;
    private TextView jobNameTextView;
    private ArrayListOfJobsAdapter adapter;
    private ShareActionProvider shareActionProvider;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_jobs);

        this.jobsListView = (ListView) this.findViewById(R.id.apm_list_of_jobs);
        this.registerForContextMenu(this.jobsListView);

        this.jobNameTextView = (TextView) this.findViewById(R.id.current_job);

        this.progress = new ProgressDialog(this);
        this.progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progress.setIndeterminate(true);
        this.progress.setCancelable(false);
        this.progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.jobNameTextView.setText(DisplayUtils.format(Job.getCurrentJobName()));
        this.drawList();
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_jobs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.action_jobs, menu);

        MenuItem item = menu.findItem(R.id.share_job_button);
        this.shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        this.updateShareIntent();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.context_list_row_delete, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.rename_job_button:
                this.renameJob();
                return true;
            case R.id.save_job_button:
                if (Job.getCurrentJobName() == null) {
                    ViewUtils.showToast(this, this.getString(R.string.error_job_no_name));
                    return true;
                }
                if (AppUtils.isPermissionGranted(this, AppUtils.Permission.WRITE_EXTERNAL_STORAGE)) {
                    this.saveJob();
                } else {
                    AppUtils.requestPermission(this, AppUtils.Permission.WRITE_EXTERNAL_STORAGE,
                            String.format(this.getString(R.string.need_storage_access), AppUtils.getAppName()));
                }
                return true;
            case R.id.clear_job_button:
                this.clearJob();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = (int) info.id;
        switch (item.getItemId()) {
            case R.id.delete_button:
                if (AppUtils.isPermissionGranted(JobsActivity.this, AppUtils.Permission.READ_EXTERNAL_STORAGE)) {
                    JobsActivity.this.deleteJob(position);
                } else {
                    AppUtils.requestPermission(JobsActivity.this, AppUtils.Permission.READ_EXTERNAL_STORAGE,
                            String.format(JobsActivity.this.getString(R.string.need_storage_access), AppUtils.getAppName()));
                    if (AppUtils.isPermissionGranted(JobsActivity.this, AppUtils.Permission.READ_EXTERNAL_STORAGE)) {
                        JobsActivity.this.deleteJob(position);
                    }
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (AppUtils.Permission.valueOf(requestCode)) {
            case READ_EXTERNAL_STORAGE:
                if (!AppUtils.isPermissionGranted(this, AppUtils.Permission.READ_EXTERNAL_STORAGE)) {
                    ViewUtils.showToast(this, this.getString(R.string.error_impossible_to_import));
                }
                break;
            case WRITE_EXTERNAL_STORAGE:
                if (AppUtils.isPermissionGranted(this, AppUtils.Permission.WRITE_EXTERNAL_STORAGE)) {
                    this.saveJob();
                } else {
                    ViewUtils.showToast(this, this.getString(R.string.error_impossible_to_export));
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Draw the main table containing all the points.
     */
    private void drawList() {
        if (AppUtils.isPermissionGranted(JobsActivity.this, AppUtils.Permission.READ_EXTERNAL_STORAGE)) {
            this.drawJobsList();
        } else {
            AppUtils.requestPermission(JobsActivity.this, AppUtils.Permission.READ_EXTERNAL_STORAGE,
                    String.format(JobsActivity.this.getString(R.string.need_storage_access), AppUtils.getAppName()));
            if (AppUtils.isPermissionGranted(JobsActivity.this, AppUtils.Permission.READ_EXTERNAL_STORAGE)) {
                this.drawJobsList();
            }
        }
    }

    private void drawJobsList() {
        this.adapter = new ArrayListOfJobsAdapter(this, R.layout.jobs_list_item, Job.getJobsList());
        this.jobsListView.setAdapter(this.adapter);
        this.jobsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (AppUtils.isPermissionGranted(JobsActivity.this, AppUtils.Permission.READ_EXTERNAL_STORAGE)) {
                    JobsActivity.this.importJob(position);
                } else {
                    AppUtils.requestPermission(JobsActivity.this, AppUtils.Permission.READ_EXTERNAL_STORAGE,
                            String.format(JobsActivity.this.getString(R.string.need_storage_access), AppUtils.getAppName()));
                    if (AppUtils.isPermissionGranted(JobsActivity.this, AppUtils.Permission.READ_EXTERNAL_STORAGE)) {
                        JobsActivity.this.importJob(position);
                    }
                }
            }
        });
    }

    private void importJob(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.job_import)
                .setMessage(R.string.warning_import_without_warning)
                .setIcon(R.drawable.ic_dialog_warning)
                .setPositiveButton(R.string.import_label,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                JobsActivity.this.doImportJob(pos);
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // nothing
                            }
                        });
        builder.create().show();
    }

    private void doImportJob(final int pos) {
        this.progress.show();
        this.progress.setContentView(new ProgressBar(this));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Job.deleteCurrentJob();
                    Job job = JobsActivity.this.adapter.getItem(pos);
                    File f = job.getTpst();
                    List<String> lines = Files.readLines(f, Charset.defaultCharset());
                    String json = Joiner.on('\n').join(lines);
                    Job.loadJobFromJSON(json);
                    Job.renameCurrentJob(Files.getNameWithoutExtension(f.getName()));
                } catch (IOException e) {
                    Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
                } catch (JSONException e) {
                    Logger.log(Logger.ErrLabel.PARSE_ERROR, e.getMessage());
                } catch (SQLiteTopoSuiteException e) {
                    Logger.log(Logger.ErrLabel.SQL_ERROR, e.getMessage());
                }

                JobsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JobsActivity.this.progress.dismiss();
                        JobsActivity.this.jobNameTextView.setText(DisplayUtils.format(Job.getCurrentJobName()));
                        JobsActivity.this.drawList();
                        JobsActivity.this.updateShareIntent();
                        if (Job.getCurrentJobName() == null) {
                            ViewUtils.showToast(JobsActivity.this, JobsActivity.this.getString(R.string.error_impossible_to_import));
                        } else {
                            ViewUtils.showToast(JobsActivity.this, JobsActivity.this.getString(R.string.success_import_job_dialog));
                        }
                    }
                });
            }
        }).start();
    }

    private void deleteJob(int pos) {
        Job job = this.adapter.getItem(pos);
        File f = job.getTpst();
        if (f.delete()) {
            ViewUtils.showToast(this, this.getString(R.string.deletion_success));
            this.drawList();
        } else {
            ViewUtils.showToast(this, this.getString(R.string.deletion_failure));
        }
    }

    private void saveJob() {
        String currentJob = Job.getCurrentJobName();
        if (currentJob == null) {
            this.renameJob();
            return;
        }

        String filename = currentJob.concat("." + Job.EXTENSION);
        File f = new File(this.getFilesDir(), filename);
        if (f.isFile()) {
            ViewUtils.showToast(this, this.getString(R.string.error_file_already_exists));
            return;
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(new File(AppUtils.publicDataDirectory(this), filename));
            outputStream.write(Job.getCurrentJobAsJson().getBytes());
            outputStream.close();
            ViewUtils.showToast(this, this.getString(R.string.success_export_job_dialog));
        } catch (IOException | JSONException e) {
            Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
            ViewUtils.showToast(this, this.getString(R.string.error_impossible_to_export));
        } finally {
            this.drawList();
        }
    }

    private void clearJob() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_job)
                .setMessage(R.string.loose_job)
                .setIcon(R.drawable.ic_dialog_warning)
                .setPositiveButton(R.string.delete,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Job.deleteCurrentJob();
                                } catch (SQLiteTopoSuiteException e) {
                                    Logger.log(Logger.ErrLabel.SQL_ERROR, e.getMessage());
                                }
                                JobsActivity.this.jobNameTextView.setText(DisplayUtils.format(Job.getCurrentJobName()));
                                JobsActivity.this.updateShareIntent();
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

    private void renameJob() {
        RenameCurrentJobFragment dialog = new RenameCurrentJobFragment();
        dialog.show(this.getSupportFragmentManager(), "RenameCurrentJobFragment");
    }

    @Override
    public void onRenameCurrentJobSuccess(String message) {
        this.jobNameTextView.setText(DisplayUtils.format(Job.getCurrentJobName()));
        ViewUtils.showToast(this, message);
        this.updateShareIntent();
    }

    @Override
    public void onRenameCurrentJobError(String message) {
        ViewUtils.showToast(this, message);
    }

    /**
     * Update the share intent.
     */
    private void updateShareIntent() {
        try {
            final File tmpTpstPath = new File(this.getCacheDir(), "jobs");
            if (!tmpTpstPath.exists()) {
                if (!tmpTpstPath.mkdir()) {
                    Logger.log(Logger.ErrLabel.IO_ERROR, "failed to create directory " + tmpTpstPath.getAbsolutePath());
                }
            }
            String currentJobName = Job.getCurrentJobName();
            String name = (currentJobName == null) || (currentJobName.isEmpty()) ? "job" : currentJobName;
            final File tmpTpstFile = new File(tmpTpstPath, name + "." + Job.EXTENSION);
            FileOutputStream outputStream = new FileOutputStream(tmpTpstFile);
            outputStream.write(Job.getCurrentJobAsJson().getBytes());
            outputStream.close();
            final Uri uri = FileProvider.getUriForFile(this, this.getPackageName(), tmpTpstFile);
            final Intent sendIntent = ShareCompat.IntentBuilder.from(this)
                    .setType("text/tpst")
                    .setStream(uri).getIntent()
                    .setAction(Intent.ACTION_SEND)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            this.setShareIntent(sendIntent);
        } catch (IOException e) {
            Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
        } catch (JSONException e) {
            Logger.log(Logger.ErrLabel.SERIALIZATION_ERROR, e.getMessage());
        }
    }

    /**
     * Call to update the share intent
     *
     * @param shareIntent The share intent.
     */
    private void setShareIntent(Intent shareIntent) {
        if (this.shareActionProvider != null) {
            this.shareActionProvider.setShareIntent(shareIntent);
        }
    }
}
