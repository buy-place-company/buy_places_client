package ru.tp.buy_places.authentication;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.tp.buy_places.R;

/**
 * Created by Ivan on 22.07.2015.
 */
public class RegistrationFragment extends Fragment implements View.OnClickListener {

    private OnRegistrationListener mListener;
    private TextInputLayout mEmailEditText;
    private TextInputLayout mUsernameEditText;
    private TextInputLayout mPasswordEditText;
    private TextInputLayout mPasswordConfirm;
    private Button mSubmitRegistrationFormButton;

    public static Fragment newInstance() {
        return new RegistrationFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnRegistrationListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registration, container, false);
        mEmailEditText = (TextInputLayout)rootView.findViewById(R.id.edit_text_email);
        mUsernameEditText = (TextInputLayout)rootView.findViewById(R.id.edit_text_username);
        mPasswordEditText = (TextInputLayout) rootView.findViewById(R.id.edit_text_password);
        mPasswordConfirm = (TextInputLayout) rootView.findViewById(R.id.edit_text_password_confirm);
        mSubmitRegistrationFormButton = (Button) rootView.findViewById(R.id.button_submit_registration);
        mSubmitRegistrationFormButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        final String email = mEmailEditText.getEditText().getText().toString();
        final String username = mUsernameEditText.getEditText().getText().toString();
        final String password = mPasswordEditText.getEditText().getText().toString();
        final String passwordConfirm = mPasswordConfirm.getEditText().getText().toString();
        boolean formIsValid = true;
        if (TextUtils.isEmpty(email)) {
            mEmailEditText.setError("Required");
            formIsValid = false;
        }
        if (TextUtils.isEmpty(username)) {
            mUsernameEditText.setError("Required");
            formIsValid = false;
        }
        if (TextUtils.isEmpty(password) && TextUtils.isEmpty(passwordConfirm)) {

            mPasswordEditText.setError("Required");
            mPasswordConfirm.setError("Required");
            formIsValid = false;
        }
        if (!password.equals(passwordConfirm)) {
            mPasswordEditText.setError("Passwords do not match");
            mPasswordConfirm.setError("Passwords do not match");
            formIsValid = false;
        }

        if (formIsValid) {
            mListener.onRegisterButtonClick(email, username, password);
        }
    }

    interface OnRegistrationListener {
        void onRegisterButtonClick(String email, String username, String password);
    }
}
