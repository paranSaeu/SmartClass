package kr.hs.gimpo.smartclass;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.hyunjun.school.SchoolSchedule;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
        final Calendar calendar1 = Calendar.getInstance();
        
        // 주어진 시작 날짜를 일요일로 바꿉니다. 일요일이라면 무시합니다.
        while ( calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY ) {
            calendar.add(Calendar.DATE, -1);
        }
        // 주어진 종료 날짜를 토요일로 바꿉니다. 토요일이라면 무시합니다.
        while ( calendar1.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY ) {
            calendar1.add(Calendar.DATE, 1);
        }
        
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
        String endDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar1.getTime());
        String endYMD[] = endDate.split("-");
    
        final TextView eventDate = (TextView) findViewById(R.id.event_date);
        String[] ymd = date.split("-");
        //String temp = String.format(getResources().getString(R.string.date_format), ymd[0], ymd[1], ymd[2]);
        String temp;
        if(ymd[1].compareTo(endYMD[1]) != 0) {
            if(ymd[0].compareTo(endYMD[0]) != 0) {
                temp =
                        String.format(getResources().getString(R.string.date_format), ymd[0], ymd[1], ymd[2])
                                + " - "
                                + String.format(getResources().getString(R.string.date_format), endYMD[0], endYMD[1], endYMD[2]);
            } else {
                temp =
                        String.format(getResources().getString(R.string.date_format), ymd[0], ymd[1], ymd[2])
                                + " - "
                                + String.format(getResources().getString(R.string.date_format_short), endYMD[1], endYMD[2]);
            }
        } else {
            temp =
                    String.format(getResources().getString(R.string.date_format), ymd[0], ymd[1], ymd[2])
                            + " - "
                            + String.format(getResources().getString(R.string.date_format_vshort), endYMD[2]);
        }
        eventDate.setText(temp);
    
        File file = new File(getFilesDir(), "privateCalendar.db");
    
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(file, null);
        
        sqLiteDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS " +
                "PRIVATE_CALENDAR" + " ( " +
                "DATE TEXT" + " , " + "" +
                "SCHEDULE TEXT" + " )"
        );
        
        ImageButton weeks_left = (ImageButton) findViewById(R.id.event_button_weeks_back);
        weeks_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -14);
                calendar1.add(Calendar.DATE, -14);
                initEventList(calendar);
    
                setDate(calendar, calendar1);
            }
        });
        
        ImageButton week_left = (ImageButton) findViewById(R.id.event_button_week_back);
        week_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -7);
                calendar1.add(Calendar.DATE, -7);
                initEventList(calendar);
    
                setDate(calendar, calendar1);
            }
        });
        
        ImageButton week_right = (ImageButton) findViewById(R.id.event_button_week_forward);
        week_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 7);
                calendar1.add(Calendar.DATE, 7);
                initEventList(calendar);
    
                setDate(calendar, calendar1);
            }
        });
        
        ImageButton weeks_right = (ImageButton) findViewById(R.id.event_button_weeks_forward);
        weeks_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 14);
                calendar1.add(Calendar.DATE, 14);
                initEventList(calendar);
    
                setDate(calendar, calendar1);
            }
        });
    
        mDatabase = FirebaseDatabase.getInstance().getReference("test");
        
        mDatabase.child("eventDataFormat").child("publicEventData").addChildEventListener(new ChildEventListener() {
            
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            boolean isInit = false;
            
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(!isInit) {
                    cal.set(calendar.get(Calendar.YEAR), 0, 1);
                    isInit = true;
                }
                
                String dateTag = sdf.format(cal.getTime());
                String data = dataSnapshot.getValue(String.class);
                
                publicData.put(dateTag, data);
            }
    
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(s != null) {
                    try {
                        cal.setTime(sdf.parse(s));
                        cal.add(Calendar.DATE, 1);
                    } catch(ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    cal.set(calendar.get(Calendar.YEAR), 0, 1);
                }
    
                String dateTag = sdf.format(cal.getTime());
                String data = dataSnapshot.getValue(String.class);
    
                publicData.put(dateTag, data);
        
            }
    
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        
            }
    
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        
            }
    
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
        
            }
        });
        
        initEventList(calendar);
        
    }
    
    DatabaseReference mDatabase;
    
    public void setDate(Calendar startDate, Calendar endDate) {
        // 주어진 시작 날짜를 일요일로 바꿉니다. 일요일이라면 무시합니다.
        while ( startDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY ) {
            startDate.add(Calendar.DATE, -1);
        }
        // 주어진 종료 날짜를 토요일로 바꿉니다. 토요일이라면 무시합니다.
        while ( endDate.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY ) {
            endDate.add(Calendar.DATE, 1);
        }
        
        TextView eventDate = (TextView) findViewById(R.id.event_date);
    
        String start = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(startDate.getTime());
        String[] startYMD = start.split("-");
    
        String end = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(endDate.getTime());
        String endYMD[] = end.split("-");
    
        String temp;
        if(startYMD[1].compareTo(endYMD[1]) != 0) {
            if(startYMD[0].compareTo(endYMD[0]) != 0) {
                temp =
                        String.format(getResources().getString(R.string.date_format), startYMD[0], startYMD[1], startYMD[2])
                                + " - "
                                + String.format(getResources().getString(R.string.date_format), endYMD[0], endYMD[1], endYMD[2]);
            } else {
                temp =
                        String.format(getResources().getString(R.string.date_format), startYMD[0], startYMD[1], startYMD[2])
                                + " - "
                                + String.format(getResources().getString(R.string.date_format_short), endYMD[1], endYMD[2]);
            }
        } else {
            temp =
                    String.format(getResources().getString(R.string.date_format), startYMD[0], startYMD[1], startYMD[2])
                            + " - "
                            + String.format(getResources().getString(R.string.date_format_vshort), endYMD[2]);
        }
        eventDate.setText(temp);
    }
    
    SQLiteDatabase sqLiteDatabase;
    RecyclerView eventList;
    RecyclerAdapter adapter;
    Map<String, String> privateData = new HashMap<>();
    Map<String, String> publicData = new HashMap<>();
    boolean isEventListInit = false;
    
    public void initEventList(final Calendar calendar) {
        // 주어진 시작 날짜를 일요일로 바꿉니다. 일요일이라면 무시합니다.
        while ( calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY ) {
            calendar.add(Calendar.DATE, -1);
        }
    
        for(int i = 0; i < 7; i++) {
            String temp = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
            
            Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM " + "PRIVATE_CALENDAR" + " WHERE DATE = " + "\"" + new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime()) + "\"", null);
            if(c.getCount() > 0) {
                c.moveToFirst();
                privateData.put(temp, c.getString(1));
            } else {
                privateData.put(temp, "");
            }
            c.close();
            calendar.add(Calendar.DATE, 1);
        }
    
        calendar.add(Calendar.DATE, -7);
    
        eventList = (RecyclerView) findViewById(R.id.event_list);
    
        eventList.setHasFixedSize(true);
    
        adapter = new RecyclerAdapter(privateData, calendar);
    
        eventList.setAdapter(adapter);
    
        eventList.setLayoutManager(new LinearLayoutManager(this));
        
        if(!isEventListInit) {
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), new LinearLayoutManager(this).getOrientation());
            eventList.addItemDecoration(dividerItemDecoration);
            isEventListInit = true;
        }
        
        eventList.addOnItemTouchListener(new EventItemClickListener(getApplicationContext(), eventList, new EventItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                calendar.add(Calendar.DATE, pos);
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
                calendar.add(Calendar.DATE, -pos);
                Intent intent = new Intent(getApplicationContext(), EventDetailActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        }));
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

    /*@Override
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
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_main) {
            finish();
        } else if (id == R.id.nav_table) {
            //Toast.makeText(getApplicationContext(),R.string.notYet,Toast.LENGTH_SHORT).show();
            intent = new Intent(SchoolEventActivity.this, TimeTableActivity.class);
            startActivity(intent);
            finish();
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
    
    class EventViewHolder
            extends RecyclerView.ViewHolder {
        
        public TextView publicEvent, pu;
        public TextView classEvent, cl;
        public TextView privateEvent, pr;
        public TextView eventDate;
        
        public EventViewHolder(View v) {
            super(v);
            eventDate = (TextView) v.findViewById(R.id.event_item_date);
            
            publicEvent = (TextView) v.findViewById(R.id.event_item_public_text);
            //classEvent = (TextView) v.findViewById(R.id.event_item_class_text);
            privateEvent = (TextView) v.findViewById(R.id.event_item_private_text);
            
            pu = (TextView) v.findViewById(R.id.event_item_public);
            //cl = (TextView) v.findViewById(R.id.event_item_class);
            pr = (TextView) v.findViewById(R.id.event_item_private);
        }
        
        
    }
    
    class RecyclerAdapter extends RecyclerView.Adapter<EventViewHolder> {
        
        Map<String, String> privateData;
        Calendar calendar;
        
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("test");
        
        SimpleDateFormat dd = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        
        public RecyclerAdapter(Map<String, String> privateData, Calendar calendar) {
            this.privateData = privateData;
            this.calendar = calendar;
        }
        
        @NonNull
        @Override
        public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new EventViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_card, parent, false));
        }
        
        @Override
        public int getItemCount() {
            return 7;
        }
        
        @Override
        public void onBindViewHolder(@NonNull final EventViewHolder holder, int position) {
            String d = dd.format(calendar.getTime());
            if(position == 0 || d.compareTo("01") == 0) {
                String tmp = new SimpleDateFormat("MM/dd", Locale.getDefault()).format(calendar.getTime());
                holder.eventDate.setText(tmp);
            } else {
                String tmp = new SimpleDateFormat("dd", Locale.getDefault()).format(calendar.getTime());
                holder.eventDate.setText(tmp);
            }
            
            if(position == 0) {
                holder.eventDate.setTextColor(getResources().getColor(R.color.sunday));
            } else if (position == 6) {
                holder.eventDate.setTextColor(getResources().getColor(R.color.saturday));
            }
            
            if(privateData.get(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime())).compareTo("")!=0) {
                holder.privateEvent.setText(privateData.get(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime())));
            } else {
                holder.privateEvent.setVisibility(TextView.GONE);
                holder.pr.setVisibility(TextView.GONE);
            }
            String temp = sdf.format(calendar.getTime());
            String ymd[] = temp.split("-");
            mDatabase.child("eventDataFormat").child("eventData").child(ymd[0]).child(String.valueOf(Integer.parseInt(ymd[1]) - 1)).child(String.valueOf(Integer.parseInt(ymd[2]) - 1)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    SchoolSchedule temp = dataSnapshot.getValue(SchoolSchedule.class);
                    if(temp != null && temp.schedule.compareTo("")!=0) {
                        holder.publicEvent.setText(temp.schedule);
                    } else {
                        holder.publicEvent.setText(getResources().getString(R.string.no_data));
                    }
                }
    
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
        
                }
            });
            
            //holder.classEvent.setText(getResources().getString(R.string.notYet));
            
            calendar.add(Calendar.DATE, 1);
            if(position == 6) {
                calendar.add(Calendar.DATE, -7);
            }
        }
    }
}

class EventItemClickListener
        implements RecyclerView.OnItemTouchListener {
    
    public interface OnItemClickListener {
        void onItemClick(View view, int pos);
    }
    
    private OnItemClickListener mListener;
    
    private GestureDetector mGestureDetector;
    
    public EventItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
        mListener = listener;
        
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
            
            @Override
            public void onLongPress(MotionEvent e) {
            
            }
            
        });
        
    }
    
    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    
    }
    
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, rv.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }
    
    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    
    }
}
