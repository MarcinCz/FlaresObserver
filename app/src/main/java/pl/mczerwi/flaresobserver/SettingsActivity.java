package pl.mczerwi.flaresobserver;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import pl.mczerwi.flaresobserver.notifications.FlarePredictionReceiver;

/**
 * Created by marcin on 2015-05-16.
 */
public class SettingsActivity extends PreferenceActivity {

    public static final String KEY_PREF_SHOW_NOTIFICATIONS = "pref_showNotifications";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

    }


    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                              String key) {
            if (key.equals(KEY_PREF_SHOW_NOTIFICATIONS)) {
                boolean showNotifications = sharedPreferences.getBoolean(key, true);
                if(showNotifications) {
                    FlarePredictionReceiver.enableFlarePredictionNotifications(getActivity());
                } else {
                    FlarePredictionReceiver.disableFlarePredictionNotifications(getActivity());
                }
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }


    }



}
