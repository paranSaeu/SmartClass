package kr.hs.gimpo.smartclass.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.app.DatePickerDialog;
import android.widget.DatePicker;

import java.util.Calendar;


public class DateDialog
        extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
    
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
    
    }
}
