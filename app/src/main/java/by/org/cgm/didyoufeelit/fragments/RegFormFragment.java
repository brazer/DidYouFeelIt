package by.org.cgm.didyoufeelit.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import by.org.cgm.didyoufeelit.R;
import by.org.cgm.didyoufeelit.models.RegisteredUser;

public class RegFormFragment extends Fragment implements View.OnClickListener {

    private OnRegistrationListener mRegListener;
    private EditText mEditTextFirst, mEditTextSecond, mEditTextPhone, mEditTextEmail;

    public interface OnRegistrationListener {
        void OnRegistrationComplete(RegisteredUser user);
    }

    public RegFormFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reg_form, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEditTextFirst = (EditText) view.findViewById(R.id.editTextFirstName);
        mEditTextSecond = (EditText) view.findViewById(R.id.editTextSecondName);
        mEditTextPhone = (EditText) view.findViewById(R.id.editTextPhone);
        mEditTextEmail = (EditText) view.findViewById(R.id.editTextEmail);
        view.findViewById(R.id.buttonReg).setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mRegListener = (OnRegistrationListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnRegistrationListener");
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
            if (isEmpty(mEditTextFirst)) {
                Toast.makeText(getActivity(), "Поле \"Имя\" пустое", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isEmpty(mEditTextSecond)) {
                Toast.makeText(getActivity(), "Поле \"Фамилия\" пустое", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isEmpty(mEditTextPhone)) {
                Toast.makeText(getActivity(), "Поле \"Телефон\" пустое", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isEmpty(mEditTextEmail)) {
                Toast.makeText(getActivity(), "Поле \"Email\" пустое", Toast.LENGTH_SHORT).show();
                return;
            }
            RegisteredUser user = new RegisteredUser();
            user.setFirstName(mEditTextFirst.getText().toString());
            user.setSecondName(mEditTextSecond.getText().toString());
            user.setPhone(mEditTextPhone.getText().toString());
            user.setEmail(mEditTextEmail.getText().toString());
            mRegListener.OnRegistrationComplete(user);
        }
    }

    private boolean isEmpty(EditText text) {
        return text.getText().toString().trim().length() == 0;
    }
}
