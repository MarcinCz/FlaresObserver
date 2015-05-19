package pl.mczerwi.flaresobserver.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import pl.mczerwi.flaresobserver.R;
import pl.mczerwi.flaresobserver.notifications.FlarePredictionReceiver;

public class SettingsActivity extends PreferenceActivity {

    public static final String KEY_PREF_SHOW_NOTIFICATIONS = "pref_show_notifications";
    public static final String KEY_PREF_DO_NOT_SHOW_NOTIFICATION_CONSTRAINTS = "pref_do_not_show_notification_constraints";
    public static final String KEY_PREF_DO_NOT_SHOW_START_HOUR = "pref_do_not_show_notification_start_hour";
    public static final String KEY_PREF_DO_NOT_SHOW_END_HOUR = "pref_do_not_show_notification_end_hour";
    public static final String KEY_PREF_NOTIFICATION_BRIGHTNESS_LIMIT = "pref_notification_brightness_limit";
    public static final String KEY_PREF_NOTIFICATION_MINUTES_AHEAD = "pref_notification_minutes_ahead";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

    }

    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
            NotificationSettings settings = NotificationSettingsProvider.getNotificationSettings(getActivity());
            if(settings.isShowNotifications()) {
                toggleDoNotShowNotificationsConstraintsSettings(settings.isDoNotShowConstrainsEnabled());
            } else {
                toggleNotificationSetting(false);
            }

            ListPreference minutesAheadPref = (ListPreference) findPreference(KEY_PREF_NOTIFICATION_MINUTES_AHEAD);
            if(minutesAheadPref.getValue() == null) {
                minutesAheadPref.setValue("3");
            }
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            switch(key) {
                case KEY_PREF_SHOW_NOTIFICATIONS:
                    boolean showNotifications = sharedPreferences.getBoolean(key, true);
                    if (showNotifications) {
                        FlarePredictionReceiver.enableFlarePredictionNotifications(getActivity());
                        toggleNotificationSetting(true);
                    } else {
                        FlarePredictionReceiver.disableFlarePredictionNotifications(getActivity());
                        toggleNotificationSetting(false);
                    }
                    break;
                case KEY_PREF_DO_NOT_SHOW_NOTIFICATION_CONSTRAINTS:
                    toggleDoNotShowNotificationsConstraintsSettings(sharedPreferences.getBoolean(key, true));
                    break;
            }
        }

        private void toggleNotificationSetting(boolean notificationsEnabled) {
            findPreference(KEY_PREF_DO_NOT_SHOW_NOTIFICATION_CONSTRAINTS).setEnabled(notificationsEnabled);
            findPreference(KEY_PREF_DO_NOT_SHOW_START_HOUR).setEnabled(notificationsEnabled);
            findPreference(KEY_PREF_DO_NOT_SHOW_END_HOUR).setEnabled(notificationsEnabled);
            findPreference(KEY_PREF_NOTIFICATION_BRIGHTNESS_LIMIT).setEnabled(notificationsEnabled);
            findPreference(KEY_PREF_NOTIFICATION_MINUTES_AHEAD).setEnabled(notificationsEnabled);

            if(notificationsEnabled) {
                NotificationSettings settings = NotificationSettingsProvider.getNotificationSettings(getActivity());
                toggleDoNotShowNotificationsConstraintsSettings(settings.isDoNotShowConstrainsEnabled());
            }
        }

        private void toggleDoNotShowNotificationsConstraintsSettings(boolean doNotShowConstraintsEnabled) {
            findPreference(KEY_PREF_DO_NOT_SHOW_END_HOUR).setEnabled(doNotShowConstraintsEnabled);
            findPreference(KEY_PREF_DO_NOT_SHOW_START_HOUR).setEnabled(doNotShowConstraintsEnabled);
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
