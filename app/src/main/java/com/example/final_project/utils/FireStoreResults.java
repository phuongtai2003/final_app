package com.example.final_project.utils;

import com.example.final_project.models.Company;
import com.example.final_project.models.User;

public interface FireStoreResults {
    public void onResult(boolean result);
    public void onLoginResult(boolean result, User user, Company company);
}
