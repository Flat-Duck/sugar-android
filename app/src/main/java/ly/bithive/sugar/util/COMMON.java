package ly.bithive.sugar.util;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;

import java.text.Format;
import java.text.ParseException;
import java.util.Date;

public class COMMON {

    static public String BASE_URL = "http://192.168.10.134/sugar/public/api/";
    static public String LOGIN_URL = BASE_URL + "login";
    static public String USER_URL = BASE_URL + "user";
    static public String CHATS_URL = BASE_URL + "chats";
    static public String SYNC_URL = BASE_URL + "analysis";
    static public String UPDATE_URL = BASE_URL + "user";
    static public String USERS_SHARED_PREF = "user_pref";

    static public int PRIVATE_MODE = 0;
    static public String WEIGHT_KEY = "weight";
    static public String WORK_TIME_KEY = "worktime";
    static public String TOTAL_INTAKE = "totalintake";
    static public String NOTIFICATION_STATUS_KEY = "notificationstatus";
    static public String NOTIFICATION_FREQUENCY_KEY = "notificationfrequency";
    static public String NOTIFICATION_MSG_KEY = "notificationmsg";
    static public String SLEEPING_TIME_KEY = "sleepingtime";
    static public String WAKEUP_TIME = "wakeuptime";
    static public String NOTIFICATION_TONE_URI_KEY = "notificationtone";
    static public String FIRST_RUN_KEY = "firstrun";

    static public String BMI_WEIGHT_KEY = "weight";
    static public String BMI_HEIGHT_KEY = "height";
    static public String BMI_READING_KEY = "reading";

    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_TYPE = "type";

    static public int DANGER_HIGH = 399;
    static public int DANGER_LOW = 51;

    static public int BE_HIGH_1 = 180;
    static public int BE_HIGH_2 = 205;
    static public int BE_HIGH_3 = 240;

    static public int BE_LOW_1 = 70;
    static public int BE_LOW_2 = 65;

    static public int AE_HIGH_1 = 201;
    static public int AE_HIGH_2 = 205;
    static public int AE_HIGH_3 = 244;

    static public int AE_LOW_1 = 79;
    static public int AE_LOW_2 = 70;

    static public int BS_HIGH_1 = 180;
    static public int BS_HIGH_2 = 210;
    static public int BS_HIGH_3 = 244;

    static public int BS_LOW_1 = 71;
    static public int BS_LOW_2 = 69;
    static public int AGE_ADULT = 19;
    static public int AGE_OLD = 65;

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(c);
    }

    public static Double calculateIntake(int weight, int workTime) {
        return ((weight * 100 / 3.0) + (workTime / 6 * 7));
    }

    public static String convertDate(String inputD) {
        String outPutD = "2001-01-21";
        @SuppressLint("SimpleDateFormat")
        java.text.SimpleDateFormat input = new java.text.SimpleDateFormat("dd-mm-yyyy");
        java.text.SimpleDateFormat output = new java.text.SimpleDateFormat("yyyy-mm-dd");
        try {
            Date oneWayTripDate = input.parse(inputD);                 // parse input
            outPutD = output.format(oneWayTripDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outPutD;
    }
}