package net.beauvine.wgucourses;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define the xml file used for preferences
        addPreferencesFromResource(R.xml.preferences);
    }

}