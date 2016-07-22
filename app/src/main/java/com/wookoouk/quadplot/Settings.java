package com.wookoouk.quadplot;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.PreferenceFragmentCompat;

class Settings extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

//        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().clear().apply(); //used to reset prefs
        addPreferencesFromResource(R.xml.settings);
    }

}
