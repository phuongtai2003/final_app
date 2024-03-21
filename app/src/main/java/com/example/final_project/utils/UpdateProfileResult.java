package com.example.final_project.utils;

import com.example.final_project.models.Company;
import com.example.final_project.models.User;

public interface UpdateProfileResult {
    void onUpdateProfileResult(boolean result, User user);
    void onUpdateCompanyProfileResult(boolean result, Company user);
}
