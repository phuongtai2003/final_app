package com.example.final_project.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesDataSource {
    private static SharedPreferencesDataSource instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SharedPreferencesDataSource() {
    }

    public static SharedPreferencesDataSource getInstance() {
        if (instance == null) {
            instance = new SharedPreferencesDataSource();
        }
        return instance;
    }

    public void init(Context mContext) {
        sharedPreferences = mContext.getSharedPreferences("final_project", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }
}
