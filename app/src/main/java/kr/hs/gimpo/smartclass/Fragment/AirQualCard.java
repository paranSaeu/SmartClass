package kr.hs.gimpo.smartclass.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kr.hs.gimpo.smartclass.R;

public class AirQualCard extends Fragment {
    
    public AirQualCard() {
    
    }
    
    final String[] targetList = {"pm10 ", "pm25", "o3", "no2", "co", "so2", "qual"};
    final String DATA_ID_FORMAT = "card_air_data_%s";
    final String STAT_ID_FORMAT = "card_air_status_%s";
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.card_frag_air, container, false);
        
        initAirQualDataListener(view);
        
        return view;
    }
    
    DatabaseReference mDatabase;
    
    private void initAirQualDataListener(final View view) {
        mDatabase = FirebaseDatabase.getInstance().getReference("test");
        
        mDatabase.child("AirQualDataFormat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.child("thisTime").getValue(String.class));
                TextView textView = view.findViewById(R.id.card_air_place);
    
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd HH", Locale.getDefault()).parse(dataSnapshot.child("thisTime").getValue(String.class));
                    
                    String date_info = new SimpleDateFormat("yyyy'년 'MM'월 'dd'일 'HH'시 'mm' 분'", Locale.getDefault()).format(date);
                    
                    textView.setText(date_info);
                } catch(ParseException e) {
                    e.printStackTrace();
                    textView.setText(getResources().getString(R.string.error));
                }
                
                
                
            }
    
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
        
            }
        });
    }
}
