package kr.hs.gimpo.smartclass.Data;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    private List<List<String>> mealDataList = new ArrayList<>();
    private DatabaseReference mDatabase;
    private boolean isInternetConnected;

    private final String DEFAULT_URL =
    "https://stu.goe.go.kr/"+
    "sts_sci_md01_001.do"+
    "?"+
    "schulCode=J100000510&"+
    "schulCrseScCode=4&"+
    "schulKndScCode=04&";

    public InitMealData(boolean isInternetConnected) {
        this.isInternetConnected = isInternetConnected;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private Calendar getStartOfWeek() {
        Calendar cal = Calendar.getInstance();
        
        while(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            cal.add(Calendar.DATE, -1);
        }
        
        return cal;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        if(isInternetConnected) {
            /*
            예:
            https://stu.goe.go.kr/sts_sci_md01_001.do?schulCode=J100000510&schulCrseScCode=4&schulKndScCode=04&schYmd=20180818&schMmealScCode=3&

            위 주소의 분석:
            https://stu.goe.go.kr/ : 경기도교육청 산하 학교의 정보
            sts_sci_md01_001.do : 주간 급식 및 영양소 정보
            ? : 이하 쿠키 내용
            schulCode=J100000510 : 김포고등학교
            & : 나열
            schulCrseScCode=4&schulKndScCode=04 : 고등학교
            schYmd=20180818 : 조회하고 싶은 주에 포함된 날짜 (2018년 08월 18일이 있는 주간의 메뉴)
            schMmealScCode=3 : 석식 (1: 조식, 2: 중식, 3: 석식)
            */
    
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            String time = sdf.format(Calendar.getInstance().getTime());
    
            for(int i = 2; i <= 3; i++) {
                StringBuffer url = new StringBuffer(DEFAULT_URL);
        
                url.append("schYmd=").append(time).append("&");
                url.append("schMmealScCode=").append(i).append("&");
        
                try {
                    Document doc = Jsoup.connect(url.toString()).get();
                    
                    Elements column = doc.select("table.tbl_type3 tbody tr");
                    
                    for(Element a : column) {
                        
                        Elements rowName = a.select("th");
                        
                        Log.i("InitMealData", "row name : " + rowName.text().trim());
                        
                        Elements row = a.select("td");
                        for(Element b : row) {
                            Log.i("InitMealData", b.text().trim());
                        }
                    }
                    
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            
            /*
            School api = new School(School.Type.HIGH, School.Region.GYEONGGI, "J100000510");

            String temp = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Calendar.getInstance().getTime());
            String[] ym = temp.split("-");

            try {
                List<SchoolMenu> menu = api.getMonthlyMenu(Integer.parseInt(ym[0]), Integer.parseInt(ym[1]));

                for(int i = 0; i < lastDay; i++) {
                    List<String> tmp = new ArrayList<>();
                    tmp.add(menu.get(i).lunch);
                    tmp.add(menu.get(i).dinner);
                    mealDataList.add(tmp);

                    DataFormat.mealDataFormat = new DataFormat.Meal(Integer.parseInt(ym[1]) ,mealDataList);
                    //mDatabase.child("mealDataFormat").setValue(DataFormat.mealDataFormat);
                    mDatabase.child("mealDataFormat").child("mealLastUpdated").setValue(DataFormat.mealDataFormat.mealLastUpdated);
                    mDatabase.child("mealDataFormat").child("thisMonth").setValue(DataFormat.mealDataFormat.thisMonth);

                    mDatabase.child("mealDataFormat").child("mealData").child(ym[0]).child(ym[1]).setValue(DataFormat.mealDataFormat.mealData);
                }

            } catch(SchoolException e) {
                e.printStackTrace();
            }*/

        }
        return null;
    }

    @Override
    protected void onPostExecute(Boolean isInitialized) {
    }
}
