package kr.hs.gimpo.smartclass;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DataFormat {

    public Time timeData;
    public Meal mealData;
    public Event eventData;
    public AirQual airQualData;

    public DataFormat() {

    }

    public DataFormat(Time timeData, Meal mealData, Event eventData, AirQual airQualData) {
        this.timeData = timeData;
        this.mealData = mealData;
        this.eventData = eventData;
        this.airQualData = airQualData;
    }
}

class Time {
    public String timeLastUpdated;
    public String timeJsonData;

    Time() {

    }

    Time(String timeJsonData) {
        this.timeJsonData = timeJsonData;
        this.timeLastUpdated =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }
}

class Meal {
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

class Event {
    public String eventLastUpdated;
    public int thisYear;
    public List<List<List<String>>> eventData;

    Event() {

    }

    Event(int thisYear, List<List<List<String>>> eventData) {
        this.thisYear = thisYear;
        this.eventData = eventData;

        this.eventLastUpdated = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }
}

class AirQual {
    public String airLastUpdated;
    public String thisTime;
    public List<String> airQualData;

    AirQual() {

    }

    AirQual(String thisTime, List<String> airQualData) {
        this.thisTime = thisTime;
        this.airQualData = airQualData;

        this.airLastUpdated = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }
}