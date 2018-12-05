package kr.hs.gimpo.smartclass;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import kr.hs.gimpo.smartclass.Fragment.MealCard;

public class MealInfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
        
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-", Locale.getDefault());
    
        final Calendar calendar = Calendar.getInstance();
        
        date = dateFormat.format(calendar.getTime());
    
        TextView mealDate = (TextView) findViewById(R.id.meal_date);
        String[] ymd = date.split("-");
        String temp = String.format(getResources().getString(R.string.date_format), ymd[0], ymd[1], ymd[2]);
        mealDate.setText(temp);
        
        final Fragment lunchCard = new MealCard();
        final Fragment dinnerCard = new MealCard();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        
        Bundle lunchCardDate = new Bundle();
        lunchCardDate.putString("date", date + "06");
        lunchCard.setArguments(lunchCardDate);
        
        Bundle dinnerCardDate = new Bundle();
        dinnerCardDate.putString("date", date + "18");
        dinnerCard.setArguments(dinnerCardDate);
        
        fragmentTransaction.add(R.id.meal_card_lunch_fragment, lunchCard);
        fragmentTransaction.add(R.id.meal_card_dinner_fragment, dinnerCard);
        
        fragmentTransaction.commit();
        
        ImageButton week_left = (ImageButton) findViewById(R.id.meal_button_month_back);
        week_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -7);
                
                String date = dateFormat.format(calendar.getTime());
    
                TextView mealDate = (TextView) findViewById(R.id.meal_date);
                String[] ymd = date.split("-");
                String temp = String.format(getResources().getString(R.string.date_format), ymd[0], ymd[1], ymd[2]);
                mealDate.setText(temp);
    
                Bundle lunchCardDate = new Bundle();
                lunchCardDate.putString("date", date + "06");
                lunchCard.setArguments(lunchCardDate);
    
                Bundle dinnerCardDate = new Bundle();
                dinnerCardDate.putString("date", date + "18");
                dinnerCard.setArguments(dinnerCardDate);
            }
        });
    
        ImageButton day_left = (ImageButton) findViewById(R.id.meal_button_day_back);
        day_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -1);
            
                String date = dateFormat.format(calendar.getTime());
    
                TextView mealDate = (TextView) findViewById(R.id.meal_date);
                String[] ymd = date.split("-");
                String temp = String.format(getResources().getString(R.string.date_format), ymd[0], ymd[1], ymd[2]);
                mealDate.setText(temp);
            
                Bundle lunchCardDate = new Bundle();
                lunchCardDate.putString("date", date + "06");
                lunchCard.setArguments(lunchCardDate);
            
                Bundle dinnerCardDate = new Bundle();
                dinnerCardDate.putString("date", date + "18");
                dinnerCard.setArguments(dinnerCardDate);
            }
        });
    
        ImageButton day_right = (ImageButton) findViewById(R.id.meal_button_day_forward);
        day_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 1);
            
                String date = dateFormat.format(calendar.getTime());
    
                TextView mealDate = (TextView) findViewById(R.id.meal_date);
                String[] ymd = date.split("-");
                String temp = String.format(getResources().getString(R.string.date_format), ymd[0], ymd[1], ymd[2]);
                mealDate.setText(temp);
            
                Bundle lunchCardDate = new Bundle();
                lunchCardDate.putString("date", date + "06");
                lunchCard.setArguments(lunchCardDate);
            
                Bundle dinnerCardDate = new Bundle();
                dinnerCardDate.putString("date", date + "18");
                dinnerCard.setArguments(dinnerCardDate);
            }
        });
    
        ImageButton week_right = (ImageButton) findViewById(R.id.meal_button_month_forward);
        week_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 7);
            
                String date = dateFormat.format(calendar.getTime());
    
                TextView mealDate = (TextView) findViewById(R.id.meal_date);
                String[] ymd = date.split("-");
                String temp = String.format(getResources().getString(R.string.date_format), ymd[0], ymd[1], ymd[2]);
                mealDate.setText(temp);
            
                Bundle lunchCardDate = new Bundle();
                lunchCardDate.putString("date", date + "06");
                lunchCard.setArguments(lunchCardDate);
            
                Bundle dinnerCardDate = new Bundle();
                dinnerCardDate.putString("date", date + "18");
                dinnerCard.setArguments(dinnerCardDate);
            }
        });
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
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_main) {
            finish();
        } else if (id == R.id.nav_table) {
            //Toast.makeText(getApplicationContext(),R.string.notYet,Toast.LENGTH_SHORT).show();
            intent = new Intent(MealInfoActivity.this, TimeTableActivity.class);
            startActivity(intent);
            finish();
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
