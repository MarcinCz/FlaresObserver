package pl.mczerwi.flaresobserver.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.List;

import pl.mczerwi.flaresobserver.FlarePredictionTask;
import pl.mczerwi.flaresobserver.R;
import pl.mczerwi.flaresobserver.SettingsActivity;
import pl.mczerwi.flaresobserver.model.ParcelableIridiumFlare;
import pl.mczerwi.flarespredict.IridiumFlare;

/**
 * Created by marcin on 2015-05-15.
 */
public class FlarePredictionReceiver extends BroadcastReceiver {

    private static AlarmManager mAlarmManager;
    private static PendingIntent mPredictionPendingIntent;
    private static PendingIntent mNotificationPendingIntent;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(getClass().getSimpleName(), "prediction alarm received");

        FlarePredictionTask task = new FlarePredictionTask(context) {
            @Override
            protected void OnPostTaskExecute(List<IridiumFlare> flares) {
                if(flares.size() > 0) {
                    Intent alarmIntent = new Intent(context, FlareNotificationReceiver.class);
                    alarmIntent.putExtra(context.getString(R.string.EXTRA_FLARE), new ParcelableIridiumFlare(flares.get(0)));
                    mNotificationPendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
                    mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                    DateTime notificationDate = flares.get(0).getDate().withMinuteOfHour(28).withSecondOfMinute(0);

                    mAlarmManager.cancel(mNotificationPendingIntent); //cancels previous notification pending intents
                    mAlarmManager.set(AlarmManager.RTC_WAKEUP, notificationDate.getMillis(), mNotificationPendingIntent);
                    Log.d(getClass().getSimpleName(), "set flare notification for " + notificationDate.toString());
                }
            }
        };

        task.execute();
    }

    /**
     * Starts alarm manager with repeating task, which checks for flares predictions every hour and schedule flare notifications
     */
    public static void enableFlarePredictionNotifications(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean showNotifications = sharedPref.getBoolean(SettingsActivity.KEY_PREF_SHOW_NOTIFICATIONS, true);

        if(showNotifications) {
            Intent alarmIntent = new Intent(context, FlarePredictionReceiver.class);
            mPredictionPendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),
                    1000, mPredictionPendingIntent);
        }

    }

    public static void disableFlarePredictionNotifications(Context context) {
        if (mAlarmManager != null) {
            mAlarmManager.cancel(mPredictionPendingIntent);
            mAlarmManager.cancel(mNotificationPendingIntent);
        }

    }

}
