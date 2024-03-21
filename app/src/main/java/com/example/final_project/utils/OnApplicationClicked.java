package com.example.final_project.utils;

import com.example.final_project.models.Application;
import com.example.final_project.models.Job;

public interface OnApplicationClicked {
    void onViewJob(Job job);
    void onOpenResume(String resumeUrl);
}
