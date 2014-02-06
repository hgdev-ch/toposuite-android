package ch.hgdev.toposuite.entry;

import java.util.Calendar;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.utils.Logger;

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

    /**
     * Set application about informations such as application name, authors,
     * copyright and so on.
     */
    private WebView about() {
        String appName = this.getString(R.string.app_name);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        WebView view = new WebView(this);
        StringBuilder html = new StringBuilder()
                .append("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />")
                // TODO add logo image
                // .append("<img src=\"file:///android_asset/icon.png\" alt=\"")
                // .append(appName)
                // .append("\"/>")
                .append("<h1>")
                .append(String.format(this.getString(R.string.about_title), appName))
                .append("</h1><p>")
                .append(appName)
                .append(" ")
                .append(String.format(this.getString(R.string.app_version), this.getVersionNumber()))
                .append("</p><p>")
                .append(this.getString(R.string.developed_by))
                .append(":<br/>")
                .append(String.format(
                        this.getString(R.string.app_developer_full_info),
                        this.getString(R.string.app_developer),
                        "<a href=\"" + this.getString(R.string.app_developer_webpage_url) + "\">"
                                + this.getString(R.string.app_developer_webpage_url_short) + "</a>"))
                .append("</p>")
                .append(this.getString(R.string.with_support_from))
                .append(":<br/>")
                .append(this.getString(R.string.cfgeo))
                .append(" - " + "<a href=\"" + this.getString(R.string.cfgeo_webpage_url) + "\">"
                        + this.getString(R.string.cfgeo_webpage_url_short) + "</a>").append("</p></p>")
                .append(String.format(this.getString(R.string.app_copyright), year, this.getString(R.string.crag)))
                .append("</p>");
        view.loadDataWithBaseURL("file:///android_res/drawable/", html.toString(), "text/html", "utf-8", null);
        return view;
    }

    /**
     * Get current application version number.
     * 
     * @return String version of the application.
     */
    private String getVersionNumber() {
        String version = "?";
        try {
            PackageInfo pi = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            version = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(Logger.RESSOURCE_NOT_FOUND, "Application name", e);
        }
        return version;
    }
}