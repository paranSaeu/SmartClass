package kr.hs.gimpo.smartclass;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemSelectedListener,onCardChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String today = new SimpleDateFormat("MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        if(today.compareTo("04-16")==0 ||today.compareTo("04-15")==0||today.compareTo("04-17")==0) {

            setTheme(R.style.remember0416);

            setContentView(R.layout.activity_main);

            TextView textView = (TextView) findViewById(R.id.home_header_school_motto);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setText(R.string.home_header_remember20140416);

        } else {

            setTheme(R.style.AppTheme_NoActionBar);

            setContentView(R.layout.activity_main);

            TextView textView = (TextView) findViewById(R.id.home_header_school_motto);
            textView.setTypeface(Typeface.DEFAULT);
            textView.setText(R.string.school_motto);
        }

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

        final Spinner spinner = (Spinner) findViewById(R.id.home_spinner);
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

        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
        }

        mDatabase.child("mealDataFormat").child("thisMonth").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot);
                thisMonth = dataSnapshot.getValue(Integer.class);
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mDatabase.child("airQualDataFormat").child("thisTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot);
                thisTime = dataSnapshot.getValue(String.class);
                System.out.println(thisTime);
                if(isConnected) {
                    InitAirQualData initAirQualData = new InitAirQualData(mDatabase, thisTime);
                    initAirQualData.execute();
                    try {
                        initAirQualData.get();
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    } catch(ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    Integer thisMonth;
    String thisTime;
    boolean isConnected;

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("test");
    private int home_spinner_selected = 0;

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

        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
        }
        

        switch(pos) {
            case 0: {
                Bundle bundle = new Bundle();
                onCardChanged(pos, bundle);
                }
                break;
            case 1:
                mDatabase.child("mealDataFormat").child("thisMonth").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println(dataSnapshot);
                        thisMonth = dataSnapshot.getValue(Integer.class);
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
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                mDatabase.child("mealDataFormat").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(Calendar.getInstance().getTime());
                        Bundle bundle = new Bundle();
                        int thisDay = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(Calendar.getInstance().getTime()));
                        int thisMeal = Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(Calendar.getInstance().getTime())) < 14? 0 : 1;
                        bundle.putInt("mealTime", thisMeal);
                        bundle.putString(
                                "mealDate",
                                new SimpleDateFormat("yyyy'년 'MM'월 'dd'일 '", Locale.getDefault()).format(Calendar.getInstance().getTime()));
                        if(!(calendar.get(Calendar.DAY_OF_WEEK) == 1 || calendar.get(Calendar.DAY_OF_WEEK) == 7)) {
                            System.out.println(dataSnapshot);
                            DataFormat.mealDataFormat = dataSnapshot.getValue(Meal.class);
                            bundle.putString(
                                    "mealData",
                                    DataFormat.mealDataFormat.mealData.get(thisDay - 1).get(thisMeal));
                        } else {
                            bundle.putString(
                                    "mealData",
                                    getResources().getString(R.string.meal_card_data_null));
                        }
                        onCardChanged(pos, bundle);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
            case 2: {
                  Bundle bundle = new Bundle();
                  onCardChanged(pos, bundle);
                }
                break;
            case 3:
                mDatabase.child("airQualDataFormat").child("thisTime").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println(dataSnapshot);
                        thisTime = dataSnapshot.getValue(String.class);
                        System.out.println(thisTime);
                        if(isConnected) {
                            InitAirQualData initAirQualData = new InitAirQualData(mDatabase, thisTime);
                            initAirQualData.execute();
                            try {
                                initAirQualData.get();
                            } catch(InterruptedException e) {
                                e.printStackTrace();
                            } catch(ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                mDatabase.child("airQualDataFormat").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataFormat.airQualDataFormat = dataSnapshot.getValue(AirQual.class);
                        String[] param = new String[8];
                        DataFormat.airQualDataFormat.airQualData.toArray(param);
                        String temp = String.format(
                                Locale.getDefault(),
                                getResources().getString(R.string.home_card_air_quality_format),
                                param[0],
                                param[1],
                                param[2],
                                param[3],
                                param[4],
                                param[5],
                                param[6],
                                param[7]
                        );
                        Bundle bundle = new Bundle();
                        bundle.putString("airData", temp);
                        bundle.putStringArray("airDataList", param);
                        onCardChanged(pos, bundle);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
            default: {
                    Bundle bundle = new Bundle();
                    onCardChanged(pos, bundle);
                }
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onCardChanged(int pos, Bundle data) {
        switch (pos) {
            case 0: {
                TimeFragment newFragment = new TimeFragment();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.home_card_fragment, newFragment);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();

            } break;
            case 1: {

                MealFragment newFragment = new MealFragment();
                data.putString("mealData_default", getResources().getString(R.string.home_card_meal_title_help));
                data.putInt("displayMode", 0);
                newFragment.setArguments(data);

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

                data.putString("airData_default", getResources().getString(R.string.home_card_air_help));
                newFragment.setArguments(data);

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.home_card_fragment, newFragment);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();

            }
            default: {

                AirFragment newFragment = new AirFragment();

                data.putString("airData_default", getResources().getString(R.string.home_card_air_help));
                newFragment.setArguments(data);

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.home_card_fragment, newFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            } break;
        }
    }
}
