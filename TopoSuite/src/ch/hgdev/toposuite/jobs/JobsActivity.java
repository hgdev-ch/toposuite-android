package ch.hgdev.toposuite.jobs;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.utils.ViewUtils;

public class JobsActivity extends TopoSuiteActivity implements ExportDialog.ExportDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_jobs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.jobs, menu);
        return true;
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_jobs);
    }

    public void onExportButtonClicked(View view) {
        ExportDialog dialog = new ExportDialog();
        dialog.show(this.getFragmentManager(), "ExportDialogFragments");
    }

    public void onImportButtonClicked(View view) {

    }

    public void onClearButtonClicked(View view) {

    }

    @Override
    public void onExportDialogSuccess(String message) {
        ViewUtils.showToast(this, message);
    }

    @Override
    public void onExportDialogError(String message) {
        ViewUtils.showToast(this, message);
    }
}
