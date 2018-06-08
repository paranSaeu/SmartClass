package kr.hs.gimpo.smartclass.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import kr.hs.gimpo.smartclass.Data.DataFormat;
import kr.hs.gimpo.smartclass.Data.InitAirQualData;
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
        
        // Firebase DB에서 데이터를 받아와 표시해 줍니다.
        initAirQualDataListener(view);
        
        return view;
    }
    
    DatabaseReference mDatabase;
    
    private void initAirQualDataListener(final View view) {
        mDatabase = FirebaseDatabase.getInstance().getReference("test");
        
        mDatabase.child("AirQualDataFormat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                
                // 시간을 받아옵니다.
                String LastUpdated = dataSnapshot.child("thisTime").getValue(String.class);
                Log.d("AirQualCard", "LastUpdated(Server): "+ LastUpdated);
                String Time = new SimpleDateFormat("yyyy-MM-dd HH", Locale.getDefault()).format(Calendar.getInstance().getTime());
                Log.d("AirQualCard", "Time(Client): "+Time);
                
                // 서버의 데이터 태그와 현재시간을 비교해서
                if(LastUpdated == null || LastUpdated.compareTo(Time) == 0) {
                    // 데이터가 최신이 아니라면 데이터를 갱신합니다.
                    InitAirQualData initAirQualData = new InitAirQualData();
                    initAirQualData.execute();
                    try {
                        initAirQualData.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.d("InitAirQualData", "Failed.");
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                        Log.d("InitAirQualData", "Failed.");
                    }
                    // 데이터가 업데이트되면 onDataChange()가 다시 시작될 것입니다.
                } else {
                    // 데이터가 최신이라면 화면에 표시합니다.
                    
                    // 맨 위의 시간 정보를 표시합니다.
                    TextView textView = view.findViewById(R.id.card_air_place);
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH", Locale.getDefault()).parse(dataSnapshot.child("thisTime").getValue(String.class));
        
                        String date_info = new SimpleDateFormat("yyyy'년 'MM'월 'dd'일 'HH'시 'mm' 분'", Locale.getDefault()).format(date);
        
                        textView.setText(date_info);
                    } catch(ParseException e) {
                        e.printStackTrace();
                        textView.setText(getResources().getString(R.string.error));
                    }
    
                    // 모든 데이터를 오프라인으로 받아옵니다.
                    DataFormat.airQualDataFormat = dataSnapshot.getValue(DataFormat.AirQual.class);
                    
                    // 모든 textView에 반복적으로 적용합니다.
                    for(int i = 1; i < 8; i++) {
                        
                        // 데이터가 담기는 TextView의 ID를 이용하여 TextView를 특정합니다.
                        String dataId = String.format(Locale.getDefault(), DATA_ID_FORMAT, targetList[i]);
                        
                        textView = view.findViewById(getResources().getIdentifier(dataId, "id", view.getContext().getPackageName()));
                        
                        // 데이터 TextView에 데이터를 담습니다.
                        // 단위가 항목에 따라 달라지므로 단위에 주의합니다.
                        String temp = DataFormat.airQualDataFormat.airQualData.get(i) + (i<=2?" ㎍/㎥":(i<6?" ppm":""));
                        textView.setText(temp);
    
                        // 상태가 담기는 TextView의 ID를 이용하여 TextView를 특정합니다.
                        String statusId = String.format(Locale.getDefault(), STAT_ID_FORMAT, targetList[i]);
    
                        textView = view.findViewById(getResources().getIdentifier(statusId, "id", view.getContext().getPackageName()));
                        
                        // 상태 TextView에 상태를 담습니다.
                        // 상태는 -1, 1, 2, 3, 4의 정수로 제공되며, 정수에 따라 상태값을 표시해 줍니다.
                        int stat = Integer.parseInt(DataFormat.airQualDataFormat.airQualData.get(i + 7));
                        switch(stat) {
                            case -1:
                                textView.setTypeface(Typeface.DEFAULT);
                                textView.setTextColor(getResources().getColor(R.color.common));
                                textView.setText(getResources().getString(R.string.unknown));
                                break;
                            case 1:
                                textView.setTypeface(Typeface.DEFAULT);
                                textView.setTextColor(getResources().getColor(R.color.vgood));
                                textView.setText(getResources().getString(R.string.vgood));
                                break;
                            case 2:
                                textView.setTypeface(Typeface.DEFAULT);
                                textView.setTextColor(getResources().getColor(R.color.good));
                                textView.setText(getResources().getString(R.string.good));
                                break;
                            case 3:
                                textView.setTypeface(Typeface.DEFAULT_BOLD);
                                textView.setTextColor(getResources().getColor(R.color.bad));
                                textView.setText(getResources().getString(R.string.bad));
                                break;
                            case 4:
                                textView.setTypeface(Typeface.DEFAULT_BOLD);
                                textView.setTextColor(getResources().getColor(R.color.vbad));
                                textView.setText(getResources().getString(R.string.vbad));
                                break;
                        }
                    }
                }
            }
    
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            
            }
        });
    }
}
