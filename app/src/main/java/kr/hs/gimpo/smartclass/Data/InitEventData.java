package kr.hs.gimpo.smartclass.Data;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.hyunjun.school.School;
import org.hyunjun.school.SchoolException;
import org.hyunjun.school.SchoolSchedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class InitEventData
        extends AsyncTask<Void, Void, Boolean> {
    
    private DatabaseReference mDatabase;
    private List<List<SchoolSchedule>> month = new ArrayList<>();
    
    public InitEventData() {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    
    @Override
    protected Boolean doInBackground(Void... param) {
    
        School api = new School(School.Type.HIGH,School.Region.GYEONGGI,"J100000510");
        try {
            int thisYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime()));
            for(int i = 1; i <= 12; i++) {
                List<SchoolSchedule> schoolSchedules = api.getMonthlySchedule(thisYear, i);
                month.add(schoolSchedules);
            }
        
            DataFormat.eventDataFormat = new DataFormat.Event(thisYear, month);
        
            mDatabase.child("eventDataFormat").child("eventData").child(String.valueOf(thisYear)).setValue(DataFormat.eventDataFormat.eventData);
        
            mDatabase.child("eventDataFormat").child("thisYear").setValue(DataFormat.eventDataFormat.thisYear);
            mDatabase.child("eventDataFormat").child("eventLastUpdated").setValue(DataFormat.eventDataFormat.eventLastUpdated);
        
            Log.d("InitEventData", "Initialization Completed Successfully.");
        
        } catch(SchoolException e) {
            Log.d("InitEventData", "Error: " + e.getMessage());
        
            Log.d("InitEventData", "Initialization Failed!");
            return false;
        }
        return true;
        
    }
    
    @Override
    protected void onPostExecute(Boolean isInitialized) {
    
    }
}
