package kr.hs.gimpo.smartclass;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        toolbar.setTitle(R.string.home_title);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final ConstraintLayout home_header = (ConstraintLayout) findViewById(R.id.home_header_layout);
        //home_header.setMinHeight(320);

        final Spinner spinner = (Spinner) findViewById(R.id.home_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.home_spinner, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(home_spinner_selected);

        final ImageButton button_left = (ImageButton) findViewById(R.id.home_button_left);
        button_left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                if(home_spinner_selected != 0) {
                    home_spinner_selected--;
                } else {
                    home_spinner_selected = 2;
                }
                spinner.setSelection(home_spinner_selected);
            }
        });

        final ImageButton button_right = (ImageButton) findViewById(R.id.home_button_right);
        button_right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(home_spinner_selected != 2) {
                    home_spinner_selected++;
                } else {
                    home_spinner_selected = 0;
                }
                spinner.setSelection(home_spinner_selected);
            }
        });
    }

    public int home_spinner_selected = 0;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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
            Toast.makeText(getApplicationContext(),R.string.notYet,Toast.LENGTH_SHORT).show();
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

        } else if (id == R.id.nav_table) {
            intent = new Intent(HomeActivity.this, TimeTableActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_meal) {
            intent = new Intent(HomeActivity.this, MealInfoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_calendar) {
            intent = new Intent(HomeActivity.this, AcademicCalendarActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Toast.makeText(getApplicationContext(),R.string.notYet,Toast.LENGTH_SHORT).show();
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

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        ConstraintLayout cl_tt = (ConstraintLayout) findViewById(R.id.home_card_timetable);
        ConstraintLayout cl_mi = (ConstraintLayout) findViewById(R.id.home_card_meal_info);
        ConstraintLayout cl_ac = (ConstraintLayout) findViewById(R.id.home_card_event);

        switch(pos) {
            case 0:
                cl_tt.setVisibility(View.VISIBLE);
                cl_mi.setVisibility(View.INVISIBLE);
                cl_ac.setVisibility(View.INVISIBLE);
                break;
            case 1:
                cl_tt.setVisibility(View.INVISIBLE);
                cl_mi.setVisibility(View.VISIBLE);
                cl_ac.setVisibility(View.INVISIBLE);
                break;
            case 2:
                cl_tt.setVisibility(View.INVISIBLE);
                cl_mi.setVisibility(View.INVISIBLE);
                cl_ac.setVisibility(View.VISIBLE);
                break;
            default:
                cl_tt.setVisibility(View.INVISIBLE);
                cl_mi.setVisibility(View.INVISIBLE);
                cl_ac.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
        ConstraintLayout cl_tt = (ConstraintLayout) findViewById(R.id.home_card_timetable);
        ConstraintLayout cl_mi = (ConstraintLayout) findViewById(R.id.home_card_meal_info);
        ConstraintLayout cl_ac = (ConstraintLayout) findViewById(R.id.home_card_event);
        cl_tt.setVisibility(View.INVISIBLE);
        cl_mi.setVisibility(View.INVISIBLE);
        cl_ac.setVisibility(View.INVISIBLE);
    }

}
