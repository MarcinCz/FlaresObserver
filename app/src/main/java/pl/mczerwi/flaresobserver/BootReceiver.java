package pl.mczerwi.flaresobserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import pl.mczerwi.flaresobserver.notifications.FlarePredictionReceiver;

/**
 * Created by marcin on 2015-05-16.
 */
class BootReceiver extends BroadcastReceiver{

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            FlarePredictionReceiver.enableFlarePredictionNotifications(context);
        }
    }
}
