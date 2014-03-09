package ch.hgdev.toposuite.entry;

import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.utils.AppUtils;

/**
 * Starting activity of the application.
 * 
 * @author HGdev
 * 
 */
public class MainActivity extends TopoSuiteActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        ViewGroup layout = (ViewGroup) this.findViewById(R.id.am_main_layout);
        layout.addView(this.about());
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.app_name);
    }

    /**
     * Set application about informations such as application name, authors,
     * copyright and so on.
     */
    private WebView about() {

        WebView view = new WebView(this);
        StringBuilder html = AppUtils.getAboutString();
        html.append("<p>")
                .append(this.getString(R.string.disclaimer) + ":<br/>")
                .append(this.getString(R.string.disclaimer_notice) + "<br/><br/>")
                .append(this.getString(R.string.disclaimer_text))
                .append("</p>");
        view.loadDataWithBaseURL("file:///android_res/drawable/", html.toString(), "text/html",
                "utf-8", null);
        return view;
    }
}