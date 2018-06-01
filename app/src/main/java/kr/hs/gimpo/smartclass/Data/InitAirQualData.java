package kr.hs.gimpo.smartclass.Data;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class InitAirQualData
        extends AsyncTask<Void, Void, Boolean> {
    
    private String[] airData = new String[15];
    private final String[] airDataFormat = {"dataTime", "pm10", "pm25", "o3", "no2", "co", "so2", "khai"};
    private DatabaseReference mDatabase;
    private boolean isUpdateNeed = true;
    
    public InitAirQualData(DatabaseReference mDatabase, String thisTime) {
        this.mDatabase = mDatabase;
        
        if(thisTime != null) {
            this.isUpdateNeed = (thisTime.compareTo(new SimpleDateFormat("yyyy-MM-dd HH", Locale.getDefault()).format(Calendar.getInstance().getTime())) != 0);
        }
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    
    @Override
    protected Boolean doInBackground(Void... param) {
        System.out.println(isUpdateNeed);
        if(isUpdateNeed) {
            try{
                Document doc = Jsoup.connect("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?serviceKey="+
                        "uTRaH16OBrv%2BrnhI1l%2BhctIkvNd6DwX%2FxpnCRXHGHLjpRpVqxmQJ7Q4cXR0wucoc%2Bx3v8hg%2BsVZRvhPTzXS1xw%3D%3D"+
                        "&numOfRows=1&pageSize=1&pageNo=1&startPage=1&stationName=%EC%82%AC%EC%9A%B0%EB%8F%99&dataTerm=DAILY&ver=1.3").get();
                
                String resultCode = doc.getElementsByTag("resultCode").text().trim();
                
                System.out.println(resultCode);
                
                if(resultCode.compareTo("00") == 0) {
                    int cnt = 0;
                    for(String e : airDataFormat) {
                        if(cnt != 0) {
                            airData[cnt] = doc.getElementsByTag(e + "Value").text().trim();
                            airData[cnt+7] = doc.getElementsByTag(e + "Grade").text().trim();
                        } else {
                            airData[cnt] = doc.getElementsByTag(e).text().trim();
                        }
                        System.out.println(airData[cnt]);
                        cnt++;
                    }
                    
                    DataFormat.airQualDataFormat = new DataFormat.AirQual(Arrays.asList(airData));
                    
                    mDatabase.child("airQualDataFormat").setValue(DataFormat.airQualDataFormat);
                }
                
                
                /*doc = Jsoup.connect("http://m.airkorea.or.kr/sub_new/sub41.jsp")
                        .cookie("isGps","N")
                        .cookie("station","131471")
                        .cookie("lat", "37.619355")
                        .cookie("lng","126,716748")
                        .get();
                Elements data = doc.select("div#detailContent div");
                int cnt = 0;
                for(Element e: data) {
                    System.out.println("place: " + e.text());
                    airData[cnt] = e.text().trim();
                    cnt++;
                }
                DataFormat.airQualDataFormat = new AirQual(Arrays.asList(airData));

                mDatabase.child("airQualDataFormat").setValue(DataFormat.airQualDataFormat);*/
                return true;
            } catch(IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
    
    @Override
    protected void onPostExecute(Boolean isInitialized) {
        if(isInitialized) {
            Log.d("setValue", "ok.");
        }
    }
}