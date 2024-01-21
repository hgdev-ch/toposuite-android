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
                .append("<br/><a href='https://github.com/hgdev-ch/toposuite-android'>github.com/hgdev-ch/toposuite-android</a>")
                .append("</p>")
                .append("<p>")
                .append(String.format(App.getContext().getString(R.string.app_copyright),
                        AppUtils.getYear(),
                        App.getContext().getString(R.string.crag)))
                .append("</p></div><br/><p style='clear: both;'>")
                .append("<p>")
                .append(App.getContext().getString(R.string.created_by))
                .append("</p>")
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
        html.append("<tr><td>")
                .append("<img src='file:///android_asset/cepm.png' alt='")
                .append(App.getContext().getString(R.string.cepm))
                .append("'/></td><td>")
                .append(App.getContext().getString(R.string.cepm))
                .append(" - " + "<a href='"
                        + App.getContext().getString(R.string.cepm_webpage_url) + "'>"
                        + App.getContext().getString(R.string.cepm_webpage_url_short) + "</a>")
                .append("</td></tr>");
        // CPNE
        html.append("<tr><td>")
                .append("<img src='file:///android_asset/cpne.png' alt='")
                .append(App.getContext().getString(R.string.cpne))
                .append("'/></td><td>")
                .append(App.getContext().getString(R.string.cpne))
                .append(" - " + "<a href='"
                        + App.getContext().getString(R.string.cpne_webpage_url) + "'>"
                        + App.getContext().getString(R.string.cpne_webpage_url_short) + "</a>")
                .append("</td></tr>");

        // EPCA
        html.append("<tr><td>")
                .append("<img src='file:///android_asset/epca.png' alt='")
                .append(App.getContext().getString(R.string.epca))
                .append("'/></td><td>")
                .append(App.getContext().getString(R.string.epca))
                .append(" - " + "<a href='"
                        + App.getContext().getString(R.string.epca_webpage_url) + "'>"
                        + App.getContext().getString(R.string.epca_webpage_url_short) + "</a>")
                .append("</td></tr>");

        // PGS-SO
        html.append("<tr><td>")
                .append("<img src='file:///android_asset/pgs.png' alt='")
                .append(App.getContext().getString(R.string.pgs))
                .append("'/></td><td>")
                .append(App.getContext().getString(R.string.pgs))
                .append(" - " + "<a href='"
                        + App.getContext().getString(R.string.pgs_webpage_url) + "'>"
                        + App.getContext().getString(R.string.pgs_webpage_url_short) + "</a>")
                .append("</td></tr>");

        // DCG
        html.append("<tr><td>")
                .append("<img src='file:///android_asset/dcg.png' alt='")
                .append(App.getContext().getString(R.string.dcg))
                .append("'/></td><td>")
                .append(App.getContext().getString(R.string.dcg))
                .append(" - " + "<a href='"
                        + App.getContext().getString(R.string.dcg_webpage_url) + "'>"
                        + App.getContext().getString(R.string.dcg_webpage_url_short) + "</a>")
                .append("</td></tr>");

        // AFG
        html.append("<tr><td>")
                .append("<img src='file:///android_asset/afg.png' alt='")
                .append(App.getContext().getString(R.string.afg))
                .append("'/></td><td>")
                .append(App.getContext().getString(R.string.afg))
                .append(" - " + "<a href='"
                        + App.getContext().getString(R.string.afg_webpage_url) + "'>"
                        + App.getContext().getString(R.string.afg_webpage_url_short) + "</a>")
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
