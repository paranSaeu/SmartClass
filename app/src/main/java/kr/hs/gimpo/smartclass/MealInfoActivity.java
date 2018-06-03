package kr.hs.gimpo.smartclass;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MealInfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("test");
    boolean isConnected;
    Integer thisMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String date = new SimpleDateFormat("MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        if(date.compareTo("04-16")==0) {
            setTheme(R.style.remember0416);
        } else {
            setTheme(R.style.AppTheme_NoActionBar);
        }
        setContentView(R.layout.activity_meal_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.meal_title);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        calendar = Calendar.getInstance();
        today = Calendar.getInstance().getTime();
        calendar.setTime(today);
        day = new SimpleDateFormat("dd", Locale.getDefault()).format(today);
        month = new SimpleDateFormat("MM", Locale.getDefault()).format(today);
        year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(today);

        /*ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
        }

        mDatabase.child("mealDataFormat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot);
                thisMonth = dataSnapshot.child("thisMonth").getValue(Integer.class);
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

                if(!(calendar.get(Calendar.DAY_OF_WEEK) == 1 || calendar.get(Calendar.DAY_OF_WEEK) == 7)) {
                    System.out.println(dataSnapshot);
                    DataFormat.mealDataFormat = dataSnapshot.getValue(Meal.class);
                    int thisDay = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(Calendar.getInstance().getTime()));
                    int thisMeal = Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(Calendar.getInstance().getTime())) < 14? 0 : 1;
                    if() {DataFormat.mealDataFormat.mealData.get(thisDay - 1).get(thisMeal);
                } else {
                    getResources().getString(R.string.meal_card_data_null));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        initDate(year, month, day);

        lunch_data = (TextView)findViewById(R.id.meal_card_lunch_data);
        lunch_data.setMovementMethod(new ScrollingMovementMethod()); //스크롤 가능한 텍스트뷰로 만들기

        dinner_data = (TextView) findViewById(R.id.meal_card_dinner_data);
        lunch_data.setMovementMethod(new ScrollingMovementMethod());

        jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();

        final ImageButton month_left = (ImageButton) findViewById(R.id.meal_button_month_back);
        month_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -7);
                today = new Date(calendar.getTimeInMillis());
                day = new SimpleDateFormat("dd", Locale.getDefault()).format(today);
                month = new SimpleDateFormat("MM", Locale.getDefault()).format(today);
                year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(today);
                initDate(year, month, day);
            }
        });

        final ImageButton month_right = (ImageButton) findViewById(R.id.meal_button_month_forward);
        month_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 7);
                today = new Date(calendar.getTimeInMillis());
                day = new SimpleDateFormat("dd", Locale.getDefault()).format(today);
                month = new SimpleDateFormat("MM", Locale.getDefault()).format(today);
                year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(today);
                initDate(year, month, day);
            }
        });

        final ImageButton day_left = (ImageButton) findViewById(R.id.meal_button_day_back);
        day_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -1);
                today = new Date(calendar.getTimeInMillis());
                day = new SimpleDateFormat("dd", Locale.getDefault()).format(today);
                month = new SimpleDateFormat("MM", Locale.getDefault()).format(today);
                year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(today);
                initDate(year, month, day);
            }
        });

        final ImageButton day_right = (ImageButton) findViewById(R.id.meal_button_day_forward);
        day_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 1);
                today = new Date(calendar.getTimeInMillis());
                day = new SimpleDateFormat("dd", Locale.getDefault()).format(today);
                month = new SimpleDateFormat("MM", Locale.getDefault()).format(today);
                year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(today);
                initDate(year, month, day);
            }
        });
    }

    private void initDate(String _year, String _month, String _day) {
        String date = String.format(Locale.getDefault(), getResources().getString(R.string.date_format), _year, _month, _day);
        TextView mealDate = (TextView) findViewById(R.id.meal_date);
        System.out.println("Current Date: " + date);
        mealDate.setText(date);
        htmlPageUrl = "http://www.gimpo.hs.kr/main.php?menugrp=021100&master=meal2&act=list&SearchYear="+_year+"&SearchMonth="+_month+"&SearchDay="+_day+"#diary_list";
        jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();
    }

    //private String htmlPageUrl = "http://www.yonhapnews.co.kr/"; //파싱할 홈페이지의 URL주소
    private JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
    private String year, month, day;
    private Date today;
    private Calendar calendar;
    private String htmlPageUrl;
    private TextView lunch_data;
    private TextView dinner_data;

    private String meal[] = new String[2];

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(htmlPageUrl).get();

                Elements mealData = doc.select("div.meal_content.col-md-7 div.meal_table table tbody");
                System.out.println("------------------------------");
                int cnt = 0;
                meal[0] = getResources().getString(R.string.meal_card_data_null);
                meal[1] = getResources().getString(R.string.meal_card_data_null);
                for(Element e: mealData) {
                    System.out.println("data:" + e.text());
                    meal[cnt] = e.text().trim();
                    cnt++;
                }
                System.out.println("------------------------------");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            for(int i = 0; i < 2; i++) {
                StringBuilder sb = new StringBuilder(meal[i]);
                if(meal[i].compareTo(getResources().getString(R.string.meal_card_data_null))!=0) {
                    for(int j = 0; j < sb.length(); j++) {
                        if(sb.charAt(j) == ' ') {
                            if(sb.charAt(j+1) == '1' || sb.charAt(j+1) == '2' || sb.charAt(j+1) == '3' || sb.charAt(j+1) == '4' || sb.charAt(j+1) == '5' || sb.charAt(j+1) == '6' || sb.charAt(j+1) == '7' || sb.charAt(j+1) == '8' || sb.charAt(j+1) == '9' || sb.charAt(j+1) == '0') {
                                sb.deleteCharAt(j);
                            } else {
                                sb.replace(j, j+1, "\n");
                            }
                        }
                    }
                    meal[i] = sb.toString();
                }
            }
            lunch_data.setText(meal[0]);
            dinner_data.setText(meal[1]);
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.meal_info, menu);
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
            Intent intent = new Intent(MealInfoActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_home) {
            finish();
        } else if (id == R.id.nav_table) {
            Toast.makeText(getApplicationContext(),R.string.notYet,Toast.LENGTH_SHORT).show();
            /*intent = new Intent(MealInfoActivity.this, TimeTableActivity.class);
            startActivity(intent);
            finish();*/
        } else if (id == R.id.nav_meal) {

        } else if (id == R.id.nav_calendar) {
            intent = new Intent(MealInfoActivity.this, SchoolEventActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_setting) {
            intent = new Intent(MealInfoActivity.this, SettingsActivity.class);
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
}
