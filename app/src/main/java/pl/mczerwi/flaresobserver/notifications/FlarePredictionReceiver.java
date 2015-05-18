package pl.mczerwi.flaresobserver.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Hours;

import java.util.ArrayList;
import java.util.List;

import pl.mczerwi.flaresobserver.FlarePredictionTask;
import pl.mczerwi.flaresobserver.R;
import pl.mczerwi.flaresobserver.model.ParcelableIridiumFlare;
import pl.mczerwi.flaresobserver.settings.NotificationSettings;
import pl.mczerwi.flaresobserver.settings.NotificationSettingsProvider;
import pl.mczerwi.flarespredict.IridiumFlare;

/**
 * Created by marcin on 2015-05-15.
 */
public class FlarePredictionReceiver extends BroadcastReceiver {

    private final static int INTERVAL_HOURS = 6;

    private static AlarmManager mAlarmManager;
    private static PendingIntent mPredictionPendingIntent;
    private static List<PendingIntent> mNotificationPendingIntents = new ArrayList<>();

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(getClass().getSimpleName(), "prediction alarm received");

        FlarePredictionTask task = new FlarePredictionTask(context) {
            @Override
            protected void onPostTaskExecute(List<IridiumFlare> flares) {
                if(flares.size() < 0) {
                    return;
                }

                cancelNotificationPendingIntents();

                int requestId = 0;
                DateTime dateNow = DateTime.now();
                for(IridiumFlare flare: flares) {
                    if(Hours.hoursBetween(dateNow, flare.getDate()).getHours() > 2 * INTERVAL_HOURS) {
                        break;  //notifications for flares which are more hours ahead are not set now, they will be set in the one of the next alarms received
                    }

                    ParcelableIridiumFlare parcelableIridiumFlare = new ParcelableIridiumFlare(flare);
                    Intent alarmIntent = new Intent(context, FlareNotificationReceiver.class);
                    alarmIntent.putExtra(context.getString(R.string.EXTRA_FLARE), parcelableIridiumFlare);
                    PendingIntent notificationPendingIntent = PendingIntent.getBroadcast(context, requestId++, alarmIntent, 0);
                    mNotificationPendingIntents.add(notificationPendingIntent);
                    mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                    DateTime notificationDate = flare.getDate().withZone(DateTimeZone.getDefault()).minusMinutes(15);

                    mAlarmManager.set(AlarmManager.RTC_WAKEUP, notificationDate.getMillis(), notificationPendingIntent);
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
        NotificationSettings settings = NotificationSettingsProvider.getNotificationSettings(context);

        if(settings.isShowNotifications()) {
            Intent alarmIntent = new Intent(context, FlarePredictionReceiver.class);
            mPredictionPendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), INTERVAL_HOURS * AlarmManager.INTERVAL_HOUR, mPredictionPendingIntent);
        }

    }

    public static void disableFlarePredictionNotifications(Context context) {
        if (mAlarmManager != null) {
            mAlarmManager.cancel(mPredictionPendingIntent);
            cancelNotificationPendingIntents();
        }
    }

    private static void cancelNotificationPendingIntents() {
        Log.d(FlarePredictionReceiver.class.getSimpleName(), "cancelling " + mNotificationPendingIntents.size() + " notification pending intents");
        for(PendingIntent notificationPendingIntent: mNotificationPendingIntents) {
            mAlarmManager.cancel(notificationPendingIntent);
        }
        mNotificationPendingIntents.clear();
    }

}
