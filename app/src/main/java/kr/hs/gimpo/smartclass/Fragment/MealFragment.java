package kr.hs.gimpo.smartclass.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kr.hs.gimpo.smartclass.R;

public class MealFragment extends Fragment {

    public MealFragment() {

    }

    @Override
    public void setArguments(Bundle bundle) {
        
        this.mealDate = bundle.getString("mealDate", "2000년 01월 01일");
        
        this.mealTime = bundle.getInt("mealTime");
        
        this.mealData = bundle.getString("mealData", bundle.getString("mealData_default"));
        
        this.displayMode = bundle.getInt("displayMode");
    }
    
    int displayMode = 0;
    int mealTime = 0;
    String mealDate, mealData;
    TextView targetTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.card_frag_meal, container, false);

        targetTextView = view.findViewById(R.id.card_meal_data);
        targetTextView.setText(mealData);
    
    
        targetTextView = view.findViewById(R.id.card_meal_date);
        
        if(displayMode == 0) {
            targetTextView.setVisibility(TextView.VISIBLE);
            targetTextView.setText(mealDate);
        } else {
            targetTextView.setVisibility(TextView.GONE);
        }
        
        
        targetTextView = view.findViewById(R.id.card_meal_time);
        targetTextView.setText(
                mealTime == 0 ?
                        getResources().getString(R.string.lunch):
                        getResources().getString(R.string.dinner)
        );

        return view;
    }
}
