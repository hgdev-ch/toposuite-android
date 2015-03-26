package ch.hgdev.toposuite.help;

import java.io.IOException;
import java.io.InputStream;

import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup;
import android.webkit.WebView;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.utils.Logger;

public class HelpActivity extends TopoSuiteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_help);
        ViewGroup layout = (ViewGroup) this.findViewById(R.id.help_layout);
        layout.addView(this.loadHelp());
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_help);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.help, menu);
        return super.onCreateOptionsMenu(menu);
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
            String helpFile = "help_" + App.locale.getLanguage().toString() + ".html";
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
