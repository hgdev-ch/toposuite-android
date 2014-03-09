package ch.hgdev.toposuite.settings;

import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.utils.AppUtils;

public class AboutActivity extends TopoSuiteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_about);

        ScrollView aboutScrollView = (ScrollView) this.findViewById(R.id.about_scrollview);
        aboutScrollView.addView(this.genAboutView());

        FrameLayout licensesFrameLayout = (FrameLayout) this.findViewById(R.id.licenses_layout);
        licensesFrameLayout.addView(this.genLicencesView());
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
        LayoutParams params = new LayoutParams(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        aboutWebView.setLayoutParams(params);

        StringBuilder html = AppUtils.getAboutString();
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
        LayoutParams params = new LayoutParams(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        licensesWebview.setLayoutParams(params);
        licensesWebview.loadUrl("file:///android_asset/about/index.html");
        return licensesWebview;
    }
}
