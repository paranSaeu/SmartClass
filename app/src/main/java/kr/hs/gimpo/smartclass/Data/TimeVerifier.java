package kr.hs.gimpo.smartclass.Data;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeVerifier {
    
    public static boolean canUpdate = false;
    
    public static void validateTime() {
        FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("InitAirQualData", "serverTimeOffset: " + dataSnapshot.toString());
                
                Double serverTimeOffset = dataSnapshot.getValue(Double.class);
                
                if(serverTimeOffset != null) {
                    // 시간이 유효한지 검사합니다. 시간이 유효하다면 데이터를 업데이트할 수 있도록 합니다.
                    canUpdate = serverTimeOffset < (5 * 60 * 1000);
                    if(!canUpdate) {
                        Log.e("TimeVerifier", "Time isn't accurate. any data can't be updated!");
                    } else {
                        Log.i("TimeVerifier", "Time is accurate. data updater is on normal operation.");
                    }
                } else {
                    Log.e("TimeVerifier", "serverTimeOffset is null and time can't be compared!");
                    canUpdate = false;
                }
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            
            }
        });
        /*TimeAsyncTask timeAsyncTask = new TimeAsyncTask();
        timeAsyncTask.execute();
        try {
            timeAsyncTask.get();
        } catch(InterruptedException e) {
            e.printStackTrace();
        } catch(ExecutionException e) {
            e.printStackTrace();
        }*/
    }
    
    static class TimeAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    
        @Override
        protected Void doInBackground(Void... voids) {
    
            try {
                Document doc = Jsoup.connect("https://time.is/just").get();
    
                Elements elements = doc.select("div#twd");
                
                for(Element element: elements) {
                    Log.i("TimeVerifier", "Time: " + element.text().trim());
    
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    
                    String timeOnlineText = new SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault()).format(Calendar.getInstance().getTime()) + element.text().trim();
                    try {
                        Date timeOffline = Calendar.getInstance().getTime();
                        Date timeOnline = dateFormat.parse(timeOnlineText);
                        
                        System.out.println(timeOffline);
                        System.out.println(timeOnline);
                        
                        long timeDifference = timeOffline.getTime() - timeOnline.getTime() > 0? timeOffline.getTime() - timeOnline.getTime() : timeOnline.getTime() - timeOffline.getTime();
                        
                        canUpdate = (timeDifference < 10 * 60 * 1000);
    
                        if(!canUpdate) {
                            Log.e("TimeVerifier", "Time isn't accurate. any data can't be updated!");
                        } else {
                            Log.i("TimeVerifier", "Time is accurate. data updater is on normal operation.");
                        }
                    } catch(ParseException e) {
                        e.printStackTrace();
                    }
                    
                    
                }
                
            } catch(IOException e) {
                e.printStackTrace();
            }
            
            return null;
        }
    
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
