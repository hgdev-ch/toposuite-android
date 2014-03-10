package ch.hgdev.toposuite.utils;

import java.util.Calendar;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;

/**
 * Provide application specific utilities such a method to provide the
 * application string.
 * 
 * @author HGdev
 * 
 */
public class AppUtils {

    /**
     * Create an html string, meant to be embedded into a textview, with
     * application about information. The caller must load the view with URL
     * from android ressources as such:
     * 
     * view.loadDataWithBaseURL("file:///android_res/drawable/",
     * html.toString(), "text/html", "utf-8", null);
     * 
     * where html is the StringBuilder returned by this method.
     * 
     * @return HTML StringBuilder with application information.
     */
    public static StringBuilder getAboutString() {
        String appName = App.getContext().getString(R.string.app_name);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        StringBuilder html = new StringBuilder()
                .append("<meta http-equiv='content-type' content='text/html; charset=utf-8' />")
                .append("<div><img src='file:///android_asset/toposuite_logo.png' style='float: left;' alt='")
                .append(appName)
                .append("'/>")
                .append("<h1>")
                .append(String.format(App.getContext().getString(R.string.about_title), appName))
                .append("</h1><p>")
                .append(appName)
                .append(" ")
                .append(String.format(App.getContext().getString(R.string.app_version),
                        AppUtils.getVersionNumber()))
                .append("</p></div><br/><p style='clear: both;'>")
                .append(App.getContext().getString(R.string.developed_by))
                .append(":<br/>")
                .append("<img src='file:///android_asset/hgdev_logo.png' alt='")
                .append(App.getContext().getString(R.string.app_developer))
                .append("'/><br/>")
                .append(String.format(
                        App.getContext().getString(R.string.app_developer_full_info),
                        App.getContext().getString(R.string.app_developer),
                        "<a href='"
                                + App.getContext().getString(R.string.app_developer_webpage_url)
                                + "'>"
                                + App.getContext().getString(
                                        R.string.app_developer_webpage_url_short) + "</a>"))
                .append("<p>")
                .append(String.format(App.getContext().getString(R.string.app_copyright), year,
                        App.getContext().getString(R.string.crag)))
                .append("</p><div>")
                .append(App.getContext().getString(R.string.with_support_from))
                .append(":<br/><table><tr><td>")
                .append("<img src='file:///android_asset/cf_geo.png' alt='")
                .append(App.getContext().getString(R.string.cfgeo))
                .append("'/></td><td>")
                .append(App.getContext().getString(R.string.cfgeo))
                .append(" - " + "<a href='"
                        + App.getContext().getString(R.string.cfgeo_webpage_url) + "'>"
                        + App.getContext().getString(R.string.cfgeo_webpage_url_short) + "</a>")
                .append("<tr><td></td><td>" + App.getContext().getString(R.string.cepm))
                .append(" - " + "<a href='"
                        + App.getContext().getString(R.string.cepm_webpage_url) + "'>"
                        + App.getContext().getString(R.string.cepm_webpage_url_short) + "</a>")
                .append("</td></tr>")
                .append("</td></tr></table></div>");

        return html;
    }

    /**
     * Get current application version number.
     * 
     * @return String version of the application.
     */
    public static String getVersionNumber() {
        String version = "?";
        try {
            PackageInfo pi = App.getContext().getPackageManager()
                    .getPackageInfo(App.getContext().getPackageName(), 0);
            version = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(Logger.TOPOSUITE_RESSOURCE_NOT_FOUND, "Application name", e);
        }
        return version;
    }
}
