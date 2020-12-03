package ly.bithive.sugar.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import static ly.bithive.sugar.util.COMMON.BMI_HEIGHT_KEY;
import static ly.bithive.sugar.util.COMMON.BMI_READING_KEY;
import static ly.bithive.sugar.util.COMMON.BMI_WEIGHT_KEY;

public class SqliteHelper extends SQLiteOpenHelper {


    private static final String LOG = SqliteHelper.class.getName();
    private static int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "Sugar";
    private static final String TABLE_STATS = "stats";
    private static final String TABLE_MEASURE = "measure";
    private static final String TABLE_SPORTS = "sport";
    private static final String TABLE_BMI = "bmi";
    private static final String KEY_ID = "id";
    public static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_TYPE = "type";
    private static final String KEY_INTOOK = "intook";
    private static final String KEY_TOTAL_INTAKE = "totalintake";
    private static final String KEY_MG = "morning_glycemia";
    private static final String KEY_NG = "noon_glycemia";
    private static final String KEY_EG = "evening_glycemia";
    private static final String KEY_SG = "sleep_glycemia";
    private static final String KEY_MI = "morning_insulin";
    private static final String KEY_NI = "noon_insulin";
    private static final String KEY_EI = "evening_insulin";
    private static final String KEY_SI = "sleep_insulin";

    public static final String KEY_PERIOD = "period";
    private static final String KEY_CURE = "cure";
    public static final String KEY_GLYCEMIA = "glycemia";
    private static final String KEY_INSULIN = "insulin";


    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private String CREATE_MEASURE_TABLE = ("CREATE TABLE " + TABLE_MEASURE + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATE + " TEXT,"+ KEY_TIME + " TEXT,"
            + KEY_PERIOD + " TEXT," + KEY_CURE + " TEXT," + KEY_GLYCEMIA + " TEXT," + KEY_INSULIN + " TEXT)");

    private String CREATE_STATS_TABLE = ("CREATE TABLE " + TABLE_STATS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATE + " TEXT UNIQUE,"
            + KEY_INTOOK + " INT," + KEY_TOTAL_INTAKE + " INT" + ")");

    private String CREATE_BMI_TABLE = ("CREATE TABLE " + TABLE_BMI + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + BMI_READING_KEY + " TEXT ,"
            + BMI_WEIGHT_KEY + " TEXT ,"
            + BMI_HEIGHT_KEY + " INT,"
            + KEY_DATE + " TEXT" + ")");

    private String CREATE_SPORTS_TABLE = ("CREATE TABLE " + TABLE_SPORTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_DURATION + " INT ,"
            + KEY_TYPE + " TEXT,"
            + KEY_TIME + " TEXT ,"
            + KEY_DATE + " TEXT" + ")");


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_STATS_TABLE);
        sqLiteDatabase.execSQL(CREATE_MEASURE_TABLE);
        sqLiteDatabase.execSQL(CREATE_BMI_TABLE);
        sqLiteDatabase.execSQL(CREATE_SPORTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_STATS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MEASURE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BMI);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SPORTS);
        onCreate(sqLiteDatabase);
    }

    public long addBMI(String reading,String height,String weight, String date) {
        if (checkExistance(date) == 0) {
            ContentValues values = new ContentValues();
            values.put(BMI_READING_KEY, reading);
            values.put(BMI_WEIGHT_KEY, weight);
            values.put(BMI_HEIGHT_KEY, height);
            values.put(KEY_DATE, date);
            SQLiteDatabase db = this.getWritableDatabase();
            long response = db.insert(TABLE_STATS, null, values);
            db.close();
            return response;
        }
        return -1;
    }
    public long addSport(String date,String time,String type, int duration) {
        if (checkExistance(date) == 0) {
            ContentValues values = new ContentValues();
            values.put(KEY_DURATION, duration);
            values.put(KEY_TYPE, type);
            values.put(KEY_TIME, time);
            values.put(KEY_DATE, date);
            SQLiteDatabase db = this.getWritableDatabase();
            long response = db.insert(TABLE_SPORTS, null, values);
            db.close();
            return response;
        }
        return -1;
    }

    public long addAll(String date, int intook, int totalintake) {
        if (checkExistance(date) == 0) {
            ContentValues values = new ContentValues();
            values.put(KEY_DATE, date);
            values.put(KEY_INTOOK, intook);
            values.put(KEY_TOTAL_INTAKE, totalintake);
            SQLiteDatabase db = this.getWritableDatabase();
            long response = db.insert(TABLE_STATS, null, values);
            db.close();
            return response;
        }
        return -1;
    }


    public int getIntook(String date) {
        String selectQuery = " SELECT " + KEY_INTOOK + " FROM " + TABLE_STATS + " WHERE " + KEY_DATE + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        String[] whereArgs = new String[]{date};
        Cursor mCursor = db.rawQuery(selectQuery, whereArgs);
        if (mCursor != null) {
            int intName = mCursor.getColumnIndexOrThrow(KEY_INTOOK);
            if (mCursor.moveToFirst()) {
                return mCursor.getInt(intName);
            }
        }
        return 0;
    }

    public int addIntook(String date, int selectedOption) {
        debugInsert(" / date / " + date);
        debugInsert(" / selectedOption / " + selectedOption);
        int intook = getIntook(date);
        debugInsert(" / intook / " + intook);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_INTOOK, intook + selectedOption);
        String[] whereArgs = new String[]{date};
        int response = db.update(TABLE_STATS, contentValues, KEY_DATE + " = ?", whereArgs);
        db.close();
        return response;
    }

    int checkExistance(String date) {
        String countQuery = "SELECT " + KEY_INTOOK + " FROM " + TABLE_STATS + " WHERE " + KEY_DATE + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        String[] whereArgs = new String[]{date};
        Cursor cursor = db.rawQuery(countQuery, whereArgs);
        int count = cursor.getCount();
        cursor.close();
        return count;

    }
    public Cursor getAllSports() {
        String selectQuery = "SELECT * FROM " + TABLE_SPORTS;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }
    public Cursor getAllAnalysis() {
        String selectQuery = "SELECT * FROM " + TABLE_MEASURE;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    public Cursor getAllBMI() {
        String selectQuery = "SELECT * FROM " + TABLE_BMI;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    public Cursor getAllStats() {
        String selectQuery = "SELECT * FROM " + TABLE_STATS;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    public int updateTotalIntake(String date, int totalintake) {
        int intook = getIntook(date);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TOTAL_INTAKE, totalintake);
        String[] whereArgs = new String[]{date};
        int response = db.update(TABLE_STATS, contentValues, KEY_DATE + "= ?", whereArgs);
        db.close();
        return response;
    }


    void debugInsert(String msg) {
        Log.d("HelperSql", "insert Data" + msg);
    }


    public long insertShot(String date,String time, String glycemia, String period, String cure) {
       // debugInsert(time);
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, date);
        values.put(KEY_TIME, time);
        values.put(KEY_GLYCEMIA, glycemia);
      //  values.put(KEY_INSULIN, insulin);
        values.put(KEY_CURE, cure);
        values.put(KEY_PERIOD, period);
        SQLiteDatabase db = this.getWritableDatabase();
        long response = db.insert(TABLE_MEASURE, null, values);
        db.close();
        return response;
    }
}

