package by.org.cgm.didyoufeelit.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

import lombok.Setter;

/**
 * Author: Anatol Salanevich
 * Date: 12.05.2015
 */
public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public interface OnDatePickCompleteListener {
        void onDatePickComplete(String date);
    }

    @Setter private OnDatePickCompleteListener datePickCompleteListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // определяем текущую дату
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // создаем DatePickerDialog и возвращаем его
        Dialog picker = new DatePickerDialog(getActivity(), this,
                year, month, day);
        picker.setTitle("Выберите дату землетрясения");
        return picker;
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "." + monthOfYear + "." + year;
        datePickCompleteListener.onDatePickComplete(date);
    }
}
