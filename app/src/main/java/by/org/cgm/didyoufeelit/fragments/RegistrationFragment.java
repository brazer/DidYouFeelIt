package by.org.cgm.didyoufeelit.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import by.org.cgm.didyoufeelit.R;
import by.org.cgm.didyoufeelit.activities.RegistrationActivity;
import by.org.cgm.didyoufeelit.models.RegisteredUser;


/**
 * A placeholder fragment containing a simple view.
 */
public class RegistrationFragment extends Fragment implements View.OnClickListener {

    private OnRegistrationListener mRegListener;
    private EditText mEditTextFirst, mEditTextSecond, mEditTextPhone, mEditTextEmail;

    public interface OnRegistrationListener {
        void OnRegistrationComplete(RegisteredUser user);
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
        mEditTextFirst = (EditText) view.findViewById(R.id.editTextFirstName);
        mEditTextSecond = (EditText) view.findViewById(R.id.editTextSecondName);
        mEditTextPhone = (EditText) view.findViewById(R.id.editTextPhone);
        mEditTextEmail = (EditText) view.findViewById(R.id.editTextEmail);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof RegistrationActivity) {
            mRegListener = (OnRegistrationListener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mRegListener = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.buttonReg) {
            RegisteredUser user = new RegisteredUser();
            user.setFirstName(mEditTextFirst.getText().toString());
            user.setSecondName(mEditTextSecond.getText().toString());
            user.setPhone(mEditTextPhone.getText().toString());
            user.setEmail(mEditTextEmail.getText().toString());
            mRegListener.OnRegistrationComplete(user);
        }
    }
}
