package kr.hs.gimpo.smartclass;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import kr.hs.gimpo.smartclass.*;
import kr.hs.gimpo.smartclass.Data.DataFormat;
import kr.hs.gimpo.smartclass.Data.InitTimeData;
import kr.hs.gimpo.smartclass.Fragment.TimeCard;

public class TimeTableActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    boolean isConnected;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("test");
    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String today = new SimpleDateFormat("MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        if(today.compareTo("04-16")==0) {
            setTheme(R.style.remember0416);
        } else {
            setTheme(R.style.AppTheme_NoActionBar);
        }
        setContentView(R.layout.activity_time_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.time_title);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
        }

        /*mDatabase.child("timeDataFormat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String updatedTime="";
                if(!isInitTable) {
                    System.out.println(dataSnapshot);
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

                    updatedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
                }
                DataFormat.timeDataFormat = dataSnapshot.getValue(DataFormat.Time.class);
                InitDataThread[] initDataThread = new InitDataThread[11];
                ArrayList<Thread> classNo = new ArrayList<>();
                for(int i = 0; i < 11; i++) {
                    initDataThread[i] = new InitDataThread(i, DataFormat.timeDataFormat.timeJsonData);
                    classNo.add(initDataThread[i]);
                    initDataThread[i].start();
                }
                for(int i = 0; i < 11; i++) {
                    try {
                        classNo.get(i).join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                JsonParser parser = new JsonParser();
                JsonElement data = parser.parse(DataFormat.timeDataFormat.timeJsonData);

                time_updated = "표시된 기간: "
                        + data.getAsJsonObject().get("일자자료").getAsJsonArray().get(0).getAsJsonArray().get(1).getAsString()
                        + "\n마지막 업데이트(서버): "
                        + data.getAsJsonObject().get("저장일").getAsString()
                        + "\n마지막 업데이트(사용자): "
                        + (updatedTime.compareTo("")!=0?updatedTime:new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime()));
                initTable(Grade, Class, time_updated);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final TextView time_class  = (TextView) findViewById(R.id.time_class);
        time_class.setText(refreshClassNo());

        final ImageButton button_grade_left = (ImageButton) findViewById(R.id.time_button_grade_back);
        button_grade_left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(Grade > 0) {
                    Grade--;
                } else {
                    Grade = 2;
                }
                time_class.setText(refreshClassNo());
            }
        });

        final ImageButton button_grade_right = (ImageButton) findViewById(R.id.time_button_grade_forward);
        button_grade_right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(Grade < 2) {
                    Grade++;
                } else {
                    Grade = 0;
                }
                time_class.setText(refreshClassNo());
            }
        });

        final ImageButton button_class_left = (ImageButton) findViewById(R.id.time_button_class_back);
        button_class_left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(Class > 0) {
                    Class--;
                } else {
                    Class = 10;
                }
                time_class.setText(refreshClassNo());
            }
        });

        final ImageButton button_class_right = (ImageButton) findViewById(R.id.time_button_class_forward);
        button_class_right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(Class < 10) {
                    Class++;
                } else {
                    Class = 0;
                }
                time_class.setText(refreshClassNo());
            }
        });
    }
    */
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        String today = new SimpleDateFormat("MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        if(today.compareTo("04-16")==0) {
            setTheme(R.style.remember0416);
        } else {
            setTheme(R.style.AppTheme_NoActionBar);
        }
        setContentView(R.layout.activity_time_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.time_title);
        setSupportActionBar(toolbar);
    
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        
        if(findViewById(R.id.time_card_fragment) != null) {
            Fragment fragment = new TimeCard();
            
            //fragment.setArguments();
            
            getSupportFragmentManager().beginTransaction().add(R.id.time_card_fragment, fragment).commit();
        }
    }
    
    private String[] date = {"mon", "tue", "wed", "thu", "fri"};
    private static final String ID_FORMAT = "time_card_%s_%d";
    private String[][][][][] table_data_default = new String[3][11][5][7][2];
    private String[][][][][] table_data_updated = new String[3][11][5][7][2];
    private String time_updated;
    private int Grade = 0, Class = 0;
    private boolean isInitTable = false;

    private CharSequence refreshClassNo() {
        if(isInitTable) {
            initTable(Grade, Class, time_updated); // TODO: 최적화 완료 후 적용 필요
        }
        return String.valueOf(Grade + 1) + getResources().getString(R.string.Grade) + " " + String.valueOf(Class + 1) + getResources().getString(R.string.Class);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.time_table, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_main) {
            finish();
        } else if (id == R.id.nav_table) {

        } else if (id == R.id.nav_meal) {
            intent = new Intent(TimeTableActivity.this, MealInfoActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_calendar) {
            intent = new Intent(TimeTableActivity.this, SchoolEventActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_setting) {
            intent = new Intent(TimeTableActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_info) {
            try {
                CharSequence version = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
                CharSequence versionName = getResources().getString(R.string.noti_version_is) + " " + version.toString();
                Toast.makeText(getApplicationContext(),versionName,Toast.LENGTH_SHORT).show();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void initTable(int classGrade, int classNo, String lastUpdatedDate) { // TODO: 스레드 처리하기
        TextView TargetTextView;
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 7; j++) {
                for(int k = 0; k < 2; k++) {
                    String id = String.format(Locale.getDefault(), ID_FORMAT, date[i], j);
                    if(k == 1) {
                        id += "_teacher";
                    }
                    TargetTextView = (TextView) findViewById(getResources().getIdentifier(id, "id", getPackageName()));
                    Log.d("TargetTextView", id);
                    /*if(table_data_default[classGrade][classNo][i][j][k].compareTo(table_data_updated[classGrade][classNo][i][j][k]) != 0) {
                        TargetTextView.setTypeface(Typeface.DEFAULT_BOLD);
                    } else {
                        TargetTextView.setTypeface(Typeface.DEFAULT);
                    }*/
                    // 원래대로라면
                    //TargetTextView.setText(table_data_updated[classGrade][classNo][i][j][k]);
                    TargetTextView.setText(table_data_default[classGrade][classNo][i][j][k].compareTo("")!=0?table_data_default[classGrade][classNo][i][j][k]:table_data_updated[classGrade][classNo][i][j][k]);
                }
            }
        }
        TargetTextView = (TextView) findViewById(R.id.time_table_info);
        TargetTextView.setText(lastUpdatedDate);
        if(!isInitTable) {
            isInitTable = true;
            Log.d("init", "Table");
        }
    }

    class InitDataThread // TODO: 스레드 만들기
            extends Thread {

        private int classNo;
        private JsonElement parsedData;

        InitDataThread(int classNo, String rawData) {
            this.classNo = classNo;
            JsonParser parser = new JsonParser();
            parsedData = parser.parse(rawData);
        }

        public void run() {
            for(int classGrade = 0; classGrade < 3; classGrade++) {
                for(int dow = 0; dow < 5; dow++) {
                    for(int period = 0; period < 7; period++) {
                        table_data_default[classGrade][classNo][dow][period] = getTime("시간표", classGrade, classNo, dow, period);
                        table_data_updated[classGrade][classNo][dow][period] = getTime("학급시간표", classGrade, classNo, dow, period);
                    }
                }
            }
        }

        private synchronized String[] getTime(String mode, int classGrade, int classNo, int dow, int period) {
            int temp =  parsedData
                    .getAsJsonObject().get(mode)
                    .getAsJsonArray().get(classGrade + 1)
                    .getAsJsonArray().get(classNo + 1)
                    .getAsJsonArray().get(dow + 1)
                    .getAsJsonArray().get(period + 1)
                    .getAsInt();
            if(temp > 10000) temp = 0;
            String[] timeData = new String[2];
            timeData[0] = getSubject(temp);
            Log.d("getTime", "time = " + temp + " in grade " + (classGrade + 1) + " class " + (classNo + 1) + " dow " + (dow + 1) + " period " + (period + 1));
            timeData[1] = getTeacher(temp);
            return timeData;
        }

        private String getSubject(int time) {
            for(;;) {
                if(time >= 100) {
                    time -= 100;
                } else {
                    break;
                }
            }
            String temp = parsedData.getAsJsonObject().get("과목명")
                    .getAsJsonArray().get(time)
                    .getAsString();
            return temp.compareTo("60") != 0 ? temp : "";
        }

        private String getTeacher(int time) {
            Log.d("getTeacher", "time = " + time);
            int subject = time;
            for(;;) {
                if(subject >= 100) {
                    subject -= 100;
                } else {
                    break;
                }
            }
            Log.d("getTeacher", "subject = " + subject);
            time = (time - subject) / 100;
            Log.d("getTeacher", "teacherIndex = " + time);
            return parsedData.getAsJsonObject().get("성명")
                    .getAsJsonArray().get(time)
                    .getAsString();
        }
    }
}
