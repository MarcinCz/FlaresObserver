package pl.mczerwi.flaresobserver.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import pl.mczerwi.flaresobserver.R;
import pl.mczerwi.flaresobserver.model.ParcelableIridiumFlare;
import pl.mczerwi.flaresobserver.settings.NotificationSettings;
import pl.mczerwi.flaresobserver.settings.NotificationSettingsProvider;
import pl.mczerwi.flarespredict.IridiumFlare;

/**
 * Created by marcin on 2015-05-16.
 */
public class FlareNotificationReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationSettings settings = NotificationSettingsProvider.getNotificationSettings(context);

        if(settings.isShowNotifications()) {
            ParcelableIridiumFlare parcelableIridiumFlare = intent.getParcelableExtra(context.getString(R.string.EXTRA_FLARE));
            IridiumFlare flare = parcelableIridiumFlare.getIridiumFlare();

            if(checkBrightnessLimit(flare, settings) && checkDoNotShowConstraints(flare, settings)) {
                Log.d(getClass().getSimpleName(), "Flare brightness: " + String.valueOf(flare.getBrightness()));
                FlareNotification.notify(context, flare);
            }
        }
    }

    /**
     * Checks brightness level of flare
     * @return true if notification should be shown for provided flare
     */
    public boolean checkBrightnessLimit(IridiumFlare flare, NotificationSettings settings) {
        return !settings.isBrightnessLimitEnabled() || flare.getBrightness() <= settings.getBrightnessLimit();
    }

    /**
     * Checks the time of flare prediction
     * @return true if notification should be shown for provided flare
     */
    public boolean checkDoNotShowConstraints(IridiumFlare flare, NotificationSettings settings) {
        if(!settings.isDoNotShowConstrainsEnabled()) {
            return true;
        } else {
            int flareHour = flare.getDate().getHourOfDay();
            return flareHour < settings.getDoNotShowStartHour() && flareHour >= settings.getDoNotShowStartHour();
        }
    }
}
