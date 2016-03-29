package ch.hgdev.toposuite.test.testutils;

import java.util.Locale;

import android.content.res.Configuration;
import android.content.res.Resources;
import ch.hgdev.toposuite.App;

/**
 * Miscellaneous utility functions, useful for tests.
 *
 * @author HGdev
 *
 */
public class Util {

    /**
     * Change the current locale.
     *
     * @param language
     *            Language identifier (example: "fr", "de", ...)
     * @param country
     *            Country code (example: "CH", "FR", ...)
     */
    public static void setLocale(String language, String country) {
        Locale locale = new Locale(language, country);
        Locale.setDefault(locale);
        Resources res = App.getContext().getResources();
        Configuration config = res.getConfiguration();
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }
}
