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

import org.hyunjun.school.School;
import org.hyunjun.school.SchoolSchedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import kr.hs.gimpo.smartclass.R;

public class EventCard extends Fragment {
    
    public EventCard() {
    
    }
    
    String date = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(Calendar.getInstance().getTime());
    
    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        String temp = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(Calendar.getInstance().getTime());
        if(args != null) {
            date = args.getString("date", temp);
        } else {
            date = temp;
        }
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.card_frag_event, container, false);
        
        initEventDataListener(view);
        
        return view;
    }
    
    DatabaseReference mDatabase;
    
    public void initEventDataListener(final View view) {
        mDatabase = FirebaseDatabase.getInstance().getReference("test");
        
        mDatabase.child("eventDataFormat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                
                System.out.println(dataSnapshot);
                System.out.println(date);
                
                String[] ymd = date.split("-");
                
                SchoolSchedule schedule = dataSnapshot.child("eventData").child(ymd[0]).child(String.valueOf(Integer.parseInt(ymd[1]) - 1)).child(String.valueOf(Integer.parseInt(ymd[2]) - 1)).getValue(SchoolSchedule.class);
                System.out.println(ymd[0]);
                System.out.println(String.valueOf(Integer.parseInt(ymd[1]) - 1));
                System.out.println(String.valueOf(Integer.parseInt(ymd[2]) - 1));
                
                System.out.println(schedule);
    
                TextView textView = view.findViewById(R.id.home_card_event_data);
                textView.setText(schedule.schedule);
            }
    
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
        
            }
        });
    }
}
