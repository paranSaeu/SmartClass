package kr.hs.gimpo.smartclass;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MealFragment extends Fragment {

    public MealFragment() {

    }

    @Override
    public void setArguments(Bundle bundle) {
        this.data = bundle.getString("mealData", bundle.getString("mealData_default"));
    }

    String data;
    TextView targetTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_card_frag_meal, container, false);

        targetTextView = view.findViewById(R.id.home_card_meal_data);
        targetTextView.setText(data);

        return view;
    }
}
