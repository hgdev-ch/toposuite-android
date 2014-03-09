package ch.hgdev.toposuite.settings;

import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup;
import android.webkit.WebView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;

public class AboutActivity extends TopoSuiteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_about);

        ViewGroup layout = (ViewGroup) this.findViewById(R.id.about_layout);
        layout.addView(this.loadAbout());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_about);
    }

    private WebView loadAbout() {
        WebView aboutWebView = new WebView(this);
        aboutWebView.loadUrl("file:///android_asset/about/index.html");
        return aboutWebView;
    }
}
