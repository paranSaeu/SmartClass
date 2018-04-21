package kr.hs.gimpo.smartclass;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

//TODO: 데이터 초기화 스레드 만들기

class InitTimeData
        extends AsyncTask<Void, Void, Boolean> {

    private DatabaseReference mDatabase;

    private String jsonData = "";

    InitTimeData(DatabaseReference mDatabase) {
        this.mDatabase = mDatabase;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... param) {
        try {
            // 김포고등학교의 실시간 시간표 정보는 http://comcigan.com:4082/_hourdat?sc=26203에서 확인할 수 있다!
            // 변경: http://comcigan.com:4082/_hourdata?sc=26203에서 확인 가능!
            // 변경: http://comcigan.com:4082/_h119870?sc=26203
            // http://comcigan.com:4083/_h119293?sc=26203
            // http://comcigan.com:4082/_h177193?sc=26203
            // http://comcigan.com:4081/119295?ODEwM18yNjIwM18xXz

            // <body><script></script></body> 사이에 있는 var stor='?'의 ?에 해당하는 부분이 바뀐다는 것을 알아내었다!
            // 포트를 4082로 고정하고 /st에서 <body><script></script></body>를 불러와 stor의 값을 읽으면
            // 시간표를 정상적으로 읽어올 수 있을 것이다!
            // http://comcigan.com:4082/"+stor+"?sc=26203

            // sc=26203: 김포고등학교의 데이터

            Document doc = Jsoup.connect("http://comcigan.com:4082/st").get();
            Elements urlData = doc.select("body script");
            String temp = urlData.toString();
            int idx = temp.lastIndexOf("stor='");
            temp = temp.substring(idx+6);
            idx = temp.indexOf("';");
            String stor = temp.substring(0, idx);
            System.out.println("------------------------------");
            System.out.println(stor);
            System.out.println("------------------------------");

            doc = Jsoup.connect("http://comcigan.com:4082/"+stor+"?sc=26203").get();

            // 시간표 데이터는 <body>에 있다!
            Element data = doc.body();

            System.out.println("------------------------------");
            System.out.println("data: " + data.text());
            System.out.println("------------------------------");

            jsonData += data.text().trim();

            if(jsonData.compareTo("") != 0 && jsonData.compareTo("{}") != 0) {
                DataFormat.timeDataFormat = new Time(jsonData);
                mDatabase.child("timeDataFormat").setValue(DataFormat.timeDataFormat);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean isInitialized) {
    }
}

class InitMealData
        extends AsyncTask<Void, Void, Boolean> {

    private int lastDay = initLastDay();
    private String[][] mealData = new String[lastDay][2];
    private List<List<String>> mealDataList = new ArrayList<>();
    private DatabaseReference mDatabase;
    private boolean isUpdateNeed;

    InitMealData(DatabaseReference mDatabase, Integer thisMonth) {
        this.mDatabase = mDatabase;

        if(thisMonth != null) {
            this.isUpdateNeed = (thisMonth != Integer.parseInt(new SimpleDateFormat("MM", Locale.getDefault()).format(Calendar.getInstance().getTime())));
        } else {
            this.isUpdateNeed = true;
        }

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
    protected Boolean doInBackground(Void... param) {
        if(isUpdateNeed) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Calendar.getInstance().getTime());
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);
            int year = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(calendar.getTime()));
            int month = Integer.parseInt(new SimpleDateFormat("MM", Locale.getDefault()).format(calendar.getTime()));
            for(int day = 1; day <= lastDay; day++) {
                calendar.set(Calendar.DAY_OF_MONTH, day);
                if(!(calendar.get(Calendar.DAY_OF_WEEK) == 1 || calendar.get(Calendar.DAY_OF_WEEK) == 7)) {
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
            }
            for(int i = 0; i < lastDay; i++) {
                List<String> temp = new ArrayList<>();
                for(int j = 0; j < 2; j++) {
                    if(mealData[i][j] != null && mealData[i][j].compareTo("등록된 식단 정보가 없습니다.") != 0) {
                        StringBuilder sb = new StringBuilder(mealData[i][j]);
                        for(int k = 0; k < sb.length(); k++) {
                            if(sb.charAt(k) == ' ') {
                                boolean isChanged = false;
                                for(int l = 0; l < 10; l++) {
                                    if(sb.charAt(k + 1) == String.valueOf(l).charAt(0)) {
                                        sb.deleteCharAt(k);
                                        isChanged = true;
                                    }
                                }
                                if(!isChanged) {
                                    sb.replace(k, k+1, "\n");
                                }
                            }
                        }
                        temp.add(sb.toString());
                    }
                }
                mealDataList.add(temp);
            }
            DataFormat.mealDataFormat = new Meal(Integer.parseInt(new SimpleDateFormat("MM", Locale.getDefault()).format(Calendar.getInstance().getTime())) ,mealDataList);
            mDatabase.child("mealDataFormat").setValue(DataFormat.mealDataFormat);
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean isInitialized) {
    }
}

class InitEventData
        extends AsyncTask<Void, Void, Boolean> {

    private DatabaseReference mDatabase;

    InitEventData(DatabaseReference mDatabase) {
        this.mDatabase = mDatabase;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... param) {

        return false;
    }

    @Override
    protected void onPostExecute(Boolean isInitialized) {

    }
}

class InitAirQualData
        extends AsyncTask<Void, Void, Boolean> {

    private String[] airData = new String[8];
    private DatabaseReference mDatabase;
    private boolean isUpdateNeed;

    InitAirQualData(DatabaseReference mDatabase, String thisTime) {
        this.mDatabase = mDatabase;

        if(thisTime != null) {
            this.isUpdateNeed = (thisTime.compareTo(new SimpleDateFormat("yyyy-MM-dd HH", Locale.getDefault()).format(Calendar.getInstance().getTime())) != 0);
        } else {
            this.isUpdateNeed = true;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... param) {
        if(isUpdateNeed) {
            try{
                Document doc = Jsoup.connect("http://m.airkorea.or.kr/sub_new/sub41.jsp")
                        .cookie("isGps","N")
                        .cookie("station","131471")
                        .cookie("lat", "37.619355")
                        .cookie("lng","126,716748")
                        .get();
                Elements data = doc.select("div#detailContent div");
                int cnt = 0;
                for(Element e: data) {
                    System.out.println("place: " + e.text());
                    airData[cnt] = e.text().trim();
                    cnt++;
                }
                DataFormat.airQualDataFormat = new AirQual(Arrays.asList(airData));

                mDatabase.child("airQualDataFormat").setValue(DataFormat.airQualDataFormat);
                return true;
            } catch(IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean isInitialized) {
        if(isInitialized) {
            Log.d("setValue", "ok.");
        }
    }
}