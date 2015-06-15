package by.org.cgm.didyoufeelit.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import by.org.cgm.didyoufeelit.R;
import by.org.cgm.didyoufeelit.activities.MainFormActivity;
import by.org.cgm.didyoufeelit.dialogs.DatePicker;
import by.org.cgm.didyoufeelit.dialogs.TimePicker;
import by.org.cgm.didyoufeelit.utils.StringUtils;

@Deprecated
public class MainFormFragment extends Fragment
        implements View.OnClickListener, DatePicker.OnDatePickCompleteListener,
        TimePicker.OnTimePickCompleteListener {

    public interface OnPlacePickListener {
        void onPlacePick();
    }

    private TextView mDate, mTime, mPlace;
    private OnPlacePickListener onPlacePickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_form, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDate = (TextView) view.findViewById(R.id.textViewDate);
        mDate.setOnClickListener(this);
        mTime = (TextView) view.findViewById(R.id.textViewTime);
        mTime.setOnClickListener(this);
        mPlace = (TextView) view.findViewById(R.id.textViewPlace);
        Bundle arg = getArguments();
        if (arg!=null) mPlace.setText(arg.getString(MainFormActivity.FRAG_ARG));
        else mPlace.setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onPlacePickListener = (OnPlacePickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onPlacePickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onPlacePickListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewDate:
                showDatePicker();
                break;
            case R.id.textViewTime:
                showTimePicker();
                break;
            case R.id.textViewPlace:
                showPlacePicker();
                break;
        }
    }

    private void showDatePicker() {
        DatePicker picker = new DatePicker();
        picker.setDatePickCompleteListener(this);
        picker.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDatePickComplete(String date) {
        mDate.setText(date);
    }

    private void showTimePicker() {
        TimePicker picker = new TimePicker();
        picker.setTimePickCompleteListener(this);
        picker.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onTimePickComplete(String time) {
        mTime.setText(time);
    }

    private void showPlacePicker() {
        onPlacePickListener.onPlacePick();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MainFormActivity.PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK)
            displayPlace(PlacePicker.getPlace(data, getActivity()));
    }

    private void displayPlace(Place place) {
        if (place==null) return;
        String content = "";
        if (!TextUtils.isEmpty(place.getAddress())) content += "Адрес: " + place.getAddress() + "\n";
        if (place.getLatLng()!=null) {
            content += "Долгота: " + StringUtils.round(place.getLatLng().longitude, 2) + "\n";
            content += "Широта: " + StringUtils.round(place.getLatLng().latitude, 2) + "\n";
        }
        mPlace.setText(content);
    }

}
