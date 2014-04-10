package ch.hgdev.toposuite.settings;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.utils.AppUtils;

public class AboutActivity extends TopoSuiteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_about);
    }

    @Override
    protected void onResume() {
        super.onResume();

        LinearLayout layout = (LinearLayout) this.findViewById(R.id.layout);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView licensesTextView = new TextView(this);
        licensesTextView.setText(R.string.licenses);
        licensesTextView.setTypeface(null, Typeface.BOLD);
        licensesTextView.setGravity(Gravity.CENTER);
        licensesTextView.setTextSize(28);
        licensesTextView.setBackgroundColor(Color.WHITE);

        layout.addView(this.genAboutView(), params);
        layout.addView(licensesTextView);
        layout.addView(this.genLicencesView(), params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_about);
    }

    /**
     * Generate about webview with application information such as version
     * number.
     * 
     * @return about webview
     */
    private WebView genAboutView() {
        WebView aboutWebView = new WebView(this);

        String appName = AppUtils.getAppName();
        StringBuilder html = new StringBuilder()
                .append("<meta http-equiv='content-type' content='text/html; charset=utf-8' />")
                .append("<div><img src='file:///android_asset/toposuite_logo.png' style='float: left;' alt='")
                .append(appName)
                .append("'/>")
                .append("<h1>")
                .append(appName)
                .append("</h1><p>")
                .append(String.format(App.getContext().getString(R.string.app_version),
                        AppUtils.getVersionName()))
                .append("</p><p>")
                .append(String.format(App.getContext().getString(R.string.app_copyright),
                        AppUtils.getYear(),
                        App.getContext().getString(R.string.crag)))
                .append("</p></div>");
        aboutWebView.loadDataWithBaseURL("file:///android_res/drawable/", html.toString(),
                "text/html", "utf-8", null);

        return aboutWebView;
    }

    /**
     * Load the licenses webview from file.
     * 
     * @return About webview
     */
    private WebView genLicencesView() {
        WebView licensesWebview = new WebView(this);
        licensesWebview.loadUrl("file:///android_asset/about/index.html");
        return licensesWebview;
    }
}
