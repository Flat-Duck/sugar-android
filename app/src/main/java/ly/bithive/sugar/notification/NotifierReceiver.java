package ly.bithive.sugar.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;

import androidx.core.app.NotificationCompat;

import ly.bithive.sugar.R;
import ly.bithive.sugar.util.COMMON;

public class NotifierReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences prefs = context.getSharedPreferences(COMMON.USERS_SHARED_PREF, COMMON.PRIVATE_MODE);
        String notificationsTone = prefs.getString(COMMON.NOTIFICATION_TONE_URI_KEY, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString());
        String title = context.getResources().getString(R.string.app_name);
        String messageToShow = prefs.getString(COMMON.NOTIFICATION_MSG_KEY, context.getResources().getString(R.string.pref_notification_message_value));
        /* Notify */
        NotificationHelper nHelper = new NotificationHelper(context);
        NotificationCompat.Builder nBuilder = nHelper.getNotification(title, messageToShow, notificationsTone);
        nHelper.notify(1, nBuilder);

    }
}
