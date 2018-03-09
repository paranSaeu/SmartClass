package kr.hs.gimpo.smartclass;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.os.AsyncTask;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MealInfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        Date c = Calendar.getInstance().getTime();
        day = new SimpleDateFormat("dd", Locale.getDefault()).format(c);
        month = new SimpleDateFormat("MM", Locale.getDefault()).format(c);
        year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(c);

        initDate(year, month, day);

        lunch_data = (TextView)findViewById(R.id.meal_card_lunch_data);
        lunch_data.setMovementMethod(new ScrollingMovementMethod()); //스크롤 가능한 텍스트뷰로 만들기

        dinner_data = (TextView) findViewById(R.id.meal_card_dinner_data);
        lunch_data.setMovementMethod(new ScrollingMovementMethod());

        final JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();

        /*Button htmlTitleButton = (Button)findViewById(R.id.button);
        htmlTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println( (cnt+1) +"번째 파싱");
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();
                cnt++;
            }
        });*/

        final ImageButton month_left = (ImageButton) findViewById(R.id.meal_button_month_back);
        month_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDate(year, month, day);
                jsoupAsyncTask.execute();
            }
        });
    }

    private void initDate(String _year, String _month, String _day) {
        String date = String.format(Locale.getDefault(), getResources().getString(R.string.date_format), _year, _month, _day);
        TextView mealDate = (TextView) findViewById(R.id.meal_date);
        mealDate.setText(date);
        htmlPageUrl = "http://www.gimpo.hs.kr/main.php?menugrp=021100&master=meal2&act=list&SearchYear="+_year+"&SearchMonth="+_month+"&SearchDay="+_day+"#diary_list";
    }

    //private String htmlPageUrl = "http://www.yonhapnews.co.kr/"; //파싱할 홈페이지의 URL주소
    private String year, month, day;
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
                /*
                //테스트1
                Elements titles= doc.select("div.news-con h1.tit-news");

                System.out.println("-------------------------------------------------------------");
                for(Element e: titles){
                    System.out.println("title: " + e.text());
                    htmlContentInStringFormat += e.text().trim() + "\n";
                }

                //테스트2
                titles= doc.select("div.news-con h2.tit-news");

                System.out.println("-------------------------------------------------------------");
                for(Element e: titles){
                    System.out.println("title: " + e.text());
                    htmlContentInStringFormat += e.text().trim() + "\n";
                }

                //테스트3
                titles= doc.select("li.section02 div.con h2.news-tl");

                System.out.println("-------------------------------------------------------------");
                for(Element e: titles){
                    System.out.println("title: " + e.text());
                    htmlContentInStringFormat += e.text().trim() + "\n";
                }
                System.out.println("-------------------------------------------------------------");
                */

                //급식
                Elements mealData = doc.select("div.meal_content.col-md-7 div.meal_table table tbody");
                System.out.println("------------------");
                int cnt = 0;
                for(Element e: mealData) {
                    System.out.println("data:" + e.text());
                    meal[cnt] = e.text().trim();
                    cnt++;
                }
                System.out.println("-------------------");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
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

        } else if (id == R.id.nav_meal) {

        } else if (id == R.id.nav_calendar) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_info) {
            try {
                CharSequence version = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
                CharSequence versionName = getResources().getString(R.string.noti_version_is) + " " + version.toString();
                Toast.makeText(getApplicationContext(),versionName,Toast.LENGTH_SHORT).show();
            } catch (PackageManager.NameNotFoundException e) { }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
