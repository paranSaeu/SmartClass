package kr.hs.gimpo.smartclass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class EventDetailActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_event_detail);
    
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    
        Intent intent = getIntent();
        String date = "0000-00-00";
        if(intent.getExtras() != null)
            date = intent.getExtras().getString("date", "0000-00-00");
        
        String ymd[] = date.split("-");
    
        TextView detailDate = (TextView) findViewById(R.id.event_detail_date);
        String temp = String.format(getResources().getString(R.string.date_format), ymd[0], ymd[1], ymd[2]);
        detailDate.setText(temp);
        
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
            {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    
}
