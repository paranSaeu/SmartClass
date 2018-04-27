package kr.hs.gimpo.smartclass.Data;

import android.os.AsyncTask;

import com.google.firebase.database.DatabaseReference;

public class InitTimeData
        extends AsyncTask<Void, Void, Boolean> {
    
    private DatabaseReference mDatabase;
    
    private String jsonData = "";
    
    public InitTimeData(DatabaseReference mDatabase) {
        this.mDatabase = mDatabase;
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    
    @Override
    protected Boolean doInBackground(Void... param) {
        /*try {
            // 김포고등학교의 실시간 시간표 정보는 http://comcigan.com:4082/_hourdat?sc=26203에서 확인할 수 있다!
            // 변경: http://comcigan.com:4082/_hourdata?sc=26203에서 확인 가능!
            // 변경: http://comcigan.com:4082/_h119870?sc=26203
            // http://comcigan.com:4083/_h119293?sc=26203
            // http://comcigan.com:4082/_h177193?sc=26203
            // http://comcigan.com:4081/119295?ODEwM18yNjIwM18xXz
            // http://comcigan.com:4082/149249_T?NDE3NF8yNjIwM18xXzIwMTgtMDQtMjMgMTA6MDg6Mj

            // <body><script></script></body> 사이에 있는 var stor='?'의 ?에 해당하는 부분이 바뀐다는 것을 알아내었다!
            // 포트를 4082로 고정하고 /st에서 <body><script></script></body>를 불러와 stor의 값을 읽으면
            // 시간표를 정상적으로 읽어올 수 있을 것이다!
            // http://comcigan.com:4082/"+stor+"?sc=26203

            // sc=26203: 김포고등학교의 데이터

            Document doc = Jsoup.connect("http://comcigan.com:4082/st").get();
            Elements urlData = doc.select("body script");
            String temp = urlData.toString();
            int idx = temp.lastIndexOf("stor='");
            temp = temp.substring(idx+6);
            idx = temp.indexOf("';");
            String stor = temp.substring(0, idx);
            System.out.println("------------------------------");
            System.out.println(stor);
            System.out.println("------------------------------");

            doc = Jsoup.connect("http://comcigan.com:4082/_hourdat?sc=26203").get();

            // 시간표 데이터는 <body>에 있다!
            Element data = doc.body();

            System.out.println("------------------------------");
            System.out.println("data: " + data.text());
            System.out.println("------------------------------");

            jsonData += data.text().trim();

            if(jsonData.compareTo("") != 0 && jsonData.compareTo("{}") != 0) {
                DataFormat.timeDataFormat = new Time(jsonData);
                mDatabase.child("timeDataFormat").setValue(DataFormat.timeDataFormat);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }*/
        return true;
    }
    
    @Override
    protected void onPostExecute(Boolean isInitialized) {
    }
}