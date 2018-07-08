package kr.hs.gimpo.smartclass;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import kr.hs.gimpo.smartclass.Fragment.*;


public class MainActivity
        extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemSelectedListener,
        QuitDialog.QuitDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String today = new SimpleDateFormat("MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        if(today.compareTo("04-16")==0 ||today.compareTo("04-15")==0||today.compareTo("04-17")==0) {

            setTheme(R.style.remember0416);

            setContentView(R.layout.activity_main);

            TextView textView = (TextView) findViewById(R.id.school_motto);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setText(R.string.home_header_remember20140416);

        } else {

            setTheme(R.style.AppTheme_NoActionBar);

            setContentView(R.layout.activity_main);

            TextView textView = (TextView) findViewById(R.id.school_motto);
            textView.setTypeface(Typeface.DEFAULT);
            textView.setText(R.string.school_motto);
        }

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.main_card_frame) != null) {
            /*if (savedInstanceState != null) {
                return;
            }*/
            
            Fragment firstFragment = new AirQualCard();
            //firstFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_card_frame, firstFragment).commit();
            
            TextView textView = (TextView) findViewById(R.id.main_card_info);
            
            textView.setText(getResources().getString(R.string.air_info));
        }

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

        final Spinner spinner = (Spinner) findViewById(R.id.main_category);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter;

        if(Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(Calendar.getInstance().getTime())) < 14) {
            adapter = ArrayAdapter.createFromResource(this,
                    R.array.home_spinner_day, android.R.layout.simple_spinner_item);
        } else {
            adapter = ArrayAdapter.createFromResource(this,
                    R.array.home_spinner_night, android.R.layout.simple_spinner_item);
        }
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(home_spinner_selected);

        final ImageButton button_left = (ImageButton) findViewById(R.id.main_category_left);
        button_left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                if(home_spinner_selected != 0) {
                    home_spinner_selected--;
                } else {
                    home_spinner_selected = 3;
                }
                spinner.setSelection(home_spinner_selected);
            }
        });

        final ImageButton button_right = (ImageButton) findViewById(R.id.main_category_right);
        button_right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(home_spinner_selected != 3) {
                    home_spinner_selected++;
                } else {
                    home_spinner_selected = 0;
                }
                spinner.setSelection(home_spinner_selected);
            }
        });
        
        /*final FloatingActionButton refresh = (FloatingActionButton) findViewById(R.id.main_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = home_spinner_selected;
                
                final Spinner spinner = (Spinner) findViewById(R.id.main_category);
                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter;
    
                if(Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(Calendar.getInstance().getTime())) < 14) {
                    adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.home_spinner_day, android.R.layout.simple_spinner_item);
                } else {
                    adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.home_spinner_night, android.R.layout.simple_spinner_item);
                }
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        
                        ((TextView) view).setTextColor(getResources().getColor(android.R.color.black));
                        
                        Fragment newFragment;
                        String text;
    
                        switch(position) {
                            case 0:
                                newFragment = new TimeFragment();
                                text = getResources().getString(R.string.time_info);
                                break;
                            case 1:
                                newFragment = new MealCard();
                                text = getResources().getString(R.string.meal_info);
                                break;
                            case 2:
                                newFragment = new EventCard();
                                text = getResources().getString(R.string.event_info);
                                break;
                            case 3:
                                newFragment = new AirQualCard();
                                text = getResources().getString(R.string.air_info);
                                break;
                            default:
                                newFragment = new AirQualCard();
                                text = getResources().getString(R.string.air_info);
                                break;
                        }
    
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    
                        fragmentTransaction.replace(R.id.main_card_frame, newFragment);
                        fragmentTransaction.addToBackStack(null);
    
                        fragmentTransaction.commit();
    
                        TextView textView = (TextView) findViewById(R.id.main_card_info);
                        textView.setText(text);
                    }
    
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
        
                    }
                });
                spinner.setSelection(pos);
            }
        });*/
    }

    private int home_spinner_selected = 0;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            DialogFragment dialogFragment = new QuitDialog();
            dialogFragment.show(getSupportFragmentManager(), "quit");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_table) {
            Toast.makeText(getApplicationContext(),R.string.notYet,Toast.LENGTH_SHORT).show();
            /*intent = new Intent(MainActivity.this, TimeTableActivity.class);
            startActivity(intent);*/
        } else if (id == R.id.nav_meal) {
            intent = new Intent(MainActivity.this, MealInfoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_calendar) {
            intent = new Intent(MainActivity.this, SchoolEventActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
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

    public void onItemSelected(AdapterView<?> parent, View view,
                               final int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    
        Fragment newFragment;
        String text;
    
        switch(pos) {
            case 0:
                newFragment = new TimeFragment();
                text = getResources().getString(R.string.time_info);
                break;
            case 1:
                newFragment = new MealCard();
                text = getResources().getString(R.string.meal_info);
                break;
            case 2:
                newFragment = new EventCard();
                text = getResources().getString(R.string.event_info);
                break;
            case 3:
                newFragment = new AirQualCard();
                text = getResources().getString(R.string.air_info);
                break;
            default:
                newFragment = new AirQualCard();
                text = getResources().getString(R.string.air_info);
                break;
        }
    
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    
        fragmentTransaction.replace(R.id.main_card_frame, newFragment);
        fragmentTransaction.addToBackStack(null);
    
        fragmentTransaction.commit();
    
        TextView textView = (TextView) findViewById(R.id.main_card_info);
        textView.setText(text);
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }
    
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        finish();
    }
    
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        
    }
}
