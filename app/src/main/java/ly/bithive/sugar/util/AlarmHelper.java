package ly.bithive.sugar.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import ly.bithive.sugar.notification.BootReceiver;
import ly.bithive.sugar.notification.NotifierReceiver;

public class AlarmHelper {


    private AlarmManager alarmManager;

    private String ACTION_BD_NOTIFICATION = "ly.bithive.sugar";

    public void setAlarm(Context context, Long notificationFrequency) {
        long notificationFrequencyMs = TimeUnit.MINUTES.toMillis(notificationFrequency);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);// as AlarmManager;
        Intent alarmIntent = new Intent(context, NotifierReceiver.class);
        alarmIntent.setAction(ACTION_BD_NOTIFICATION);

        PendingIntent pendingAlarmIntent = PendingIntent.getBroadcast(
                context,
                0,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        Log.i("AlarmHelper", "Setting Alarm Interval to: " + notificationFrequency + " minutes");

        assert alarmManager != null;
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                notificationFrequencyMs,
                pendingAlarmIntent
        );

        /* Restart if rebooted */
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        context.getPackageManager().setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
        );
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);// as AlarmManager;

        Intent alarmIntent = new Intent(context, NotifierReceiver.class);
        alarmIntent.setAction(ACTION_BD_NOTIFICATION);

        PendingIntent pendingAlarmIntent = PendingIntent.getBroadcast(
                context,
                0,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        alarmManager.cancel(pendingAlarmIntent);

        /* Alarm won't start again if device is rebooted */
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
        );
        Log.i("AlarmHelper", "Cancelling alarms");
    }

    public Boolean checkAlarm(Context context) {
        Intent alarmIntent = new Intent(context, NotifierReceiver.class);
        alarmIntent.setAction(ACTION_BD_NOTIFICATION);
        return PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE) != null;
    }
}

