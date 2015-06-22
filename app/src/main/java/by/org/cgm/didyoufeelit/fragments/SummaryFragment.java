package by.org.cgm.didyoufeelit.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import by.org.cgm.didyoufeelit.AppCache;
import by.org.cgm.didyoufeelit.R;
import by.org.cgm.didyoufeelit.activities.MainFormActivity;
import by.org.cgm.didyoufeelit.listeners.OnNavigationListener;
import by.org.cgm.didyoufeelit.models.Data;
import by.org.cgm.didyoufeelit.models.RegisteredUser;
import by.org.cgm.didyoufeelit.utils.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryFragment extends Fragment implements View.OnClickListener {

    private OnNavigationListener mListener;
    private int position;
    private static EditText mMessage;
    private static TextView mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arg = getArguments();
        position = arg.getInt(StringUtils.PAGE_POSITION);
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        view.findViewById(R.id.btnPrevious).setOnClickListener(this);
        view.findViewById(R.id.btnSend).setOnClickListener(this);
    }

    private void initViews(View view) {
        RegisteredUser user = AppCache.getInstance().getUser();
        mUser = (TextView) view.findViewById(R.id.from);
        String strFrom = user.getFirstName() + " " + user.getSecondName() + ", " +
                user.getEmail();
        mUser.setText(strFrom);
        mMessage = (EditText) view.findViewById(R.id.editTextMessage);
    }

    public void updateMessage() {
        Data data = AppCache.getInstance().getData();
        String strMsg = "Дата: " + data.date + ", время: " + data.time + ".\n" +
                "Mестоположение: \n" + data.place + ".\n" +
                "Доп. информация: ";
        mMessage.setText(strMsg);
    }

    @Override
    public void onStart() {
        super.onStart();
        AppCache.getInstance().updateUser();
        updateUserField();
    }

    public void updateUserField() {
        RegisteredUser user = AppCache.getInstance().getUser();
        String strFrom = user.getFirstName() + " " + user.getSecondName() + ", " +
                user.getEmail();
        mUser.setText(strFrom);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPrevious:
                mListener.onNavigatePage(position - 1);
                break;
            case R.id.btnSend:
                sendMessage();
                break;
        }
    }

    private void sendMessage() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{StringUtils.EMAIL_CGM});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Сообщение о сейсмическом событии");
        intent.putExtra(Intent.EXTRA_TEXT, mMessage.getText());
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Отправка почты..."),
                    MainFormActivity.EMAIL_REQUEST_CODE
            );
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(
                    getActivity(),
                    "В устройстве не установлен ни один почтовый клиент",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnNavigationListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNavigationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
