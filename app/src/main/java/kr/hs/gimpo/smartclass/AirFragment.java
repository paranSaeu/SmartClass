package kr.hs.gimpo.smartclass;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AirFragment extends Fragment {

    public AirFragment() {

    }

    @Override
    public void setArguments(Bundle bundle) {
        this.data = bundle.getString("airData", bundle.getString("airData_default"));
        this.dataList = bundle.getStringArray("airDataList");
    }
    
    String[] dataList;
    String data;
    TextView targetTextView;
    String[] targetList = {"pm10", "pm25", "o3", "no2", "co", "so2", "qual"};
    final String DATA_ID_FORMAT = "card_air_data_%s";
    final String STAT_ID_FORMAT = "card_air_status_%s";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.card_frag_air, container, false);

        targetTextView = view.findViewById(R.id.home_card_air_place);
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH'시 사우동측정소'", Locale.getDefault()).parse(dataList[0]);
    
            String _temp = new SimpleDateFormat("yyyy'년 'MM'월 'dd'일 'HH'시 사우동측정소'", Locale.getDefault()).format(date.getTime());
            targetTextView.setText(_temp);
        } catch(ParseException e) {
            e.printStackTrace();
            targetTextView.setText(dataList[0]);
        }
        
        for(int i = 0; i < 7; i++) {
            String dataId = String.format(Locale.getDefault(), DATA_ID_FORMAT, targetList[i]);;
            String statusId = String.format(Locale.getDefault(), STAT_ID_FORMAT, targetList[i]);;
            targetTextView = view.findViewById(getResources().getIdentifier(dataId, "id", getContext().getPackageName()));
            targetTextView.setText(dataList[i + 1]);
            
            targetTextView = view.findViewById(getResources().getIdentifier(statusId, "id", getContext().getPackageName()));
            System.out.println(dataList[i+1]);
            System.out.println(dataList[i+1].indexOf(" "));
            String temp = i < 6 ?
                    dataList[i+1].substring(0, dataList[i+1].indexOf(" ")):
                    dataList[i+1];
            float value = temp.compareTo("--") != 0? Float.parseFloat(temp): -1;
            int stat = 0;
            switch(i) {
                case 0:
                    if(value >= 0) {
                        if ( value <= 30 )
                            stat = 1;
                        else if ( value <= 80 )
                            stat = 2;
                        else if (value <= 150 )
                            stat = 3;
                        else
                            stat = 4;
                    } else {
                        stat = -1;
                    }
                    break;
                case 1:
                    if(value >= 0) {
                        if ( value <= 15 )
                            stat = 1;
                        else if ( value <= 35 )
                            stat = 2;
                        else if (value <= 75 )
                            stat = 3;
                        else
                            stat = 4;
                    } else {
                        stat = -1;
                    }
                    break;
                case 2:
                    if(value >= 0) {
                        if ( value <= 0.03f )
                            stat = 1;
                        else if ( value <= 0.09f )
                            stat = 2;
                        else if (value <= 0.15f )
                            stat = 3;
                        else
                            stat = 4;
                    } else {
                        stat = -1;
                    }
                    break;
                case 3:
                    if(value >= 0) {
                        if ( value <= 0.03f )
                            stat = 1;
                        else if ( value <= 0.06f )
                            stat = 2;
                        else if (value <= 2f )
                            stat = 3;
                        else
                            stat = 4;
                    } else {
                        stat = -1;
                    }
                    break;
                case 4:
                    if(value >= 0) {
                        if ( value <= 2f )
                            stat = 1;
                        else if ( value <= 9f )
                            stat = 2;
                        else if (value <= 15f )
                            stat = 3;
                        else
                            stat = 4;
                    } else {
                        stat = -1;
                    }
                    break;
                case 5:
                    if(value >= 0) {
                        if ( value <= 0.02f )
                            stat = 1;
                        else if ( value <= 0.05f )
                            stat = 2;
                        else if (value <= 0.15f )
                            stat = 3;
                        else
                            stat = 4;
                    } else {
                        stat = -1;
                    }
                    break;
                case 6:
                    if(value >= 0) {
                        if ( value <= 50 )
                            stat = 1;
                        else if ( value <= 100 )
                            stat = 2;
                        else if (value <= 250 )
                            stat = 3;
                        else
                            stat = 4;
                    } else {
                        stat = -1;
                    }
                    break;
            }
            switch(stat) {
                case -1:
                    targetTextView.setTypeface(Typeface.DEFAULT);
                    targetTextView.setText(getResources().getString(R.string.unknown));
                    break;
                case 1:
                    targetTextView.setTypeface(Typeface.DEFAULT);
                    targetTextView.setText(getResources().getString(R.string.vgood));
                    break;
                case 2:
                    targetTextView.setTypeface(Typeface.DEFAULT);
                    targetTextView.setText(getResources().getString(R.string.good));
                    break;
                case 3:
                    targetTextView.setTypeface(Typeface.DEFAULT_BOLD);
                    targetTextView.setText(getResources().getString(R.string.bad));
                    break;
                case 4:
                    targetTextView.setTypeface(Typeface.DEFAULT_BOLD);
                    targetTextView.setText(getResources().getString(R.string.vbad));
                    break;
            }
        }

        return view;
    }
}
