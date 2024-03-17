package com.example.final_project.utils;

import com.example.final_project.models.Job;
import com.example.final_project.models.JobCategory;

import java.util.List;

public interface JobFireStoreResult {
    public void onGetJobCatResult(boolean result, List<JobCategory> jobCategories);
    public void onAddJobResult(boolean result);
    public void onGetJobsByCompanyResult(boolean result, List<Job> jobList);
}
