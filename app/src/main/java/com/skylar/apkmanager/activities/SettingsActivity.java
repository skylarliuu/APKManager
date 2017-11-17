package com.skylar.apkmanager.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.skylar.apkmanager.APKManagerApplication;
import com.skylar.apkmanager.R;
import com.skylar.apkmanager.utils.AppPreferences;
import com.skylar.apkmanager.utils.UtilsUI;

import yuku.ambilwarna.widget.AmbilWarnaPreference;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    // Load Settings
    private AppPreferences appPreferences;
    private Toolbar toolbar;
    private Context context;

    private Preference prefDefaultValues, prefNavigationBlack;
    private AmbilWarnaPreference prefPrimaryColor, prefFABColor;
    private ListPreference prefSortMode;
//    private DirectoryChooserFragment chooserDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        this.context = this;
        this.appPreferences = APKManagerApplication.getAppPreferences();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);


        prefPrimaryColor = (AmbilWarnaPreference) findPreference("prefPrimaryColor");
        prefFABColor = (AmbilWarnaPreference) findPreference("prefFABColor");
        prefDefaultValues = findPreference("prefDefaultValues");
        prefNavigationBlack = findPreference("prefNavigationBlack");
        prefSortMode = (ListPreference) findPreference("prefSortMode");


        setInitialConfiguration();

        // prefSortMode
        setSortModeSummary();

        // prefDefaultValues
        prefDefaultValues.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                appPreferences.setPrimaryColorPref(getResources().getColor(R.color.primary));
                appPreferences.setFABColorPref(getResources().getColor(R.color.fab));
                return true;
            }
        });

    }

    @Override
    public void setContentView(int layoutResID) {
        ViewGroup contentView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_settings, new LinearLayout(this), false);
        toolbar = (Toolbar) contentView.findViewById(R.id.toolbar);
        //TODO Toolbar should load the default style in XML (white title and back arrow), but doesn't happen
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ViewGroup contentWrapper = (ViewGroup) contentView.findViewById(R.id.content_wrapper);
        LayoutInflater.from(this).inflate(layoutResID, contentWrapper, true);
        getWindow().setContentView(contentView);

    }

    private void setInitialConfiguration() {
        toolbar.setTitle(getResources().getString(R.string.action_settings));

        // Android 5.0+ devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(UtilsUI.darker(appPreferences.getPrimaryColorPref(), 0.8));
            toolbar.setBackgroundColor(appPreferences.getPrimaryColorPref());
            if (!appPreferences.getNavigationBlackPref()) {
                getWindow().setNavigationBarColor(appPreferences.getPrimaryColorPref());
            }
        }

        // Pre-Lollipop devices
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            prefPrimaryColor.setEnabled(false);
            prefNavigationBlack.setEnabled(false);
            prefNavigationBlack.setDefaultValue(true);
        }
    }


    private void setSortModeSummary() {
        int sortValue = Integer.valueOf(appPreferences.getSortMode())-1;
        prefSortMode.setSummary(getResources().getStringArray(R.array.sortEntries)[sortValue]);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);

         if (pref == prefSortMode) {
            setSortModeSummary();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_forward, R.anim.slide_out_right);
    }

}
