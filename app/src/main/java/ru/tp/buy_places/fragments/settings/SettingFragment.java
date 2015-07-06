package ru.tp.buy_places.fragments.settings;

import android.os.Bundle;

import net.xpece.android.support.preference.PreferenceFragment;

import ru.tp.buy_places.R;

public class SettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main_preferences);
    }
}
