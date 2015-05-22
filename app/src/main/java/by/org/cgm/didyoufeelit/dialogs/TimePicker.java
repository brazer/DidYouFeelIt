package by.org.cgm.didyoufeelit.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

import lombok.Setter;

/**
 * Author: Anatol Salanevich
 * Date: 12.05.2015
 */
@Deprecated
public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public interface OnTimePickCompleteListener {
        void onTimePickComplete(String time);
    }

    @Setter private OnTimePickCompleteListener timePickCompleteListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        Dialog picker = new TimePickerDialog(getActivity(), this,
                hour, minute, true);
        picker.setTitle("Выберите время в момент землетрясения");
        return picker;
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        String time = hourOfDay + ":" + minute;
        timePickCompleteListener.onTimePickComplete(time);
    }

}
