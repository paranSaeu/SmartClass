package kr.hs.gimpo.smartclass;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.ExecutionException;

public class IntroActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        boolean isConnected = false;
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        mDatabase = FirebaseDatabase.getInstance().getReference("test");

        initData(isConnected, mDatabase);

        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    private void initData(Boolean isConnected, DatabaseReference mDatabase) {
        InitTimeData initTimeData = new InitTimeData();
        initTimeData.execute(isConnected);
        InitMealData initMealData = new InitMealData();
        initMealData.execute(isConnected);
        InitEventData initEventData = new InitEventData();
        initEventData.execute(isConnected);
        InitAirQualData initAirQualData = new InitAirQualData(getResources().getString(R.string.home_card_air_quality_format));
        initAirQualData.execute(isConnected);
        try {
            initTimeData.get();
            initMealData.get();
            initEventData.get();
            initAirQualData.get();
        } catch(InterruptedException e) {
            e.printStackTrace();
        } catch(ExecutionException e) {
            e.printStackTrace();
        }

    }

}

