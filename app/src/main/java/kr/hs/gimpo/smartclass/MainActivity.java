package kr.hs.gimpo.smartclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /*
    * TODO: initData()
    * 1. Firebase DB 연결
    * 2. 시간표 데이터 업데이트
    * 3. 급식 데이터 업데이트
    * 4. 학사일정 데이터 업데이트
    * 5. 기상 데이터 업데이트
    * 6. 오프라인으로 저장하기
    * 7. 객체 전달하기
    * */
    private void initData() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("test");

        // Read from the database
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("test", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("test", "Failed to read value.", error.toException());
            }
        });

        Calendar calendar = Calendar.getInstance();
        Date today = Calendar.getInstance().getTime();
        calendar.setTime(today);

        Document doc;

        // 시간표 데이터 업데이트
        try {
            doc = Jsoup.connect("http://comcigan.com:4082/_hourdat?sc=26203").get();

            Element data = doc.body();
            Log.d("TimeTable", "Data initializing...");
            JsonParser parser = new JsonParser();
            JsonElement timeTableData = parser.parse(data.text().trim());
            TimeTable table = new TimeTable(timeTableData);
            ref.child("TimeTable").setValue(table);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 급식 데이터 업데이트
        /*
        try {
            for(int i = 1; i < calendar.get(Calendar.DAY_OF_MONTH); i++) {

                calendar.set(Calendar.DAY_OF_MONTH, i);
                today = new Date(calendar.getTimeInMillis());
                String[] date = new String[3];
                date[0] = new SimpleDateFormat("yyyy", Locale.getDefault()).format(today);
                date[1] = new SimpleDateFormat("MM", Locale.getDefault()).format(today);
                date[2] = new SimpleDateFormat("dd", Locale.getDefault()).format(today);

                doc = Jsoup.connect("http://www.gimpo.hs.kr/main.php?menugrp=021100&master=meal2&act=list&SearchYear="+date[0]+"&SearchMonth="+date[1]+"&SearchDay="+date[2]+"#diary_list").get();
                Elements data = doc.select("div.meal_content.col-md-7 div.meal_table table tbody");
                Log.d("MealInfo", "Data initializing...");
                for(Element element: data) {
                    element.text().trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 학사일정 데이터 업데이트

        // 기상 데이터 업데이트
        */
    }
    // TODO: 스레드 작성(Jsoup.connect().get()은 메인 스레드에서 작동하지 않는다!
    class InitDataThread extends Thread {

    }
}
