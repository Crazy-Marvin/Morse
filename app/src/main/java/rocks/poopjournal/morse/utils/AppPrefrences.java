package rocks.poopjournal.morse.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPrefrences {
    private static final String PREF_NAME = "morse_settings";
    private static final String KEY_METHOD = "method";
    private static final String KEY_DURATION = "duration";

    private static final String KEY_SOURCE_LANG = "source_language"; // new


    private final SharedPreferences prefs;

    public AppPrefrences(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveMethod(String method) {
        prefs.edit().putString(KEY_METHOD, method).apply();
    }

    public String getMethod() {
        return prefs.getString(KEY_METHOD, "Standard word"); // default
    }

    public void saveDuration(int durationMs) {
        prefs.edit().putInt(KEY_DURATION, durationMs).apply();
    }

    public int getDuration() {
        return prefs.getInt(KEY_DURATION, 100); // default 100ms
    }

    public void saveSourceLanguage(String lang) {
        prefs.edit().putString(KEY_SOURCE_LANG, lang).apply();
    }

    public String getSourceLanguage() {
        return prefs.getString(KEY_SOURCE_LANG, "Latin");
    }
}
