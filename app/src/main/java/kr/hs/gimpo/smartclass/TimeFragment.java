package kr.hs.gimpo.smartclass;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TimeFragment
        extends Fragment {

    String text;

    onCardChangeListener mCallback;

    public TimeFragment() {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.card_frag_time, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (onCardChangeListener) context;
        } catch(ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnCardChangeListener");
        }
    }

    public void setText(String data) {
        this.text = data;
    }

}

interface onCardChangeListener {
    void onCardChanged(int pos, Bundle data);
}