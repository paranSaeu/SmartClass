package kr.hs.gimpo.smartclass.Setting;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import kr.hs.gimpo.smartclass.Data.DataFormat;
import kr.hs.gimpo.smartclass.R;

public class ClassPickerPreference extends Preference {
    public ClassPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        setLayoutResource(R.layout.pref_class_picker);
        
        setDefaultValue(new DataFormat.Class(1, 1));
        
    }
}
