package com.example.final_project.utils;

import com.example.final_project.models.Job;

public interface GetJobResult {
    void onGetJobResult(boolean result, Job job);
}
