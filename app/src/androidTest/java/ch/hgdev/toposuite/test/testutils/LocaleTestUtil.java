package ch.hgdev.toposuite.test.testutils;

import android.content.res.Configuration;
import android.content.res.Resources;
import androidx.test.InstrumentationRegistry;

/**
 * Miscellaneous utility functions, useful for tests.
 *
 * @author HGdev
 *
 */
public class LocaleTestUtil {

    /**
     * Change the current locale.
     *
     * @param language
     *            Language identifier (example: "fr", "de", ...)
     * @param country
     *            Country code (example: "CH", "FR", ...)
     */
    public static void setLocale(String language, String country) {
        java.util.Locale locale = new java.util.Locale(language, country);
        java.util.Locale.setDefault(locale);
        Resources res = InstrumentationRegistry.getTargetContext().getResources();
        Configuration config = res.getConfiguration();
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }
}
