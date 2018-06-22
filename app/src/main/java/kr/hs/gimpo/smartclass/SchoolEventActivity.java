package kr.hs.gimpo.smartclass;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import kr.hs.gimpo.smartclass.Fragment.EventCard;

public class SchoolEventActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 테마를 설정합니다.
        String date = new SimpleDateFormat("MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        if(date.compareTo("04-16")==0) {
            setTheme(R.style.remember0416);
        } else {
            setTheme(R.style.AppTheme_NoActionBar);
        }
        
        // 레이아웃을 불러옵니다.
        setContentView(R.layout.activity_academic_calendar);
        
        // 툴바를 초기화합니다.
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.event_title);
        setSupportActionBar(toolbar);
        
        // 앱 서랍을 초기화합니다.
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    
        
        final Calendar calendar = Calendar.getInstance();
        
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
    
        TextView eventDate = (TextView) findViewById(R.id.event_date);
        String[] ymd = date.split("-");
        String temp = String.format(getResources().getString(R.string.date_format), ymd[0], ymd[1], ymd[2]);
        eventDate.setText(temp);
    
        final Fragment fragment = new EventCard();
        
        // Fragment를 불러옵니다.
        if(findViewById(R.id.event_card_public_fragment) != null) {
            Bundle bundle = new Bundle();
            bundle.putString("date", date);
            fragment.setArguments(bundle);
            
            getSupportFragmentManager().beginTransaction().add(R.id.event_card_public_fragment, fragment).commit();
        }
    
        ImageButton week_left = (ImageButton) findViewById(R.id.event_button_month_back);
        week_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -7);
                
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
    
                TextView eventDate = (TextView) findViewById(R.id.event_date);
                String[] ymd = date.split("-");
                String temp = String.format(getResources().getString(R.string.date_format), ymd[0], ymd[1], ymd[2]);
                eventDate.setText(temp);
                
                Bundle bundle = new Bundle();
                bundle.putString("date", date);
                fragment.setArguments(bundle);
            }
        });
    
        ImageButton day_left = (ImageButton) findViewById(R.id.event_button_day_back);
        day_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -1);
            
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
            
                TextView eventDate = (TextView) findViewById(R.id.event_date);
                String[] ymd = date.split("-");
                String temp = String.format(getResources().getString(R.string.date_format), ymd[0], ymd[1], ymd[2]);
                eventDate.setText(temp);
            
                Bundle bundle = new Bundle();
                bundle.putString("date", date);
                fragment.setArguments(bundle);
            }
        });
    
        ImageButton day_right = (ImageButton) findViewById(R.id.event_button_day_forward);
        day_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 1);
            
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
            
                TextView eventDate = (TextView) findViewById(R.id.event_date);
                String[] ymd = date.split("-");
                String temp = String.format(getResources().getString(R.string.date_format), ymd[0], ymd[1], ymd[2]);
                eventDate.setText(temp);
            
                Bundle bundle = new Bundle();
                bundle.putString("date", date);
                fragment.setArguments(bundle);
            }
        });
    
        ImageButton week_right = (ImageButton) findViewById(R.id.event_button_month_forward);
        week_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 7);
            
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
            
                TextView eventDate = (TextView) findViewById(R.id.event_date);
                String[] ymd = date.split("-");
                String temp = String.format(getResources().getString(R.string.date_format), ymd[0], ymd[1], ymd[2]);
                eventDate.setText(temp);
            
                Bundle bundle = new Bundle();
                bundle.putString("date", date);
                fragment.setArguments(bundle);
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.academic_calendar, menu);
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
    
            Intent intent = new Intent(SchoolEventActivity.this, SettingsActivity.class);
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
            /*intent = new Intent(SchoolEventActivity.this, TimeTableActivity.class);
            startActivity(intent);
            finish();*/
        } else if (id == R.id.nav_meal) {
            intent = new Intent(SchoolEventActivity.this, MealInfoActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_calendar) {

        } else if (id == R.id.nav_setting) {
            intent = new Intent(SchoolEventActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_info) {
            try {
                CharSequence versionName = getResources().getString(R.string.noti_version_is) + " " + getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
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
