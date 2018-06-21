package kr.hs.gimpo.smartclass.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.hyunjun.school.SchoolSchedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import kr.hs.gimpo.smartclass.R;


public class EventCard extends Fragment {
    
    public EventCard() {
    
    }
    
    // 날짜 데이터의 기본값을 설정합니다.
    String date = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(Calendar.getInstance().getTime());
    
    boolean isInit = false;
    
    // 데이터를 설정할 수 있는 함수입니다. 이 함수가 호출되면 날짜를 따로 입력할 수 있으며, 이 함수를 호출하지 않으면 기본값(그날의 날짜)을 사용하여 학사일정 데이터를 불러옵니다.
    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        
        // 오늘의 날짜 값을 받아옵니다. 주어진 날짜 데이터가 유효하지 않을 때 기본값으로 지정됩니다.
        String temp = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(Calendar.getInstance().getTime());
        
        // 날짜 데이터 유효성을 검사합니다.
        if(args != null) {
            // 날짜 데이터가 유효하다면 데이터를 받아옵니다.
            date = args.getString("date", temp);
        } else {
            // 날짜 데이터가 유효하지 않다면 기본값을 사용합니다.
            date = temp;
        }
        
        // 화면에 데이터를 띄웁니다.
        if(isInit) {
            initEventDataListener(getView());
        }
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.event_card, container, false);
        
        isInit = true;
        
        // 화면에 데이터를 띄웁니다. 이미 date에는 setArgument() 를 통해 날짜 데이터가 입력되어 있어야 합니다.
        initEventDataListener(view);
        
        return view;
    }
    
    // 데이터를 받아올 Firebase 클라이언트를 설정합니다.
    DatabaseReference mDatabase;
    
    public void initEventDataListener(final View view) {
        // 데이터를 받아올 Firebase 클라이언트를 설정합니다.
        mDatabase = FirebaseDatabase.getInstance().getReference("test");
        
        // 날짜 데이터를 참고하여 데이터를 불러옵니다.
        mDatabase.child("eventDataFormat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                
                // 날짜 정보는 "연-월-일"의 형식으로 저장되어 있습니다.
                // 따라서 유효한 날짜 정보라면 '-'을 기준으로 나누었을 때 연, 월, 일의 3개로 나누어져야 합니다.
                String[] ymd = date.split("-");
                
                // 날짜 정보에 맞추어 데이터를 받아옵니다.
                // 데이터는 eventDataFormat/eventData/년/월-1/일-1/ 의 위치에 SchoolSchedule 객체의 형식으로 저장되어 있습니다.
                SchoolSchedule schedule = dataSnapshot
                        .child("eventData")
                        .child(ymd[0])
                        .child(String.valueOf(Integer.parseInt(ymd[1]) - 1))
                        .child(String.valueOf(Integer.parseInt(ymd[2]) - 1))
                        .getValue(SchoolSchedule.class);
                
                // 학사일정 카드의 데이터 부분에 있는 텍스트뷰에 접근합니다.
                TextView textView = view.findViewById(R.id.home_card_event_data);
                
                // 데이터가 알맞게 들어왔다면 ""의 형태 혹은 "일정"의 형태입니다.
                // 데이터가 어떻게 되어 있는지 검사합니다.
                if(schedule != null && schedule.schedule.compareTo("") != 0) {
                    // 데이터가 존재한다면 그 데이터를 그대로 표시합니다.
                   textView.setText(schedule.schedule);
                } else {
                    // 데이터가 존재하지 않는다면 데이터가 존재하지 않는다고 표시합니다.
                    textView.setText(getResources().getString(R.string.no_data));
                }
            }
    
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
        
            }
        });
    }
}
