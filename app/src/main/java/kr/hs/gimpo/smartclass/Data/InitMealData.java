package kr.hs.gimpo.smartclass.Data;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import org.hyunjun.school.School;
import org.hyunjun.school.SchoolException;
import org.hyunjun.school.SchoolMenu;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class InitMealData
        extends AsyncTask<Void, Void, Boolean> {
    
    private int lastDay = initLastDay();
    private String[][] mealData = new String[lastDay][2];
    private List<List<String>> mealDataList = new ArrayList<>();
    private DatabaseReference mDatabase;
    private boolean isUpdateNeed;
    
    public InitMealData(DatabaseReference mDatabase, Integer thisMonth) {
        this.mDatabase = mDatabase;
        
        if(thisMonth != null) {
            this.isUpdateNeed = (thisMonth != Integer.parseInt(new SimpleDateFormat("MM", Locale.getDefault()).format(Calendar.getInstance().getTime())));
        } else {
            this.isUpdateNeed = true;
        }
        
    }
    
    private int initLastDay() {
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Calendar.getInstance().getTime());
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        
        return Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(calendar.getTime()));
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    
    @Override
    protected Boolean doInBackground(Void... voids) {
        if(isUpdateNeed) {
            School api = new School(School.Type.HIGH, School.Region.GYEONGGI, "J100000510");
            
            String temp = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Calendar.getInstance().getTime());
            String[] ym = temp.split("-");
            
            try {
                List<SchoolMenu> menu = api.getMonthlyMenu(Integer.parseInt(ym[0]), Integer.parseInt(ym[1]));
                
                for(int i = 0; i < lastDay; i++) {
                    List<String> tmp = new ArrayList<>();
                    tmp.add(menu.get(i).lunch);
                    tmp.add(menu.get(i).dinner);
                    mealDataList.add(tmp);
    
                    DataFormat.mealDataFormat = new DataFormat.Meal(Integer.parseInt(ym[1]) ,mealDataList);
                    //mDatabase.child("mealDataFormat").setValue(DataFormat.mealDataFormat);
                    mDatabase.child("mealDataFormat").child("mealLastUpdated").setValue(DataFormat.mealDataFormat.mealLastUpdated);
                    mDatabase.child("mealDataFormat").child("thisMonth").setValue(DataFormat.mealDataFormat.thisMonth);
    
                    mDatabase.child("mealDataFormat").child("mealData").child(ym[0]).child(ym[1]).setValue(DataFormat.mealDataFormat.mealData);
                }
                
            } catch(SchoolException e) {
                e.printStackTrace();
            }
            
        }
        return null;
    }
    
    @Override
    protected void onPostExecute(Boolean isInitialized) {
    }
}
