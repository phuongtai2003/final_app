package com.example.final_project;

import android.app.Application;

import com.example.final_project.data.SharedPreferencesDataSource;

public class FinalProjectApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesDataSource.getInstance().init(FinalProjectApp.this);
    }
}
