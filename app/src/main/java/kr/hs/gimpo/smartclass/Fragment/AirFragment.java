package kr.hs.gimpo.smartclass.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kr.hs.gimpo.smartclass.R;

public class AirFragment extends Fragment {

    public AirFragment() {

    }

    @Override
    public void setArguments(Bundle bundle) {
        this.data = bundle.getString("airData", bundle.getString("airData_default"));
        this.dataList = bundle.getStringArray("airDataList");
        if(isCreated) {
            updateView(getView());
        }
    }
    
    public void updateView(View view) {
        targetTextView = view.findViewById(R.id.card_air_place);
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(dataList[0]);
        
            String _temp = new SimpleDateFormat("yyyy'년 'MM'월 'dd'일 'HH'시' mm'분'", Locale.getDefault()).format(date.getTime());
            targetTextView.setText(_temp);
        } catch(ParseException e) {
            e.printStackTrace();
            targetTextView.setText(dataList[0]);
        }
    
        for(int i = 0; i < 7; i++) {
            String dataId = String.format(Locale.getDefault(), DATA_ID_FORMAT, targetList[i]);;
            String statusId = String.format(Locale.getDefault(), STAT_ID_FORMAT, targetList[i]);;
            targetTextView = view.findViewById(getResources().getIdentifier(dataId, "id", getContext().getPackageName()));
            System.out.println(dataList[i + 1]);
            targetTextView.setText(dataList[i + 1] + (i <= 2? " ㎍/㎥":(i < 6? " ppm":"")));
        
            targetTextView = view.findViewById(getResources().getIdentifier(statusId, "id", getContext().getPackageName()));
            int stat = Integer.parseInt(dataList[i + 1 + 7]);
            switch(stat) {
                case -1:
                    targetTextView.setTypeface(Typeface.DEFAULT);
                    targetTextView.setTextColor(getResources().getColor(R.color.common));
                    targetTextView.setText(getResources().getString(R.string.unknown));
                    break;
                case 1:
                    targetTextView.setTypeface(Typeface.DEFAULT);
                    targetTextView.setTextColor(getResources().getColor(R.color.common));
                    targetTextView.setText(getResources().getString(R.string.vgood));
                    break;
                case 2:
                    targetTextView.setTypeface(Typeface.DEFAULT);
                    targetTextView.setTextColor(getResources().getColor(R.color.common));
                    targetTextView.setText(getResources().getString(R.string.good));
                    break;
                case 3:
                    targetTextView.setTypeface(Typeface.DEFAULT_BOLD);
                    targetTextView.setTextColor(getResources().getColor(R.color.danger));
                    targetTextView.setText(getResources().getString(R.string.bad));
                    break;
                case 4:
                    targetTextView.setTypeface(Typeface.DEFAULT_BOLD);
                    targetTextView.setTextColor(getResources().getColor(R.color.danger));
                    targetTextView.setText(getResources().getString(R.string.vbad));
                    break;
            }
        }
    }
    
    String[] dataList;
    String data;
    TextView targetTextView;
    String[] targetList = {"pm10", "pm25", "o3", "no2", "co", "so2", "qual"};
    final String DATA_ID_FORMAT = "card_air_data_%s";
    final String STAT_ID_FORMAT = "card_air_status_%s";
    boolean isCreated = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.card_frag_air, container, false);
        
        ConstraintLayout constraintLayout = view.findViewById(R.id.card_air_layout);

        updateView(view);
        
        isCreated = true;
        
        return view;
    }
}
