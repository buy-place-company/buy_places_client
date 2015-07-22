package ru.tp.buy_places.authentication;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.tp.buy_places.R;

/**
 * Created by Ivan on 22.07.2015.
 */
public class RegistrationFragment extends Fragment {

    private OnRegistrationListener mListener;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registration, container, false);

        return rootView;
    }

    interface OnRegistrationListener {
        void onRegisterButtonClick();
    }
}
