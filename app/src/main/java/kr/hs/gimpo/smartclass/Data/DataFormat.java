package kr.hs.gimpo.smartclass.Data;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.hyunjun.school.SchoolSchedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataFormat {

    public static Time timeDataFormat;
    public static Meal mealDataFormat;
    public static Event eventDataFormat;
    public static AirQual airQualDataFormat;

    public DataFormat() {

    }
    
    public static class Time {
        public String timeLastUpdated;
        public String timeJsonData;
        public String serverLastUpdated;
        public String displayTerm;
        
        Time() {
        
        }
        
        Time(String timeJsonData) {
            this.timeJsonData = timeJsonData;
            this.timeLastUpdated =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
            
            JsonParser parser = new JsonParser();
            JsonElement data = parser.parse(this.timeJsonData);
            
            this.serverLastUpdated = data.getAsJsonObject().get("저장일").getAsString();
            this.displayTerm = data.getAsJsonObject().get("일자자료").getAsJsonArray().get(0).getAsJsonArray().get(1).getAsString();
        }
    }
    
    public static class Meal {
        public String mealLastUpdated;
        public int thisMonth;
        public List<List<String>> mealData;
        
        Meal() {
        
        }
        
        Meal(int thisMonth, List<List<String>> mealData) {
            this.thisMonth = thisMonth;
            this.mealData = mealData;
            
            this.mealLastUpdated = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
        }
    }
    
    public static class Event {
        public String eventLastUpdated;
        public int thisYear;
        public List<List<SchoolSchedule>> eventData;
        
        Event() {
        
        }
        
        Event(int thisYear, List<List<SchoolSchedule>> eventData) {
            this.thisYear = thisYear;
            this.eventData = eventData;
            
            this.eventLastUpdated = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
        }
    }
    
    public static class AirQual {
        public String airLastUpdated;
        public String thisTime;
        public List<String> airQualData;
        
        AirQual() {
        
        }
        
        AirQual(List<String> airQualData) {
            this.airQualData = airQualData;
            
            this.airLastUpdated = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
            try {
                Date temp = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(airQualData.get(0));
                System.out.println(temp);
                this.thisTime = new SimpleDateFormat("yyyy-MM-dd HH", Locale.getDefault()).format(temp.getTime());
            } catch(ParseException e) {
                e.printStackTrace();
            }
        }
    }
    
}

