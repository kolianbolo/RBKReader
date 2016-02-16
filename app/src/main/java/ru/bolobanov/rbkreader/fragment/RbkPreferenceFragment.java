package ru.bolobanov.rbkreader.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import ru.bolobanov.rbkreader.LoadService_;
import ru.bolobanov.rbkreader.R;

/**
 * Created by Bolobanov Nikolay on 10.12.15.
 */

public class RbkPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (getActivity() != null) {
            getActivity().startService(new Intent(getActivity(), LoadService_.class));
        }
    }

}
