package kr.hs.gimpo.smartclass;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import kr.hs.gimpo.smartclass.Data.InitAirQualData;
import kr.hs.gimpo.smartclass.Data.InitEventData;
import kr.hs.gimpo.smartclass.Data.InitMealData;
import kr.hs.gimpo.smartclass.Data.InitTimeData;
import kr.hs.gimpo.smartclass.Data.TimeVerifier;

public class IntroActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    Integer thisMonth;
    String thisTime, thisYear;

    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String today = new SimpleDateFormat("MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        if(today.compareTo("04-16")==0) {
            setTheme(R.style.remember0416_AppTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_intro);
        System.out.println("setContentView: activity_intro");
        
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch(DatabaseException e) {
            e.printStackTrace();
        }
    
        TimeVerifier.validateTime();

        isConnected = false;
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("test");
        mDatabase.keepSynced(true);

        mDatabase.child("mealDataFormat").child("thisMonth").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot);
                thisMonth = dataSnapshot.getValue(Integer.class);
                System.out.println(thisMonth);

                if(isConnected) {
                    InitMealData initMealData = new InitMealData(mDatabase, thisMonth);
                    initMealData.execute();
                    try {
                        initMealData.get();
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    } catch(ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("airQualDataFormat").child("thisTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot);
                thisTime = dataSnapshot.getValue(String.class);
                System.out.println(thisTime);
                if(isConnected) {
                    InitAirQualData initAirQualData = new InitAirQualData(mDatabase, thisTime);
                    initAirQualData.execute();
                    try {
                        initAirQualData.get();
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    } catch(ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        
        mDatabase.child("eventDataFormat").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot);
                thisYear = String.valueOf(dataSnapshot.child("thisYear").getValue(Long.class));
                System.out.println(thisYear);
                if(isConnected) {
                    InitEventData initEventData = new InitEventData(mDatabase, thisYear);
                    initEventData.execute();
                    try {
                        initEventData.get();
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    } catch(ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
    
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
        
            }
        });

        initData(isConnected, mDatabase);

        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    private void initData(Boolean isConnected, DatabaseReference mDatabase) {
        if(isConnected) {
            InitTimeData initTimeData = new InitTimeData(mDatabase);
            initTimeData.execute();
            try {
                initTimeData.get();
            } catch(InterruptedException e) {
                e.printStackTrace();
            } catch(ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}

