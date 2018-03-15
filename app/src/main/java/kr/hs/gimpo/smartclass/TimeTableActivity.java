package kr.hs.gimpo.smartclass;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
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

        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.setUrl("http://comcigan.com:4082/_hourdat?sc=26203");
        System.out.println("trying to connect to " + jsoupAsyncTask.getUrl());
        jsoupAsyncTask.execute();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabaseReference.addValueEventListener(valueEventListener);
    }
    private String[] date = {"mon", "tue", "wed", "thu", "fri"};
    private static final String ID_FORMAT = "time_card_%s_%d";
    private String[][][][] timetable_data = new String[3][11][5][7];
    private int Grade = 0, Class = 0;
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference();


    private CharSequence refreshClassNo() {
        initTable(timetable_data, Grade, Class);
        return String.valueOf(Grade + 1) + getResources().getString(R.string.Grade) + " " + String.valueOf(Class + 1) + getResources().getString(R.string.Class);
        /*try {

        } catch (java.lang.NullPointerException e) {
            return "Error";
        }*/
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
            intent = new Intent(TimeTableActivity.this, MealInfoActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_calendar) {
            intent = new Intent(TimeTableActivity.this, AcademicCalendarActivity.class);
            startActivity(intent);
            finish();
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

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        private String htmlPageUrl = "";
        private String htmlContentInStringFormat = "";

        public void setUrl(String url) {
            this.htmlPageUrl = url;
        }

        public String getUrl() {
            return this.htmlPageUrl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(htmlPageUrl).get();

                //테스트1
                Element titles= doc.body();

                System.out.println("------------------------------");
                System.out.println("data: " + titles.text());
                System.out.println("------------------------------");
                htmlContentInStringFormat = titles.text().trim();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            initData(htmlContentInStringFormat);
        }
    }

    void initData(String json) {
        JsonParser parser = new JsonParser();
        JsonElement data = parser.parse(json);
        JsonArray time_def, time_now, teacher_name, subject_name;

        time_def = data.getAsJsonObject().get("시간표").getAsJsonArray();
        time_now = data.getAsJsonObject().get("학급시간표").getAsJsonArray();
        teacher_name = data.getAsJsonObject().get("성명").getAsJsonArray();
        subject_name = data.getAsJsonObject().get("과목명").getAsJsonArray();
        int temp, sub, tea; String subj, teac;
        for(int a = 0; a < 3; a++) {
            for(int b = 0; b < 11; b++) {
                for(int c = 0; c < 5; c++) {
                    for(int d = 0; d < 7; d++) {
                        temp = time_def.get(a + 1)
                                .getAsJsonArray().get(b + 1)
                                .getAsJsonArray().get(c + 1)
                                .getAsJsonArray().get(d + 1)
                                .getAsInt();
                        sub = getSubject(temp);
                        subj = subject_name.get(sub).getAsString();
                        tea = getTeacher(temp);
                        teac = teacher_name.get(tea).getAsString();
                        timetable_data[a][b][c][d] = subj + "\n" + teac;
                    }
                }
            }
        }

        initTable(timetable_data, Grade, Class);
        /*
        JsonArray grade3 = tdata.getAsJsonObject().get("3").getAsJsonArray();
        JsonArray class11 = grade3.getAsJsonObject().get("11").getAsJsonArray();
        JsonArray day1 = class11.getAsJsonObject().get("1").getAsJsonArray();
        int subject = day1.getAsJsonObject().get("1").getAsInt();
        System.out.println("data: "+ subject);*/
    }

    int getSubject(int subjectNo) {
        for(;;) {
            if(subjectNo >= 100) {
                subjectNo -= 100;
            } else {
                break;
            }
        }
        return subjectNo;
    }

    int getTeacher(int subjectNo) {
        return (subjectNo - getSubject(subjectNo)) / 100;
    }

    void initTable(String[][][][] data, int classGrade, int classNo) {
        TextView timetable;
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 7; j++) {
                String id = String.format(Locale.getDefault(), ID_FORMAT, date[i], j);
                timetable = (TextView) findViewById(getResources().getIdentifier(id, "id", getPackageName()));
                timetable.setText(data[classGrade][classNo][i][j]);


            }
        }
    }
}
