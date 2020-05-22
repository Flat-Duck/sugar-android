package ly.bithive.sugar.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;

import ly.bithive.sugar.R;
import ly.bithive.sugar.activity.MainActivity;
import ly.bithive.sugar.util.COMMON;
import ly.bithive.sugar.util.Helper;
import ly.bithive.sugar.util.SqliteHelper;

public class NotificationHelper {
    Context context;
    public NotificationHelper(Context mContext) {
        this.context = mContext;
    }
    private NotificationManager notificationManager = null;

    private String CHANNEL_ONE_ID = "io.github.z3r0c00l_2k.sugarApp.CHANNELONE";
    private String CHANNEL_ONE_NAME = "Channel One";


 //   @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannels() {
       // if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            SharedPreferences prefs = context.getSharedPreferences(COMMON.USERS_SHARED_PREF, COMMON.PRIVATE_MODE);
            String notificationsNewMessageRingtone = prefs.getString(
                    COMMON.NOTIFICATION_TONE_URI_KEY, RingtoneManager.getDefaultUri(
                            RingtoneManager.TYPE_NOTIFICATION
                    ).toString()
            );
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(
                    CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH
            );

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            if (!notificationsNewMessageRingtone.isEmpty()) {
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();
                notificationChannel.setSound(Uri.parse(notificationsNewMessageRingtone), audioAttributes);
            }

            getManager().createNotificationChannel(notificationChannel);
        }
        }
    //}

    NotificationCompat.Builder getNotification(String  title, String  body, String  notificationsTone){
        createChannels();
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ONE_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.ic_small_logo)
                .setAutoCancel(true);

        notification.setShowWhen(true);
        notification.setSound(Uri.parse(notificationsTone));
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //|| Intent.FLAG_ACTIVITY_SINGLE_TOP
        PendingIntent contentIntent = PendingIntent.getActivity(context, 99, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(contentIntent);
        return notification;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Boolean shallNotify() {
        SharedPreferences prefs = context.getSharedPreferences(COMMON.USERS_SHARED_PREF, COMMON.PRIVATE_MODE);
        SqliteHelper sqliteHelper = new SqliteHelper(context);

        int percent = sqliteHelper.getIntook(Helper.getCurrentDate()) * 100 / prefs.getInt(COMMON.TOTAL_INTAKE, 0);

        boolean doNotDisturbOff = true;

        long startTimestamp = prefs.getLong(COMMON.WAKEUP_TIME, 0);
        long stopTimestamp = prefs.getLong(COMMON.SLEEPING_TIME_KEY, 0);

        if (startTimestamp > 0 && stopTimestamp > 0) {
            Date now = Calendar.getInstance().getTime();

            Date start = new Date(startTimestamp);
            Date stop = new Date(stopTimestamp);

            doNotDisturbOff = compareTimes(now, start) >= 0 && compareTimes(now, stop) <= 0;
        }

        return doNotDisturbOff && (percent < 100);
    }

    /* Thanks to:
     * https://stackoverflow.com/questions/7676149/compare-only-the-time-portion-of-two-dates-ignoring-the-date-part
     */
    private Long compareTimes(Date currentTime, Date timeToRun) {
        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(currentTime);

        Calendar runCal = Calendar.getInstance();
        runCal.setTime(timeToRun);
        runCal.set(Calendar.DAY_OF_MONTH, currentCal.get(Calendar.DAY_OF_MONTH));
        runCal.set(Calendar.MONTH, currentCal.get(Calendar.MONTH));
        runCal.set(Calendar.YEAR, currentCal.get(Calendar.YEAR));

        return currentCal.getTimeInMillis() - runCal.getTimeInMillis();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void notify(int id, NotificationCompat.Builder notification) {
        if (shallNotify()) {
            getManager().notify(id, notification.build());
        } else {
            Log.i("SugarApp", "dnd period");
        }
    }

    private NotificationManager  getManager() {
        //if (notificationManager == null) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        return notificationManager;
    }



}
