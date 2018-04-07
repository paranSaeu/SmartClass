package kr.hs.gimpo.smartclass;

import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeTable {
    List<List<List<List<Time>>>> tableData = new ArrayList<>();
    List<List<List<Time>>> classGrade = new ArrayList<>();
    List<List<Time>> classNo = new ArrayList<>();
    List<Time> dow = new ArrayList<>();

    public TimeTable() {

    }

    public TimeTable(JsonElement timeTableData) {
        Time time;
        for(int a = 0; a < 3; a++) {
            for(int b = 0; b < 11; b++) {
                for(int c = 0; c < 5; c++) {
                    for(int d = 0; d < 7; d++) {
                        time = new Time(
                                timeTableData.getAsJsonObject().get("시간표")
                                        .getAsJsonArray().get(a + 1)
                                        .getAsJsonArray().get(b + 1)
                                        .getAsJsonArray().get(c + 1)
                                        .getAsJsonArray().get(d + 1)
                                        .getAsInt(),
                                timeTableData.getAsJsonObject().get("학급시간표")
                                        .getAsJsonArray().get(a + 1)
                                        .getAsJsonArray().get(b + 1)
                                        .getAsJsonArray().get(c + 1)
                                        .getAsJsonArray().get(d + 1)
                                        .getAsInt());
                        dow.add(time);
                    }
                    classNo.add(dow);
                }
                classGrade.add(classNo);
            }
            tableData.add(classGrade);
        }
    }



}

class Time {
    public int subject;
    public int teacher;
    public int defaultSubject;
    public int defaultTeacher;
    public boolean isUpdated = false;

    public Time() {

    }

    public Time(int defaultData, int updatedData) {
        this.subject = getSubject(updatedData);
        this.teacher = getTeacher(updatedData);
        this.defaultSubject = getSubject(defaultData);
        this.defaultTeacher = getTeacher(defaultData);
        isUpdated = (defaultData != updatedData);
    }

    private int getSubject(int rawData) {
        for(;;) {
            if(rawData >= 100) {
                rawData -= 100;
            } else {
                break;
            }
        }
        return rawData;
    }

    private int getTeacher(int rawData) {
        return (rawData - getSubject(rawData)) / 100;
    }
}
