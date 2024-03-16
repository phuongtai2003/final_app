package com.example.final_project.utils;

import com.example.final_project.models.Company;
import com.example.final_project.models.JobCategory;

import java.util.List;

public interface HomeFireStoreResult {
    public void onGetJobCatResult(boolean result, List<JobCategory> jobCategories);
    public void onGetCompaniesResult(boolean result, List<Company> companies);
}
