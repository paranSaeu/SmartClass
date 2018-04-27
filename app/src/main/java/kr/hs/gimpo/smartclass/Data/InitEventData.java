package kr.hs.gimpo.smartclass.Data;

import android.os.AsyncTask;

import com.google.firebase.database.DatabaseReference;

public class InitEventData
        extends AsyncTask<Void, Void, Boolean> {
    
    private DatabaseReference mDatabase;
    
    InitEventData(DatabaseReference mDatabase) {
        this.mDatabase = mDatabase;
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    
    @Override
    protected Boolean doInBackground(Void... param) {
        
        return false;
    }
    
    @Override
    protected void onPostExecute(Boolean isInitialized) {
    
    }
}
