package kr.hs.gimpo.smartclass.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.hs.gimpo.smartclass.R;

public class TimeCard extends Fragment {
    
    public TimeCard() {
    
    }
    
    String classroom = "0-0";
    
    // 탑재(Attach)된 액티비티에 따라 표시되는 레이아웃이 달라집니다!
    // 0: 에러, 1: 시간표, 2: 메인
    int mode = 0;
    
    boolean isInit = false;
    
    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        
        if(args != null) {
            classroom = args.getString("classroom", "0-0");
            mode = args.getInt("mode", 0);
        } else {
            classroom = "0-0";
            mode = 0;
        }
        
        if(isInit) {
            initTimeTableDataListener(getView());
        }
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.time_card, container, false);
        
        isInit = true;
        initTimeTableDataListener(view);
        
        return view;
    }
    
    public void initTimeTableDataListener(final View view) {
    
    }
}
