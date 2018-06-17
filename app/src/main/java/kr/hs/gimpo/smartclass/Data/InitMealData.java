package kr.hs.gimpo.smartclass.Data;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class InitMealData
        extends AsyncTask<Void, Void, Boolean> {
    
    private int lastDay = initLastDay();
    private String[][] mealData = new String[lastDay][2];
    private List<List<String>> mealDataList = new ArrayList<>();
    private DatabaseReference mDatabase;
    private boolean isUpdateNeed;
    
    public InitMealData(DatabaseReference mDatabase, Integer thisMonth) {
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
                    } else {
                        temp.add("등록된 식단 정보가 없습니다.");
                    }
                }
                mealDataList.add(temp);
            }
            DataFormat.mealDataFormat = new DataFormat.Meal(Integer.parseInt(new SimpleDateFormat("MM", Locale.getDefault()).format(Calendar.getInstance().getTime())) ,mealDataList);
            //mDatabase.child("mealDataFormat").setValue(DataFormat.mealDataFormat);
            mDatabase.child("mealDataFormat").child("mealLastUpdated").setValue(DataFormat.mealDataFormat.mealLastUpdated);
            mDatabase.child("mealDataFormat").child("thisMonth").setValue(DataFormat.mealDataFormat.thisMonth);
            
            String[] ym = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Calendar.getInstance().getTime()).split("-");
            
            mDatabase.child("mealDataFormat").child("mealData").child(ym[0]).child(ym[1]).setValue(DataFormat.mealDataFormat.mealData);
        }
        
        return true;
    }
    
    @Override
    protected void onPostExecute(Boolean isInitialized) {
    }
}
