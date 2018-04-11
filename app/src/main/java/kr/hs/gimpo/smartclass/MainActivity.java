package kr.hs.gimpo.smartclass;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemSelectedListener,onCardChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.home_card_fragment) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            Fragment firstFragment = new TimeFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.home_card_fragment, firstFragment).commit();
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

        timeNow = new Date(System.currentTimeMillis());
        hourFormat = new SimpleDateFormat("HH", Locale.getDefault());
        hourNow = hourFormat.format(timeNow);
        timeNow = Calendar.getInstance().getTime();
        Log.d("init", "hourNow");
        dateFormat[0] = new SimpleDateFormat("yyyy", Locale.getDefault());
        dateFormat[1] = new SimpleDateFormat("MM", Locale.getDefault());
        dateFormat[2] = new SimpleDateFormat("dd", Locale.getDefault());
        for(int i = 0; i < 3; i++) {
            dateNow[i] = dateFormat[i].format(timeNow);
        }

        final Spinner spinner = (Spinner) findViewById(R.id.home_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter;

        if(Integer.parseInt(hourNow) < 14) {
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

        final ImageButton button_left = (ImageButton) findViewById(R.id.home_button_left);
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

        final ImageButton button_right = (ImageButton) findViewById(R.id.home_button_right);
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

        jsoupAsyncTask.execute();
    }

    private JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
    private int home_spinner_selected = 0;
    private Date timeNow;
    private String hourNow;
    private String dateNow[] = new String[3];
    private SimpleDateFormat hourFormat;
    private SimpleDateFormat dateFormat[] = new SimpleDateFormat[3];

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
            intent = new Intent(MainActivity.this, TimeTableActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_meal) {
            intent = new Intent(MainActivity.this, MealInfoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_calendar) {
            intent = new Intent(MainActivity.this, SchoolEventActivity.class);
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

        jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onCardChanged(String data) {
        switch (home_spinner_selected) {
            case 0: {
                TimeFragment newFragment = new TimeFragment();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.home_card_fragment, newFragment);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();

            } break;
            case 1: {

                MealFragment newFragment = new MealFragment();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.home_card_fragment, newFragment);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();

            } break;
            case 2: {

                EventFragment newFragment = new EventFragment();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.home_card_fragment, newFragment);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();

            } break;
            case 3: {

                AirFragment newFragment = new AirFragment();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.home_card_fragment, newFragment);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();

            }
            default: {

                AirFragment newFragment = new AirFragment();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.home_card_fragment, newFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                Bundle bundle = new Bundle();

                bundle.putString("airData", " ");
                bundle.putString("airData_default", getResources().getString(R.string.home_card_air_help));
                newFragment.setArguments(bundle);

                newFragment.setText();

            } break;
        }
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        private String air_data;
        private String meal_data[] = new String[2];
        private String meal_url;

        private String airQualityData[] = new String[8];

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc;
                System.out.println("------------------------------");
                switch (home_spinner_selected) {
                    case 0:
                        break;
                    case 1: {
                        meal_url = "http://www.gimpo.hs.kr/main.php?menugrp=021100&master=meal2&act=list&SearchYear="+dateNow[0]+"&SearchMonth="+dateNow[1]+"&SearchDay="+dateNow[2]+"#diary_list";
                        doc = Jsoup.connect(meal_url).get();
                        meal_data[0] = getResources().getString(R.string.meal_card_data_null);
                        meal_data[1] = getResources().getString(R.string.meal_card_data_null);

                        Elements mealData = doc.select("div.meal_content.col-md-7 div.meal_table table tbody");

                        int cnt = 0;
                        for(Element e: mealData) {
                            System.out.println("data:" + e.text());
                            meal_data[cnt] = e.text().trim();
                            cnt++;
                        }
                        break; }
                    case 2:
                        break;
                    case 3: {
                        doc = Jsoup.connect("http://m.airkorea.or.kr/sub_new/sub41.jsp")
                                .cookie("isGps","N")
                                .cookie("station","131471")
                                .cookie("lat", "37.619355")
                                .cookie("lng","126,716748")
                                .get();
                        air_data = "";
                        Elements airData = doc.select("div#detailContent div");
                        int cnt = 0;
                        for(Element e: airData) {
                            System.out.println("place: " + e.text());
                            airQualityData[cnt] = e.text().trim();
                            cnt++;
                        }
                        break; }
                    default:
                        break;
                }
                System.out.println("------------------------------");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            switch (home_spinner_selected) {
                case 0:
                    onCardChanged("");
                    break;
                case 1:
                    for(int i = 0; i < 2; i++) {
                        StringBuilder sb = new StringBuilder(meal_data[i]);
                        if(meal_data[i].compareTo(getResources().getString(R.string.meal_card_data_null))!=0) {
                            for(int j = 0; j < sb.length(); j++) {
                                if(sb.charAt(j) == ' ') {
                                    if(sb.charAt(j+1) == '1' || sb.charAt(j+1) == '2' || sb.charAt(j+1) == '3' || sb.charAt(j+1) == '4' || sb.charAt(j+1) == '5' || sb.charAt(j+1) == '6' || sb.charAt(j+1) == '7' || sb.charAt(j+1) == '8' || sb.charAt(j+1) == '9' || sb.charAt(j+1) == '0') {
                                        sb.deleteCharAt(j);
                                    } else {
                                        sb.replace(j, j+1, "\n");
                                    }
                                }
                            }
                            meal_data[i] = sb.toString();
                        }
                        meal_data[i] = String.format(Locale.getDefault(), getResources().getString(R.string.date_format), dateNow[0], dateNow[1], dateNow[2]) + "\n" + meal_data[i];
                    }
                    if(Integer.parseInt(hourNow) < 14) {
                        onCardChanged(meal_data[0]);
                    } else {
                        onCardChanged(meal_data[1]);
                    }
                    break;
                case 2:
                    onCardChanged("");
                    break;
                case 3:
                    air_data = String.format(Locale.getDefault(),
                            getResources().getString(R.string.home_card_air_quality_format),
                            airQualityData[0],
                            airQualityData[1],
                            airQualityData[2],
                            airQualityData[3],
                            airQualityData[4],
                            airQualityData[5],
                            airQualityData[6],
                            airQualityData[7]);
                    onCardChanged(air_data);
                    break;
                default:
                    break;
            }
        }
    }

}
