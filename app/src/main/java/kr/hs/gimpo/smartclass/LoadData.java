package kr.hs.gimpo.smartclass;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class LoadData {

}

class DataSQLiteHelper
        extends SQLiteOpenHelper {

    // Common Resource
    private static final String DATABASE_NAME = "data.db";
    private static final int DATABASE_VERSION = 2;
    private static final String COLUMN_LAST_UPDATED = "LastUpdated";
    static final int LAST_UPDATED = 0;

    // Timetable Resource

    static final String TIME_TABLE_NAME = "TimeTable";
    private static final String COLUMN_TIME_JSON_DATA = "TimeJsonData";
    static final String TIME_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TIME_TABLE_NAME + "(" +
                    COLUMN_LAST_UPDATED + " TEXT, " +
                    COLUMN_TIME_JSON_DATA + " TEXT)";
    static final String TIME_TABLE_DROP =
            "DROP TABLE IF EXISTS " + TIME_TABLE_NAME;
    static final int TIME_JSON_DATA = 1;
    static final String TIME_SELECT =
            "SELECT * FROM " + TIME_TABLE_NAME;

    // Meal info Resource

    static final String MEAL_TABLE_NAME = "MealInfo";
    private static final String COLUMN_MEAL_DAY = "MealDay";
    private static final String COLUMN_MEAL_TIME = "MealTime";
    private static final String COLUMN_MEAL_DATA = "MealData";
    static final String MEAL_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + MEAL_TABLE_NAME + "(" +
                    COLUMN_LAST_UPDATED + " TEXT, " +
                    COLUMN_MEAL_DAY + " INT, " +
                    COLUMN_MEAL_TIME + " INT, " +
                    COLUMN_MEAL_DATA + " TEXT)";
    static final String MEAL_TABLE_DROP =
            "DROP TABLE IF EXISTS " + MEAL_TABLE_NAME;

    // Air quality Resource

    static final String AIR_TABLE_NAME = "AirQual";
    private static final String COLUMN_AIR_DATA = "AirData";
    static final String AIR_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + AIR_TABLE_NAME + "(" +
                    COLUMN_LAST_UPDATED + " TEXT, " +
                    COLUMN_AIR_DATA + " TEXT)";
    static final String AIR_TABLE_DROP =
            "DROP TABLE IF EXISTS " + AIR_TABLE_NAME;


    DataSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TIME_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

//TODO: 데이터 초기화 스레드 만들기

class InitTimeData
        extends AsyncTask<Boolean, Void, Boolean> {

    private String jsonData = "";
    private DataSQLiteHelper db;
    private DataFormat dataFormat;

    InitTimeData(DataSQLiteHelper db, DataFormat dataFormat) {
        this.db = db;
        this.dataFormat = dataFormat;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Boolean... isConnected) {

        if(isConnected[0]) {
            try {
                // 김포고등학교의 실시간 시간표 정보는 http://comcigan.com:4082/_hourdat?sc=26203에서 확인할 수 있다!
                // sc=26203: 김포고등학교의 데이터
                Document doc = Jsoup.connect("http://comcigan.com:4082/_hourdata?sc=26203").get();

                // 시간표 데이터는 <body>에 있다!
                Element data = doc.body();

                System.out.println("------------------------------");
                System.out.println("data: " + data.text());
                System.out.println("------------------------------");

                jsonData += data.text().trim();

                dataFormat.timeData = new Time(jsonData);

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean isInitialized) {
        if(isInitialized) {

            //dataFormat.timeData = new Time(jsonData);

            StringBuilder sb = new StringBuilder(jsonData);
            for(int i = 0; i < sb.length(); i++) {
                if(sb.charAt(i) == '\"') {
                    sb.replace(i, i + 1, "\"\"");
                    i++;
                }
            }
            jsonData = sb.toString();

            String LastUpdated = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());



            /*
            String sqlInsert = "insert or replace into " + DataSQLiteHelper.TIME_TABLE_NAME +" values(\"" + LastUpdated + "\", \"" + jsonData + "\")";
            db.getWritableDatabase().execSQL(DataSQLiteHelper.TIME_TABLE_DROP);
            db.getWritableDatabase().execSQL(DataSQLiteHelper.TIME_TABLE_CREATE);
            db.getWritableDatabase().execSQL(sqlInsert);


            Cursor cursor = db.getReadableDatabase().rawQuery(DataSQLiteHelper.TIME_SELECT, null);

            if(cursor.moveToFirst()) {
                String time = cursor.getString(DataSQLiteHelper.LAST_UPDATED);
                String data = cursor.getString(DataSQLiteHelper.TIME_JSON_DATA);
            }

            cursor.close();
        } else {
            Cursor cursor = db.getReadableDatabase().rawQuery(DataSQLiteHelper.TIME_SELECT, null);

            if(cursor.moveToFirst()) {
                String time = cursor.getString(DataSQLiteHelper.LAST_UPDATED);
                String data = cursor.getString(DataSQLiteHelper.TIME_JSON_DATA);
            } else {
                String temp = "{\"성명\":[\" \", \" \"],\"긴과목명\":[\" \", \" \"],\"과목명\":[\" \", \" \"],\"시간표\":[[[]],[[],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]]],[[],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]]],[[],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]]]],\"학급시간표\":[[[]],[[],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]]],[[],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]]],[[],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]],[[],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]]]],\"저장일\":\"0000-00-00 00:00:00\",\"일자자료\":[[1,\"00-00-00 ~ 00-00-00\"],[2,\"00-00-00 ~ 00-00-00\"]]}";
                StringBuilder sb = new StringBuilder(temp);
                for(int i = 0; i < sb.length(); i++) {
                    if(sb.charAt(i) == '\"') {
                        sb.replace(i, i + 1, "\"\"");
                        i++;
                    }
                }
                jsonData = sb.toString();
                String LastUpdated = "0000-12-31 23:59:59";
                String sqlInsert = "insert or replace into TimeTable values(\"" + LastUpdated + "\", \"" + jsonData + "\")";
                db.getWritableDatabase().execSQL(DataSQLiteHelper.TIME_TABLE_DROP);
                db.getWritableDatabase().execSQL(DataSQLiteHelper.TIME_TABLE_CREATE);
                db.getWritableDatabase().execSQL(sqlInsert);

                String time = cursor.getString(DataSQLiteHelper.LAST_UPDATED);
                String data = cursor.getString(DataSQLiteHelper.TIME_JSON_DATA);
            }

            cursor.close();*/
        }
    }
}

class InitMealData
        extends AsyncTask<Boolean, Void, Boolean> {

    private int lastDay = initLastDay();
    private String[][] mealData = new String[lastDay][2];
    private List<List<String>> mealDataList = new ArrayList<>();
    private DataSQLiteHelper db;
    private DataFormat dataFormat;

    InitMealData(DataSQLiteHelper db, DataFormat dataFormat) {
        this.db = db;
        this.dataFormat = dataFormat;
    }

    private int initLastDay() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Calendar.getInstance().getTime());
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);

        return Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(calendar.getTime()));
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Boolean... isConnected) {
        if(isConnected[0]) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Calendar.getInstance().getTime());
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);
            int year = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(calendar.getTime()));
            int month = Integer.parseInt(new SimpleDateFormat("MM", Locale.getDefault()).format(calendar.getTime()));
            for(int day = 1; day <= lastDay; day++) {
                try {
                    // 급식 정보는 http://www.gimpo.hs.kr/main.php?menugrp=021100&master=meal2&act=list&SearchYear=year&SearchMonth=month&SearchDay=day#diary_list에서 확인할 수 있음!
                    // year,month,day: 연,월,일 날짜
                    String url = "http://www.gimpo.hs.kr/main.php?menugrp=021100&master=meal2&act=list&"
                            + "SearchYear="  + String.valueOf(year)
                            + "&SearchMonth="+ String.valueOf(month)
                            + "&SearchDay="  + String.valueOf(day) + "#diary_list";
                    Document doc = Jsoup.connect(url).get();
                    Log.d("url", url);

                    Elements data = doc.select("div.meal_content.col-md-7 div.meal_table table tbody");
                    int cnt = 0;
                    for(Element e: data) {
                        mealData[day - 1][cnt++] = e.text().trim();
                        Log.d("data", e.text().trim());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            for(int i = 0; i < lastDay; i++) {
                List<String> temp = new ArrayList<>();
                for(int j = 0; j < 2; j++) {
                    if(mealData[i][j] != null && mealData[i][j].compareTo("등록된 식단 정보가 없습니다.") != 0) {
                        StringBuilder sb = new StringBuilder(mealData[i][j]);
                        for(int k = 0; k < sb.length(); k++) {
                            if(sb.charAt(k) == ' ') {
                                boolean isDeleted = false;
                                for(int l = 0; l < 10; l++) {
                                    if(String.valueOf(l).compareTo(String.valueOf(sb.charAt(k))) == 0) {
                                        sb.deleteCharAt(j);
                                        isDeleted = true;
                                        break;
                                    }
                                }
                                if(!isDeleted) {
                                    sb.replace(j, j+1, "\n");
                                }
                            }
                        }
                        mealData[i][j] = sb.toString();
                        temp.add(sb.toString());
                        Log.d("data" + i + "," + j, mealData[i][j]);
                    }
                    /*String LastUpdated = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
                    String sqlInsert = "insert or replace into " + DataSQLiteHelper.MEAL_TABLE_NAME + " values(\"" + LastUpdated + "\", \"" + i + 1 + "\", \"" + j + "\", \"" + mealData[i][j] + "\")";
                    db.getWritableDatabase().execSQL(DataSQLiteHelper.MEAL_TABLE_DROP);
                    db.getWritableDatabase().execSQL(DataSQLiteHelper.MEAL_TABLE_CREATE);
                    db.getWritableDatabase().execSQL(sqlInsert);*/
                }
                mealDataList.add(temp);
            }
            dataFormat.mealData = new Meal(Integer.parseInt(new SimpleDateFormat("MM", Locale.getDefault()).format(Calendar.getInstance().getTime())) ,mealDataList);
            return true;
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean isInitialized) {
        /*if(isInitialized) {
            for(int i = 0; i < lastDay; i++) {
                List<String> temp = new ArrayList<>();
                for(int j = 0; j < 2; j++) {
                    if(mealData[i][j] != null && mealData[i][j].compareTo("등록된 식단 정보가 없습니다.") != 0) {
                        StringBuilder sb = new StringBuilder(mealData[i][j]);
                        for(int k = 0; k < sb.length(); k++) {
                            if(sb.charAt(k) == ' ') {
                                boolean isDeleted = false;
                                for(int l = 0; l < 10; l++) {
                                    if(String.valueOf(l).compareTo(String.valueOf(sb.charAt(k))) == 0) {
                                        sb.deleteCharAt(j);
                                        isDeleted = true;
                                        break;
                                    }
                                }
                                if(!isDeleted) {
                                    sb.replace(j, j+1, "\n");
                                }
                            }
                        }
                        mealData[i][j] = sb.toString();
                        temp.add(sb.toString());
                        Log.d("data" + i + "," + j, mealData[i][j]);
                    }
                    /*String LastUpdated = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
                    String sqlInsert = "insert or replace into " + DataSQLiteHelper.MEAL_TABLE_NAME + " values(\"" + LastUpdated + "\", \"" + i + 1 + "\", \"" + j + "\", \"" + mealData[i][j] + "\")";
                    db.getWritableDatabase().execSQL(DataSQLiteHelper.MEAL_TABLE_DROP);
                    db.getWritableDatabase().execSQL(DataSQLiteHelper.MEAL_TABLE_CREATE);
                    db.getWritableDatabase().execSQL(sqlInsert);*//*
                }
                mealDataList.add(temp);
            }
            dataFormat.mealData = new Meal(Integer.parseInt(new SimpleDateFormat("MM", Locale.getDefault()).format(Calendar.getInstance().getTime())) ,mealDataList);
        }*/
    }
}

class InitEventData
        extends AsyncTask<Boolean, Void, Boolean> {

    private DataSQLiteHelper db;
    private DataFormat dataFormat;

    InitEventData(DataSQLiteHelper db, DataFormat dataFormat) {
        this.db = db;
        this.dataFormat = dataFormat;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Boolean... isConnected) {

        return false;
    }

    @Override
    protected void onPostExecute(Boolean isInitialized) {

    }
}

class InitAirQualData
        extends AsyncTask<Boolean, Void, Boolean> {

    private String airData;
    private String airQualityFormat;
    private String[] airQualityData = new String[8];
    private DataSQLiteHelper db;
    private DataFormat dataFormat;

    InitAirQualData(DataSQLiteHelper db, DataFormat dataFormat, String airQualityFormat) {
        this.db = db;
        this.airQualityFormat = airQualityFormat;
        this.dataFormat = dataFormat;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Boolean... isConnected) {
        try{
            Document doc = Jsoup.connect("http://m.airkorea.or.kr/sub_new/sub41.jsp")
                    .cookie("isGps","N")
                    .cookie("station","131471")
                    .cookie("lat", "37.619355")
                    .cookie("lng","126,716748")
                    .get();
            airData = "";
            Elements airData = doc.select("div#detailContent div");
            int cnt = 0;
            for(Element e: airData) {
                System.out.println("place: " + e.text());
                airQualityData[cnt] = e.text().trim();
                cnt++;
            }
            dataFormat.airQualData = new AirQual(new SimpleDateFormat("yyyy-MM-dd HH", Locale.getDefault()).format(Calendar.getInstance().getTime()), Arrays.asList(airQualityData));
            return true;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean isInitialized) {
        airData += String.format(Locale.getDefault(),
                airQualityFormat,
                airQualityData[0],
                airQualityData[1],
                airQualityData[2],
                airQualityData[3],
                airQualityData[4],
                airQualityData[5],
                airQualityData[6],
                airQualityData[7]);
        //dataFormat.airQualData = new AirQual(new SimpleDateFormat("yyyy-MM-dd HH", Locale.getDefault()).format(Calendar.getInstance().getTime()), Arrays.asList(airQualityData));
        /*String LastUpdated = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
        String sqlInsert = "insert or replace into " + DataSQLiteHelper.AIR_TABLE_NAME +" values(\"" + LastUpdated + "\", \"" + airData + "\")";
        db.getWritableDatabase().execSQL(DataSQLiteHelper.AIR_TABLE_DROP);
        db.getWritableDatabase().execSQL(DataSQLiteHelper.AIR_TABLE_CREATE);
        db.getWritableDatabase().execSQL(sqlInsert);*/

    }
}