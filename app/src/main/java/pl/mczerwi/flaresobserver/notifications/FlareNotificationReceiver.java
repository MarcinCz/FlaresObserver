package pl.mczerwi.flaresobserver.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import pl.mczerwi.flaresobserver.R;
import pl.mczerwi.flaresobserver.SettingsActivity;
import pl.mczerwi.flaresobserver.model.ParcelableIridiumFlare;
import pl.mczerwi.flarespredict.IridiumFlare;

/**
 * Created by marcin on 2015-05-16.
 */
public class FlareNotificationReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean showNotifications = sharedPref.getBoolean(SettingsActivity.KEY_PREF_SHOW_NOTIFICATIONS, true);

        if(showNotifications) {
            ParcelableIridiumFlare parcelableIridiumFlare = intent.getParcelableExtra(context.getString(R.string.EXTRA_FLARE));
            IridiumFlare flare = parcelableIridiumFlare.getIridiumFlare();
            Log.d(getClass().getSimpleName(), "Flare brightness: " + String.valueOf(flare.getBrightness()));
            FlareNotification.notify(context, flare);
        }

    }
}
