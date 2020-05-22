package ly.bithive.sugar.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static ly.bithive.sugar.util.COMMON.USERS_SHARED_PREF;

class PrefManager {
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    //  private int PRIVATE_MODE = 0;
    private Context context;
    //    @SuppressLint("CommitPrefEdits")
    //    public PrefManager() {
    //        pref = context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
    //        editor = pref.edit();
    //    }
    @SuppressLint("CommitPrefEdits")
    PrefManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(USERS_SHARED_PREF, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

//    void setPinCode(String pinCode) {
//        editor.putString(SHARED_PREF_PIN_KEY, pinCode);
//        editor.apply();
//    }
//
//    String getPinCode() {
//        return pref.getString(SHARED_PREF_PIN_KEY, null);
//    }
//
//    boolean checkPin(String pinCode) {
//        return getPinCode().equals(pinCode);
//    }
//
//    boolean isLoginRequired() {
//        return pref.getBoolean(SHARED_PREF_LOGIN_KEY, false);
//    }
//
//    void setRequireLogin(boolean val) {
//        editor.putBoolean(SHARED_PREF_LOGIN_KEY, val);
//        editor.apply();
//    }
//
//    void setCallPermission(boolean val) {
//        editor.putBoolean(SHARED_PREF_MAKE_CALL_PREM_KEY, val);
//        editor.apply();
//    }
//
//    void setContactsPermission(boolean val) {
//        editor.putBoolean(SHARED_PREF_READ_CONTACTS_PREM_KEY, val);
//        editor.apply();
//    }
//
//    boolean isCallPermissionGranted() { return pref.getBoolean(SHARED_PREF_MAKE_CALL_PREM_KEY, false); }
//
//    void setFirstTimeLaunch() {
//        editor.putBoolean(SHARED_PREF_IS_FIRST_TIME_RUN_KEY, false);
//        editor.commit();
//    }
//
//    boolean isFirstTimeLaunch() {
//        return pref.getBoolean(SHARED_PREF_IS_FIRST_TIME_RUN_KEY, true);
//    }
//
//    boolean isPinCodeSat() {
//        return pref.getBoolean(SHARED_PREF_PIN_CODE_SAT_KEY, false);
//    }
//
//    void setPinCodeSat(boolean isPinSat) {
//        editor.putBoolean(SHARED_PREF_PIN_CODE_SAT_KEY, isPinSat);
//        editor.commit();
//    }
//
//    boolean checkLogin(Intent data) {
//        if (!data.getBooleanExtra(CODE_LOGIN_RESULT, false)) {
//            int counter = pref.getInt(SHARED_PREF_FAILED_LOGIN_COUNTER_KEY, 0);
//            setLoginCounter(counter + 1);
//            return false;
//        } else {
//            setLoginCounter(0);
//            return true;
//        }
//    }
//
//    int LoginCounter() {
//        return pref.getInt(SHARED_PREF_FAILED_LOGIN_COUNTER_KEY, 0);
//    }
//
//    boolean loginCounterLimit() { return  pref.getInt(SHARED_PREF_FAILED_LOGIN_COUNTER_KEY, 0) >= 3; }
//
//    void setLoginCounter(int counter) {
//        editor.putInt(SHARED_PREF_FAILED_LOGIN_COUNTER_KEY, counter);
//        editor.commit();
//    }
//
//    public void reset() {
//        this.setLoginCounter(0);
//        this.setPinCodeSat(false);
//        this.setRequireLogin(false);
//        this.setLoginCounter(0);
//        this.setPinCode("0000");
//
//    }
}