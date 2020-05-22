package ly.bithive.sugar.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import ly.bithive.sugar.util.AlarmHelper;
import ly.bithive.sugar.util.COMMON;

public class BootReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
         AlarmHelper alarm = new  AlarmHelper();
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                SharedPreferences prefs = context.getSharedPreferences(COMMON.USERS_SHARED_PREF, COMMON.PRIVATE_MODE);
                int notificationFrequency = prefs.getInt(COMMON.NOTIFICATION_FREQUENCY_KEY, 60);
                boolean notificationsNewMessage = prefs.getBoolean("notifications_new_message", true);
                alarm.cancelAlarm(context);
                if (notificationsNewMessage) {
                    alarm.setAlarm(context, Long.parseLong(String.valueOf(notificationFrequency)));
                }
            }
        }
    }
}
