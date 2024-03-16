package com.example.final_project.utils;

import com.example.final_project.models.JobCategory;

import java.util.List;

public interface JobFireStoreResult {
    public void onGetJobCatResult(boolean result, List<JobCategory> jobCategories);
    public void onAddJobResult(boolean result);
}
