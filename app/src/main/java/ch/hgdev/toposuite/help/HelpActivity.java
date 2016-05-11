package ch.hgdev.toposuite.help;

import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.utils.Logger;

public class HelpActivity extends TopoSuiteActivity {
    private ViewGroup layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_help);
        this.layout = (ViewGroup) this.findViewById(R.id.help_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.layout.removeAllViews();
        this.layout.addView(this.loadHelp());
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_help);
    }

    private WebView loadHelp() {
        WebView helpWebView = new WebView(this);
        helpWebView.loadUrl("file:///android_asset/help/" + this.getHelpFileName());
        return helpWebView;
    }

    /**
     * Return appropriate html help file name, taking care of the locale set.
     *
     * @return HTML help file name.
     */
    private String getHelpFileName() {
        InputStream stream = null;
        try {
            String helpFile = "help_" + App.getLocale().getLanguage() + ".html";
            stream = this.getAssets().open("help/" + helpFile);
            return helpFile;
        } catch (IOException exception) {
            return "help.html";
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
                }
            }
        }
    }
}
