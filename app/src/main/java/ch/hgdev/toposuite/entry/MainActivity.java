package ch.hgdev.toposuite.entry;

import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebView;
import ch.hgdev.toposuite.App;
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
        String appName = AppUtils.getAppName();

        WebView view = new WebView(this);
        StringBuilder html = new StringBuilder();

        // header, copyright, app logo, dev logo and link
        html.append("<meta http-equiv='content-type' content='text/html; charset=utf-8' />")
                .append("<div><img src='file:///android_asset/toposuite_logo.png' style='float: left;' alt='")
                .append(appName)
                .append("'/>")
                .append("<h1>")
                .append(appName)
                .append("</h1>")
                .append("<p>")
                .append(App.getContext().getString(R.string.app_website))
                .append("&nbsp;<a href='http://toposuite.hgdev.ch'>toposuite.hgdev.ch</a>")
                .append("</p>")
                .append("<p>")
                .append(String.format(App.getContext().getString(R.string.app_copyright),
                        AppUtils.getYear(),
                        App.getContext().getString(R.string.crag)))
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
                .append("<div>");

        // sponsors list, in a two column tables (first one for the logo, second
        // one for name and link)
        html.append(App.getContext().getString(R.string.with_support_from))
                .append(":<br/>")
                .append("<table>");
        // CFGeo
        html.append("<tr><td>")
                .append("<img src='file:///android_asset/cf_geo.png' alt='")
                .append(App.getContext().getString(R.string.cfgeo))
                .append("'/></td><td>")
                .append(App.getContext().getString(R.string.cfgeo))
                .append(" - " + "<a href='"
                        + App.getContext().getString(R.string.cfgeo_webpage_url) + "'>"
                        + App.getContext().getString(R.string.cfgeo_webpage_url_short) + "</a>")
                .append("</td></tr>");

        // CEPM
        html.append("<tr><td></td><td>" + App.getContext().getString(R.string.cepm))
                .append(" - " + "<a href='"
                        + App.getContext().getString(R.string.cepm_webpage_url) + "'>"
                        + App.getContext().getString(R.string.cepm_webpage_url_short) + "</a>")
                .append("</td></tr>");

        // CPLN
        html.append("<tr><td>")
                .append("<img src='file:///android_asset/cpln_logo.png' alt='")
                .append(App.getContext().getString(R.string.cfgeo))
                .append("'/></td><td>")
                .append(App.getContext().getString(R.string.cpln))
                .append(" - " + "<a href='"
                        + App.getContext().getString(R.string.cpln_webpage_url) + "'>"
                        + App.getContext().getString(R.string.cpln_webpage_url_short) + "</a>")
                .append("</td></tr>");

        // EPCA
        html.append("<tr><td></td><td>" + App.getContext().getString(R.string.epca))
                .append(" - " + "<a href='"
                        + App.getContext().getString(R.string.epca_webpage_url) + "'>"
                        + App.getContext().getString(R.string.epca_webpage_url_short) + "</a>")
                .append("</td></tr>");

        // PGS-SO
        html.append("<tr><td>")
                .append("<img src='file:///android_asset/pgs_logo.png' alt='")
                .append(App.getContext().getString(R.string.pgsso))
                .append("'/></td><td>")
                .append(App.getContext().getString(R.string.pgsso))
                .append(" - " + "<a href='"
                        + App.getContext().getString(R.string.pgs_webpage_url) + "'>"
                        + App.getContext().getString(R.string.pgs_webpage_url_short) + "</a>")
                .append("</td></tr>");

        // end of sponsors list
        html.append("</table></div>");

        // Disclaimer
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
