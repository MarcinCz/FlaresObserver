package pl.mczerwi.flaresobserver.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class NotificationSettingsProvider {

    public static NotificationSettings getNotificationSettings(Context context) {
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return new NotificationSettings() {
            @Override
            public boolean isShowNotifications() {
                return sharedPref.getBoolean(SettingsActivity.KEY_PREF_SHOW_NOTIFICATIONS, true);
            }

            @Override
            public boolean isDoNotShowConstrainsEnabled() {
                return sharedPref.getBoolean(SettingsActivity.KEY_PREF_DO_NOT_SHOW_NOTIFICATION_CONSTRAINTS, false);
            }

            @Override
            public int getDoNotShowStartHour() {
                return sharedPref.getInt(SettingsActivity.KEY_PREF_DO_NOT_SHOW_START_HOUR, 22);
            }

            @Override
            public int getDoNotShowEndHour() {
                return sharedPref.getInt(SettingsActivity.KEY_PREF_DO_NOT_SHOW_END_HOUR, 8);
            }

            @Override
            public boolean isBrightnessLimitEnabled() {
                return sharedPref.getBoolean(SettingsActivity.KEY_PREF_NOTIFICATION_BRIGHTNESS_LIMIT, true);
            }

            @Override
            public int getMinutesAhead() {
                return Integer.valueOf(sharedPref.getString(SettingsActivity.KEY_PREF_NOTIFICATION_MINUTES_AHEAD, "15"));
            }

        };
    }
}
