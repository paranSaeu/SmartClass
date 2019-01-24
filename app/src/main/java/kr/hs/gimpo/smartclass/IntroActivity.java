package kr.hs.gimpo.smartclass;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.concurrent.ExecutionException;

import kr.hs.gimpo.smartclass.Data.InitEventData;
import kr.hs.gimpo.smartclass.Data.InitMealData;
import kr.hs.gimpo.smartclass.Data.InitTimeData;

public class IntroActivity extends AppCompatActivity {

    DatabaseReference mDatabase;

    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);
        long startTime = System.currentTimeMillis();
        TextView version = findViewById(R.id.intro_version);
        try {
            CharSequence versionName = "v " + getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
            version.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        
        isConnected = false;
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
        }
    
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch(DatabaseException e) {
            e.printStackTrace();
        }
        
        mDatabase = FirebaseDatabase.getInstance().getReference("test");
        mDatabase.keepSynced(true);
        
        if(isConnected) {
            initData();
        }
        
        long endTime = System.currentTimeMillis();
        
        long term = endTime - startTime;
        
        Log.i("IntroActivity", "term: " + term);
    
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000 - term > 0 ? 3000 - term : 1000);
    }

    private void initData() {
        InitMealData initMealData = new InitMealData();
        initMealData.execute();
        try {
            initMealData.get();
        } catch(InterruptedException e) {
            e.printStackTrace();
        } catch(ExecutionException e) {
            e.printStackTrace();
        }
    
        InitEventData initEventData = new InitEventData();
        initEventData.execute();
        try {
            initEventData.get();
        } catch(InterruptedException e) {
            e.printStackTrace();
        } catch(ExecutionException e) {
            e.printStackTrace();
        }
    
        InitTimeData initTimeData = new InitTimeData();
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

