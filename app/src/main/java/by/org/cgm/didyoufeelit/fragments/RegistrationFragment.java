package by.org.cgm.didyoufeelit.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.org.cgm.didyoufeelit.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class RegistrationFragment extends Fragment implements View.OnClickListener {

    private OnRegistrationClickListener mRegListener;

    public interface OnRegistrationClickListener {
        void onRegistrationClick();
    }

    public RegistrationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.button_registration).setOnClickListener(this);
        view.findViewById(R.id.button_odnoklassniki).setOnClickListener(this);
        view.findViewById(R.id.button_vkontakte).setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mRegListener = (OnRegistrationClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnRegistrationClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mRegListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_registration:
                mRegListener.onRegistrationClick();
                break;
            case R.id.button_vkontakte:
                //todo:
                break;
            case R.id.button_odnoklassniki:
                //todo:
                break;
        }
    }
}
