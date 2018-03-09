package kr.hs.gimpo.smartclass;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import org.w3c.dom.Text;

import java.util.Locale;

public class TimeTableActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        final TextView time_class  = (TextView) findViewById(R.id.time_class);
        time_class.setText(refreshClassNo());

        final ImageButton button_grade_left = (ImageButton) findViewById(R.id.time_button_grade_back);
        button_grade_left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(Grade != 1) {
                    Grade--;
                } else {
                    Grade = 3;
                }
                time_class.setText(refreshClassNo());
            }
        });

        final ImageButton button_grade_right = (ImageButton) findViewById(R.id.time_button_grade_forward);
        button_grade_right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(Grade != 3) {
                    Grade++;
                } else {
                    Grade = 1;
                }
                time_class.setText(refreshClassNo());
            }
        });

        final ImageButton button_class_left = (ImageButton) findViewById(R.id.time_button_class_back);
        button_class_left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(Class != 1) {
                    Class--;
                } else {
                    Class = 11;
                }
                time_class.setText(refreshClassNo());
            }
        });

        final ImageButton button_class_right = (ImageButton) findViewById(R.id.time_button_class_forward);
        button_class_right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(Class != 11) {
                    Class++;
                } else {
                    Class = 1;
                }
                time_class.setText(refreshClassNo());
            }
        });

        for(int a = 0; a < 5; a++) {
            for(int b = 0; b < 7; b++) {
                String id = String.format(Locale.getDefault(), ID_FORMAT, date[a], b);
                String dateNow = date[a] + "\n" + (b + 1);
                timetable[a][b] = (TextView) findViewById(getResources().getIdentifier(id, "id", getPackageName()));
                timetable[a][b].setText(dateNow);
            }
        }
    }
    private String[] date = {"mon", "tue", "wed", "thu", "fri"};
    private static final String ID_FORMAT = "time_card_%s_%d";
    private TextView timetable[][] = new TextView[5][7];

    private CharSequence refreshClassNo() {
        try {
            return String.valueOf(Grade) + getResources().getString(R.string.Grade) + " " + String.valueOf(Class) + getResources().getString(R.string.Class);
        } catch (java.lang.NullPointerException e) {
            return "Error";
        }
    }


    private int Grade = 1, Class = 1;

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
