package ru.tp.buy_places.fragments.settings;

import android.app.Activity;
import android.os.Bundle;

import net.xpece.android.support.preference.Preference;
import net.xpece.android.support.preference.PreferenceFragment;

import ru.tp.buy_places.R;

public class SettingFragment extends PreferenceFragment {

    private Preference mLogoutPreference;
    private long mLogoutRequestId;

    private OnLogoutClickListener mListener;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnLogoutClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main_preferences);
        mLogoutPreference = (Preference)findPreference("logout_button");
        mLogoutPreference.setOnPreferenceClickListener(new android.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(android.preference.Preference preference) {
                mListener.onLogoutClick();
                return true;
            }
        });
    }

    public interface OnLogoutClickListener {
        void onLogoutClick();
    }

}
